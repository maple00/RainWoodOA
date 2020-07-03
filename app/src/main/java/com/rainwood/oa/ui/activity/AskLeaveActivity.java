package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.dialog.SelectorListDialog;
import com.rainwood.oa.ui.dialog.TimerDialog;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/3 16:12
 * @Desc: 请假申请
 */
public final class AskLeaveActivity extends BaseActivity implements IMineCallbacks {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // title
    @ViewInject(R.id.tv_leave_type)
    private TextView leaveTypeTv;
    @ViewInject(R.id.tv_start_time)
    private TextView startTimeTv;
    @ViewInject(R.id.tv_end_time)
    private TextView endTimeTv;
    @ViewInject(R.id.tv_leave_reason)
    private TextView leaveReasonTv;
    // content
    @ViewInject(R.id.tv_leave_type_text)
    private TextView mTextLeaveType;
    @ViewInject(R.id.tv_start_time_text)
    private TextView mTextStartTime;
    @ViewInject(R.id.tv_end_time_text)
    private TextView mTextEndTime;
    @ViewInject(R.id.et_leave_reason)
    private EditText mEditLeaveReason;

    private IMinePresenter mMinePresenter;
    private List<String> mLeaveList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ask_leave_mine;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        setRequiredValue(leaveTypeTv, "请假类型");
        setRequiredValue(startTimeTv, "开始时间");
        setRequiredValue(endTimeTv, "结束时间");
        setRequiredValue(leaveReasonTv, "请假事由");

    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求请假类型
        mMinePresenter.requestMineLeaveCondition();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_leave_type_text, R.id.tv_start_time_text, R.id.tv_end_time_text,
            R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_leave_type_text:
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(mLeaveList)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                mTextLeaveType.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }

                        })
                        .show();
                break;
            case R.id.tv_start_time_text:
                setTextTime(mTextStartTime);

                break;
            case R.id.tv_end_time_text:
                setTextTime(mTextEndTime);
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(mTextLeaveType.getText())) {
                    toast("请选择请假类型");
                    return;
                }
                if (TextUtils.isEmpty(mTextStartTime.getText())) {
                    toast("请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(mTextEndTime.getText())) {
                    toast("请选择结束时间");
                    return;
                }
                if (TextUtils.isEmpty(mEditLeaveReason.getText())) {
                    toast("请填写请假事由");
                    return;
                }
                String leaveType = mTextLeaveType.getText().toString().trim();
                String startTime = mTextStartTime.getText().toString().trim();
                String endTime = mTextEndTime.getText().toString().trim();
                String leaveReason = mEditLeaveReason.getText().toString().trim();
                // TODO: 提交请假申请
                mMinePresenter.createMineLeaveRecord("", leaveType, startTime, endTime, leaveReason);
                break;
        }
    }

    /**
     * 选择时间
     *
     * @param textView
     */
    private void setTextTime(TextView textView) {
        new TimerDialog.Builder(this, true)
                .setTitle(null)
                .setConfirm(getString(R.string.common_text_confirm))
                .setCancel(getString(R.string.common_text_clear_screen))
                .setAutoDismiss(false)
                .setIgnoreSecond()
                .setCanceledOnTouchOutside(false)
                .setListener(new TimerDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minutes, int second) {
                        dialog.dismiss();
                        textView.setText(year + "-" + (month < 10 ? "0" + month : month) + "-"
                                + (day < 10 ? "0" + day : day) + " " + (hour < 10 ? "0" + hour : hour)
                                + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (second < 10 ? "0" + second : second));
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void getMineLeaveRecords(List<SelectedItem> stateList, List<SelectedItem> leaveTypeList) {
        mLeaveList = new ArrayList<>();
        for (SelectedItem item : leaveTypeList) {
            mLeaveList.add(item.getName());
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mMinePresenter != null) {
            mMinePresenter.unregisterViewCallback(this);
        }
    }
}
