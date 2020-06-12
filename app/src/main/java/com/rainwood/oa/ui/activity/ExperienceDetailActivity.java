package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/25 13:14
 * @Desc: 工作经历详情
 */
public final class ExperienceDetailActivity extends BaseActivity implements IStaffCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_company)
    private TextView companyName;
    @ViewInject(R.id.tv_post)
    private TextView post;
    @ViewInject(R.id.tv_entry_out_time)
    private TextView entryOutTime;
    @ViewInject(R.id.tv_duty)
    private TextView duty;
    @ViewInject(R.id.tv_reason)
    private TextView reason;

    private IStaffPresenter mStaffPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_experience_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String experienceId = getIntent().getStringExtra("experienceId");
        if (experienceId != null){
            mStaffPresenter.requestExperienceById(experienceId);
        }
        // TODO：无员工经历详情接口
    }

    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
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
