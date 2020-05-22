package com.rainwood.oa.ui.fragment;

import android.view.View;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 结算账户
 */
public final class StaffSettleFragment extends BaseFragment {

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_staff_settlement;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
    }
}
