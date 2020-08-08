package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineReimbursement;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/6/4 18:03
 * @Desc: 费用详情activity
 */
public final class CostDetailActivity extends BaseActivity implements IFinancialCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_applier)
    private TextView mTextApplier;
    @ViewInject(R.id.tv_type)
    private TextView mTextType;
    @ViewInject(R.id.tv_money)
    private TextView mTextMoney;
    @ViewInject(R.id.tv_payer)
    private TextView mTextPayer;
    @ViewInject(R.id.tv_reason)
    private TextView mTextReason;
    @ViewInject(R.id.tv_time)
    private TextView mTextTime;
    @ViewInject(R.id.iv_voucher)
    private ImageView mImageVoucher;
    @ViewInject(R.id.git_state)
    private GroupIconText mState;


    private IFinancialPresenter mFinancialPresenter;
    private MineReimbursement mReimbursement;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cost_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.darkMode(this, false);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String reimburseId = getIntent().getStringExtra("reimburseId");
        if (reimburseId != null) {
            // TODO: 费用报销详情
            mFinancialPresenter.requestReimburseDetail(reimburseId);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_voucher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_voucher:
                ImageActivity.start(this, mReimbursement.getIco());
                break;
        }
    }

    @Override
    public void getReimburseDetail(MineReimbursement reimbursement) {
        mReimbursement = reimbursement;
        mTextApplier.setText(reimbursement.getStaffName());
        mTextType.setText(reimbursement.getType());
        mTextMoney.setText(reimbursement.getMoney());
        mTextPayer.setText(reimbursement.getPayer());
        mTextReason.setText(reimbursement.getText());
        mTextTime.setText(reimbursement.getPayDate());
        Glide.with(this)
                .load(reimbursement.getIco())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(mImageVoucher);
    }

    @Override
    public void onError(String tips) {
        toast(tips);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mFinancialPresenter != null) {
            mFinancialPresenter.unregisterViewCallback(this);
        }
    }
}
