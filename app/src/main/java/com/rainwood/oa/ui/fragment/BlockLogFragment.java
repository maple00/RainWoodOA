package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.wheel.BaseDialog;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment {

    @ViewInject(R.id.sxs_status_bar)
    private View statusBar;


    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_blocklog;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        // 设置状态栏高度
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(getContext()));
        statusBar.setLayoutParams(layoutParams);
    }

    @OnClick(R.id.btn_dialog_pay)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_pay:
                // 支付密码输入对话框
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setMoney("￥ 100.00")
                        .setAutoDismiss(false) // 设置点击按钮后不关闭对话框
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
        }
    }
}
