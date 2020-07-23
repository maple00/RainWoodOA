package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.TeamFunds;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.utils.Null;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.TeamFundsAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.widget.XCollapsingToolbarLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
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

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/5 9:56
 * @Desc: 团队基金、会计账户（个人中心）、结算账户（个人中心）activity
 */
public final class AccountFundsActivity extends BaseActivity implements IFinancialCallbacks, IMineCallbacks
        , StatusAction {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageBack;
    @ViewInject(R.id.tv_search)
    private TextView searchView;
    @ViewInject(R.id.et_search_tips)
    private TextView searchTips;
    // content
    @ViewInject(R.id.app_layout_bar)
    private AppBarLayout mAppBarLayout;
    @ViewInject(R.id.xtl_team_funds)
    private XCollapsingToolbarLayout mToolbarLayout;
    @ViewInject(R.id.ll_balance)
    private LinearLayout balanceView;
    @ViewInject(R.id.tv_account_balance)
    private TextView balanceValue;
    @ViewInject(R.id.tv_balance)
    private TextView balanceDesc;
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
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_team_cost)
    private RecyclerView teamCostView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IFinancialPresenter mFinancialPresenter;
    private IMinePresenter mMinePresenter;
    private TeamFundsAdapter mFundsAdapter;

    private int pageCount = 1;
    private String mStartTime;
    private String mEndTime;
    private String mSearchContent;
    private String mType;
    private String mSearchText;

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
        // 设置TextView加粗
        mTextQueryAll.getPaint().setFakeBoldText(true);
        mTextQueryAll.invalidate();
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

    /**
     * 请求数据
     *
     * @param type
     */
    private void requestData(String type) {
        showDialog();
        switch (moduleMenu) {
            case "teamFunds":
            case "personTeamAccount":
                // 个人中心---团队基金
                // 管理--团队基金
                mType = type;
                mFinancialPresenter.requestTeamFundsData(mSearchText, mType, mStartTime, mEndTime, pageCount = 1);
                break;
            case "accountAccount":
                // 个人中心---会计账户
                mMinePresenter.requestAccountingAccount(type);
                break;
            case "settlementAccount":
                // 个人中心---结算账户
                mMinePresenter.requestSettlementAccount(type);
                break;
        }
    }

    @Override
    protected void initEvent() {
        // 滑动监听
        teamCostView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollY = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //未滚动
                    if (!recyclerView.canScrollVertically(-1) && scrollY < -10) {
                        //do somthing
                        mAppBarLayout.setExpanded(true, true);//通知AppBarLayout伸展
                        scrollY = 0;
                    }
                }
                //判断慢速滚动：当滚动到顶部时靠手指拖动后的惯性让RecyclerView处于Fling状态时的速度大于5时，让AppBarLayout展开
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    //正在滚动中，惯性滚动
                    if (!recyclerView.canScrollVertically(-1) && scrollY < -5) {
                        //do somthing
                        mAppBarLayout.setExpanded(true, true);
                        scrollY = 0;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY = dy;
            }
        });
        // 搜索内容监听
        searchTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchText = s.toString();
                switch (moduleMenu) {
                    case "accountAccount":
                        break;
                    case "teamFunds":
                    case "personTeamAccount":
                        // 团队基金
                        mFinancialPresenter.requestTeamFundsData(mSearchText, mType,
                                mStartTime, mEndTime, pageCount = 1);
                        break;
                    case "settlementAccount":
                        // 结算账户
                        break;
                }
            }
        });
        // 加载更多
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                switch (moduleMenu) {
                    case "accountAccount":
                        break;
                    case "teamFunds":
                    case "personTeamAccount":
                        // 团队基金
                        mFinancialPresenter.requestTeamFundsData(mSearchText, mType,
                                mStartTime, mEndTime, ++pageCount);
                        break;
                    case "settlementAccount":
                        // 结算账户
                        break;
                }
            }
        });
    }


    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_screen_time, R.id.tv_screen_time, R.id.tv_query_all,
            R.id.tv_allocated, R.id.tv_un_allocated, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_screen_time:
            case R.id.tv_screen_time:
                new StartEndDateDialog.Builder(this, false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setStartTime(mStartTime)
                        .setEndTime(mEndTime)
                        .setCanceledOnTouchOutside(true)
                        .setAutoDismiss(false)
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
                                dialog.dismiss();
                                showDialog();
                                switch (moduleMenu) {
                                    case "accountAccount":
                                        break;
                                    case "teamFunds":
                                    case "personTeamAccount":
                                        // 团队基金
                                        mStartTime = startTime;
                                        mEndTime = endTime;
                                        mFinancialPresenter.requestTeamFundsData(mSearchText, mType,
                                                mStartTime, mEndTime, pageCount = 1);
                                        break;
                                    case "settlementAccount":
                                        // 结算账户
                                        break;
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_query_all:
                // toast("全部");
                if (lineAll.getVisibility() == View.VISIBLE) {
                    return;
                }
                //showDialog();
                setTextBold(true, false, false);
                setStatusLine(true, false, false);
                requestData("");
                break;
            case R.id.tv_allocated:
                // toast("收入");
                if (lineAllocated.getVisibility() == View.VISIBLE) {
                    return;
                }
                //showDialog();
                setTextBold(false, true, false);
                setStatusLine(false, true, false);
                requestData("收入");
                break;
            case R.id.tv_un_allocated:
                // toast("支出");
                if (lineUnAllocated.getVisibility() == View.VISIBLE) {
                    return;
                }
                // showDialog();
                setTextBold(false, false, true);
                setStatusLine(false, false, true);
                requestData("支出");
                break;
            case R.id.tv_search:
                if (Null.isNull(searchTips)) {
                    toast(searchTips.getHint());
                    return;
                }
                showDialog();
                mSearchContent = searchTips.getText().toString().trim();
                mFinancialPresenter.requestTeamFundsData(mSearchContent, mType,
                        mStartTime, mEndTime, pageCount = 1);
                break;
        }
    }

    private void setTextBold(boolean b, boolean b2, boolean b3) {
        mTextQueryAll.getPaint().setFakeBoldText(b);
        mTextAllocated.getPaint().setFakeBoldText(b2);
        mTextUnAllocated.getPaint().setFakeBoldText(b3);
        mTextQueryAll.invalidate();
        mTextAllocated.invalidate();
        mTextUnAllocated.invalidate();
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
        pagerRefresh.finishLoadmore();
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        balanceValue.setText(money);
        if (pageCount != 1) {
            toast("为您加载了" + ListUtils.getSize(fundsList) + "条数据");
            mFundsAdapter.addData(fundsList);
        } else {
            if (ListUtils.getSize(fundsList) == 0) {
                showEmpty();
            }
            mFundsAdapter.setList(fundsList);
        }
    }

    @Override
    public void getAccountListData(List<TeamFunds> accountList, String money) {
        pagerRefresh.finishLoadmore();
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
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

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
