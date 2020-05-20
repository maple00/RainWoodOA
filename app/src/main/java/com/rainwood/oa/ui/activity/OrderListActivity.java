package com.rainwood.oa.ui.activity;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.views.RWNestedScrollView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.model.domain.OrderStatics;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.OrderListAdapter;
import com.rainwood.oa.ui.adapter.OrderStaticsAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/20 9:11
 * @Desc: 订单列表
 */
public final class OrderListActivity extends BaseActivity implements IOrderCallbacks {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.iv_show_hide)
    private ImageView showHideView;
    @ViewInject(R.id.mlv_statics_order)
    private MeasureListView staticsOrder;
    @ViewInject(R.id.gti_status)
    private GroupTextIcon orderStatus;
    @ViewInject(R.id.gti_stuff)
    private GroupTextIcon departStuff;
    @ViewInject(R.id.gti_sorting)
    private GroupTextIcon sorting;
    @ViewInject(R.id.rv_order)
    private RecyclerView orderView;
    // 算高度，优化卡顿现象
    @ViewInject(R.id.order_pager_nested_scroller)
    private RWNestedScrollView pagerNestedScroller;
    @ViewInject(R.id.ll_order_pager_parent)
    private LinearLayout orderPagerParent;
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout searchViewPager;
    @ViewInject(R.id.ll_status_top)
    private LinearLayout statusTop;

    // 收起或展开-- 默认收起
    private boolean isShowHide = false;

    private IOrderPresenter mOrderPresenter;

    private OrderStaticsAdapter mStaticsAdapter;
    private OrderListAdapter mOrderListAdapter;

    private List<OrderStatics> mOrderStatics;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchContent.setOnFocusChangeListener((v, hasFocus) -> searchTV.setText(hasFocus
                ? getString(R.string.text_common_search) : getString(R.string.custom_text_manager)));
        // 布局管理器
        orderView.setLayoutManager(new GridLayoutManager(this, 1));
        orderView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mStaticsAdapter = new OrderStaticsAdapter();
        mOrderListAdapter = new OrderListAdapter();
        // 设置适配器
        staticsOrder.setAdapter(mStaticsAdapter);
         orderView.setAdapter(mOrderListAdapter);
        mOrderListAdapter.setContext(this);
    }

    @Override
    protected void initEvent() {
        orderPagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (searchViewPager == null) {
                    return;
                }
                // 头部搜索框
                int headerHeight = searchViewPager.getMeasuredHeight();
                int staticsHeight = staticsOrder.getMeasuredHeight();
                int statusTopHeight = statusTop.getMeasuredHeight();
                int hideViewHeight = showHideView.getMeasuredHeight();
                // nestScrollView
                pagerNestedScroller.setHeaderHeight(headerHeight + staticsHeight + statusTopHeight
                        + StatusBarUtils.getStatusBarHeight(OrderListActivity.this) + hideViewHeight);
                // parentPager
                int measuredHeight = orderPagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) orderView.getLayoutParams();
                layoutParams.height = measuredHeight;
                // 滑动高度
                orderView.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    orderPagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        // 请求数据
        mOrderPresenter.requestOrderData();
    }

    @Override
    protected void initPresenter() {
        mOrderPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_show_hide, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_show_hide:
                isShowHide = !isShowHide;
                showHideView.setImageResource(isShowHide ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
                mStaticsAdapter.setList(isShowHide ? mOrderStatics : mOrderStatics.subList(0, 3));
                break;
        }
    }

    /**
     * 返回所有的统计信息
     *
     * @param orderListData 订单列表信息
     */
    @SuppressWarnings("all")
    @Override
    public void getAllOrderPage(Map orderListData) {
        //LogUtils.d("sxs", "订单列表：" + orderListData);
        // 订单统计信息
        mOrderStatics = (List<OrderStatics>) orderListData.get("orderStatics");
        showHideView.setVisibility(ListUtils.getSize(mOrderStatics) <= 3 ? View.GONE : View.VISIBLE);
        // 默认展示3条
        mStaticsAdapter.setList(mOrderStatics.subList(0, 3));
        // 订单列表
        List<Order> orderList = (List<Order>) orderListData.get("order");
        mOrderListAdapter.setList(orderList);
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
