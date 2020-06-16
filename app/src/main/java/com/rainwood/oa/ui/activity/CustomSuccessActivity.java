package com.rainwood.oa.ui.activity;

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
 * @Desc:
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
    private TextView customName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_success;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {

    }


    @OnClick({R.id.iv_page_back, R.id.btn_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_return:
                //setResult(Constants.MANAGER_FRAGMENT_RESULT_SIZE);
                //startActivityForResult(getNewIntent(this, HomeActivity.class, "管理", "管理"), Constants.MANAGER_FRAGMENT_RESULT_SIZE);
                //finish();
                backHome("manager");
                break;
        }
    }
}
