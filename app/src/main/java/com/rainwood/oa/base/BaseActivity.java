package com.rainwood.oa.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.service.autofill.OnClickAction;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.MessageEvent;
import com.rainwood.oa.ui.activity.HomeActivity;
import com.rainwood.oa.ui.dialog.WaitDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.utils.Constants;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.action.HandlerAction;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:16
 * @Desc: activity基类
 */
@SuppressLint("NewApi")
public abstract class BaseActivity extends AppCompatActivity implements OnClickAction, HandlerAction {

    // public String TAG = this.getClass().getSimpleName();
    public final String TAG = "sxs";
    protected String title;
    protected String moduleMenu;
    public CommonPopupWindow mCommonPopupWindow;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    /**
     * 加载对话框
     */
    private BaseDialog mDialog;
    /**
     * 对话框数量
     */
    private int mDialogTotal;

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        return mDialog != null && mDialog.isShowing();
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        if (mDialog == null) {
            mDialog = new WaitDialog.Builder(this)
                    .setCancelable(false)
                    .create();
        }
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mDialogTotal++;
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        if (mDialogTotal == 1) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        if (mDialogTotal > 0) {
            mDialogTotal--;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ViewBind.inject(this);
        title = getIntent().getStringExtra("title") == null ? "" : getIntent().getStringExtra("title");
        moduleMenu = getIntent().getStringExtra("menu") == null ? "" : getIntent().getStringExtra("menu");
        setStatusBar();
        initView();
        initData();
        initPresenter();
        loadData();
        initEvent();
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 设置默认的状态栏背景颜色
     */
    protected void setStatusBar() {
        StatusBarUtils.darkMode(this);
        StatusBarUtils.immersive(this);
    }

    /**
     * event initial，需要时覆写
     */
    protected void initEvent() {

    }

    /**
     * 布局initial，需要时覆写
     */
    protected void initView() {

    }

    protected void loadData() {
        //加载数据
    }

    /**
     * 子类需要释放资源，覆盖即可
     */
    protected void release() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.release();
        if (isShowDialog()) {
            mDialog.dismiss();
        }
        mDialog = null;
    }

    /**
     * 覆写子类布局
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * presenter 管理
     */
    protected abstract void initPresenter();

    /**
     * 获取当前 Activity 对象
     */
    public BaseActivity getActivity() {
        return this;
    }

