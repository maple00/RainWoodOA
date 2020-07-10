package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RecordsDetail;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.ui.adapter.AuditRecordAdapter;
import com.rainwood.oa.ui.adapter.RecordsResultsAdapter;
import com.rainwood.oa.ui.dialog.BottomRejectDialog;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICommonCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/26 13:08
 * @Desc: 记录详情---- 加班记录、请假记录、外出记录、补卡记录
 */
public final class RecordDetailActivity extends BaseActivity implements ICommonCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    // 共有的
    @ViewInject(R.id.git_state)
    private GroupIconText mGroupState;
    @ViewInject(R.id.btn_through)
    private Button btnThrough;
    @ViewInject(R.id.ll_record_result)
    private LinearLayout recordResultLl;
    // content
    // 加班记录
    @ViewInject(R.id.ll_include_overtime)
    private LinearLayout includeOvertime;
    @ViewInject(R.id.tv_overtime_apply_name)
    private TextView overTimeName;
    @ViewInject(R.id.mlv_audit_records)
    private MeasureListView auditRecords;
    @ViewInject(R.id.tv_overtime_obj)
    private TextView overtimeObj;
    @ViewInject(R.id.tv_pre_time)
    private TextView preTime;
    @ViewInject(R.id.tv_actual_time)
    private TextView actualTime;
    // 请假记录
    @ViewInject(R.id.ll_include_leave)
    private LinearLayout includeLeave;
    @ViewInject(R.id.tv_askLeave_apply_name)
    private TextView askLeaveName;
    @ViewInject(R.id.tv_depart_post)
    private TextView departPost;
    @ViewInject(R.id.tv_leave_time)
    private TextView leaveTime;
    @ViewInject(R.id.tv_type)
    private TextView type;
    @ViewInject(R.id.tv_askLeave_reason)
    private TextView askLeaveReason;
    // 外出记录
    @ViewInject(R.id.ll_include_out)
    private LinearLayout includeOut;
    @ViewInject(R.id.tv_out_apply_name)
    private TextView outApplyName;
    @ViewInject(R.id.tv_out_obj)
    private TextView outObj;
    @ViewInject(R.id.tv_out_pre_time)
    private TextView outPreTime;
    @ViewInject(R.id.tv_out_actual_time)
    private TextView outActualTime;
    // 补卡记录
    @ViewInject(R.id.ll_include_card)
    private LinearLayout includeCard;
    @ViewInject(R.id.tv_reissue_apply_name)
    private TextView reissueApplyName;
    @ViewInject(R.id.tv_reissue_time)
    private TextView reissueTime;
    @ViewInject(R.id.tv_reissue_reason)
    private TextView reissueReason;
    // 成果列表
    @ViewInject(R.id.tv_record_title)
    private TextView resultRecord;
    @ViewInject(R.id.mlv_record_result)
    private MeasureListView resultRecordListView;

    @ViewInject(R.id.ll_bottom_button)
    private LinearLayout bottomButton;

    private ICommonPresenter mCommonPresenter;
    // 审核记录
    private AuditRecordAdapter mAuditRecordAdapter;
    // 成果记录
    private RecordsResultsAdapter mResultsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        if (title.contains("加班")) {
            // 加班记录
            includeOvertime.setVisibility(View.VISIBLE);
            includeLeave.setVisibility(View.GONE);
            includeOut.setVisibility(View.GONE);
            includeCard.setVisibility(View.GONE);
        } else if (title.contains("请假")) {
            // 请假记录
            includeLeave.setVisibility(View.VISIBLE);
            includeOvertime.setVisibility(View.GONE);
            includeOut.setVisibility(View.GONE);
            includeCard.setVisibility(View.GONE);
        } else if (title.contains("外出")) {
            includeOut.setVisibility(View.VISIBLE);
            includeLeave.setVisibility(View.GONE);
            includeOvertime.setVisibility(View.GONE);
            includeCard.setVisibility(View.GONE);
        } else if (title.contains("补卡")) {
            includeOut.setVisibility(View.GONE);
            includeLeave.setVisibility(View.GONE);
            includeOvertime.setVisibility(View.GONE);
            includeCard.setVisibility(View.VISIBLE);
        }
        // 审核记录列表
        mAuditRecordAdapter = new AuditRecordAdapter();
        auditRecords.setAdapter(mAuditRecordAdapter);
        // 成果记录列表
        mResultsAdapter = new RecordsResultsAdapter();
        resultRecordListView.setAdapter(mResultsAdapter);
    }

    @Override
    protected void initData() {
        String pageFlag = getIntent().getStringExtra("pageFlag");
        if (pageFlag != null) {
            switch (pageFlag) {
                case "mineOvertime":
                case "mineLeaveOut":
                    bottomButton.setVisibility(View.GONE);
                    break;
                case "ovetime":
                case "leaveOut":
                    bottomButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    protected void initPresenter() {
        mCommonPresenter = PresenterManager.getOurInstance().getCommonPresenter();
        mCommonPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String recordId = getIntent().getStringExtra("recordId");
        // 请求数据
        if (title.contains("加班")) {
            if (moduleMenu.equals(Constants.PERSONAL_OVER_TIME_DETAIL_MENU)) {
                // 行政人事--加班详情
                mCommonPresenter.requestOverTimeRecordsById(recordId);
            } else {
                mCommonPresenter.AuditRecords();
            }
        } else if (title.contains("请假")) {
            if (moduleMenu.equals(Constants.PERSONAL_ASK_LEAVE_DETAIL_MENU)) {
                // 行政人事 --- 请假详情
                mCommonPresenter.requestAskLeaveById(recordId);
            } else {
                mCommonPresenter.AuditRecords();
            }
        } else if (title.contains("外出")) {
            if (moduleMenu.equals(Constants.PERSONAL_ASK_OUT_DETAIL_MENU)) {
                mCommonPresenter.requestAskOutByStaffId(recordId);
            } else {
                mCommonPresenter.AuditRecords();
            }
        } else if (title.contains("补卡")) {
            if (moduleMenu.equals(Constants.PERSONAL_REISSUE_CARD_DETAIL_MENU)) {
                mCommonPresenter.requestReissueCardDetailById(recordId);
            } else {
                mCommonPresenter.AuditRecords();
            }
        }
    }

    @SingleClick
    @OnClick({R.id.btn_reject, R.id.btn_through, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_reject:
                // 驳回
                new BottomRejectDialog.Builder(this)
                        .setTitle(getString(R.string.text_common_reject))
                        .setCancel("关闭")
                        .setGravity(Gravity.BOTTOM)
                        .setAnimStyle(R.style.BottomAnimStyle)
                        .setRejectToVisibility(false)
                        .setAutoDismiss(false)
                        .setListener(new BottomRejectDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String content) {
                                toast("content----" + content);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.btn_through:
                // 通过
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getAuditRecords(Map recordsMap) {
        // 模拟得到数据

    }

    /**
     * 加班详情
     *
     * @param recordsDetail
     */
    @Override
    public void getOverTimeDetail(RecordsDetail recordsDetail) {
        overTimeName.setText(recordsDetail.getStaffName());
        preTime.setText(recordsDetail.getExpectTime());
        actualTime.setText(recordsDetail.getTime());
        mGroupState.setValue(recordsDetail.getWorkFlow());
        mGroupState.setIcon(recordsDetail.getWorkFlow().contains("已")
                ? R.drawable.ic_status_finished : R.drawable.ic_status_wait_in);
        mAuditRecordAdapter.setAuditRecordsList(recordsDetail.getAuditingFollow());
        // 审核成果，待审核
        btnThrough.setVisibility(recordsDetail.getWorkFlow().contains("审核") ? View.VISIBLE : View.GONE);
        // 成果
        recordResultLl.setVisibility(ListUtils.getSize(recordsDetail.getWorkAddFruit()) == 0 ? View.GONE : View.VISIBLE);
        resultRecord.setText("加班成果");
        mResultsAdapter.setResultsList(recordsDetail.getWorkAddFruit());
    }

    /**
     * 请假详情
     *
     * @param recordsDetail
     */
    @Override
    public void getAskLeaveDetailData(RecordsDetail recordsDetail) {
        askLeaveName.setText(recordsDetail.getStaffName());
        leaveTime.setText(recordsDetail.getTime());
        mGroupState.setValue(recordsDetail.getWorkFlow());
        type.setText(recordsDetail.getType());
        askLeaveReason.setText(recordsDetail.getText());
        mGroupState.setIcon(recordsDetail.getWorkFlow().contains("已")
                ? R.drawable.ic_status_finished : R.drawable.ic_status_wait_in);
        // 审核记录
        mAuditRecordAdapter.setAuditRecordsList(recordsDetail.getAuditingFollow());
        // 审核成果，待审核
        btnThrough.setVisibility(recordsDetail.getWorkFlow().contains("审核") ? View.VISIBLE : View.GONE);
        // 成果
        recordResultLl.setVisibility(View.GONE);
    }

    /**
     * 外出详情
     *
     * @param recordsDetail
     */
    @Override
    public void getAskOutDetailData(RecordsDetail recordsDetail) {
        outApplyName.setText(recordsDetail.getStaffName());
        outPreTime.setText(recordsDetail.getExpectTime());
        outActualTime.setText(recordsDetail.getTime());
        mGroupState.setValue(recordsDetail.getWorkFlow());
        mGroupState.setIcon(recordsDetail.getWorkFlow().contains("已")
                ? R.drawable.ic_status_finished : R.drawable.ic_status_wait_in);
        mAuditRecordAdapter.setAuditRecordsList(recordsDetail.getAuditingFollow());
        // 审核成果，待审核
        btnThrough.setVisibility(recordsDetail.getWorkFlow().contains("审核") ? View.VISIBLE : View.GONE);
        // 成果
        recordResultLl.setVisibility(ListUtils.getSize(recordsDetail.getWorkOutFruit()) == 0 ? View.GONE : View.VISIBLE);
        resultRecord.setText("外出成果");
        mResultsAdapter.setResultsList(recordsDetail.getWorkOutFruit());
    }

    @Override
    public void getReissueDetailData(RecordsDetail recordsDetail) {
        reissueApplyName.setText(recordsDetail.getStaffName());
        reissueTime.setText(recordsDetail.getSignTime());
        reissueReason.setText(recordsDetail.getText());
        mGroupState.setValue(recordsDetail.getWorkFlow());
        mGroupState.setIcon(recordsDetail.getWorkFlow().contains("已")
                ? R.drawable.ic_status_finished : R.drawable.ic_status_wait_in);
        // 审核记录
        mAuditRecordAdapter.setAuditRecordsList(recordsDetail.getAuditingFollow());
        // 审核成果，待审核
        btnThrough.setVisibility(recordsDetail.getWorkFlow().contains("审核") ? View.VISIBLE : View.GONE);
        // 成果
        recordResultLl.setVisibility(View.GONE);
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
