package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/5/27 18:12
 * @Desc: 行政处罚activity
 */
public final class AdminPunishActivity extends BaseActivity implements IFinancialCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_choose_staff)
    private TextView chooseStaff;
    @ViewInject(R.id.tv_punish_money)
    private TextView punishMoney;
    @ViewInject(R.id.tv_punish_reason)
    private TextView punishReason;
    @ViewInject(R.id.tv_punish_tips)
    private TextView punishTips;
    @ViewInject(R.id.cet_choose_staff)
    private EditText mEditStaff;
    @ViewInject(R.id.cet_punish_money)
    private EditText mEditMoney;
    @ViewInject(R.id.cet_punish_reason)
    private EditText mEditReason;

    private IFinancialPresenter mFinancialPresenter;
    private String mStaffId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_admin_punish;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);

        setRequiredValue(chooseStaff, "选择员工");
        setRequiredValue(punishMoney, "罚款金额(元)");
        setRequiredValue(punishReason, "处罚原因");
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            // 选择被介绍员工
            if (data != null) {
                String staff = data.getStringExtra("staff");
                String position = data.getStringExtra("position");
                mStaffId = data.getStringExtra("staffId");
                mEditStaff.setText(position + "-" + staff);
            }
        }
    }

    @SingleClick
    @OnClick({R.id.cet_choose_staff, R.id.iv_choose_staff, R.id.iv_page_back, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.cet_choose_staff:
            case R.id.iv_choose_staff:
                // toast("选择员工");
                startActivityForResult(getNewIntent(this, ContactsActivity.class, "通讯录", ""),
                        CHOOSE_STAFF_REQUEST_SIZE);
                break;
            case R.id.btn_confirm:
                // 确认
                if (TextUtils.isEmpty(mEditStaff.getText())) {
                    toast("请选择员工");
                    return;
                }
                if (TextUtils.isEmpty(mEditMoney.getText())) {
                    toast("请填写罚款金额");
                    return;
                }
                if (TextUtils.isEmpty(mEditReason.getText())) {
                    toast("请填写处罚原因");
                    return;
                }
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setAutoDismiss(false)
                        .setAnimStyle(R.style.BottomAnimStyle)
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                dialog.dismiss();
                                String money = mEditMoney.getText().toString().trim();
                                String reason = mEditReason.getText().toString().trim();
                                mFinancialPresenter.requestPunishStaff(mStaffId, money, reason, password);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void getPunishResult(boolean success) {
        if (success) {
            toast("处罚成功");
            postDelayed(this::finish, 500);
        } else {
            toast("处罚失败");
        }
    }

    @Override
    public void onError(String tips) {
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
        if (mFinancialPresenter != null) {
            mFinancialPresenter.unregisterViewCallback(this);
        }
    }
}
