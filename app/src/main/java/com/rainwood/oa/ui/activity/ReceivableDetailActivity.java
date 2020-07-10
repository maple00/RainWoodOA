package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: sxs
 * @Time: 2020/5/30 13:32
 * @Desc: 回款记录详情
 */
public final class ReceivableDetailActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_get_money)
    private TextView getMoney;
    @ViewInject(R.id.gtt_note)
    private GroupTextText note;
    @ViewInject(R.id.gtt_pay_date)
    private GroupTextText payDate;
    @ViewInject(R.id.iv_pay_voucher)
    private ImageView payVoucher;

    private IRecordManagerPresenter mRecordManagerPresenter;
    private ReceivableRecord mReceivableRecord;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_receivable_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        String receivableId = getIntent().getStringExtra("receivableId");
        mRecordManagerPresenter.requestCustomReceivableRecordDetail(receivableId);
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_pay_voucher})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_pay_voucher:
                ImageActivity.start(this, mReceivableRecord.getIco());
                break;
        }
    }

    @Override
    public void getCustomReceivableRecordDetail(ReceivableRecord receivableRecord) {
        mReceivableRecord = receivableRecord;
        // 回款信息详情
        getMoney.setText(receivableRecord.getMoney());
        note.setValue(receivableRecord.getText());
        payDate.setValue(receivableRecord.getPayDate());
        Glide.with(this).load(receivableRecord.getIco())
                .error(R.drawable.bg_monkey_king)
                .placeholder(R.drawable.bg_monkey_king)
                //.apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(payVoucher);
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
