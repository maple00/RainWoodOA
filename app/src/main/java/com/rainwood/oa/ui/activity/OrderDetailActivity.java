package com.rainwood.oa.ui.activity;

import android.widget.RelativeLayout;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;

/**
 * @Author: a797s
 * @Date: 2020/6/3 19:33
 * @Desc: 订单详情
 */
public final class OrderDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initPresenter() {

    }
}
