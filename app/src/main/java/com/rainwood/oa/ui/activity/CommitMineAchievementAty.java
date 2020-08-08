package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/7/23 10:45
 * @Desc: 提交成果 （加班成果提交、）
 */
public final class CommitMineAchievementAty extends BaseActivity {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView rightPageTitle;

    @ViewInject(R.id.tv_start_time_title)
    private TextView startTimeTitle;
    @ViewInject(R.id.tv_end_time_title)
    private TextView endTimeTitle;
    @ViewInject(R.id.tv_overtime_reason_title)
    private TextView achievementTitle;
    @ViewInject(R.id.tv_approver_title)
    private TextView approverTitle;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_commint_mine_achievement;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        rightPageTitle.setText(moduleMenu);
        rightPageTitle.setTextColor(getColor(R.color.fontColor));
        setRequiredValue(startTimeTitle, "开始时间");
        setRequiredValue(endTimeTitle, "结束时间");
        setRequiredValue(achievementTitle, "加班成果");
        setRequiredValue(approverTitle, "审批人");
        toast("暂无接口");
    }

    @Override
    protected void initPresenter() {

    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                toast("审核记录");
                break;
        }
    }
}
