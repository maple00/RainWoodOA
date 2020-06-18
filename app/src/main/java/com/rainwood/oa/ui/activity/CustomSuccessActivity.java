package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author a797s
 * @Date 2020/5/18 9:01
 * @Desc: 成功页面
 */
public final class CustomSuccessActivity extends BaseActivity {

    // action Bar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.iv_success_flag)
    private ImageView successImg;
    @ViewInject(R.id.tv_introduce_name)
    private TextView introduceName;
    @ViewInject(R.id.tv_introduced_name)
    private TextView introducedName;
    @ViewInject(R.id.tv_custom_name)
    private TextView customNameTv;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_success;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        successImg.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String introduce = intent.getStringExtra("introduce");
        String introduced = intent.getStringExtra("introduced");
        String customName = intent.getStringExtra("customName");
        introduceName.setText(introduce);
        introducedName.setText(introduced);
        customNameTv.setText(customName);
    }

    @OnClick({R.id.iv_page_back, R.id.btn_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
            case R.id.btn_return:
                backHome("manager");
                break;
        }
    }
}
