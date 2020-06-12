package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.AdminLeaveOut;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.AdminLeaveOutAdapter;
import com.rainwood.oa.ui.adapter.AdminOvertimeAdapter;
import com.rainwood.oa.ui.adapter.CardRecordAdapter;
import com.rainwood.oa.ui.adapter.LeaveAdapter;
import com.rainwood.oa.ui.adapter.LeaveOutAdapter;
import com.rainwood.oa.ui.adapter.OvertimeAdapter;
import com.rainwood.oa.ui.adapter.ReceivableRecordAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:39
 * @Desc: 记录管理----- 加班记录、请假记录、外出记录、补卡记录、回款记录
 */
public final class RecordManagerActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.iv_menu)
    private ImageView topRightMenu;
    //content
    @ViewInject(R.id.ll_top_status)
    private LinearLayout topStatusLL;
    @ViewInject(R.id.gti_status)
    private GroupTextIcon topStatus;
    @ViewInject(R.id.gti_leave_type)
    private GroupTextIcon topLeaveType;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_record_list)
    private RecyclerView recordListView;

    private IRecordManagerPresenter mRecordManagerPresenter;

    /*
    record适配器
     */
    // 加班记录
    private OvertimeAdapter mOvertimeAdapter;
    private AdminOvertimeAdapter mAdminOvertimeAdapter;
    // 请假记录
    private LeaveAdapter mLeaveAdapter;
    // 外出记录
    private LeaveOutAdapter mOutAdapter;
    // 补卡记录
    private CardRecordAdapter mCardRecordAdapter;
    // 回款记录
    private ReceivableRecordAdapter mReceivableRecordAdapter;

    // 选择flag
    private boolean selectedTimeFlag = false;
    private AdminLeaveOutAdapter mAdminLeaveOutAdapter;

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
        topStatusLL.setVisibility(Constants.CUSTOM_ID == null ? View.VISIBLE : View.GONE);
        topRightMenu.setVisibility(Constants.CUSTOM_ID == null ? View.GONE : View.VISIBLE);
        // 设置布局管理器
        recordListView.setLayoutManager(new GridLayoutManager(this, 1));
        recordListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 设置适配器属性
        if (title.equals("加班记录")) {
            mOvertimeAdapter = new OvertimeAdapter();
            mAdminOvertimeAdapter = new AdminOvertimeAdapter();
            recordListView.setAdapter(mOvertimeAdapter);
            recordListView.setAdapter(mAdminOvertimeAdapter);
        } else if ("请假记录".equals(title)) {
            mLeaveAdapter = new LeaveAdapter();
            recordListView.setAdapter(mLeaveAdapter);
        } else if (title.contains("外出")) {
            mOutAdapter = new LeaveOutAdapter();
            mAdminLeaveOutAdapter = new AdminLeaveOutAdapter();
            recordListView.setAdapter(mOutAdapter);
            recordListView.setAdapter(mAdminLeaveOutAdapter);
        } else if (title.contains("补卡")) {
            mCardRecordAdapter = new CardRecordAdapter();
            recordListView.setAdapter(mCardRecordAdapter);
        } else if (title.contains("回款")) {
            mReceivableRecordAdapter = new ReceivableRecordAdapter();
            recordListView.setAdapter(mReceivableRecordAdapter);
        }
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void loadData() {
        // 从这里request data
        if (title.contains("加班")) {
            if (Constants.CUSTOM_ID != null) {
                mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
            } else {
                mRecordManagerPresenter.requestOvertimeRecord();
            }
        } else if (title.contains("请假")) {
            mRecordManagerPresenter.requestLeaveRecord();
        } else if (title.contains("外出")) {
            if (Constants.CUSTOM_ID != null) {
                mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
            } else {
                mRecordManagerPresenter.requestGoOutRecord();
            }
        } else if (title.contains("补卡")) {
            mRecordManagerPresenter.requestReissueRecord();
        } else if (title.contains("回款")) {
            mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
        }
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initEvent() {
        if (title.contains("加班")) {
            mOvertimeAdapter.setItemOvertime(overtimeRecord -> {
                // 客户管理--加班详情
                toast("点击了---" + overtimeRecord.getStaffName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "加班详情", "加班详情"));
            });
            // 行政人事
            mAdminOvertimeAdapter.setItemOvertime(new AdminOvertimeAdapter.OnClickItemOvertimeListener() {
                @Override
                public void onClickOvertime(AdminOverTime overtimeRecord) {
                    toast("加班详情");
                }
            });
        } else if (title.contains("请假")) {
            mLeaveAdapter.setClickItemLeave(leaveRecord -> {
                // 请假详情
                toast("点击了-----" + leaveRecord.getStaffName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "请假详情", "请假详情"));
            });
        } else if (title.contains("外出")) {
            mOutAdapter.setItemGoOut(leaveOutRecord -> {
                // 外出详情
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "外出详情", "外出详情"));
            });
        } else if (title.contains("补卡")) {
            mCardRecordAdapter.setItemGoOut(cardRecord -> {
                // 补卡详情
                toast("查看详情---" + cardRecord.getStaffName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "补卡详情", "补卡详情"));
            });
        } else if (title.contains("回款")) {
            mReceivableRecordAdapter.setClickReceivable((record, position) ->
                    PageJumpUtil.receivable2Detail(RecordManagerActivity.this, ReceivableDetailActivity.class, record.getId()));
        }

        //选择时间段
        periodTime.setOnItemClick(text -> {
            selectedTimeFlag = !selectedTimeFlag;
            new StartEndDateDialog.Builder(RecordManagerActivity.this, false)
                    .setTitle(null)
                    .setConfirm(getString(R.string.common_text_confirm))
                    .setCancel(getString(R.string.common_text_clear_screen))
                    .setAutoDismiss(false)
                    //.setIgnoreDay()
                    .setCanceledOnTouchOutside(false)
                    .setListener(new StartEndDateDialog.OnListener() {
                        @Override
                        public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                            dialog.dismiss();
                            toast("选中的时间段：" + startTime + "至" + endTime);
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }
                    })
                    .show();
            periodTime.setRightIcon(selectedTimeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedTimeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
        });

    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                toast("menu");
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getOvertimeRecords(List<OvertimeRecord> overtimeRecords) {
        // 客户管理---加班记录
        mOvertimeAdapter.setOvertimeRecordList(overtimeRecords);
    }

    @SuppressWarnings("all")
    @Override
    public void getGoOutRecords(List<LeaveOutRecord> leaveOutList) {
        // 客户管理--- 外出记录
        mOutAdapter.setLeaveOutRecordList(leaveOutList);
    }

    @SuppressWarnings("all")
    @Override
    public void getReissueRecords(List<CardRecord> cardRecordList) {
        //  行政人事---补卡记录
        mCardRecordAdapter.setLeaveOutRecordList(cardRecordList);
    }

    @Override
    public void getCustomReceivableRecords(List<ReceivableRecord> receivableRecordList) {
        //  客户管理---回款记录
        mReceivableRecordAdapter.setRecordList(receivableRecordList);
    }

    @Override
    public void getAdminOverTimeRecords(List<AdminOverTime> adminOverTimeList) {
        // 行政人事-- 加班记录
        mAdminOvertimeAdapter.setOvertimeRecordList(adminOverTimeList);
    }

    @Override
    public void getLeaveRecords(List<LeaveRecord> leaveRecordList) {
        //  行政人事---请假记录
        mLeaveAdapter.setLeaveList(leaveRecordList);
    }

    @Override
    public void getAdminLeaveOutRecords(List<AdminLeaveOut> adminLeaveOutList) {
        // 行政人事---外出记录
        mAdminLeaveOutAdapter.setLeaveOutRecordList(adminLeaveOutList);
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
