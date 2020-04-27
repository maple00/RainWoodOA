package com.rainwood.oa.base;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.oa.network.app.App;
import com.rainwood.tools.annotation.ViewBind;
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
        //==========================================
        //ColorMatrix cm = new ColorMatrix();
        //cm.setSaturation(0);
        //Paint paint = new Paint();
        //paint.setColorFilter(new ColorMatrixColorFilter(cm));
        //View contentContainer = getWindow().getDecorView();
        //contentContainer.setLayerType(View.LAYER_TYPE_SOFTWARE,paint);
        //===========================================
        ViewBind.inject(this);
        initView();
        initEvent();
        initPresenter();
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
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * presenter 管理
     */
    protected abstract void initPresenter();

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
     * 返回到桌面
     */
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {         // 回到Home页
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                toast("再按一次退出到桌面");
                mExitTime = System.currentTimeMillis();
                return false;
            } else {
                App.backHome(this);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
