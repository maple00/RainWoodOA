package com.rainwood.oa.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.oa.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.toast.ToastUtils;

/**
 * @Author: a797s
 * @Date: 2020/4/27 16:16
 * @Desc: activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    // public String TAG = this.getClass().getSimpleName();
    public final String TAG = "sxs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        ViewBind.inject(this);
        initView();
        initEvent();
        initPresenter();
        loadData();
        initData();
        setStatusBar();
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

  /*  @Override
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

}
