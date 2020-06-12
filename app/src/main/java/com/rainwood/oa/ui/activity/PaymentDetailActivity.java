package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:02
 * @Desc: 员工收支详情
 */
public final class PaymentDetailActivity extends BaseActivity implements IStaffCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_payment)
    private TextView payment;
    @ViewInject(R.id.tv_money)
    private TextView money;
    @ViewInject(R.id.gtt_desc)
    private GroupTextText desc;
    @ViewInject(R.id.gtt_time)
    private GroupTextText time;

    private IStaffPresenter mStaffPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitvity_payment_details;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String accountId = getIntent().getStringExtra("accountId");
        if (accountId != null) {
            mStaffPresenter.requestStaffAccountDetailById(accountId);
        }
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
    public void getStaffAccountDetail(StaffAccount account) {
        payment.setText("收入".equals(account.getDirection())
                ? "收入金额" : "支出金额");
        money.setText("收入".equals(account.getDirection())
                ? "+" + account.getMoney() : "-" + account.getMoney());
        desc.setValue(account.getText());
        time.setValue(account.getTime());
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
