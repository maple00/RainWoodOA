package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.AuditRecords;
import com.rainwood.oa.presenter.ICommonPresenter;
import com.rainwood.oa.ui.adapter.AuditRecordAdapter;
import com.rainwood.oa.ui.dialog.BottomRejectDialog;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICommonCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
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
    @ViewInject(R.id.tv_apply_name)
    private TextView applyName;
    @ViewInject(R.id.tv_reason)
    private TextView reason;
    // content
    // 加班记录
    @ViewInject(R.id.ll_include_overtime)
    private LinearLayout includeOvertime;
    @ViewInject(R.id.mlv_audit_records)
    private MeasureListView auditRecords;
    @ViewInject(R.id.tv_overtime_task)
    private TextView overtimeTask;
    @ViewInject(R.id.tv_pre_time)
    private TextView preTime;
    // 请假记录
    @ViewInject(R.id.ll_include_leave)
    private LinearLayout includeLeave;
    @ViewInject(R.id.tv_depart_post)
    private TextView departPost;
    @ViewInject(R.id.tv_leave_time)
    private TextView leaveTime;
    @ViewInject(R.id.tv_type)
    private TextView type;
    // 外出记录
    @ViewInject(R.id.ll_include_out)
    private LinearLayout includeOut;
    @ViewInject(R.id.tv_out_obj)
    private TextView outObj;
    @ViewInject(R.id.tv_out_pre_time)
    private TextView outPreTime;
    // 补卡记录
    @ViewInject(R.id.ll_include_card)
    private LinearLayout includeCard;
    @ViewInject(R.id.tv_reissue_time)
    private TextView reissueTime;

    private ICommonPresenter mCommonPresenter;
    // 审核记录
    private AuditRecordAdapter mAuditRecordAdapter;

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
        }else if (title.contains("外出")){
            includeOut.setVisibility(View.VISIBLE);
            includeLeave.setVisibility(View.GONE);
            includeOvertime.setVisibility(View.GONE);
            includeCard.setVisibility(View.GONE);
        }else if (title.contains("补卡")){
            includeOut.setVisibility(View.GONE);
            includeLeave.setVisibility(View.GONE);
            includeOvertime.setVisibility(View.GONE);
            includeCard.setVisibility(View.VISIBLE);
        }
        mAuditRecordAdapter = new AuditRecordAdapter();
        auditRecords.setAdapter(mAuditRecordAdapter);
    }

    @Override
    protected void initPresenter() {
        mCommonPresenter = PresenterManager.getOurInstance().getCommonPresenter();
        mCommonPresenter.registerViewCallback(this);
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

    @Override
    protected void loadData() {
        // 请求数据
        if (title.contains("加班")) {
            mCommonPresenter.AuditRecords();
        } else if (title.contains("请假")) {
            mCommonPresenter.AuditRecords();
        }else if (title.contains("外出")){
            mCommonPresenter.AuditRecords();
        }else if (title.contains("补卡")){
            mCommonPresenter.AuditRecords();
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getAuditRecords(Map recordsMap) {
        // 得到数据
        List<AuditRecords> recordsList = (List<AuditRecords>) recordsMap.get("overtime");
        mAuditRecordAdapter.setAuditRecordsList(recordsList);
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
