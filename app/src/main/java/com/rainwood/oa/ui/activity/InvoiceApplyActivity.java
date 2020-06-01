package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.dialog.SelectorListDialog;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/1 9:57
 * @Desc: 开票申请
 */
public final class InvoiceApplyActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_sellers)
    private TextView sellerTv;
    @ViewInject(R.id.tv_invoice_type)
    private TextView invoiceTypeTv;
    @ViewInject(R.id.tv_tax_total)
    private TextView taxTotalTv;
    @ViewInject(R.id.tv_seller_name)
    private TextView sellerName;
    @ViewInject(R.id.cb_normal_ticket)
    private CheckBox normalTicket;
    @ViewInject(R.id.cb_special_ticket)
    private CheckBox specialTicket;
    @ViewInject(R.id.cet_price_tax_total)
    private EditText priceTaxTotal;
    @ViewInject(R.id.tv_invoice_money)
    private TextView invoiceMoneyTv;
    @ViewInject(R.id.tv_tax_money)
    private TextView taxMoneyTv;
    @ViewInject(R.id.cet_note)
    private EditText note;

    private IRecordManagerPresenter mRecordManagerPresenter;
    private List<String> mSellerList;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_apply;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);

        // 设置必填属性
        setRequiredValue(sellerTv, "销售方");
        setRequiredValue(invoiceTypeTv, "发票类型");
        setRequiredValue(taxTotalTv, "价税合计");
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mRecordManagerPresenter.requestCustomInvoiceParams();
    }

    @Override
    protected void initEvent() {

        priceTaxTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void afterTextChanged(Editable s) {
                // 开票金额/1.03， 且保留两位小数
                double invoiceMoney;
                if (s != null && !"".contentEquals(s)) {
                    invoiceMoney = new BigDecimal(Double.parseDouble(s.toString()))
                            .setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
                } else {
                    invoiceMoney = 0;
                }
                invoiceMoneyTv.setText("开票金额：" + String.format("%.2f", invoiceMoney / 1.03));
                taxMoneyTv.setText("纳税金额：" + String.format("%.2f", invoiceMoney - invoiceMoney / 1.03));
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.cb_normal_ticket, R.id.tv_normal_ticket, R.id.cb_special_ticket,
            R.id.tv_special_ticket, R.id.btn_commit, R.id.tv_seller_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.cb_normal_ticket:
            case R.id.tv_normal_ticket:
                boolean normalChecked = normalTicket.isChecked();
                normalTicket.setChecked(normalChecked ? true : true);
                specialTicket.setChecked(false);
                break;
            case R.id.cb_special_ticket:
            case R.id.tv_special_ticket:
                boolean specialChecked = specialTicket.isChecked();
                normalTicket.setChecked(false);
                specialTicket.setChecked(specialChecked ? true : true);
                break;
            case R.id.tv_seller_name:
                if (ListUtils.getSize(mSellerList) == 0) {
                    toast("当前没有客户可选择，可尝试重新刷新");
                    return;
                }
                // 选择客户
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(mSellerList)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                sellerName.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                // dialog.dismiss();
                                sellerName.setText("");
                            }

                        })
                        .show();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(sellerName.getText())) {
                    toast("请选择销售方");
                    return;
                }
                if (TextUtils.isEmpty(priceTaxTotal.getText())) {
                    toast("请填写价税合计");
                    return;
                }
                String sellerStr = sellerName.getText().toString().trim();
                String ticketType = normalTicket.isChecked() ? "普票" : "专票";
                String taxPriceTotal = priceTaxTotal.getText().toString().trim();
                String noteStr = note.getText().toString().trim();
                mRecordManagerPresenter.CreateInvoiceRecord(sellerStr, ticketType, taxPriceTotal, noteStr, Constants.CUSTOM_ID);
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getCustomNewInvoiceRecordsPageParams(Map pageMap) {
        mSellerList = (List<String>) pageMap.get("sellers");
        String rate = (String) pageMap.get("rate");

    }

    @Override
    public void createInvoiceRecord(boolean flag) {
        toast(flag ? "创建成功" : "创建失败！请完善系统信息");
        postDelayed(this::finish, 500);
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