    /**
     * startActivity 优化
     */
    protected Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title, String menu) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        intent.putExtra("menu", menu);
        return intent;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.show(object);
    }

    /**
     * 延迟执行
     */
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public final Object mHandlerToken = hashCode();

    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime(r, mHandlerToken, uptimeMillis);
    }

    /**
     * 设置软键盘自动调起
     */
    protected void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * startActivity 方法简化
     */
    public void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(this, clazz));
    }

    /**
     * startActivityForResult 方法优化
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        hideSoftKeyboard();
        // 查看源码得知 startActivity 最终也会调用 startActivityForResult
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 使editText点击外部时候失去焦点
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    hideSoftKeyboard();
                    if (editText != null) {
                        editText.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    EditText editText = null;

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            editText = (EditText) v;
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    /**
     * 设置必填信息
     *
     * @param requestedText text
     * @param s             value
     */
    public void setRequiredValue(TextView requestedText, String s) {
        requestedText.setText(Html.fromHtml("<font color=" + getColor(R.color.colorMiddle)
                + " size=" + FontSwitchUtil.dip2px(this, 16f) + ">" + s + "</font>" +
                "<font color=" + getColor(R.color.red05) + " size= "
                + FontSwitchUtil.dip2px(this, 13f) + ">*</font>"));
    }

    /**
     * 判断手机是否有底部虚拟键位
     *
     * @param context
     * @return
     */
    public boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            @SuppressLint("PrivateApi")
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("0".equals(navBarOverride)) {
                //不存在虚拟按键
                hasNavigationBar = false;
            } else if ("1".equals(navBarOverride)) {
                //存在虚拟按键
                hasNavigationBar = true;
            }
            Log.d(TAG, " 虚拟键位 ========== " + hasNavigationBar);
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 快捷方式
     *
     * @param parentView 根布局
     */
    public void showQuickFunction(Context context, ViewGroup parentView) {
        mCommonPopupWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.ScaleAnimStyle)
                .setView(R.layout.pop_quick_function)
                .setBackGroundLevel(0.7f)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    LinearLayout pageParent = view.findViewById(R.id.ll_page_parent);
                    StatusBarUtils.setPaddingSmart(context, pageParent);

                    ImageView close = view.findViewById(R.id.iv_dialog_close);
                    close.setOnClickListener(v -> mCommonPopupWindow.dismiss());
                    // 点击事件
                    LinearLayout backHome = view.findViewById(R.id.ll_home);
                    LinearLayout backManager = view.findViewById(R.id.ll_manager);
                    LinearLayout backLog = view.findViewById(R.id.ll_backlog);
                    LinearLayout backMine = view.findViewById(R.id.ll_mine);
                    MessageEvent event = new MessageEvent("逻辑页面跳转");
                    // 返回首页
                    backHome.setOnClickListener(v -> onBackHomePager(event, Constants.HOME_FRAGMENT_RESULT_SIZE));
                    // 返回我的管理
                    backManager.setOnClickListener(v -> onBackHomePager(event, Constants.MANAGER_FRAGMENT_RESULT_SIZE));
                    // 返回代办事项
                    backLog.setOnClickListener(v -> onBackHomePager(event, Constants.BLOCK_FRAGMENT_RESULT_SIZE));
                    // 返回个人中心
                    backMine.setOnClickListener(v -> onBackHomePager(event, Constants.MINE_FRAGMENT_RESULT_SIZE));
                })
                .create();
        fitPopupWindowOverStatusBar(mCommonPopupWindow);
        mCommonPopupWindow.showAtLocation(parentView, Gravity.TOP, 0, 0);
        mCommonPopupWindow.setOnDismissListener(() -> mCommonPopupWindow.dismiss());
    }

    /**
     * 跳转回HomeAty
     *
     * @param event
     * @param mineFragmentResultSize
     */
    private void onBackHomePager(MessageEvent event, int mineFragmentResultSize) {
        openActivity(HomeActivity.class);
        event.setType(mineFragmentResultSize);
        EventBus.getDefault().post(event);
        finish();
        mCommonPopupWindow.dismiss();
    }

    /**
     * popWindow 沉浸
     *
     * @param mPopupWindow
     */
    private void fitPopupWindowOverStatusBar(PopupWindow mPopupWindow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(mPopupWindow, true);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回指定的fragment
     *
     * @param navigationBar
     */
    public void backHome(String navigationBar) {
        MessageEvent event = new MessageEvent("逻辑页面跳转");
        switch (navigationBar) {
            case "home":
                // 返回首页
                onBackHomePager(event, Constants.HOME_FRAGMENT_RESULT_SIZE);
                break;
            case "manager":
                // 返回我的管理
                onBackHomePager(event, Constants.MANAGER_FRAGMENT_RESULT_SIZE);
                break;
            case "blockLog":
                // 返回待办事项
                onBackHomePager(event, Constants.BLOCK_FRAGMENT_RESULT_SIZE);
                break;
            case "mine":
                // 返回我的个人中心
                onBackHomePager(event, Constants.MINE_FRAGMENT_RESULT_SIZE);
                break;
        }
    }

    /**
     * 设置上下平移的动画
     *
     * @param view
     */
    protected void setUpDownAnimation(View view) {
        TranslateAnimation translateAnimation;
        if (view.getVisibility() == View.VISIBLE) {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
        } else {
            translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.5f);
        }
        translateAnimation.setDuration(300);
        view.setAnimation(translateAnimation);
    }
}
