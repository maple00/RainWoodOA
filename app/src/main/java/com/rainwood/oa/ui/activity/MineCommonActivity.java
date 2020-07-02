package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.AuditRecord;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.AuditRecordsAdapter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 14:35
 * @Desc: 公共页面---- title + recyclerView
 * 补卡申请（审核记录）
 */
public final class MineCommonActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_common_list)
    private RecyclerView commonView;

    private IMinePresenter mMinePresenter;
    private AuditRecordsAdapter mRecordsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_common;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        commonView.setLayoutManager(new GridLayoutManager(this, 1));
        commonView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mRecordsAdapter = new AuditRecordsAdapter();
        // 设置适配器
        commonView.setAdapter(mRecordsAdapter);
        //设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String recordId = getIntent().getStringExtra("recordId");
        if (recordId != null) {
            mMinePresenter.requestAuditRecords(recordId);
        }
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
    public void getMineAuditRecords(List<AuditRecord> auditRecordList) {
        mRecordsAdapter.setRecordList(auditRecordList);
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
