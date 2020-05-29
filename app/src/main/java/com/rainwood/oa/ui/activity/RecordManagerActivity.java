package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.CardRecordAdapter;
import com.rainwood.oa.ui.adapter.LeaveAdapter;
import com.rainwood.oa.ui.adapter.LeaveOutAdapter;
import com.rainwood.oa.ui.adapter.OvertimeAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.tencent.smtt.utils.LogFileUtils;

import java.util.List;
import java.util.Map;

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
    private RecyclerView recordList;

    private IRecordManagerPresenter mRecordManagerPresenter;

    /*
    record适配器
     */
    // 加班记录
    private OvertimeAdapter mOvertimeAdapter;
    // 请假记录
    private LeaveAdapter mLeaveAdapter;
    // 外出记录
    private LeaveOutAdapter mOutAdapter;
    // 补卡记录
    private CardRecordAdapter mCardRecordAdapter;

    // 选择flag
    private boolean selectedTimeFlag = false;


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
        recordList.setLayoutManager(new GridLayoutManager(this, 1));
        recordList.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 设置适配器属性
        if (title.equals("加班记录")) {
            mOvertimeAdapter = new OvertimeAdapter();
            recordList.setAdapter(mOvertimeAdapter);
        } else if ("请假记录".equals(title)) {
            mLeaveAdapter = new LeaveAdapter();
            recordList.setAdapter(mLeaveAdapter);
        } else if (title.contains("外出")) {
            mOutAdapter = new LeaveOutAdapter();
            recordList.setAdapter(mOutAdapter);
        } else if (title.contains("补卡")) {
            mCardRecordAdapter = new CardRecordAdapter();
            recordList.setAdapter(mCardRecordAdapter);
        }else if (title.contains("回款")){

        }
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void loadData() {
        // 从这里request data
        if (title.contains("加班")) {
            mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
        } else if (title.contains("请假")) {
            mRecordManagerPresenter.requestLeaveRecord();
        } else if (title.contains("外出")) {
            mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
        } else if (title.contains("补卡")) {
            mRecordManagerPresenter.requestReissueRecord();
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
                // 加班详情
                toast("点击了---" + overtimeRecord.getStaffName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "加班详情"));
            });
        } else if (title.contains("请假")) {
            mLeaveAdapter.setClickItemLeave(leaveRecord -> {
                // 请假详情
                toast("点击了-----" + leaveRecord.getName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "请假详情"));
            });
        } else if (title.contains("外出")) {
            mOutAdapter.setItemGoOut(leaveOutRecord -> {
                // 外出详情
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "外出详情"));
            });
        } else if (title.contains("补卡")) {
            mCardRecordAdapter.setItemGoOut(cardRecord -> {
                // 补卡详情
                toast("查看详情---" + cardRecord.getName());
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "补卡详情"));
            });
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
        // 加班记录
        mOvertimeAdapter.setOvertimeRecordList(overtimeRecords);
    }

    @SuppressWarnings("all")
    @Override
    public void getLeaveRecords(Map recordMap) {
        // 请假记录
        List<LeaveRecord> leaveRecordList = (List<LeaveRecord>) recordMap.get("leave");
        mLeaveAdapter.setLeaveList(leaveRecordList);
    }

    @SuppressWarnings("all")
    @Override
    public void getGoOutRecords(List<LeaveOutRecord> leaveOutList) {
        // 外出记录
        mOutAdapter.setLeaveOutRecordList(leaveOutList);
    }

    @SuppressWarnings("all")
    @Override
    public void getReissueRecords(Map reissueMap) {
        // 补卡记录
        List<CardRecord> cardList = (List<CardRecord>) reissueMap.get("cardRecord");
        mCardRecordAdapter.setLeaveOutRecordList(cardList);
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
