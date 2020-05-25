package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:39
 * @Desc: 记录管理----- 加班记录、请假记录、外出记录、补卡记录
 */
public final class RecordManagerActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    //content
    @ViewInject(R.id.gti_status)
    private GroupTextIcon topStatus;
    @ViewInject(R.id.gti_leave_type)
    private GroupTextIcon topLeaveType;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        topLeaveType.setVisibility(title.contains("请假记录") ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void initPresenter() {

    }
}
