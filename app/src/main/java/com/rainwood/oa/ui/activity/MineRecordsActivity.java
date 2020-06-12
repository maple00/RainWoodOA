package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.MineOverTimeAdapter;
import com.rainwood.oa.ui.adapter.MineRecordsAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 15:27
 * @Desc: 我的记录Activity(请假记录 、 加班记录)
 */
public final class MineRecordsActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.gti_status)
    private GroupTextIcon state;
    @ViewInject(R.id.gti_leave_type)
    private GroupTextIcon leaveType;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_mine_records)
    private RecyclerView mineRecordsView;

    private IMinePresenter mMinePresenter;
    private MineRecordsAdapter mRecordsAdapter;
    private MineOverTimeAdapter mOverTimeAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_ask_leave;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setTextSize(14f);
        pageRightTitle.setTextColor(getColor(R.color.white));
        pageRightTitle.setBackgroundResource(R.drawable.selector_apply_button);
        // 设置布局管理器
        mineRecordsView.setLayoutManager(new GridLayoutManager(this, 1));
        mineRecordsView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mRecordsAdapter = new MineRecordsAdapter();
        mOverTimeAdapter = new MineOverTimeAdapter();
        switch (moduleMenu) {
            case "mywork":
                pageRightTitle.setText("请假申请");
                leaveType.setVisibility(View.VISIBLE);
                mineRecordsView.setAdapter(mRecordsAdapter);
                break;
            case "myworkAdd":
                pageRightTitle.setText("加班申请");
                leaveType.setVisibility(View.GONE);
                mineRecordsView.setAdapter(mOverTimeAdapter);
                break;
            case "myworkOut":
                pageRightTitle.setText("外出申请");
                leaveType.setVisibility(View.GONE);
                mineRecordsView.setAdapter(mOverTimeAdapter);
                break;
        }
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        switch (moduleMenu) {
            case "mywork":
                mMinePresenter.requestAskLeaveRecords();
                break;
            case "myworkAdd":
                mMinePresenter.requestMineOverTimeRecords();
                break;
            case "myworkOut":
                mMinePresenter.requestMineLeaveOutRecords();
                break;
        }
    }

    @Override
    protected void initEvent() {
        // 请假记录
        mRecordsAdapter.setItemReissue(new MineRecordsAdapter.OnClickItemReissue() {
            @Override
            public void onClickItem(MineRecords reissue, int position) {
                toast("查看详情");
            }

            @Override
            public void onClickEdit(MineRecords reissue, int position) {
                toast("编辑");
            }

            @Override
            public void onClickDelete(MineRecords reissue, int position) {
                toast("删除");
            }
        });
        // 加班记录
        mOverTimeAdapter.setOnClickOverTime(new MineOverTimeAdapter.OnClickOverTime() {
            @Override
            public void onClickItem(MineRecordTime record, int position) {
                toast("查看详情");
            }

            @Override
            public void onClickDelete(MineRecordTime record, int position) {
                toast("删除");
            }

            @Override
            public void onClickEdit(MineRecordTime record, int position, String clickContent) {
                switch (clickContent) {
                    case "编辑":
                        toast("编辑");
                        break;
                    case "提交成果":
                        toast("提交成果");
                        break;
                }

            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                switch (moduleMenu) {
                    case "mywork":
                        toast("请假申请");
                        break;
                    case "myworkAdd":
                        toast("加班申请");
                        break;
                    case "myworkOut":
                        toast("外出申请");
                        break;
                }
                break;
        }
    }

    @Override
    public void getMineAskLeaveRecords(List<MineRecords> askLeaveList) {
        mRecordsAdapter.setReissueList(askLeaveList);
    }

    @Override
    public void getMineOverTimeRecords(List<MineRecordTime> overTimeList) {
        mOverTimeAdapter.setList(overTimeList);
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
