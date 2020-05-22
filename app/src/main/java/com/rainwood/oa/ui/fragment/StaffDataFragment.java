package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.tools.annotation.ViewInject;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 员工资料
 */
public final class StaffDataFragment extends BaseFragment {

    // actionBar
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_staff_data;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        pageTitle.setText("员工详情");

    }
}
