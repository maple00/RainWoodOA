package com.rainwood.oa.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.oa.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:16
 * @Desc: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements OnClickAction {

    // public String TAG = this.getClass().getSimpleName();
    public final String TAG = "sxs";
    protected String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ViewBind.inject(this);
        title = getIntent().getStringExtra("title") == null ? "" : getIntent().getStringExtra("title");
        setStatusBar();
        initView();
        initEvent();
        initPresenter();
        initData();
        loadData();
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
        // StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.white));
        StatusBarUtils.darkMode(this);
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
    protected Intent getNewIntent(Context context, Class<? extends BaseActivity> clazz, String title) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("title", title);
        return intent;
    }

  /*  // 设置动画
  @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }*/

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
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
    private void hideSoftKeyboard() {
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
     * 设置必填信息
     *
     * @param requestedText text
     * @param s             value
     */
    public void setRequiredValue(TextView requestedText, String s) {
        requestedText.setText(Html.fromHtml("<font color=" + this.getColor(R.color.colorMiddle)
                + " size=" + FontSwitchUtil.dip2px(this, 16f)+">" + s +"</font>" +
                "<font color=" + this.getColor(R.color.red05) + " size= "
                + FontSwitchUtil.dip2px(this, 13f) + ">*</font>"));
    }

}
