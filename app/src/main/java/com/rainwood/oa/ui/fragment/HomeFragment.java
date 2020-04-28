package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.ui.widget.XCollapsingToolbarLayout;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 首页
 */
public final class HomeFragment extends BaseFragment implements XCollapsingToolbarLayout.OnScrimsListener {

    @ViewInject(R.id.ctl_test_bar)
    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    public boolean mScrimsShown;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);
        mScrimsShown = mCollapsingToolbarLayout.isScrimsShown();
    }

    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
             StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        } else {
             StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }
}
