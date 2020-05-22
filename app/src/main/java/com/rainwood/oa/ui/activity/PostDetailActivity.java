package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:16
 * @Desc: 职位详情
 */
public final class PostDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_post_detail;
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

    @SingleClick
    @OnClick(R.id.iv_page_back)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }
}
