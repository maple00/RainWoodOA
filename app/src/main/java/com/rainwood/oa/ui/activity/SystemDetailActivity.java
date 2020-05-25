package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:24
 * @Desc:
 */
public final class SystemDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_title)
    private TextView systemTitle;
    @ViewInject(R.id.tv_screen)
    private TextView screen;
    @ViewInject(R.id.tv_content)
    private TextView content;
    @ViewInject(R.id.tv_time)
    private TextView time;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_detail;
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

    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }
}
