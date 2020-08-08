package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:16
 * @Desc: 职位详情
 */
public final class PostDetailActivity extends BaseActivity implements IAdministrativeCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_post)
    private TextView post;
    @ViewInject(R.id.git_depart)
    private GroupIconText depart;
    @ViewInject(R.id.git_role)
    private GroupIconText role;
    @ViewInject(R.id.tv_salary)
    private TextView salary;
    @ViewInject(R.id.tv_allowance_desc)
    private TextView allowanceDesc;
    @ViewInject(R.id.tv_post_desc)
    private TextView postDesc;
    @ViewInject(R.id.tv_salary_sub)
    private TextView mTextSalarySub;

    private IAdministrativePresenter mAdministrativePresenter;

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
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {
        // 职位详情
        String postId = getIntent().getStringExtra("postId");
        if (postId != null) {
            mAdministrativePresenter.requestPostDetailById(postId);
        } else {
            toast("数据异常");
            finish();
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

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void getPostDetailData(Post postData) {
        post.setText(postData.getName());
        depart.setValue(postData.getDepartmentName());
        role.setValue(postData.getRoleName());
        salary.setText("基本工资：￥" + String.format("%.2f", postData.getBasePay()));
        salary.setText(Html.fromHtml("<font color='" + getColor(R.color.labelColor) + "'>基本工资：</font>" +
                "<font color='" + getColor(R.color.fontColor) + "'>￥" + String.format("%.2f", postData.getBasePay()) + " </font>"));
        mTextSalarySub.setText(Html.fromHtml("<font color='" + getColor(R.color.labelColor) + "'>岗位津贴：</font>" +
                "<font color='" + getColor(R.color.fontColor) + "'>￥" + String.format("%.2f", postData.getSubsidy()) + "</font>"));
        allowanceDesc.setText(TextUtils.isEmpty(postData.getSubsidyText()) ? "无" : postData.getSubsidyText());
        postDesc.setText(TextUtils.isEmpty(postData.getText()) ? "无" : postData.getText());
    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
