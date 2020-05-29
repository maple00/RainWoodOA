package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.SystemLogcat;
import com.rainwood.oa.presenter.ISystemSettingPresenter;
import com.rainwood.oa.ui.adapter.LogcatAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ISystemSettingCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:25
 * @Desc: 系统日志
 */
public final class LogcatActivity extends BaseActivity implements ISystemSettingCallbacks {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    // content
    @ViewInject(R.id.gti_logcat_type)
    private GroupTextIcon logcatType;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_time)
    private GroupTextIcon time;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_logcat_content)
    private RecyclerView logcatContent;

    private ISystemSettingPresenter mSettingPresenter;
    private LogcatAdapter mLogcatAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logcat;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // 设置布局管理器
        logcatContent.setLayoutManager(new GridLayoutManager(this, 1));
        logcatContent.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mLogcatAdapter = new LogcatAdapter();
        // 设置适配器
        logcatContent.setAdapter(mLogcatAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void loadData() {
        mSettingPresenter.requestLogcatData();
    }

    @Override
    protected void initPresenter() {
        mSettingPresenter = PresenterManager.getOurInstance().getSystemSettingPresenter();
        mSettingPresenter.registerViewCallback(this);
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
    public void getSystemLogcat(List<SystemLogcat> logcatList) {
        mLogcatAdapter.setLogcatList(logcatList);
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
