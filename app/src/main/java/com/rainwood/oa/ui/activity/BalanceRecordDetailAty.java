package com.rainwood.oa.ui.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.BalanceDetailData;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/7/22 11:10
 * @Desc: 收支记录详情
 */
public final class BalanceRecordDetailAty extends BaseActivity implements IFinancialCallbacks {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.git_state)
    private GroupIconText mGroupState;
    @ViewInject(R.id.tv_balance_origin)
    private TextView balanceOrigin;
    @ViewInject(R.id.tv_balance_type)
    private TextView balanceType;
    @ViewInject(R.id.tv_balance_money)
    private TextView balanceMoney;
    @ViewInject(R.id.tv_balance_time)
    private TextView balanceTime;
    @ViewInject(R.id.tv_balance_note)
    private TextView balanceNote;
    @ViewInject(R.id.iv_balance_voucher)
    private ImageView balanceVoucher;

    private IFinancialPresenter mFinancialPresenter;
    private String mIcoPath;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_record_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String recordId = getIntent().getStringExtra("recordId");
        if (recordId != null) {
            showDialog();
            mFinancialPresenter.requestBalanceDetail(recordId);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_balance_voucher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_balance_voucher:
                FileManagerUtil.queryBigPicture(this, mIcoPath);
                break;
        }
    }

    @Override
    public void getBalanceDetailData(BalanceDetailData balanceDetailData) {
        if (isShowDialog()) {
            hideDialog();
        }
        balanceOrigin.setText(balanceDetailData.getTarget());
        balanceType.setText(balanceDetailData.getType());
        balanceMoney.setText(String.valueOf(balanceDetailData.getMoney()));
        balanceTime.setText(balanceDetailData.getPayDate());
        balanceNote.setText(balanceDetailData.getText());
        mIcoPath = balanceDetailData.getIco();
        Glide.with(this).load(mIcoPath)
                .placeholder(R.drawable.ic_hint_empty)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mGroupState.setIcon(R.drawable.ic_status_wait_in);
                        mGroupState.setValue("未开票");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mGroupState.setIcon(R.drawable.ic_status_finished);
                        mGroupState.setValue("已开票");
                        return false;
                    }
                })
                .into(balanceVoucher);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
