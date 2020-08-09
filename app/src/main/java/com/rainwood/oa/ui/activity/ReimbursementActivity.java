package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
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
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.MineReimbursementAdapter;
import com.rainwood.oa.ui.adapter.ReimbursementAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/6/4 11:55
 * @Desc: 费用报销
 */
public final class ReimbursementActivity extends BaseActivity implements IFinancialCallbacks, IMineCallbacks,
        StatusAction {

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
    // content
    //  @ViewInject(R.id.tv_search)
    // private TextView searchView;
    @ViewInject(R.id.tv_query_all)
    private TextView mTextQueryAll;
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.tv_allocated)
    private TextView mTextAllocated;
    @ViewInject(R.id.line_allocated)
    private View lineAllocated;
    @ViewInject(R.id.tv_un_allocated)
    private TextView mTextUnAllocated;
    @ViewInject(R.id.line_un_allocated)
    private View lineUnAllocated;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_type)
    private GroupTextIcon typeView;
    @ViewInject(R.id.gti_pay_team)
    private GroupTextIcon payTeam;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.divider)
    private View divider;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_reimbursement)
    private RecyclerView reimbursementView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IFinancialPresenter mFinancialPresenter;
    private IMinePresenter mMinePresenter;
    private ReimbursementAdapter mReimbursementAdapter;
    private MineReimbursementAdapter mMineReimbursementAdapter;

    private static boolean SELECTED_TYPE_FLAG = false;
    private static boolean SELECTED_PAYER_FLAG = false;
    private static boolean selectedTimeFlag = false;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private List<SelectedItem> mTypeList;
    private List<SelectedItem> mPayerList;
    private int pageCount = 1;
    private String mStaffId;
    private String mStartTime;
    private String mEndTime;
    private String hasAllocated = "";
    private String mType;
    private String mPayer;
    private String mKeyWord;
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reimbursement;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        reimbursementView.setLayoutManager(new GridLayoutManager(this, 1));
        reimbursementView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mReimbursementAdapter = new ReimbursementAdapter();
        mMineReimbursementAdapter = new MineReimbursementAdapter();
        switch (moduleMenu) {
            case "reimbursement":
                reimbursementView.setAdapter(mReimbursementAdapter);
                break;
            case "mycost":
                // 我的费用报销
                departStaff.setVisibility(View.GONE);
                // 设置适配器
                reimbursementView.setAdapter(mMineReimbursementAdapter);
                break;
        }
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
        // 字体加粗
        mTextQueryAll.getPaint().setFakeBoldText(true);
        mTextQueryAll.postInvalidate();
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showDialog();
        // condition
        mFinancialPresenter.requestReimburseCondition();
        switch (moduleMenu) {
            case "mycost":
                //我的费用报销
                setNetMineReimbursement("", "", "", "", "", "");
                break;
            case "reimbursement":
                // 管理--费用报销
                netManagerReimbursement("", "", "", "", "", "", "");
                break;
        }
    }

    /**
     * 我的 -- 费用报销
     */
    private void setNetMineReimbursement(String text, String type, String payer, String payState, String startTime, String endTime) {
        showDialog();
        mMinePresenter.requestMineReimburseData(text, type, payer, payState, startTime, endTime, pageCount = 1);
    }

    /**
     * 管理费用报销
     */
    private void netManagerReimbursement(String allocated, String staffId, String type, String payer,
                                         String startTime, String endTime, String searchText) {
        showDialog();
        mFinancialPresenter.requestReimburseData(allocated, staffId, type, payer, startTime, endTime, searchText, pageCount = 1);
    }


    /**
     * 财务管理 -- 费用报销
     */
    private void netReimbursementData(String allocated, String staffId, String type, String payer,
                                      String startTime, String endTime, String keyWord) {
        netManagerReimbursement(allocated, staffId, type, payer, startTime, endTime, keyWord);
    }

    @Override
    protected void initEvent() {
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(ReimbursementActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));
        typeView.setOnItemClick(text -> {
            SELECTED_TYPE_FLAG = !SELECTED_TYPE_FLAG;
            typeView.setRightIcon(SELECTED_TYPE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_TYPE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_TYPE_FLAG) {
                stateConditionPopDialog(mTypeList, typeView);
            }
        });
        payTeam.setOnItemClick(text -> {
            SELECTED_PAYER_FLAG = !SELECTED_PAYER_FLAG;
            payTeam.setRightIcon(SELECTED_PAYER_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_PAYER_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_PAYER_FLAG) {
                stateConditionPopDialog(mPayerList, payTeam);
            }
        });
        periodTime.setOnItemClick(new GroupTextIcon.onItemClick() {
            @SingleClick
            @Override
            public void onItemClick(String text) {
                selectedTimeFlag = !selectedTimeFlag;
                new StartEndDateDialog.Builder(ReimbursementActivity.this, false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        .setStartTime(mStartTime)
                        .setEndTime(mEndTime)
                        //.setIgnoreDay()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new StartEndDateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                                    toast("请选择时间");
                                    return;
                                }
                                if (DateTimeUtils.isDateOneBigger(startTime, endTime, DateTimeUtils.DatePattern.ONLY_DAY)) {
                                    toast("开始时间不能大于结束时间");
                                    return;
                                }
                                // TODO: 通过时间查询
                                dialog.dismiss();
                                selectedTimeFlag = false;
                                periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                                mStartTime = startTime;
                                mEndTime = endTime;
                                switch (moduleMenu) {
                                    case "mycost":
                                        //我的费用报销
                                        setNetMineReimbursement("", "", "", "", startTime, endTime);
                                        break;
                                    case "reimbursement":
                                        // 管理--费用报销
                                        netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, mKeyWord);
                                        break;
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                                selectedTimeFlag = false;
                                mStartTime = "";
                                mEndTime = "";
                                periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                            }
                        })
                        .show();
                periodTime.setRightIcon(selectedTimeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        selectedTimeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            }
        });

        // 管理费用报销查看详情
        mReimbursementAdapter.setOnClickReimburse((reimbursement, position) ->
                PageJumpUtil.reimburseList2Detail(ReimbursementActivity.this,
                        CostDetailActivity.class, reimbursement.getId()));
        // 我的费用报销查看详情
        mMineReimbursementAdapter.setOnClickReimburse((reimbursement, position) ->
                PageJumpUtil.reimburseList2Detail(ReimbursementActivity.this, CostDetailActivity.class,
                        reimbursement.getId()));

        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                switch (moduleMenu) {
                    case "mycost":
                        //我的费用报销
                        mMinePresenter.requestMineReimburseData(mKeyWord, hasAllocated, mPayer, mType, mStartTime, mEndTime, ++pageCount);
                        break;
                    case "reimbursement":
                        // 管理--费用报销
                        mFinancialPresenter.requestReimburseData(hasAllocated, mStaffId, mType,
                                mPayer, mStartTime, mEndTime, "", (++pageCount));
                        break;
                }
            }
        });
        // 搜索条件监听
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
                switch (moduleMenu) {
                    case "mycost":
                        //我的费用报销
                        setNetMineReimbursement(mKeyWord, "", "", "", "", "");
                        break;
                    case "reimbursement":
                        // 管理--费用报销
                        mFinancialPresenter.requestReimburseData(hasAllocated, mStaffId, mType, mPayer,
                                mStartTime, mEndTime, mKeyWord, pageCount = 1);
                        break;
                }
                /*if (TextUtils.isEmpty(s)) {
                    searchTopView.setVisibility(View.GONE);
                    hideSoftKeyboard();
                } else {
                    searchTopView.setVisibility(View.VISIBLE);
                    netReimbursementData("", "", "", "", "", "", s.toString());
                }*/
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

                toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, "");
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_cancel, R.id.tv_query_all, R.id.tv_allocated, R.id.tv_un_allocated,
            R.id.iv_search})
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
                searchTipsView.setHint("请输入备注信息");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_query_all:
                if (lineAll.getVisibility() == View.VISIBLE) {
                    return;
                }
                setStatusLine(true, false, false);
                setTextBold(mTextQueryAll, mTextUnAllocated, mTextAllocated);
                hasAllocated = "";
                switch (moduleMenu) {
                    case "mycost":
                        //我的费用报销
                        setNetMineReimbursement("", "全部", "", "", "", "");
                        break;
                    case "reimbursement":
                        // 管理--费用报销
                        netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, mKeyWord);
                        break;
                }
                break;
            case R.id.tv_allocated:
                if (lineAllocated.getVisibility() == View.VISIBLE) {
                    return;
                }
                setStatusLine(false, true, false);
                setTextBold(mTextAllocated, mTextQueryAll, mTextUnAllocated);
                hasAllocated = "是";
                switch (moduleMenu) {
                    case "mycost":
                        //我的费用报销
                        setNetMineReimbursement("", "已拨付", "", "", "", "");
                        break;
                    case "reimbursement":
                        // 管理--费用报销
                        netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, mKeyWord);
                        break;
                }
                break;
            case R.id.tv_un_allocated:
                if (lineUnAllocated.getVisibility() == View.VISIBLE) {
                    return;
                }
                setStatusLine(false, false, true);
                setTextBold(mTextUnAllocated, mTextQueryAll, mTextAllocated);
                hasAllocated = "否";
                switch (moduleMenu) {
                    case "mycost":
                        //我的费用报销
                        setNetMineReimbursement("", "未拨付", "", "", "", "");
                        break;
                    case "reimbursement":
                        // 管理--费用报销
                        netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, mKeyWord);
                        break;
                }
                break;
        }
    }

    private void setTextBold(TextView textQueryAll, TextView textUnAllocated, TextView textAllocated) {
        textQueryAll.getPaint().setFakeBoldText(true);
        textUnAllocated.getPaint().setFakeBoldText(false);
        textAllocated.getPaint().setFakeBoldText(false);
        mTextUnAllocated.postInvalidate();
        mTextQueryAll.postInvalidate();
        mTextAllocated.postInvalidate();
    }

    /**
     * 设置状态下划线
     *
     * @param selectedAll         全部
     * @param selectedAllocated   已拨付
     * @param selectedUnAllocated 未拨付
     */
    private void setStatusLine(boolean selectedAll, boolean selectedAllocated, boolean selectedUnAllocated) {
        lineAll.setVisibility(selectedAll ? View.VISIBLE : View.INVISIBLE);
        lineAllocated.setVisibility(selectedAllocated ? View.VISIBLE : View.INVISIBLE);
        lineUnAllocated.setVisibility(selectedUnAllocated ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void getReimburseData(List<Reimbursement> reimbursementList) {
        // 管理---费用报销
        pageRefresh.finishLoadmore();
        showComplete();
        if (isShowDialog()) {
            hideDialog();
        }
        if (pageCount != 1) {
            toast("为您加载了" + ListUtils.getSize(reimbursementList) + "条数据");
            mReimbursementAdapter.addData(reimbursementList);
        } else {
            if (ListUtils.getSize(reimbursementList) == 0) {
                showEmpty();
            }
            mReimbursementAdapter.setList(reimbursementList);
        }
    }

    @Override
    public void getReimburseCondition(List<SelectedItem> typeList, List<SelectedItem> payerList) {
        // 管理 -- 费用报销condition
        Collections.reverse(typeList);
        Collections.reverse(payerList);
        mTypeList = typeList;
        mPayerList = payerList;
    }

    @Override
    public void getMineReimbursementRecords(List<MineReimbursement> reimbursementList) {
        // 我的-- 费用报销
        if (isShowDialog()) {
            hideDialog();
        }
        mMineReimbursementAdapter.setList(reimbursementList);
    }

    /**
     * 类型选择
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
                    contentList.setVisibility(View.GONE);
                    mItemFlowLayout = view.findViewById(R.id.tfl_text);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SELECTED_TYPE_FLAG = false;
            SELECTED_PAYER_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SELECTED_TYPE_FLAG = false;
                SELECTED_PAYER_FLAG = false;
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
            if (SELECTED_TYPE_FLAG) {
                mType = "全部".equals(selectedItem.getName()) ? "" : selectedItem.getName();
            }
            if (SELECTED_PAYER_FLAG) {
                mPayer = "全部".equals(selectedItem.getName()) ? "" : selectedItem.getName();
            }
            switch (moduleMenu) {
                case "mycost":
                    //我的费用报销
                    setNetMineReimbursement("", "", mPayer, mType, "", "");
                    break;
                case "reimbursement":
                    // 管理--费用报销
                    netReimbursementData(hasAllocated, mStaffId, mType, mPayer, mStartTime, mEndTime, mKeyWord);
                    break;
            }
            mStatusPopWindow.dismiss();
        });

        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {

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
    protected void release() {
        super.release();
        if (mFinancialPresenter != null) {
            mFinancialPresenter = null;
        }
        if (mMinePresenter != null) {
            mMinePresenter = null;
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
