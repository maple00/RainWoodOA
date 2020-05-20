package com.rainwood.oa.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * create by a797s in 2020/5/15 16:41
 *
 * @Description : 添加联系人
 * @Usage :
 **/
public final class AddContactActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // 必填项
    @ViewInject(R.id.tv_post)
    private TextView post;
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumber;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_append_contact;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);

        setRequiredValue(post, ">职位</font>");
        setRequiredValue(name, ">姓名</font>");
        setRequiredValue(telNumber, ">手机号</font>");
    }

    @Override
    protected void initPresenter() {

    }

    @OnClick(R.id.btn_confirm)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_confirm:
                startActivity(getNewIntent(this, CommonActivity.class, "联系人"));
                break;
        }
    }
}
