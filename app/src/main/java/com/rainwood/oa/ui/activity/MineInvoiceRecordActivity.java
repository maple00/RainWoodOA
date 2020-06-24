package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineInvoice;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.MineInvoiceRecordAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 17:38
 * @Desc: 我的开票记录
 */
public final class MineInvoiceRecordActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    // content
    @ViewInject(R.id.gti_seller)
    private GroupTextIcon sellerView;
    @ViewInject(R.id.gti_type)
    private GroupTextIcon typeView;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTimeView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_invoice_records)
    private RecyclerView invoiceView;

    private IMinePresenter mMinePresenter;
    private MineInvoiceRecordAdapter mInvoiceRecordAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_invoice;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // 设置布局管理器
        invoiceView.setLayoutManager(new GridLayoutManager(this, 1));
        invoiceView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 设置适配器
        mInvoiceRecordAdapter = new MineInvoiceRecordAdapter();
        // 创建适配器
        invoiceView.setAdapter(mInvoiceRecordAdapter);
        // 设置刷新属性
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
        mMinePresenter.requestMineInvoiceRecords();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back,R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search:
                toast("搜索");
                break;
        }
    }

    @Override
    public void getMineInvoiceRecords(List<MineInvoice> invoiceList) {
        mInvoiceRecordAdapter.setRecordList(invoiceList);
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
