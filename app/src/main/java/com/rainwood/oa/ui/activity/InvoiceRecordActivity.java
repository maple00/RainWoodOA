package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.InvoiceRecordAdapter;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/30 17:41
 * @Desc: 客户开票记录
 */
public final class InvoiceRecordActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_price_tax_summary)
    private TextView priceTaxSummary;
    @ViewInject(R.id.tv_invoice_money)
    private TextView invoiceMoney;
    @ViewInject(R.id.tv_tax_money)
    private TextView taxMoney;
    @ViewInject(R.id.tv_rate)
    private TextView rate;
    @ViewInject(R.id.mlv_invoice_records)
    private MeasureListView invoiceRecords;
    @ViewInject(R.id.ll_apply_invoice)
    private LinearLayout applyInvoice;

    private InvoiceRecordAdapter mInvoiceRecordAdapter;
    private IRecordManagerPresenter mRecordManagerPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_record;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        pageTop.setBackgroundColor(getColor(R.color.colorPrimary));
        StatusBarUtils.setPaddingSmart(this, pageTop);
        StatusBarUtils.darkMode(this, false);
        pageTitle.setText(title);
        pageTitle.setTextColor(getColor(R.color.white));
        // 创建适配器
        mInvoiceRecordAdapter = new InvoiceRecordAdapter();
        // 设置适配器
        invoiceRecords.setAdapter(mInvoiceRecordAdapter);
    }

    @Override
    protected void initEvent() {
        int applyInvoiceHeight = applyInvoice.getMeasuredHeight();
        /*ViewGroup.LayoutParams layoutParams = applyInvoice.getLayoutParams();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(this, null);
        layoutParams.setMargins(0, 0, 0, FontSwitchUtil.dip2px(this, applyInvoiceHeight));
        applyInvoice.setLayoutParams(layoutParams);*/
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mRecordManagerPresenter.requestCustomInvoiceRecords(Constants.CUSTOM_ID);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu, R.id.btn_apply_open})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                toast("menu");
                break;
            case R.id.btn_apply_open:
                toast("申请开通");
                break;
        }
    }

    @Override
    public void getCustomInvoiceRecords(List<InvoiceRecord> invoiceRecordList) {
        mInvoiceRecordAdapter.setRecordList(invoiceRecordList);
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
