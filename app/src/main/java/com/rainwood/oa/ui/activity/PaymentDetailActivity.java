package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.StaffAccount;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

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
    @ViewInject(R.id.iv_doubt)
    private ImageView mImageDoubt;

    private IStaffPresenter mStaffPresenter;
    private String mIco;

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
    @OnClick({R.id.iv_page_back, R.id.iv_doubt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_doubt:
                if (mIco.endsWith(".jpg") || mIco.endsWith(".png")) {
                    FileManagerUtil.queryBigPicture(this, mIco);
                } else {
                    toast("图片错误");
                }
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
        mIco = account.getIco();
        Glide.with(this).load(account.getIco())
                .error(R.drawable.ic_hint_empty)
                .placeholder(R.drawable.ic_hint_empty)
                .into(mImageDoubt);
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
