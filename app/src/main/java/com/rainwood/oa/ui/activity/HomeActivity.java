package com.rainwood.oa.ui.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.ui.fragment.BlockLogFragment;
import com.rainwood.oa.ui.fragment.HomeFragment;
import com.rainwood.oa.ui.fragment.ManagerFragment;
import com.rainwood.oa.ui.fragment.MineFragment;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.ViewInject;

import static com.google.android.material.bottomnavigation.LabelVisibilityMode.LABEL_VISIBILITY_LABELED;

public class HomeActivity extends BaseActivity {

    @ViewInject(R.id.main_navigation_bar)
    private BottomNavigationView mBottomNavigationView;

    private HomeFragment mHomeFragment;
    private ManagerFragment mManagerFragment;
    private BlockLogFragment mBlockLogFragment;
    private MineFragment mMineFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        // 解决navigation显示超过三个时不显示文字
        mBottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        initFragments();
    }

    @Override
    protected void initEvent() {
        initListener();
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.navigation_home) {
                LogUtils.d(this,"切换到首页");
                switchFragment(mHomeFragment);
            } else if(item.getItemId() == R.id.navigation_manager) {
                LogUtils.i(this,"切换到管理");
                switchFragment(mManagerFragment);
            } else if(item.getItemId() == R.id.navigation_backlog) {
                LogUtils.w(this,"切换到待办事项");
                switchFragment(mBlockLogFragment);
            } else if(item.getItemId() == R.id.navigation_mine) {
                LogUtils.e(this,"切换到我的");
                switchFragment(mMineFragment);
            }
            return true;
        });
    }

    private void initFragments() {
        mHomeFragment = new HomeFragment();
        mManagerFragment = new ManagerFragment();
        mBlockLogFragment = new BlockLogFragment();
        mMineFragment = new MineFragment();
        mFragmentManager = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    @Override
    protected void initPresenter() {

    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //如果上一个fragment跟当前要切换的fragment是同一个，那么不需要切换
        if (lastOneFragment == targetFragment) {
            return;
        }
        //修改成add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_main_container, targetFragment);
        } else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        //fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }
}
