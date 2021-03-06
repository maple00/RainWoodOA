package com.rainwood.oa.ui.activity;

import android.content.Intent;
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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.KnowledgeAttach;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.adapter.AttachKnowledgeAdapter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.view.RegexEditText;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:10
 * @Desc: 知识管理-- 附件管理
 */
public final class AttachManagerActivity extends BaseActivity implements IAttachmentCallbacks, StatusAction {

    //actionBar
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
    // content
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_secret)
    private GroupTextIcon attachSecret;
    @ViewInject(R.id.gti_obj_type)
    private GroupTextIcon objType;
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon attachSorting;
    @ViewInject(R.id.divider)
    private View divider;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_attach_list)
    private RecyclerView attachView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IAttachmentPresenter mAttachmentPresenter;
    private AttachKnowledgeAdapter mAttachKnowledgeAdapter;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private StaffDefaultSortAdapter mDefaultSortAdapter;
    private List<SelectedItem> mTargetList;
    private List<SelectedItem> mSecretList;
    private List<SelectedItem> mSortList;

    private boolean CHECKED_TARGET_FLAG = false;
    private boolean CHECKED_SECRET_FLAG = false;
    private boolean CHECKED_SORT_FLAG = false;
    private int pageCount = 1;
    private String mStaffId;
    private String mSecret;
    private String mTargetType;
    private String mDefaultSorting;
    private String mKeyWord;
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attach_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        attachView.setLayoutManager(new GridLayoutManager(this, 1));
        attachView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mAttachKnowledgeAdapter = new AttachKnowledgeAdapter();
        // 设置适配器
        attachView.setAdapter(mAttachKnowledgeAdapter);
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // list
        netRequestData("", "");
        // request Condition
        mAttachmentPresenter.requestAttachCondition();
    }

    /**
     * 请求网络数据
     */
    private void netRequestData(String attachName, String staffId) {
        showLoading();
        mAttachmentPresenter.requestKnowledgeAttach(attachName, staffId, mSecret, mTargetType, mDefaultSorting, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(AttachManagerActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));

        attachSecret.setOnItemClick(text -> {
            CHECKED_SECRET_FLAG = !CHECKED_SECRET_FLAG;
            attachSecret.setRightIcon(CHECKED_SECRET_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_SECRET_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_SECRET_FLAG) {
                stateConditionPopDialog(mSecretList, attachSecret);
            }
        });
        objType.setOnItemClick(text -> {
            CHECKED_TARGET_FLAG = !CHECKED_TARGET_FLAG;
            objType.setRightIcon(CHECKED_TARGET_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_TARGET_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_TARGET_FLAG) {
                stateConditionPopDialog(mTargetList, objType);
            }
        });
        attachSorting.setOnItemClick(text -> {
            CHECKED_SORT_FLAG = !CHECKED_SORT_FLAG;
            attachSorting.setRightIcon(CHECKED_SORT_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_SORT_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_SORT_FLAG) {
                defaultSortConditionPopDialog();
            }
        });
        // itemClickListener
        mAttachKnowledgeAdapter.setClickAttach(new AttachKnowledgeAdapter.OnClickKnowledgeAttach() {
            @Override
            public void onClickTarget(KnowledgeAttach attach, int position) {
                if (attach.getTargetId().startsWith(RegexEditText.REGEX_CHINESE)
                        && attach.getTargetId().endsWith(RegexEditText.REGEX_CHINESE)) {
                    toast("数据异常");
                    LogUtils.d("sxs", "传入的id有问题");
                    return;
                }
                //toast("页面跳转");
                if ("客户".equals(attach.getTarget())) {
                    PageJumpUtil.listJump2CustomDetail(AttachManagerActivity.this, CustomDetailActivity.class, attach.getTargetId());
                } else {
                    PageJumpUtil.orderList2Detail(AttachManagerActivity.this, OrderDetailActivity.class, attach.getTargetId(), "");
                }
            }

            @Override
            public void onClickDownload(KnowledgeAttach attach, int position) {
                FileManagerUtil.fileDownload(AttachManagerActivity.this, TbsActivity.class, attach.getSrc(), attach.getName(), attach.getFormat());
            }

            @Override
            public void onClickPreview(KnowledgeAttach attach, int position) {
                FileManagerUtil.filePreview(AttachManagerActivity.this, TbsActivity.class, attach.getSrc(), attach.getName(), attach.getFormat());
            }
        });

        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mAttachmentPresenter.requestKnowledgeAttach(mKeyWord, mStaffId, mSecret, mTargetType, mDefaultSorting, ++pageCount);
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
                mAttachmentPresenter.requestKnowledgeAttach(mKeyWord, mStaffId, mSecret, mTargetType, mDefaultSorting, pageCount = 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
                String staff = data.getStringExtra("staff");
                mStaffId = data.getStringExtra("staffId");
                String position = data.getStringExtra("position");
                //toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                netRequestData("", mStaffId);
            }
        }
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
                searchTipsView.setHint("请输入附件名称");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
        }
    }

    @Override
    public void getKnowledgeAttach(List<KnowledgeAttach> attachList) {
        showComplete();
        if (pageCount != 1) {
            pageRefresh.finishLoadmore();
            toast("为您加载了" + ListUtils.getSize(attachList) + "条数据");
            mAttachKnowledgeAdapter.addData(attachList);
        } else {
            if (ListUtils.getSize(attachList) == 0) {
                showEmpty();
            }
            mAttachKnowledgeAdapter.setAttachList(attachList);
        }
    }

    @Override
    public void getAttachCondition(List<SelectedItem> targetList, List<SelectedItem> secretList,
                                   List<SelectedItem> sortList) {
        Collections.reverse(targetList);
        Collections.reverse(secretList);
        mTargetList = targetList;
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
            CHECKED_TARGET_FLAG = false;
            CHECKED_SECRET_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_TARGET_FLAG = false;
                CHECKED_SECRET_FLAG = false;
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
            mSecret = "";
            mTargetType = "";
            if (CHECKED_SECRET_FLAG) {
                mSecret = selectedItem.getName();
            } else if (CHECKED_TARGET_FLAG) {
                mTargetType = selectedItem.getName();
            }
            netRequestData("", ""
            );
            mStatusPopWindow.dismiss();
        });

        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {

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
            attachSorting.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_SORT_FLAG = false;
                attachSorting.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 筛选条件点击事件
        mDefaultSortAdapter.setItemList(mSortList);
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mSortList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            mStatusPopWindow.dismiss();
            // TODO: 排序方式查询
            mDefaultSorting = selectedItem.getName();
            netRequestData("", "");
        });
    }

    @Override
    public void onError(String tips) {
        toast(tips);
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
