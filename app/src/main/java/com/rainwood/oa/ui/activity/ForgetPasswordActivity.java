package com.rainwood.oa.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.sqlite.SQLiteHelper;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ILoginAboutCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.view.CountdownView;
import com.sxs.verification.CheckUtil;
import com.sxs.verification.VerificationView;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 10:36
 * @Desc: 忘记密码
 */
public final class ForgetPasswordActivity extends BaseActivity implements ILoginAboutCallbacks {

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
    @ViewInject(R.id.verification_view)
    private VerificationView mVerificationView;

    private ILoginAboutPresenter mLoginAboutPresenter;
    private int[] mCheckNum;

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
    protected void initData() {
        initVerification();
    }

    /**
     * 初始化验证码
     */
    private void initVerification() {
        mCheckNum = CheckUtil.getCheckNum();
        mVerificationView.setCheckNum(mCheckNum);
        mVerificationView.invaliChenkNum();
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
        mLoginAboutPresenter = PresenterManager.getOurInstance().getLoginAboutPresenter();
        mLoginAboutPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.verification_view, R.id.cv_password_forget_countdown, R.id.btn_login, R.id.tv_login_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verification_view:
                initVerification();
                break;
            case R.id.cv_password_forget_countdown:
                // toast("发送短信验证码");
                if (TextUtils.isEmpty(account.getText())) {
                    toast("请输入用户名");
                    return;
                }
                if (!CheckUtil.checkNum(verification.getText().toString().trim(), mCheckNum)) {
                    toast("验证码错误");
                    return;
                }
                if (true) {
                    mCountdownView.start();
                    //return;
                }
                mLoginAboutPresenter.sendVerifyCode(account.getText().toString().trim());
                break;
            case R.id.btn_login:
                // toast("登录");
                if (TextUtils.isEmpty(smsVerification.getText())){
                    toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(account.getText())){
                    toast("请输入用户名");
                    return;
                }
                showDialog();
                mLoginAboutPresenter.Login(account.getText().toString().trim(),
                        smsVerification.getText().toString().trim());
                break;
            case R.id.tv_login_account:
                // toast("账号密码登录");
                startActivity(getNewIntent(this, LoginActivity.class, "账号密码登录", "账号密码登录"));
                break;
        }
    }

    @Override
    public void getVerifyResult(boolean warn) {
        if (warn){
            toast(R.string.common_code_send_hint);
        }
    }

    @Override
    public void Login(String life) {
        if (isShowDialog()){
            hideDialog();
        }
        // 登录之后的参数 -- 默认得id：pyj7y98y9juq
        Constants.life = life;
        // TODO: 登录成功之后免登录
        String insertSql = "REPLACE INTO loginTab(userName, pwd) VALUES ( '" + account.getText().toString()
                + "','" + smsVerification.getText().toString() + "');";
        SQLiteHelper.with(this).insert(insertSql);
        String querySql = "select * from loginTab;";
        List<Map<String, String>> query = SQLiteHelper.with(this).query(querySql);
        LogUtils.d("sxs", "-- 登录 --" + query.toString());
        startActivity(HomeActivity.class);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        super.release();
        if (mLoginAboutPresenter != null) {
            mLoginAboutPresenter.unregisterViewCallback(this);
        }
    }
}
