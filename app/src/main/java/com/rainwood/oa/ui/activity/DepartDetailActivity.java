package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/21 18:09
 * @Desc: 部门详情
 */
public final class DepartDetailActivity extends BaseActivity implements IAdministrativeCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pagerTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_depart_group)
    private TextView departGroup;
    @ViewInject(R.id.tv_duty)
    private TextView duty;

    private IAdministrativePresenter mAdministrativePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_depart_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pagerTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String departId = getIntent().getStringExtra("departId");
        if (departId != null) {
            mAdministrativePresenter.requestDepartDetailById(departId);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void getDepartDetailData(ProjectGroup projectGroup) {
        departGroup.setText(projectGroup.getType() + "-" + projectGroup.getName());
        duty.setText(projectGroup.getText());
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
