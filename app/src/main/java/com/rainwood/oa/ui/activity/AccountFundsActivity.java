package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.TeamFundsAdapter;
import com.rainwood.oa.ui.widget.XCollapsingToolbarLayout;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 9:56
 * @Desc: 团队基金、会计账户（个人中心）、结算账户（个人中心）activity
 */
public final class AccountFundsActivity extends BaseActivity implements IFinancialCallbacks, IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageBack;
    @ViewInject(R.id.tv_search)
    private TextView searchView;
    // content
    @ViewInject(R.id.xtl_team_funds)
    private XCollapsingToolbarLayout mToolbarLayout;
    @ViewInject(R.id.ll_balance)
    private LinearLayout balanceView;
    @ViewInject(R.id.tv_account_balance)
    private TextView balanceValue;
    @ViewInject(R.id.tv_balance)
    private TextView balanceDesc;
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.line_allocated)
    private View lineAllocated;
    @ViewInject(R.id.line_un_allocated)
    private View lineUnAllocated;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_team_cost)
    private RecyclerView teamCostView;
    @ViewInject(R.id.tv_tips)
    private TextView bottomTips;

    private IFinancialPresenter mFinancialPresenter;
    private IMinePresenter mMinePresenter;
    private TeamFundsAdapter mFundsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_team_funds;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        setScrollViewBG(false, R.drawable.ic_page_return_white, R.color.colorPrimary, R.color.white);
        switch (moduleMenu) {
            case "accountAccount":
                balanceDesc.setText("会计账户(元)");
                break;
            case "teamFunds":
                // 团队基金
                balanceDesc.setText("账户余额(元)");
                break;
            case "settlementAccount":
                // 结算账户
                balanceDesc.setText("结算账户(元)");
                break;
            case "personTeamAccount":
                balanceDesc.setText("团队基金(元)");
                break;
        }
        // 设置布局管理器
        teamCostView.setLayoutManager(new GridLayoutManager(this, 1));
        teamCostView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mFundsAdapter = new TeamFundsAdapter();
        // 设置适配器
        teamCostView.setAdapter(mFundsAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
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
        requestData("");
    }

    @Override
    protected void initEvent() {
        // 滑动属性设置
        mToolbarLayout.setOnScrimsListener((layout, shown) -> {
            if (shown) {
                setScrollViewBG(true, R.drawable.ic_page_return, R.color.white, R.color.fontColor);
            } else {
                setScrollViewBG(false, R.drawable.ic_page_return_white, R.color.colorPrimary, R.color.white);
            }
        });
        // 滑动监听
        teamCostView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    if (manager != null) {
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();
                        if ((lastVisibleItem == (totalItemCount - 1))) {
                            LogUtils.d("sxs", "滑动到底部了.........");
                        }
                        // 判断是否滚动到底部
                        bottomTips.setVisibility((lastVisibleItem == (totalItemCount - 1))
                                ? View.VISIBLE : View.INVISIBLE);
                    }
                }
            }
        });
    }


    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_screen_time, R.id.tv_screen_time, R.id.tv_query_all,
            R.id.tv_allocated, R.id.tv_un_allocated})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_screen_time:
            case R.id.tv_screen_time:
                toast("选择时间");
                break;
            case R.id.tv_query_all:
                // toast("全部");
                setStatusLine(true, false, false);
                requestData("");
                break;
            case R.id.tv_allocated:
                // toast("收入");
                setStatusLine(false, true, false);
                requestData("收入");
                break;
            case R.id.tv_un_allocated:
                // toast("支出");
                setStatusLine(false, false, true);
                requestData("支出");
                break;
        }
    }

    /**
     * 请求数据
     * @param type
     */
    private void requestData(String type) {
        switch (moduleMenu) {
            case "teamFunds":
                // 管理--团队基金
                mFinancialPresenter.requestTeamFundsData(type);
                break;
            case "accountAccount":
                // 个人中心---会计账户
                mMinePresenter.requestAccountingAccount(type);
                break;
            case "settlementAccount":
                // 个人中心---结算账户
                mMinePresenter.requestSettlementAccount(type);
                break;
            case "personTeamAccount":
                // 个人中心---团队基金
                mFinancialPresenter.requestTeamFundsData(type);
                break;
        }
    }

    /**
     * 设置滑动属性
     *
     * @param shown
     * @param pageBackView
     * @param pageTopView
     * @param TextColor
     */
    private void setScrollViewBG(boolean shown, int pageBackView, int pageTopView, int TextColor) {
        StatusBarUtils.darkMode(AccountFundsActivity.this, shown);
        pageBack.setImageResource(pageBackView);
        pageTop.setBackgroundColor(getColor(pageTopView));
        searchView.setTextColor(getColor(TextColor));
        balanceView.setBackgroundColor(getColor(pageTopView));
        balanceValue.setTextColor(getColor(TextColor));
        balanceDesc.setTextColor(getColor(TextColor));
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
    public void getTeamFundsData(List<TeamFunds> fundsList, String money) {
        mFundsAdapter.setList(fundsList);
        balanceValue.setText(money);
    }

    @Override
    public void getAccountListData(List<TeamFunds> accountList, String money) {
        mFundsAdapter.setList(accountList);
        balanceValue.setText(money);
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
