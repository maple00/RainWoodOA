package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * create by a797s in 2020/5/14 15:28
 *
 * @Description :填写需求详情activity
 * @Usage :
 **/
public final class DemandWriteActivity extends BaseActivity {

    // page
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;

    @ViewInject(R.id.et_demand_content)
    private EditText demandContent;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_demand_content;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setText("保存");

        showSoftInputFromWindow(demandContent);
    }

    @Override
    protected void initPresenter() {

    }


    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                // 保存
                toast("保存了");
                finish();
                break;
        }
    }
}
