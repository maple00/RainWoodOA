package com.rainwood.oa.ui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.network.sqlite.SQLiteHelper;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ILoginAboutCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.sxs.verification.CheckUtil;
import com.sxs.verification.VerificationView;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 9:45
 * @Desc: 登录
 */
public final class LoginActivity extends BaseActivity implements ILoginAboutCallbacks {

    @ViewInject(R.id.tv_login_title)
    private TextView loginTitle;
    @ViewInject(R.id.cet_account)
    private EditText account;
    @ViewInject(R.id.iv_account)
    private ImageView accountBG;
    @ViewInject(R.id.iv_password)
    private ImageView passwordBG;
    @ViewInject(R.id.iv_verification)
    private ImageView verificationBG;
    @ViewInject(R.id.pet_password)
    private EditText password;
    @ViewInject(R.id.cet_verification)
    private EditText verification;
    // 自定义验证码控件
    @ViewInject(R.id.verification_view)
    private VerificationView mVerificationView;

    private ILoginAboutPresenter mLoginAboutPresenter;
    private int[] mCheckNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, loginTitle);
        if (!NetworkUtils.isAvailable(this)) {
            toast("当前网络不佳，请确认网络");
        }
    }

    @Override
    protected void initData() {
        initVerification();
    }

    /**
     * 初始化验证码
     */
    private void initVerification() {
        // 验证码随机数
        mCheckNum = CheckUtil.getCheckNum();
        mVerificationView.setCheckNum(mCheckNum);
        mVerificationView.invaliChenkNum();
    }

    @Override
    protected void initEvent() {
        // 焦点监听
        account.setOnFocusChangeListener((v, hasFocus) -> accountBG.setImageResource(hasFocus ? R.drawable.ic_account_focus : R.drawable.ic_account));
        password.setOnFocusChangeListener(((v, hasFocus) -> passwordBG.setImageResource(hasFocus ? R.drawable.ic_password_focus : R.drawable.ic_password)));
        verification.setOnFocusChangeListener((v, hasFocus) -> verificationBG.setImageResource(hasFocus ? R.drawable.ic_verification_focus : R.drawable.ic_verification));
    }

    @Override
    protected void initPresenter() {
        mLoginAboutPresenter = PresenterManager.getOurInstance().getLoginAboutPresenter();
        mLoginAboutPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.verification_view, R.id.btn_login, R.id.tv_forget_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verification_view:
                //toast("点击更换验证码");
                initVerification();
                break;
            case R.id.btn_login:
                // 登录
               /* account.setText("邵雪松");
                password.setText("123456");*/
                if (TextUtils.isEmpty(account.getText())) {
                    toast("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    toast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(verification.getText())) {
                    toast("请输入验证码");
                    return;
                }
                if (!CheckUtil.checkNum(verification.getText().toString().trim(), mCheckNum)) {
                    toast("验证码错误");
                    return;
                }
                showDialog();

                mLoginAboutPresenter.Login(account.getText().toString().trim(),
                        password.getText().toString().trim());
                break;
            case R.id.tv_forget_password:
                //toast("忘记密码");
                startActivity(getNewIntent(this, ForgetPasswordActivity.class, "忘记密码", "忘记密码"));
                break;
        }
    }

    @Override
    public void Login(String life) {
        if (isShowDialog()) {
            hideDialog();
        }
        // 登录之后的参数 -- 默认得id：pyj7y98y9juq
        Constants.life = life;
        // TODO: 登录成功之后免登录
        String insertSql = "REPLACE INTO loginTab(userName, pwd) VALUES ( '" + account.getText().toString()
                + "','" + password.getText().toString() + "') ;";
        SQLiteHelper.with(this).update(insertSql);
        String querySql = "select * from loginTab;";
        List<Map<String, String>> query = SQLiteHelper.with(this).query(querySql);
        LogUtils.d("sxs", "-- 登录 --" + query.toString());
        startActivity(HomeActivity.class);
    }

    @Override
    public void onError(String tips) {
        hideDialog();
        toast(tips);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
