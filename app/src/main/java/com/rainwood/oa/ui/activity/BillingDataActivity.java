package com.rainwood.oa.ui.activity;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: sxs
 * @Time: 2020/5/30 14:07
 * @Desc: 开票信息
 */
public final class BillingDataActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    //content

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_billing_data;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        pageTitle.setTextColor(getColor(R.color.white));
    }

    @Override
    protected void loadData() {
        // TODO：绘制开票信息UI
    }

    @Override
    protected void initPresenter() {

    }
}
