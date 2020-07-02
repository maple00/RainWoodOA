package com.rainwood.oa.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.activity.PaymentDetailActivity;
import com.rainwood.oa.ui.adapter.StaffAccountAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 会计账户
 */
public final class StaffAccountFragment extends BaseFragment implements IStaffCallbacks {

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_account_list)
    private RecyclerView accountContentList;

    @ViewInject(R.id.line_all)
    private View checkedAll;
    @ViewInject(R.id.line_income)
    private View checkedIncome;
    @ViewInject(R.id.line_speeding)
    private View checkedSpeeding;

    private IStaffPresenter mStaffPresenter;
    private StaffAccountAdapter mAccountAdapter;
    private String mStaffId;

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
        mStaffPresenter.requestAllAccountData("accountAll");
    }

    @Override
    protected void initListener() {
        mAccountAdapter.setItemAccount(account -> {
            // 查看会计账户详情
            PageJumpUtil.staffAccount2Detail(getContext(), PaymentDetailActivity.class, account.getId());
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
                toast("搜索");
                break;
            case R.id.iv_screen_time:
            case R.id.tv_screen_time:
                new StartEndDateDialog.Builder(getContext(), false)
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
                checkedType(true, false, false);
                // 查询全部
                mStaffPresenter.requestAllAccountData("accountAll");
                break;
            case R.id.tv_income:
            case R.id.line_income:
                checkedType(false, true, false);
                // 查询收入
                mStaffPresenter.requestAllAccountData("accountIn");
                break;
            case R.id.tv_speeding:
            case R.id.line_speeding:
                checkedType(false, false, true);
                // 查询支出
                mStaffPresenter.requestAllAccountData("accountOut");
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
        mAccountAdapter.setAccountList(accountList);
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
