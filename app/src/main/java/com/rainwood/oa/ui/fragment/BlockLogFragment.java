package com.rainwood.oa.ui.fragment;

import android.view.View;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_blocklog;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
    }
}
