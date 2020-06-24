package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.OfficeFile;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.OfficeFileAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 14:20
 * @Desc: 办公文件activity
 */
public final class OfficeFileActivity extends BaseActivity implements IAttachmentCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_office_file;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTips.setText("输入文件名称");
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
        mAttachmentPresenter.requestOfficeFileData();
        // condition
        mAttachmentPresenter.requestOfficeCondition();
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
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getOfficeFileData(List<OfficeFile> fileList) {
        mOfficeFileAdapter.setFileList(fileList);
    }

    @Override
    public void getOfficeCondition(List<SelectedItem> typeList, List<SelectedItem> formatList,
                                   List<SelectedItem> secretList, List<SelectedItem> sortList) {
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
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_TYPE_FLAG = false;
                CHECKED_FORMAT_FLAG = false;
                CHECKED_SECRET_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mSelectedAdapter.setTextList(stateList);
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
                    mDefaultSortAdapter = new StaffDefaultSortAdapter();
                    contentList.setAdapter(mDefaultSortAdapter);
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
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mSortList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
        });
        mDefaultSortAdapter.setItemList(mSortList);
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
}
