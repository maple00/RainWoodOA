package com.rainwood.oa.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:02
 * @Desc: 员工收支详情
 */
public final class PaymentDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.acitvity_payment_details;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {

    }
}
