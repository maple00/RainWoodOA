package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/18 18:01
 * @Desc: 新建订单
 */
public final class OrderNewActivity extends BaseActivity {
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_custom_name)
    private TextView customNameTV;
    @ViewInject(R.id.cet_custom_name)
    private EditText customName;
    @ViewInject(R.id.tv_order_name)
    private TextView orderNameTV;
    @ViewInject(R.id.cet_order_name)
    private EditText orderName;
    @ViewInject(R.id.tv_money)
    private TextView moneyTV;
    @ViewInject(R.id.cet_money)
    private EditText money;
    @ViewInject(R.id.tv_note)
    private TextView noteTV;
    @ViewInject(R.id.cet_note)
    private TextView note;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_new;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        setRequiredValue(customNameTV, "客户名称");
        setRequiredValue(orderNameTV, "订单名称");
        setRequiredValue(moneyTV, "合同金额");
        setRequiredValue(noteTV, "备注");

    }

    @Override
    protected void initPresenter() {

    }

    @SingleClick
    @OnClick({R.id.btn_next_step, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_next_step:
                // toast("下一步");
                startActivity(getNewIntent(this, OrderActivity.class, "编辑订单"));
                break;
        }
    }
}
