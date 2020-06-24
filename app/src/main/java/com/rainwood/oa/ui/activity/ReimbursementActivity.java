package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.MineReimbursementAdapter;
import com.rainwood.oa.ui.adapter.ReimbursementAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/6/4 11:55
 * @Desc: 费用报销
 */
public final class ReimbursementActivity extends BaseActivity implements IFinancialCallbacks, IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    // content
    @ViewInject(R.id.tv_search)
    private TextView searchView;
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.line_allocated)
    private View lineAllocated;
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reimbursement;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
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
                searchView.setTextSize(14f);
                searchView.setText("费用申请");
                searchView.setTextColor(getColor(R.color.white));
                searchView.setPadding(FontSwitchUtil.dip2px(this, 12f), FontSwitchUtil.dip2px(this, 10f),
                        FontSwitchUtil.dip2px(this, 12f), FontSwitchUtil.dip2px(this, 10f));
                searchView.setBackgroundResource(R.drawable.selector_apply_button);
                departStaff.setVisibility(View.GONE);
                // 设置适配器
                reimbursementView.setAdapter(mMineReimbursementAdapter);
                break;
        }
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mFinancialPresenter.registerViewCallback(this);
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        switch (moduleMenu) {
            case "mycost":
                //我的费用报销
                mMinePresenter.requestMineReimburseData();
                break;
            case "reimbursement":
                // 管理--费用报销
                mFinancialPresenter.requestReimburseData();
                mFinancialPresenter.requestReimburseCondition();
                break;
        }
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
        periodTime.setOnItemClick(text -> {
            selectedTimeFlag = !selectedTimeFlag;
            new StartEndDateDialog.Builder(ReimbursementActivity.this, false)
                    .setTitle(null)
                    .setConfirm(getString(R.string.common_text_confirm))
                    .setCancel(getString(R.string.common_text_clear_screen))
                    .setAutoDismiss(false)
                    //.setIgnoreDay()
                    .setCanceledOnTouchOutside(false)
                    .setListener(new StartEndDateDialog.OnListener() {
                        @Override
                        public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                            dialog.dismiss();
                            toast("选中的时间段：" + startTime + "至" + endTime);
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }
                    })
                    .show();
            periodTime.setRightIcon(selectedTimeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedTimeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
        });

        // 管理费用报销查看详情
        mReimbursementAdapter.setOnClickReimburse((reimbursement, position) ->
                PageJumpUtil.receivable2Detail(ReimbursementActivity.this, CostDetailActivity.class, reimbursement.getStaffName()));
        // 我的费用报销查看详情
        mMineReimbursementAdapter.setOnClickReimburse(new MineReimbursementAdapter.OnClickReimburse() {
            @Override
            public void onClickItem(MineReimbursement reimbursement, int position) {
                toast("查看详情");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            String staff = data.getStringExtra("staff");
            String staffId = data.getStringExtra("staffId");
            String position = data.getStringExtra("position");

            toast("员工：" + staff + "\n员工编号：" + staffId + "\n 职位：" + position);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search, R.id.tv_query_all, R.id.tv_allocated, R.id.tv_un_allocated})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search:
                switch (searchView.getText().toString().trim()) {
                    case "搜索":
                        toast("搜索");
                        break;
                    case "费用申请":
                        toast("费用申请");
                        break;
                }
                break;
            case R.id.tv_query_all:
                toast("全部");
                setStatusLine(true, false, false);
                break;
            case R.id.tv_allocated:
                toast("已拨付");
                setStatusLine(false, true, false);
                break;
            case R.id.tv_un_allocated:
                toast("未拨付");
                setStatusLine(false, false, true);
                break;
        }
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
        mReimbursementAdapter.setList(reimbursementList);
    }

    @Override
    public void getReimburseCondition(List<SelectedItem> typeList, List<SelectedItem> payerList) {
        // 管理 -- 费用报销condition
        mTypeList = typeList;
        mPayerList = payerList;
    }

    @Override
    public void getMineReimbursementRecords(List<MineReimbursement> reimbursementList) {
        // 我的-- 费用报销
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
        mSelectedAdapter.setTextList(stateList);
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
