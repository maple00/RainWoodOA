package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ILoginAboutCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.sxs.verification.CheckUtil;
import com.sxs.verification.VerificationView;

import java.util.List;

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

    // 验证码随机数
    private int[] mCheckNum;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
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
                account.setText("邵雪松");
                password.setText("ds1124+0929");
               /* if (TextUtils.isEmpty(account.getText())) {
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
                    toast("请核对验证码");
                    return;
                }*/
                XXPermissions.with(this)
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        .permission(Permission.Group.STORAGE)    // 读写权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    startActivity(HomeActivity.class);
                                    // mLoginAboutPresenter.Login(account.getText().toString().trim(),
                                    // password.getText().toString().trim());
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                if (quick) {
                                    toast("被永久拒绝授权，请手动授予权限");
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.gotoPermissionSettings(getActivity());
                                } else {
                                    toast("获取权限失败");
                                }
                            }
                        });
                break;
            case R.id.tv_forget_password:
                //toast("忘记密码");
                startActivity(getNewIntent(this, ForgetPasswordActivity.class, "忘记密码", "忘记密码"));
                break;
        }
    }

    @Override
    public void Login() {
        // 登录之后的参数 -- 默认得id：pyj7y98y9juq
        startActivity(HomeActivity.class);
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
