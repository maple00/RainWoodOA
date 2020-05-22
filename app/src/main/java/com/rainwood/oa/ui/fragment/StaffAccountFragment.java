package com.rainwood.oa.ui.fragment;

import android.view.View;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 会计账户
 */
public final class StaffAccountFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_staff_account;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
