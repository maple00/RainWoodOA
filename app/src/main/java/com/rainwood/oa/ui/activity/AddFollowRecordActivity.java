package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

/**
 * @Author: a797s
 * @Date: 2020/5/19 17:56
 * @Desc: 新增跟进记录
 */
public final class AddFollowRecordActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.cet_choose_time)
    private EditText chooseTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_follow_record;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void initPresenter() {

    }

    @OnClick({R.id.iv_page_back, R.id.btn_confirm, R.id.cet_choose_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.cet_choose_time:
                //toast("选择时间");
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        .setShowConfirm(true)
                        .setShowImageClose(false)
                        .setListener(new DateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + getString(R.string.common_year) + month + getString(R.string.common_month) + day + getString(R.string.common_day));
                                chooseTime.setText(year + "-" + month + "-" + day);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_confirm:
                toast("确定了");
                finish();
                break;
        }
    }
}
