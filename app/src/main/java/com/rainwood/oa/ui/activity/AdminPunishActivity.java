package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/27 18:12
 * @Desc: 行政处罚activity
 */
public final class AdminPunishActivity extends BaseActivity {

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
    private EditText staff;
    @ViewInject(R.id.cet_punish_money)
    private EditText money;
    @ViewInject(R.id.cet_punish_reason)
    private EditText reason;

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
                toast("选择员工");
                break;
            case R.id.btn_confirm:
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setAutoDismiss(false)
                        .setAnimStyle(R.style.BottomAnimStyle)
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                dialog.dismiss();
                                toast(password);
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
}
