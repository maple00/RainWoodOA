package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.view.SmartTextView;

/**
 * @Author: a797s
 * @Date: 2020/7/6 11:46
 * @Desc: 闪屏页面
 */
public final class SplashActivity extends BaseActivity implements Animation.AnimationListener {

    private static final int ANIM_TIME = 1000;      // 动画时长

    @ViewInject(R.id.iv_splash_bg)
    private View splashBG;           // 背景
    @ViewInject(R.id.iv_splash_icon)
    private View iconLogo;           // 启动logo
    @ViewInject(R.id.tv_splash_copyright)
    private SmartTextView copyright;        // 版权

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initPresenter() {

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

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        openActivity(LoginActivity.class);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
