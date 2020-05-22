package com.rainwood.oa.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.ui.fragment.StaffAccountFragment;
import com.rainwood.oa.ui.fragment.StaffDataFragment;
import com.rainwood.oa.ui.fragment.StaffSettleFragment;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.statusbar.StatusBarUtils;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

/**
 * @Author: a797s
 * @Date: 2020/5/22 16:11
 * @Desc: 员工页面
 */
public final class StaffMainActivity extends BaseActivity {

    @ViewInject(R.id.staff_navigation_bar)
    private BottomNavigationView mBottomNavigationView;

    private StaffDataFragment mStaffDataFragment;
    private StaffAccountFragment mStaffAccountFragment;
    private StaffSettleFragment mStaffSettleFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_staff;
    }

    @Override
    protected void initView() {
        // 解决navigation显示超过三个时不显示文字
        mBottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        initFragment();
    }

    private void initFragment() {
        mStaffDataFragment = new StaffDataFragment();
        mStaffAccountFragment = new StaffAccountFragment();
        mStaffSettleFragment = new StaffSettleFragment();
        mFragmentManager = getSupportFragmentManager();
        // 切换fragment
        switchFragment(mStaffDataFragment);
    }

    @Override
    protected void initEvent() {
        initListener();
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_data) {
                switchFragment(mStaffDataFragment);
            } else if (item.getItemId() == R.id.navigation_account) {
                switchFragment(mStaffAccountFragment);
            } else if (item.getItemId() == R.id.navigation_settlement) {
                switchFragment(mStaffSettleFragment);
            }
            return true;
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        StatusBarUtil.setStatusBarColor(this, getColor(R.color.white));
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        //如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
        if (lastOneFragment == targetFragment) {
            return;
        }
        //修改成add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_staff_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        // fragmentTransaction.replace(R.id.fl_staff_container, targetFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void setStatusBar() {

    }
}
