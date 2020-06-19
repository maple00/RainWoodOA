package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IBlockLogCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/19 11:48
 * @Desc: 待办事项详情
 */
public final class BlockLogDetailActivity extends BaseActivity implements IBlockLogCallbacks {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_name)
    private TextView mTextName;
    @ViewInject(R.id.tv_type)
    private TextView mTextType;
    @ViewInject(R.id.tv_desc)
    private TextView mTextDesc;
    @ViewInject(R.id.tv_date_tips)
    private TextView mTextDateTip;
    @ViewInject(R.id.tv_date_deal)
    private TextView mTextDateDeal;

    private IBlockLogPresenter mBlockLogPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_block_log_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mBlockLogPresenter = PresenterManager.getOurInstance().getBlockLogPresenter();
        mBlockLogPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String blockId = getIntent().getStringExtra("blockId");
        // TODO: 请求详情数据
        mBlockLogPresenter.requestBlockDetail(blockId);
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getBlockLogDetail(BlockLog blockLog) {
        mTextName.setText(blockLog.getTypeId());
        mTextType.setText(blockLog.getType());
        mTextDesc.setText(blockLog.getText());
        mTextDateTip.setText(blockLog.getStartDay());
        mTextDateDeal.setText(blockLog.getTime());
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mBlockLogPresenter != null) {
            mBlockLogPresenter.unregisterViewCallback(this);
        }
    }
}
