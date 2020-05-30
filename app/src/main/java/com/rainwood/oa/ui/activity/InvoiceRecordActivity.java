package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.InvoiceRecordAdapter;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
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
    @ViewInject(R.id.nsv_invoice_record)
    private NestedScrollView invoiceRecordNest;
    @ViewInject(R.id.ll_apply_invoice)
    private LinearLayout applyInvoice;
    @ViewInject(R.id.btn_apply_open)
    private Button applyOpen;

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
        int applyInvoiceHeight = applyInvoice.getMinimumHeight();
        int measuredHeight = applyOpen.getMinHeight();
        LogUtils.d("sxs", "applyInvoiceHeight---- " + applyInvoiceHeight);
        LogUtils.d("sxs", "measuredHeight---- " + measuredHeight);
        invoiceRecordNest.setPadding(0, 0, 0, measuredHeight + 70);
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
