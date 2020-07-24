package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.dialog.TimerDialog;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

/**
 * @Author: a797s
 * @Date: 2020/7/6 9:12
 * @Desc: 加班申请
 */
public final class MineOverTimeApplyActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.rb_custom)
    private RadioButton customIc;
    @ViewInject(R.id.rb_task)
    private RadioButton taskIc;
    @ViewInject(R.id.rb_transaction)
    private RadioButton transactionIc;
    @ViewInject(R.id.tv_obj)
    private TextView obj;
    @ViewInject(R.id.tv_obj_title)
    private TextView objTitle;
    @ViewInject(R.id.tv_start_time_title)
    private TextView startTimeTitle;
    @ViewInject(R.id.tv_end_time_title)
    private TextView endTimeTitle;
    @ViewInject(R.id.tv_overtime_reason_title)
    private TextView reasonTitle;

    @ViewInject(R.id.et_obj)
    private EditText objName;
    @ViewInject(R.id.tv_start_time)
    private TextView startTime;
    @ViewInject(R.id.tv_end_time)
    private TextView endTime;
    @ViewInject(R.id.et_overtime_reason)
    private EditText overTimeReason;
    @ViewInject(R.id.tv_approver)
    private TextView mTextApprover;

    private boolean customSelected = true;
    private boolean taskSelected = false;
    private boolean transactionSelected = false;

    private IMinePresenter mMinePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_over_time_apply;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置必填样式
        setRequiredValue(obj, "加班对象");
        setRequiredValue(objTitle, "对象名称");
        setRequiredValue(startTimeTitle, "开始时间");
        setRequiredValue(endTimeTitle, "结束时间");
        setRequiredValue(reasonTitle, "加班事由");
        //
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 获取当前的审批人

    }

    @Override
    protected void initEvent() {

    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_custom, R.id.rb_custom, R.id.tv_task, R.id.rb_task, R.id.tv_transaction,
            R.id.rb_transaction, R.id.tv_start_time,R.id.tv_end_time,R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_custom:
            case R.id.rb_custom:
                setSelector(false, false, true, true);
                break;
            case R.id.rb_task:
            case R.id.tv_task:
                setSelector(false, true, true, false);
                break;
            case R.id.rb_transaction:
            case R.id.tv_transaction:
                setSelector(true, false, false, false);
                break;
            case R.id.tv_start_time:
                // 开始时间
                setTextTime(startTime);
                break;
            case R.id.tv_end_time:
                // 结束时间
                setTextTime(endTime);
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(startTime.getText())){
                    toast("请填写开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endTime.getText())){
                    toast("请填写结束时间");
                    return;
                }
                if (TextUtils.isEmpty(overTimeReason.getText())){
                    toast("请填写加班事由");
                    return;
                }
                // TODO: 提交加班申请
                String recordId = RandomUtil.getItemID(20);
                String startTimeStr = startTime.getText().toString().trim();
                String endTimeStr = endTime.getText().toString().trim();
                String reasonStr = overTimeReason.getText().toString().trim();
                showDialog();
                switch (moduleMenu){
                    case "askOvertime":
                        mMinePresenter.createMineOvertimeApply(recordId, startTimeStr, endTimeStr, reasonStr);
                        break;
                    case "askLeaveOut":
                        mMinePresenter.createMineLeaveOutApply(recordId, startTimeStr, endTimeStr, reasonStr);
                        break;
                }
                break;
        }
    }

    private void setSelector(boolean b, boolean b2, boolean b3, boolean b4) {
        transactionSelected = b;
        taskSelected = b2;
        customSelected = b3;
        customIc.setChecked(b4);
        taskIc.setChecked(b2);
        transactionIc.setChecked(b);
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
    public void onError(String tips) {
        if (isShowDialog()){
            hideDialog();
        }
        toast(tips);
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
