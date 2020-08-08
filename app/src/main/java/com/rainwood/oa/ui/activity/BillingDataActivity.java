package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CustomInvoice;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: sxs
 * @Time: 2020/5/30 14:07
 * @Desc: 开票信息
 */
public final class BillingDataActivity extends BaseActivity implements ICustomCallbacks {

    @ViewInject(R.id.ll_parent_pager)
    private LinearLayout parentPager;
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    //content
    @ViewInject(R.id.tv_company_name)
    private TextView companyName;
    @ViewInject(R.id.tv_tax_num)
    private TextView taxNum;
    @ViewInject(R.id.gtt_invoice_address)
    private GroupTextText invoiceAddress;
    @ViewInject(R.id.gtt_email_address)
    private GroupTextText emailAddress;
    @ViewInject(R.id.gtt_lineal_tel)
    private GroupTextText lineTel;
    @ViewInject(R.id.gtt_open_bank)
    private GroupTextText openBank;
    @ViewInject(R.id.gtt_bank_num)
    private GroupTextText bankNum;

    private ICustomPresenter mCustomPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_billing_data;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        pageTitle.setText(title);
        pageTitle.setTextColor(getColor(R.color.white));
    }

    @Override
    protected void loadData() {
        //request 开票信息
        mCustomPresenter.requestCustomInvoice(Constants.CUSTOM_ID);
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_copy_tax_num, R.id.btn_copy_invoice_data, R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_copy_tax_num:
                // taxNum
                ClipboardUtil.clipFormat2Board(this, "customTaxNam", taxNum.getText().toString().trim());
                toast("已复制");
                break;
            case R.id.btn_copy_invoice_data:
                // 复制开票信息
                String clipBoardData = "公司名称：" + companyName.getText().toString().trim() + "，" +
                        "纳税人识别号：" + taxNum.getText().toString().trim() + "，" +
                        "开票地址：" + invoiceAddress.getValue() + "，" +
                        "邮寄地址：湖南省长沙市岳麓区中南大学研究院南楼，" +
                        "公司座机：" + lineTel.getValue() + "，" +
                        "开户行：" + openBank.getValue() + "，" +
                        "对公账号：" + bankNum.getValue();
                ClipboardUtil.clipFormat2Board(this, "customInvoiceData", clipBoardData);
                toast("文本已复制");
                break;
            case R.id.iv_menu:
                showQuickFunction(this, parentPager);
                break;
        }
    }

    @Override
    public void getCustomInvoice(CustomInvoice invoice) {
        // 获取返回数据
        companyName.setText(invoice.getCompanyName());
        taxNum.setText(invoice.getCompanyNum());
        invoiceAddress.setValue(invoice.getCompanyAddress());
        lineTel.setValue(invoice.getLandline());
        openBank.setValue(invoice.getCompanyBank());
        bankNum.setValue(invoice.getCompanyBankNum());
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
