package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/8 11:59
 * @Desc: 日志详情
 */
public final class LogcatDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.tv_content)
    private TextView content;
    @ViewInject(R.id.tv_origin)
    private TextView origin;
    @ViewInject(R.id.tv_time)
    private TextView time;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logcat_detail;
    }

    @Override
    protected void initPresenter() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initData() {
        Logcat logcat = (Logcat) getIntent().getSerializableExtra("logcat");
        if (logcat != null) {
            name.setText(logcat.getStaffName());
            content.setText(logcat.getText());
            origin.setText(logcat.getType());
            time.setText(logcat.getTime());
        }
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }
}
