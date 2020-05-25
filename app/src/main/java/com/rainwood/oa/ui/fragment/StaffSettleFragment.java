package com.rainwood.oa.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.model.domain.StaffAccountType;
import com.rainwood.oa.model.domain.StaffSettlement;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.activity.PaymentDetailActivity;
import com.rainwood.oa.ui.adapter.StaffAccountAdapter;
import com.rainwood.oa.ui.adapter.StaffSettlementAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 结算账户
 */
public final class StaffSettleFragment extends BaseFragment implements IStaffCallbacks {

    // content
  /*  @ViewInject(R.id.tl_staff_account_type)
    private TabLayout accountType;
    @ViewInject(R.id.vp_staff_account_pager)
    private ViewPager accountPager;*/
    @ViewInject(R.id.rv_account_list)
    private RecyclerView accountContentList;


    private IStaffPresenter mStaffPresenter;
    private StaffSettlementAdapter mSettlementAdapter;

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
        mSettlementAdapter = new StaffSettlementAdapter();
        // 设置适配器
        accountContentList.setAdapter(mSettlementAdapter);
    }

    @Override
    protected void initListener() {

    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search, R.id.tv_screen_time, R.id.iv_screen_time})
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
        }
    }

    @Override
    protected void loadData() {
        // 请求数据
        mStaffPresenter.requestAllSettlementData();
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    public void getSettlementData(List<StaffSettlement> settlementList) {
        mSettlementAdapter.setSettlementList(settlementList);
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