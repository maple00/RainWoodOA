package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

/**
 * @Author: sxs
 * @Time: 2020/5/17 13:13
 * @Desc: 介绍客户
 */
public final class CustomIntroduceActivity extends BaseActivity implements ICustomCallbacks {

    // top action bar
    @ViewInject(R.id.rl_page_top)
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

    private ICustomPresenter mCustomPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_introduce_custom;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, mPageTop);
        mPageTitle.setText(title);
        setRequiredValue(companyNameTV, "公司名称");
        setRequiredValue(contactTV, "联系人");
        setRequiredValue(telNumberTV, "手机号");
        setRequiredValue(demandDescTV, "需求描述");
        setRequiredValue(followStatusTV, "跟进状态");
        setRequiredValue(customOriginTV, "客户来源");
        setRequiredValue(introduceToTV, "介绍给");
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @OnClick({R.id.iv_warn_prompt, R.id.tv_warm_prompt, R.id.cet_demand_desc, R.id.iv_page_back,
            R.id.cet_follow_status, R.id.cet_custom_origin_note, R.id.cet_introduce_to, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_warn_prompt:
            case R.id.tv_warm_prompt:
                // 温馨提示
                toast("温馨提示");
                break;
            case R.id.cet_demand_desc:
                // 填写需求详情
                startActivity(getNewIntent(this, DemandWriteActivity.class, "需求详情","需求详情"));
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
                // toast("确定");
                // 支付密码输入对话框
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setAutoDismiss(false)
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                dialog.dismiss();
                                // toast(password);
                                startActivityForResult(getNewIntent(CustomIntroduceActivity.this, CustomSuccessActivity.class, "提示", "提示"),
                                        Constants.MANAGER_FRAGMENT_RESULT_SIZE);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
