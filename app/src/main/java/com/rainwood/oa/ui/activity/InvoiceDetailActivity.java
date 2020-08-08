package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.InvoiceDetailData;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/7/21 17:05
 * @Desc: 开票记录详情
 */
public final class InvoiceDetailActivity extends BaseActivity implements IRecordCallbacks {

    // ActionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.git_state)
    private GroupIconText state;
    @ViewInject(R.id.tv_invoice_name)
    private TextView invoiceName;
    @ViewInject(R.id.tv_sellers)
    private TextView sellers;
    @ViewInject(R.id.tv_purchaser)
    private TextView purchaser;
    @ViewInject(R.id.tv_invoice_type)
    private TextView invoiceType;
    @ViewInject(R.id.tv_levied_in_tax)
    private TextView leviedInTax;
    @ViewInject(R.id.tv_invoice_amount)
    private TextView invoiceAmount;
    @ViewInject(R.id.tv_invoice_tax)
    private TextView invoiceTax;
    @ViewInject(R.id.tv_invoice_note)
    private TextView invoiceNote;
    @ViewInject(R.id.iv_invoice_ico)
    private ImageView invoiceIco;

    private IRecordManagerPresenter mRecordManagerPresenter;
    private String mIcoPath;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // TODO: 开票记录详情
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String hasOpen = getIntent().getStringExtra("hasOpen");
        state.setValue("是".equals(hasOpen) ? "已开票" : "未开票");
        state.setIcon("是".equals(hasOpen) ? R.drawable.ic_status_finished : R.drawable.ic_status_wait_in);
        String invoiceId = getIntent().getStringExtra("invoiceId");
        if (invoiceId != null) {
            showDialog();
            mRecordManagerPresenter.requestInvoiceDetail(invoiceId);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_invoice_ico})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_invoice_ico:
                FileManagerUtil.queryBigPicture(this, mIcoPath);
                break;
        }
    }

    @Override
    public void getInvoiceDetail(InvoiceDetailData invoiceDetail) {
        if (isShowDialog()) {
            hideDialog();
        }
        invoiceName.setText(invoiceDetail.getStffName());
        sellers.setText(invoiceDetail.getCompany());
        purchaser.setText(invoiceDetail.getCompanyName());
        invoiceType.setText(invoiceDetail.getType());
        leviedInTax.setText(String.valueOf(invoiceDetail.getMoney()));
        invoiceAmount.setText(String.valueOf(invoiceDetail.getMoneyInvoice()));
        invoiceTax.setText(String.valueOf(invoiceDetail.getMoneyTax()));
        invoiceNote.setText(invoiceDetail.getText());
        mIcoPath = invoiceDetail.getIco();
        Glide.with(this).load(mIcoPath)
                .placeholder(R.drawable.ic_hint_empty)
                .into(invoiceIco);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mRecordManagerPresenter != null) {
            mRecordManagerPresenter.unregisterViewCallback(this);
        }
    }
}
