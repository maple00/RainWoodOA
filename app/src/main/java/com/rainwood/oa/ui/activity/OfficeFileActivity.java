package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.OfficeFileAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 14:20
 * @Desc: 办公文件activity
 */
public final class OfficeFileActivity extends BaseActivity implements IAttachmentCallbacks, StatusAction {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchTopView;
    @ViewInject(R.id.et_search_tips)
    private EditText searchTipsView;
    @ViewInject(R.id.tv_cancel)
    private TextView mTextCancel;
    @ViewInject(R.id.iv_search)
    private ImageView mImageSearch;
    //content
    @ViewInject(R.id.gti_type)
    private GroupTextIcon fileType;
    @ViewInject(R.id.gti_file_format)
    private GroupTextIcon fileFormat;
    @ViewInject(R.id.gti_secret)
    private GroupTextIcon fileSecret;
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon fileSort;
    @ViewInject(R.id.divider)
    private View divider;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_office_file)
    private RecyclerView officeFileView;

    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IAttachmentPresenter mAttachmentPresenter;
    private OfficeFileAdapter mOfficeFileAdapter;
    private CommonGridAdapter mSelectedAdapter;
    private StaffDefaultSortAdapter mDefaultSortAdapter;
    private View mMaskLayer;
    private List<SelectedItem> mTypeList;
    private List<SelectedItem> mFormatList;
    private List<SelectedItem> mSecretList;
    private List<SelectedItem> mSortList;
    // flag
    private boolean CHECKED_TYPE_FLAG = false;
    private boolean CHECKED_FORMAT_FLAG = false;
    private boolean CHECKED_SECRET_FLAG = false;
    private boolean CHECKED_SORT_FLAG = false;
    private int pageCount = 1;
    private String mClassify;
    private String mFormat;
    private String mSecret;
    private String mSorting;
    private String mKeyWord;
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_office_file;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        officeFileView.setLayoutManager(new GridLayoutManager(this, 1));
        officeFileView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mOfficeFileAdapter = new OfficeFileAdapter();
        // 设置适配器
        officeFileView.setAdapter(mOfficeFileAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showLoading();
        // list
        netRequestData("", "", "", "", "");
        // condition
        mAttachmentPresenter.requestOfficeCondition();
    }

    /**
     * 数据请求
     */
    private void netRequestData(String searchText, String classify, String format, String secret, String sorting) {
        showLoading();
        mAttachmentPresenter.requestOfficeFileData(searchText, classify, format, secret, sorting, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        fileType.setOnItemClick(text -> {
            CHECKED_TYPE_FLAG = !CHECKED_TYPE_FLAG;
            fileType.setRightIcon(CHECKED_TYPE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_TYPE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_TYPE_FLAG) {
                stateConditionPopDialog(mTypeList, fileType);
            }
        });
        fileFormat.setOnItemClick(text -> {
            CHECKED_FORMAT_FLAG = !CHECKED_FORMAT_FLAG;
            fileFormat.setRightIcon(CHECKED_FORMAT_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_FORMAT_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_FORMAT_FLAG) {
                stateConditionPopDialog(mFormatList, fileFormat);
            }
        });
        fileSecret.setOnItemClick(text -> {
            CHECKED_SECRET_FLAG = !CHECKED_SECRET_FLAG;
            fileSecret.setRightIcon(CHECKED_SECRET_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_SECRET_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_SECRET_FLAG) {
                stateConditionPopDialog(mSecretList, fileSecret);
            }
        });
        fileSort.setOnItemClick(text -> {
            CHECKED_SORT_FLAG = !CHECKED_SORT_FLAG;
            fileSort.setRightIcon(CHECKED_SORT_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_SORT_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_SORT_FLAG) {
                defaultSortConditionPopDialog();
            }
        });
        // 文件预览下载
        mOfficeFileAdapter.setItemFile(new OfficeFileAdapter.OnClickItemFile() {
            @Override
            public void onClickDownload(OfficeFile file, int position) {
                FileManagerUtil.fileDownload(OfficeFileActivity.this, TbsActivity.class, file.getSrc(),
                        file.getName(), file.getFormat());
            }

            @Override
            public void onClickPreview(OfficeFile file, int position) {
                FileManagerUtil.filePreview(OfficeFileActivity.this, TbsActivity.class, file.getSrc(),
                        file.getName(), file.getFormat());
            }
        });
        // 搜索框UI监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mKeyWord = s.toString();
                netRequestData(mKeyWord, mClassify, mFormat, mSecret, mSorting);
            }
        });
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mAttachmentPresenter.requestOfficeFileData(mKeyWord, mClassify, mFormat, mSecret, mSorting, ++pageCount);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_cancel:
                mTextCancel.setVisibility(View.GONE);
                searchTopView.setVisibility(View.GONE);
                pageTitle.setVisibility(View.VISIBLE);
                mImageSearch.setVisibility(View.VISIBLE);
                searchTipsView.setText("");
                // 向左边移出
                searchTopView.setAnimation(AnimationUtils.makeOutAnimation(this, false));
                break;
            case R.id.iv_search:
                mTextCancel.setVisibility(View.VISIBLE);
                searchTopView.setVisibility(View.VISIBLE);
                pageTitle.setVisibility(View.GONE);
                mImageSearch.setVisibility(View.GONE);
                searchTipsView.setHint("请输入文件名称");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
        }
    }

    @Override
    public void getOfficeFileData(List<OfficeFile> fileList) {
        showComplete();
        if (pageCount != 1) {
            pageRefresh.finishLoadmore();
            toast("为您加载了" + ListUtils.getSize(fileList) + "条数据");
            mOfficeFileAdapter.addData(fileList);
        } else {
            if (ListUtils.getSize(fileList) == 0) {
                showEmpty();
                return;
            }
            mOfficeFileAdapter.setFileList(fileList);
        }
    }

    @Override
    public void getOfficeCondition(List<SelectedItem> typeList, List<SelectedItem> formatList,
                                   List<SelectedItem> secretList, List<SelectedItem> sortList) {
        Collections.reverse(typeList);
        Collections.reverse(formatList);
        Collections.reverse(secretList);
        mTypeList = typeList;
        mFormatList = formatList;
        mSecretList = secretList;
        mSortList = sortList;
    }

    /**
     * 状态选择
     */
    private void stateConditionPopDialog(List<SelectedItem> stateList, GroupTextIcon targetGTI) {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSelectedAdapter);
                    mItemFlowLayout = view.findViewById(R.id.tfl_text);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            CHECKED_TYPE_FLAG = false;
            CHECKED_FORMAT_FLAG = false;
            CHECKED_SECRET_FLAG = false;
            CHECKED_SORT_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_TYPE_FLAG = false;
                CHECKED_FORMAT_FLAG = false;
                CHECKED_SECRET_FLAG = false;
                CHECKED_SORT_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });

        mItemFlowLayout.setTextList(stateList);
        mItemFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : stateList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            // TODO: 条件查询
            mClassify = "";
            mFormat = "";
            mSecret = "";
            if (CHECKED_TYPE_FLAG) {
                mClassify = selectedItem.getName();
            } else if (CHECKED_FORMAT_FLAG) {
                mFormat = selectedItem.getName();
            } else if (CHECKED_SECRET_FLAG) {
                mSecret = selectedItem.getName();
            }
            netRequestData("", "全部".equals(mClassify) ? "" : mClassify, "全部".equals(mFormat) ? "" : mFormat,
                    "全部".equals(mSecret) ? "" : mSecret, "");
            mStatusPopWindow.dismiss();
        });
    }

    /**
     * 排序
     */
    private void defaultSortConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(1);
                    contentList.setVisibility(View.VISIBLE);
                    mDefaultSortAdapter = new StaffDefaultSortAdapter();
                    contentList.setAdapter(mDefaultSortAdapter);
                    TextSelectedItemFlowLayout itemFlowLayout = view.findViewById(R.id.tfl_text);
                    itemFlowLayout.setVisibility(View.GONE);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            CHECKED_SORT_FLAG = false;
            fileSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_SORT_FLAG = false;
                fileSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 筛选条件点击事件
        mDefaultSortAdapter.setItemList(mSortList);
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mSortList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            // TODO： 查询排序条件
            mSorting = selectedItem.getName();
            netRequestData("", "", "", "", "默认排序".equals(mSorting) ? "" : mSorting);

            mStatusPopWindow.dismiss();
        });
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
