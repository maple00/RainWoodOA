package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.network.sqlite.SQLiteHelper;
import com.rainwood.oa.presenter.ILoginAboutPresenter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ILoginAboutCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.view.SmartTextView;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/7/6 11:46
 * @Desc: 闪屏页面
 */
public final class SplashActivity extends BaseActivity implements Animation.AnimationListener,
        ILoginAboutCallbacks, StatusAction {

    private static final int ANIM_TIME = 1000;      // 动画时长

    @ViewInject(R.id.iv_splash_bg)
    private View splashBG;           // 背景
    @ViewInject(R.id.iv_splash_icon)
    private View iconLogo;           // 启动logo
    @ViewInject(R.id.tv_splash_copyright)
    private SmartTextView copyright;        // 版权
    @ViewInject(R.id.hl_status_hint)    // 判断加载状态
    private HintLayout mHintLayout;
    @ViewInject(R.id.tv_tips)
    private TextView mTextTips;
    @ViewInject(R.id.tv_tips_note)
    private TextView tipNote;

    private ILoginAboutPresenter mLoginAboutPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {
        mLoginAboutPresenter = PresenterManager.getOurInstance().getLoginAboutPresenter();
        mLoginAboutPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        // 获取当前年
        copyright.setText("\u00a9" + DateTimeUtils.getNowYear() + "All Rights Reserved");
        // 初始化动画
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
        aa.setDuration(ANIM_TIME * 2);
        aa.setAnimationListener(this);
        splashBG.startAnimation(aa);
        // 企业管理软件定制开发专家
        //
        mTextTips.setText("企业管理软件定制开发专家");
        tipNote.setText("Professional on ERP Custom development");

        /*RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(ANIM_TIME);
        mTextTips.startAnimation(ra);*/

       /* ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIM_TIME);
        iconLogo.startAnimation(sa);

        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(ANIM_TIME);
        copyright.startAnimation(ra);*/
        // 设置导航栏参数
    }

    @Override
    public void onAnimationStart(Animation animation) {
       /* // 判断网络是否可用
        boolean available = NetworkUtils.isAvailable(this);
        if (available) {
            permissionLogin();
        } else {
            showError(v -> {
                showDialog();
                postDelayed(() -> {
                    if (isShowDialog()) {
                        hideDialog();
                    }
                }, 2000);
                permissionLogin();
            });
        }*/

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        XXPermissions.with(this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .constantRequest()
                .permission(Permission.Group.STORAGE)    // 读写权限
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 查询数据库
                            String queryLoginTab = "select * from loginTab;";
                            List<Map<String, String>> queryList = SQLiteHelper.with(SplashActivity.this)
                                    .query(queryLoginTab);
                            LogUtils.d("sxs", " -- 闪屏页 --- " + queryList.toString());
                            // 没有登录
                            if (ListUtils.getSize(queryList) == 0) {
                                openActivity(LoginActivity.class);
                                return;
                            }
                            // 没有网络
                            boolean available = NetworkUtils.isAvailable(SplashActivity.this);
                            if (!available) {
                                startActivity(LoginActivity.class);
                                return;
                            }
                            //
                            String userName = queryList.get(ListUtils.getSize(queryList) - 1).get("userName");
                            String pwd = queryList.get(ListUtils.getSize(queryList) - 1).get("pwd");
                            String life = queryList.get(ListUtils.getSize(queryList) - 1).get("life");
                            mLoginAboutPresenter.Login(userName, pwd);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            openActivity(LoginActivity.class);
                            toast("获取权限失败");
                        }
                    }
                });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onError(String tips) {
        toast(tips);
        openActivity(LoginActivity.class);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void Login(String life) {
        // 说明处于可登录状态
        Constants.life = life;
        startActivity(HomeActivity.class);
    }

    @Override
    protected void release() {
        if (mLoginAboutPresenter != null) {
            mLoginAboutPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK;
    }


}
