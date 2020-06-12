package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.model.domain.Reimbursement;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.MineReimbursementAdapter;
import com.rainwood.oa.ui.adapter.ReimbursementAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

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

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_reimbursement)
    private RecyclerView reimbursementView;

    private IFinancialPresenter mFinancialPresenter;
    private IMinePresenter mMinePresenter;
    private ReimbursementAdapter mReimbursementAdapter;
    private MineReimbursementAdapter mMineReimbursementAdapter;


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
                break;
        }
    }

    @Override
    protected void initEvent() {
        departStaff.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("部门员工");
            }
        });
        typeView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("类型");
            }
        });
        payTeam.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("支付方");
            }
        });
        periodTime.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("时间段");
            }
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
    public void getMineReimbursementRecords(List<MineReimbursement> reimbursementList) {
        // 我的-- 费用报销
        mMineReimbursementAdapter.setList(reimbursementList);
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
