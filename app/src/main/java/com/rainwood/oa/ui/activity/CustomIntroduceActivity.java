package com.rainwood.oa.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * @Author: sxs
 * @Time: 2020/5/17 13:13
 * @Desc: 介绍客户
 */
public final class CustomIntroduceActivity extends BaseActivity {

    // top action bar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout mPageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView mPageTitle;
    // 必填项样式
    @ViewInject(R.id.tv_company_name)
    private TextView companyNameTV;
    @ViewInject(R.id.tv_contact)
    private TextView contactTV;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumberTV;
    @ViewInject(R.id.tv_demand_desc)
    private TextView demandDescTV;
    @ViewInject(R.id.tv_follow_status)
    private TextView followStatusTV;
    @ViewInject(R.id.tv_custom_origin)
    private TextView customOriginTV;
    @ViewInject(R.id.tv_introduce_to)
    private TextView introduceToTV;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_introduce_custom;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, mPageTop);
        mPageTitle.setText(title);
        setRequiredValue(companyNameTV, ">公司名称</font>");
        setRequiredValue(contactTV, ">联系人</font>");
        setRequiredValue(telNumberTV, ">手机号</font>");
        setRequiredValue(demandDescTV, ">需求描述</font>");
        setRequiredValue(followStatusTV, ">跟进状态</font>");
        setRequiredValue(customOriginTV, ">客户来源</font>");
        setRequiredValue(introduceToTV, ">介绍给</font>");

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
    protected void initPresenter() {

    }

    @OnClick({R.id.iv_warn_prompt, R.id.tv_warm_prompt, R.id.cet_demand_desc, R.id.iv_page_back,
            R.id.cet_follow_status, R.id.cet_custom_origin_note, R.id.cet_introduce_to, R.id.btn_confirm})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_warn_prompt:
            case R.id.tv_warm_prompt:
                toast("温馨提示");
                break;
            case R.id.cet_demand_desc:
                toast("填写需求描述");
                break;
            case R.id.cet_follow_status:
                toast("跟进状态");
                break;
            case R.id.cet_custom_origin_note:
                toast("客户来源");
                break;
            case R.id.cet_introduce_to:
                toast("介绍客户给....");
                break;
            case R.id.btn_confirm:
                toast("确定");
                break;
        }
    }
}
