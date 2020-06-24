package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.oa.ui.dialog.TimeDialog;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/12 13:42
 * @Desc: 补卡申请
 */
public final class ReissueApplyActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView rightTitle;
    // content
    @ViewInject(R.id.tv_reissue_date)
    private TextView reissueDate;
    @ViewInject(R.id.tv_reissue_date_content)
    private TextView reissueDateContent;
    @ViewInject(R.id.tv_reissue_time)
    private TextView reissueTime;
    @ViewInject(R.id.tv_reissue_time_content)
    private TextView reissueTimeContent;
    @ViewInject(R.id.tv_reissue_content)
    private TextView reissueContent;
    @ViewInject(R.id.cet_content)
    private EditText content;

    private IMinePresenter mMinePresenter;
    private String reissueId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_reissue_apply;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        rightTitle.setText("审核记录");
        rightTitle.setTextColor(getColor(R.color.fontColor));
        setRequiredValue(reissueDate, "补卡日期");
        setRequiredValue(reissueTime, "时间点");
        setRequiredValue(reissueContent, "补卡事由");
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void initData() {
        MineRecords reissue = (MineRecords) getIntent().getSerializableExtra("reissue");
        if (reissue != null) {
            reissueId = reissue.getId();
            String[] dateSplit = reissue.getSignTime().split(" ");
            reissueDateContent.setText(dateSplit[0]);
            reissueTimeContent.setText(dateSplit[1]);
            content.setText(reissue.getText());
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title, R.id.btn_delete, R.id.btn_commit,
            R.id.tv_reissue_date_content, R.id.tv_reissue_time_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                //toast("审核记录");
                PageJumpUtil.reissueApply2AuditList(this, MineCommonActivity.class, "审核记录", reissueId);
                break;
            case R.id.tv_reissue_date_content:
                //toast("选择日期");
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setShowConfirm(true)
                        .setShowImageClose(false)
                        .setListener(new DateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                reissueDateContent.setText(year + "-" + month + "-" + day);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_reissue_time_content:
                // toast("选择时间");
                new TimeDialog.Builder(this)
                        .setTitle(getString(R.string.time_title))
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setIgnoreSecond()
                        .setShowConfirm(true)
                        .setShowImageClose(false)
                        .setListener(new TimeDialog.OnListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int hour, int minute, int second) {
                                reissueTimeContent.setText(hour + ":" + minute + ":00");
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_delete:
                toast("删除");
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(reissueDateContent.getText())) {
                    toast("请选择补卡日期");
                    return;
                }
                if (TextUtils.isEmpty(reissueTimeContent.getText())) {
                    toast("请选择补卡时间");
                    return;
                }
                if (TextUtils.isEmpty(content.getText())) {
                    toast("请填写补卡事由");
                    return;
                }
                mMinePresenter.requestMineReissueApply();
                toast("提交我的补卡申请");
                break;
        }
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
