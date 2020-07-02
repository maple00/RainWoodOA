package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.utils.Constants;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/19 17:23
 * @Desc: 添加费用计提
 */
public final class AddProvisionActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.cet_provision_money)
    private EditText provisionMoney;
    @ViewInject(R.id.tv_fee_desc)
    private TextView feeDescTV;
    @ViewInject(R.id.cet_fee_desc)
    private EditText feeDesc;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_provision;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setText(getString(R.string.common_confirm));

        showSoftInputFromWindow(provisionMoney);
        setRequiredValue(feeDescTV, "费用说明");
    }

    @Override
    protected void initPresenter() {

    }

    @OnClick({R.id.tv_page_right_title, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                if (TextUtils.isEmpty(provisionMoney.getText())) {
                    toast("请填写计提金额");
                    return;
                }
                if (TextUtils.isEmpty(feeDesc.getText())) {
                    toast("请填写费用说明");
                    return;
                }
                Intent demandIntent = new Intent();
                demandIntent.putExtra("money", provisionMoney.getText().toString().trim());
                demandIntent.putExtra("used", feeDesc.getText().toString().trim());
                setResult(Constants.COST_OF_PROVISION, demandIntent);
                finish();
                break;
        }
    }
}
