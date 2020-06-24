package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Order;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.OrderListAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

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
    @ViewInject(R.id.cl_order_list_parent)
    private CoordinatorLayout orderListParent;
    @ViewInject(R.id.gti_status)
    private GroupTextIcon orderStatus;
    @ViewInject(R.id.gti_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_sorting)
    private GroupTextIcon sorting;
    @ViewInject(R.id.divider)
    private View divider;

    @ViewInject(R.id.rv_order)
    private RecyclerView orderView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private IOrderPresenter mOrderPresenter;
    private OrderListAdapter mOrderListAdapter;
    // 选中标记
    private boolean selectedStatusFlag = false;
    private boolean selectedDepartFlag = false;
    private boolean selectedSortingFlag = false;
    private CommonGridAdapter mSelectedAdapter;
    private StaffDefaultSortAdapter mDefaultSortAdapter;
    private View mMaskLayer;
    private List<SelectedItem> mStateList;
    private List<SelectedItem> mSortList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_pager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        /*
         *订单列表
         */
        searchContent.setOnFocusChangeListener((v, hasFocus) -> searchTV.setText(hasFocus
                ? getString(R.string.text_common_search) : getString(R.string.custom_text_manager)));
        // 布局管理器
        orderView.setLayoutManager(new GridLayoutManager(this, 1));
        orderView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mOrderListAdapter = new OrderListAdapter();
        // 设置适配器
        orderView.setAdapter(mOrderListAdapter);
        // 设置刷新相关属性 -- 可加载不可刷新
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
        mOrderPresenter.requestOrderList();
        // 订单-- condition
        mOrderPresenter.requestCondition();
    }


    @Override
    protected void initEvent() {
        // 订单状态
        orderStatus.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            orderStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            // TODO: 查询状态
            if (selectedStatusFlag) {
                stateConditionPopDialog(mStateList, orderStatus);
            }
        });
        // 部门员工
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(OrderListActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));
        // 排序状态
        sorting.setOnItemClick(text -> {
            selectedSortingFlag = !selectedSortingFlag;
            sorting.setRightIcon(selectedSortingFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedSortingFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            if (selectedSortingFlag) {
                defaultSortConditionPopDialog();
            }
        });

        /*
        滑动优化
         */
        orderListParent.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

            }
        });

        // 查看订单详情
        mOrderListAdapter.setClickItemOrder((order, position) -> {
            // 订单详情
            PageJumpUtil.orderList2Detail(OrderListActivity.this, OrderDetailActivity.class, order.getId(), order.getWorkFlow());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            String staff = data.getStringExtra("staff");
            String staffId = data.getStringExtra("staffId");
            String position = data.getStringExtra("position");

            toast("员工：" + staff + "\n员工编号：" + staffId + "\n 职位：" + position);
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            /*case R.id.iv_show_hide:
                isShowHide = !isShowHide;
                showHideView.setImageResource(isShowHide ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow);
                mStaticsAdapter.setList(isShowHide ? mOrderStatics : mOrderStatics.subList(0, 3));
                break;*/
        }
    }

/*
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
        // mContentListAdapter.setList(orderList);
    }*/

    /**
     * 总的订单列表
     *
     * @param orderList
     */
    @Override
    public void getOrderList(List<Order> orderList) {
        LogUtils.d("sxs", "共-- " + ListUtils.getSize(orderList) + "-- 条数据");
        mOrderListAdapter.setList(orderList);
    }

    @Override
    public void getOrderCondition(List<SelectedItem> stateList, List<SelectedItem> sortList) {
        mStateList = stateList;
        mSortList = sortList;
    }

    /**
     * 状态选择
     */
    private void stateConditionPopDialog(List<SelectedItem> stateList, GroupTextIcon targetGTI) {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSelectedAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedStatusFlag = false;
            selectedDepartFlag = false;
            selectedSortingFlag = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedStatusFlag = false;
                selectedDepartFlag = false;
                selectedSortingFlag = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mSelectedAdapter.setTextList(stateList);
    }

    private void defaultSortConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(1);
                    mDefaultSortAdapter = new StaffDefaultSortAdapter();
                    contentList.setAdapter(mDefaultSortAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedSortingFlag = false;
            sorting.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedSortingFlag = false;
                sorting.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 筛选条件点击事件
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mSortList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
        });
        mDefaultSortAdapter.setItemList(mSortList);
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
