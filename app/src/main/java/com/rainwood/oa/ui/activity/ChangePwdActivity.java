package com.rainwood.oa.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.tools.wheel.view.CountdownView;

/**
 * @Author: a797s
 * @Date: 2020/6/15 10:27
 * @Desc: 修改密码
 */
public final class ChangePwdActivity extends BaseActivity implements IMineCallbacks {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.pet_current_pwd)
    private EditText currentPwdView;
    @ViewInject(R.id.pet_new_pwd)
    private EditText newPwdView;
    @ViewInject(R.id.pet_confirm_pwd)
    private EditText confirmPwdView;
    @ViewInject(R.id.cet_tel)
    private EditText telNumView;
    @ViewInject(R.id.ret_sms_code)
    private EditText smsCodeView;
    @ViewInject(R.id.cv_verify_code)
    private CountdownView verifyCodeView;

    private IMinePresenter mMinePresenter;
    private String secretId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_changed_pwd;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initData() {
        String telNumber = getIntent().getStringExtra("menu");
        telNumView.setText(TextUtils.isEmpty(telNumber) ? "当前账户手机号异常" : telNumber);
        int currentSecond = verifyCodeView.getCurrentSecond();
        LogUtils.d("sxs", "-- currentSecond--  " + currentSecond);
        if (currentSecond != 0) {
            verifyCodeView.setTotalTime(currentSecond);
            verifyCodeView.start();
        }
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.cv_verify_code, R.id.btn_confirm_changed})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.cv_verify_code:
                if (true) {
                    toast("验证码已发送，请注意查收");
                    verifyCodeView.start();
                    return;
                }
                mMinePresenter.requestSmsVerifyCode();
                // 获取验证码
                break;
            case R.id.btn_confirm_changed:
                if (TextUtils.isEmpty(currentPwdView.getText())) {
                    toast("请输入旧密码");
                    return;
                }
                if (TextUtils.isEmpty(newPwdView.getText())) {
                    toast("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(confirmPwdView.getText())) {
                    toast("请输入确认密码");
                    return;
                }
                if (TextUtils.isEmpty(smsCodeView.getText())) {
                    toast("请输入短信验证码");
                    return;
                }
                String current = currentPwdView.getText().toString().trim();
                String newPwd = newPwdView.getText().toString().trim();
                String confirmPwd = confirmPwdView.getText().toString().trim();
                String verifyVCode = smsCodeView.getText().toString().trim();
                if (!newPwd.equals(confirmPwd)) {
                    toast("新密码与确认密码不一致");
                    return;
                }
                if (current.equals(newPwd)) {
                    toast("旧密码与新密码一致");
                    return;
                }
                mMinePresenter.requestCheckedSms(current, newPwd, confirmPwd, verifyVCode, secretId);
                break;
        }
    }

    @Override
    public void getSmsVerifyCode(String smsId) {
        toast("验证码已送达，请注意查收");
        secretId = smsId;
    }

    @Override
    public void getVerifySuccess(boolean success) {
        toast("修改成功");
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
