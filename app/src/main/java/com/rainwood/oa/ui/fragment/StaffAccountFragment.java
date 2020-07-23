package com.rainwood.oa.ui.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.activity.PaymentDetailActivity;
import com.rainwood.oa.ui.adapter.StaffAccountAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 会计账户
 */
public final class StaffAccountFragment extends BaseFragment implements IStaffCallbacks, StatusAction {

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_account_list)
    private RecyclerView accountContentList;
    @ViewInject(R.id.et_search_tips)
    private EditText searchTip;

    @ViewInject(R.id.line_all)
    private View checkedAll;
    @ViewInject(R.id.line_income)
    private View checkedIncome;
    @ViewInject(R.id.line_speeding)
    private View checkedSpeeding;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IStaffPresenter mStaffPresenter;
    private StaffAccountAdapter mAccountAdapter;
    private String mStaffId;
    private int pageCount = 1;
    private boolean isClickType = false;
    private String queryType = "accountAll";
    private String mKeyWord;
    private String mStartTime;
    private String mEndTime;

    public StaffAccountFragment(String staffId) {
        mStaffId = staffId;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_staff_account;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        // 设置布局管理器
        accountContentList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        accountContentList.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mAccountAdapter = new StaffAccountAdapter();
        // 设置适配器
        accountContentList.setAdapter(mAccountAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据-- 默认请求全部数据
        netDataLoading(null);
    }

    /**
     * 网络数据请求
     */
    private void netDataLoading(String startTime) {
        showDialog();
        mStaffPresenter.requestAllAccountData(queryType, mKeyWord, startTime, mEndTime, pageCount = 1);
    }

    @Override
    protected void initListener() {
        mAccountAdapter.setItemAccount(account -> {
            // 查看会计账户详情
            PageJumpUtil.staffAccount2Detail(getContext(), PaymentDetailActivity.class, account.getId());
        });
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mStaffPresenter.requestAllAccountData(queryType, mKeyWord, TextUtils.isEmpty(mEndTime) ?"":mStartTime, mEndTime, ++pageCount);
            }
        });
        // 搜索监听
        searchTip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mKeyWord = s.toString();
                mStaffPresenter.requestAllAccountData(queryType, mKeyWord, TextUtils.isEmpty(mEndTime) ? "" : mStartTime,
                        mEndTime, pageCount = 1);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search, R.id.tv_screen_time, R.id.iv_screen_time, R.id.tv_query_all,
            R.id.line_all, R.id.tv_income, R.id.line_income, R.id.tv_speeding, R.id.line_speeding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                Objects.requireNonNull(getActivity()).finish();
                break;
            case R.id.tv_search:
                toast("搜索\n 接口没字段");
                break;
            case R.id.iv_screen_time:
            case R.id.tv_screen_time:
                new StartEndDateDialog.Builder(getContext(), false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setStartTime(mStartTime)
                        .setEndTime(mEndTime)
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

                                dialog.dismiss();
                                mStartTime = startTime;
                                mEndTime = endTime;
                                // toast("选中的时间段：" + startTime + "至" + endTime);
                                showDialog();
                                mStaffPresenter.requestAllAccountData(queryType, mKeyWord, mStartTime, mEndTime, pageCount = 1);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_query_all:
            case R.id.line_all:
                isClickType = true;
                checkedType(true, false, false);
                // 查询全部
                queryType = "accountAll";
                netDataLoading(TextUtils.isEmpty(mEndTime) ? "" : mStartTime);
                break;
            case R.id.tv_income:
            case R.id.line_income:
                isClickType = true;
                checkedType(false, true, false);
                // 查询收入
                queryType = "accountIn";
                netDataLoading(TextUtils.isEmpty(mEndTime) ? "" : mStartTime);
                break;
            case R.id.tv_speeding:
            case R.id.line_speeding:
                isClickType = true;
                checkedType(false, false, true);
                // 查询支出
                queryType = "accountOut";
                netDataLoading(TextUtils.isEmpty(mEndTime) ? "" : mStartTime);
                break;
        }
    }

    /**
     * 设置选中状态
     *
     * @param b
     * @param b2
     * @param b3
     */
    private void checkedType(boolean b, boolean b2, boolean b3) {
        // 选中状态flag-- 默认选中全部
        checkedAll.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
        checkedIncome.setVisibility(b2 ? View.VISIBLE : View.INVISIBLE);
        checkedSpeeding.setVisibility(b3 ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void getAccountTypes(List<StaffAccountType> accountTypes) {

    }

    @Override
    public void getAccountData(List<StaffAccount> accountList) {
        pageRefresh.finishLoadmore();
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        if (pageCount != 1) {
            if (ListUtils.getSize(accountList) == 0) {
                toast("");
            }
            mAccountAdapter.addData(accountList);
        } else {
            if (ListUtils.getSize(accountList) == 0) {
                showEmpty();
            }
            mAccountAdapter.setAccountList(accountList);
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        if (isShowDialog()) {
            hideDialog();
        }
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
