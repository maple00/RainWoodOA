package com.rainwood.oa.ui.activity;

import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CustomOrder;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.CustomOrderAdapter;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/28 19:15
 * @Desc: 客户订单列表
 */
public final class CustomOrderListActivity extends BaseActivity implements IOrderCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pagerTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_company_name)
    private TextView companyName;
    @ViewInject(R.id.rv_custom_order_list)
    private RecyclerView customOrderView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private IOrderPresenter mOrderPresenter;
    private CustomOrderAdapter mOrderAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_order_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pagerTop);
        pageTitle.setText(title);
        // 设置布局管理器
        customOrderView.setLayoutManager(new GridLayoutManager(this, 1));
        customOrderView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mOrderAdapter = new CustomOrderAdapter();
        // 设置适配器
        customOrderView.setAdapter(mOrderAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableLoadmore(true);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mOrderPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mOrderPresenter.requestCustomOrderList(Constants.CUSTOM_ID);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    /**
     * 客户订单列表
     * @param customOrderList
     */
    @Override
    public void getCustomOrderList(List<CustomOrder> customOrderList) {
        mOrderAdapter.setCustomOrderList(customOrderList);
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
