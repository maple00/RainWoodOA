package com.rainwood.oa.ui.activity;

import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;

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

    @Override
    public void getPostDetailData(Post postData) {
        post.setText(postData.getName());
        depart.setValue(postData.getDepartmentName());
        role.setValue(postData.getRoleName());
        salary.setText(Html.fromHtml("<font color=\"" + getColor(R.color.fontColor) + "\">基" + postData.getBasePay() + "+</font>"
                + "<font color=\"" + getColor(R.color.colorPrimary) + "\">津" + postData.getSubsidy() + "</font>"));
        allowanceDesc.setText(postData.getSubsidy());
        postDesc.setText(postData.getText());
        // 缺少一个津贴说明字段
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
