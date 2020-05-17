package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.oa.ui.dialog.TimerDialog;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment {

    @ViewInject(R.id.rl_item_search)
    private RelativeLayout searchView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_blocklog;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        StatusBarUtils.immersive(this.getActivity());
        StatusBarUtils.setPaddingSmart(this.getContext(), searchView);
    }

    @SingleClick
    @OnClick({R.id.btn_dialog_pay, R.id.btn_date_timer, R.id.btn_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_pay:
                // 支付密码输入对话框
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setAutoDismiss(false)
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                toast(password);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_date_timer:
                // 自定义日期时间控件
                LogUtils.d(TAG, "点击了日期时间组合控件");
                new TimerDialog.Builder(getContext(), true)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setIgnoreSecond()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new TimerDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minutes, int second) {
                                dialog.dismiss();
                                toast("选中了：" + year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_date:
                // 自定义日期控件
                new DateDialog.Builder(getContext(), false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                dialog.dismiss();
                                toast("选中的时间段：" + startTime + "至" + endTime);
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
