package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.dialog.SelectorListDialog;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.ArrayList;
import java.util.List;

/**
 * create by a797s in 2020/5/14 11:38
 *
 * @Description : 新建客户activity
 * @Usage :
 **/
public final class CustomNewActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // 必填信息
    @ViewInject(R.id.tv_contact)
    private TextView contactTV;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumber;
    @ViewInject(R.id.tv_follow_status)
    private TextView followStatus;
    @ViewInject(R.id.tv_demand_detail)
    private TextView demandDetail;
    @ViewInject(R.id.tv_company_name)
    private TextView companyNameTV;
    // 填写内容
    @ViewInject(R.id.cet_contact)
    private EditText contactCET;
    @ViewInject(R.id.cet_tel_number)
    private EditText telNumberCET;
    @ViewInject(R.id.cet_wxNumber)
    private EditText wxNumber;
    @ViewInject(R.id.cet_qqNumber)
    private EditText qqNumber;
    @ViewInject(R.id.cet_post)
    private EditText post;
    @ViewInject(R.id.tv_follow_status_content)
    private TextView followContent;
    @ViewInject(R.id.tv_custom_origin)
    private TextView customOrigin;
    @ViewInject(R.id.cet_custom_origin_note)
    private EditText customOriginNote;
    @ViewInject(R.id.cet_budget)
    private EditText budget;
    @ViewInject(R.id.cet_industry)
    private EditText industry;
    @ViewInject(R.id.tv_demand_detail_content)
    private TextView demandContent;
    @ViewInject(R.id.cet_company_name)
    private EditText companyName;
    @ViewInject(R.id.cet_tax_num)
    private EditText taxNum;
    @ViewInject(R.id.cet_mailing_address)
    private EditText mailingAddress;
    @ViewInject(R.id.cet_billing_address)
    private EditText billingAddress;
    @ViewInject(R.id.cet_special_plane)
    private EditText specialPlane;
    @ViewInject(R.id.cet_opening_bank)
    private EditText openingBank;
    @ViewInject(R.id.cet_brought_account)
    private EditText broughtAccount;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_new;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        setRequiredValue(contactTV, ">联系人</font>");
        setRequiredValue(telNumber, ">手机号</font>");
        setRequiredValue(followStatus, ">跟进状态</font>");
        setRequiredValue(demandDetail, ">需求详情</font>");
        setRequiredValue(companyNameTV, ">公司名称</font>");
    }

    /**
     * 设置必填信息
     *
     * @param requestedText text
     * @param s             value
     */
    private void setRequiredValue(TextView requestedText, String s) {
        requestedText.setText(Html.fromHtml("<font color=" + this.getColor(R.color.colorMiddle)
                + " size=" + FontSwitchUtil.dip2px(this, 16f) + s +
                "<font color=" + this.getColor(R.color.red05) + " size= "
                + FontSwitchUtil.dip2px(this, 13f) + ">*</font>"));
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {
        // 客户来源备注
        customOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customOriginNote.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_follow_status_content, R.id.btn_confirm,
            R.id.tv_custom_origin, R.id.tv_demand_detail_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_follow_status_content:
                // 选择跟进状态
                // toast("选择跟进状态");
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    data.add("我是数据" + i);
                }
                // 底部选择框
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(data)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                followContent.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                // dialog.dismiss();
                                followContent.setText("");
                            }

                        })
                        .show();
                break;
            case R.id.tv_custom_origin:
                // 选择客户来源
                List<String> data2 = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data2.add("我是数据" + i);
                }
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(data2)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                customOrigin.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                // dialog.dismiss();
                                customOrigin.setText("");
                            }

                        })
                        .show();
                break;
            case R.id.tv_demand_detail_content:
                // 填写需求详情
                startActivity(getNewIntent(this, DemandWriteActivity.class, "需求详情"));
                break;
            case R.id.btn_confirm:
                //确认新建
                startActivity(getNewIntent(this, CustomDetailActivity.class, "客户详情"));
                break;
        }
    }
}
