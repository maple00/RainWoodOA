package com.rainwood.oa.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.rainwood.tools.wheel.view.CountdownView;

/**
 * @Author: a797s
 * @Date: 2020/5/18 10:36
 * @Desc: 忘记密码
 */
public final class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.tv_login_title)
    private TextView loginTitle;
    @ViewInject(R.id.iv_account)
    private ImageView accountBG;
    @ViewInject(R.id.cet_account)
    private EditText account;
    @ViewInject(R.id.iv_verification)
    private ImageView verificationBG;
    @ViewInject(R.id.cet_verification)
    private EditText verification;
    @ViewInject(R.id.iv_sms_verification)
    private ImageView smsVerificationBG;
    @ViewInject(R.id.cet_sms_verification)
    private EditText smsVerification;
    @ViewInject(R.id.cv_password_forget_countdown)
    private CountdownView mCountdownView;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, loginTitle);
    }

    @Override
    protected void initEvent() {
        account.setOnFocusChangeListener((v, hasFocus) -> accountBG.setImageResource(hasFocus ? R.drawable.ic_account_focus : R.drawable.ic_account));
        verification.setOnFocusChangeListener((v, hasFocus) -> verificationBG.setImageResource(hasFocus ? R.drawable.ic_verification_focus : R.drawable.ic_verification));
        smsVerification.setOnFocusChangeListener((v, hasFocus) -> smsVerificationBG.setImageResource(hasFocus ? R.drawable.ic_verification_focus :
                R.drawable.ic_verification));
    }

    @Override
    protected void initPresenter() {

    }


    @SingleClick
    @OnClick({R.id.iv_verification_click, R.id.cv_password_forget_countdown, R.id.btn_login, R.id.tv_login_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_verification_click:
                toast("更换图片验证码");
                break;
            case R.id.cv_password_forget_countdown:
                // toast("发送短信验证码");
                if (TextUtils.isEmpty(account.getText())) {
                    toast("请输入用户名");
                    return;
                }
                if (true) {
                    toast(R.string.common_code_send_hint);
                    mCountdownView.start();
                    return;
                }

                break;
            case R.id.btn_login:
                toast("登录");
                break;
            case R.id.tv_login_account:
                // toast("账号密码登录");
                startActivity(getNewIntent(this, LoginActivity.class, "账号密码登录","账号密码登录"));
                break;
        }
    }
}
