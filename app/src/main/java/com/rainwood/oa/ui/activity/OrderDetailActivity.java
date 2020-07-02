package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.OrderCost;
import com.rainwood.oa.model.domain.OrderDetailAttachment;
import com.rainwood.oa.model.domain.OrderFollow;
import com.rainwood.oa.model.domain.OrderPayed;
import com.rainwood.oa.model.domain.OrderReceivable;
import com.rainwood.oa.model.domain.OrderTask;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.OrderCostAdapter;
import com.rainwood.oa.ui.adapter.OrderDataAdapter;
import com.rainwood.oa.ui.adapter.OrderDataCostAdapter;
import com.rainwood.oa.ui.adapter.OrderDetailAttachAdapter;
import com.rainwood.oa.ui.adapter.OrderFollowAdapter;
import com.rainwood.oa.ui.adapter.OrderPayedAdapter;
import com.rainwood.oa.ui.adapter.OrderReceivableAdapter;
import com.rainwood.oa.ui.adapter.OrderTaskAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/3 19:33
 * @Desc: 订单详情
 */
public final class OrderDetailActivity extends BaseActivity implements IOrderCallbacks {

    @ViewInject(R.id.ll_page_parent)
    private LinearLayout pageParent;
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.tv_status)
    private TextView statusTV;
    // 订单信息
    @ViewInject(R.id.mlv_order_data)
    private MeasureListView orderDataView;
    @ViewInject(R.id.mgv_order_data_cost)
    private MeasureGridView orderDataCostView;
    @ViewInject(R.id.iv_arrow)
    private ImageView arrow;
    // 订单附件
    @ViewInject(R.id.mlv_order_attach)
    private MeasureListView orderAttachView;
    // 费用计提
    @ViewInject(R.id.mlv_order_cost)
    private MeasureListView orderCostView;
    // 跟进记录
    @ViewInject(R.id.mlv_order_follow)
    private MeasureListView orderFollowView;
    // 任务分配
    @ViewInject(R.id.mlv_order_task)
    private MeasureListView orderTaskView;
    // 回款记录
    @ViewInject(R.id.mlv_order_receivable)
    private MeasureListView orderReceivableView;
    // 已付费用
    @ViewInject(R.id.mlv_order_prepaid)
    private MeasureListView orderPrepaidView;

    private OrderDataAdapter mDataAdapter;
    private OrderDataCostAdapter mDataCostAdapter;
    private OrderDetailAttachAdapter mAttachAdapter;
    private OrderCostAdapter mCostAdapter;
    private OrderFollowAdapter mFollowAdapter;
    private OrderTaskAdapter mTaskAdapter;
    private OrderReceivableAdapter mReceivableAdapter;
    private OrderPayedAdapter mPayedAdapter;

    private IOrderPresenter mOrderPresenter;

    // 选择
    private boolean selectedArrowFlag = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTop.setBackgroundColor(getColor(R.color.assistColor15));
        pageRightTitle.setText("审核记录");
        pageTitle.setText(title);
        // 创建适配器
        mDataAdapter = new OrderDataAdapter();
        mDataCostAdapter = new OrderDataCostAdapter();
        mAttachAdapter = new OrderDetailAttachAdapter();
        mCostAdapter = new OrderCostAdapter();
        mFollowAdapter = new OrderFollowAdapter();
        mTaskAdapter = new OrderTaskAdapter();
        mReceivableAdapter = new OrderReceivableAdapter();
        mPayedAdapter = new OrderPayedAdapter();
        // 设置适配器
        orderDataView.setAdapter(mDataAdapter);
        orderDataCostView.setAdapter(mDataCostAdapter);
        orderDataCostView.setNumColumns(2);
        orderDataCostView.setVisibility(View.GONE);
        orderAttachView.setAdapter(mAttachAdapter);
        orderCostView.setAdapter(mCostAdapter);
        orderFollowView.setAdapter(mFollowAdapter);
        orderTaskView.setAdapter(mTaskAdapter);
        orderReceivableView.setAdapter(mReceivableAdapter);
        orderPrepaidView.setAdapter(mPayedAdapter);
    }

    @Override
    protected void initPresenter() {
        mOrderPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        String orderId = getIntent().getStringExtra("orderId");
        String status = getIntent().getStringExtra("status");
        if (orderId != null) {
            mOrderPresenter.requestOrderDetailById(orderId);
            statusTV.setText(status);
        }
    }

    @Override
    protected void initEvent() {
        mAttachAdapter.setOnClickAttachListener(new OrderDetailAttachAdapter.OnClickAttachListener() {
            @Override
            public void onClickDownload(OrderDetailAttachment attachment, int position) {
                FileManagerUtil.fileDownload(OrderDetailActivity.this, null, attachment.getSrc(),
                        attachment.getName(), attachment.getFormat());
            }

            @Override
            public void onClickPreview(OrderDetailAttachment attachment, int position) {
                FileManagerUtil.filePreview(OrderDetailActivity.this, TbsActivity.class,
                        attachment.getSrc(), attachment.getName(), attachment.getFormat());
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu, R.id.iv_arrow, R.id.tv_order_attach_all, R.id.tv_order_cost_all,
            R.id.tv_order_follow_all, R.id.iv_follow_record, R.id.tv_follow_record, R.id.tv_order_task_all,
            R.id.tv_order_receivable_all, R.id.tv_order_prepaid_all, R.id.tv_page_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                toast("审核记录");
                break;
            case R.id.iv_menu:
                showQuickFunction(this, pageParent);
                break;
            case R.id.iv_arrow:
                if (selectedArrowFlag) {
                    arrow.setImageDrawable(getDrawable(R.drawable.ic_up_arrow));
                } else {
                    arrow.setImageDrawable(getDrawable(R.drawable.ic_down_arrow));
                }
                orderDataCostView.setVisibility(selectedArrowFlag ? View.VISIBLE : View.GONE);
                selectedArrowFlag = !selectedArrowFlag;
                //toast("选择");
                break;
            case R.id.tv_order_attach_all:
                toast("订单附件查看全部");
                break;
            case R.id.tv_order_cost_all:
                toast("费用计提查看全部");
                break;
            case R.id.tv_order_follow_all:
                toast("跟进记录查看全部");
                break;
            case R.id.iv_follow_record:
            case R.id.tv_follow_record:
                toast("添加跟记录");
                break;
            case R.id.tv_order_task_all:
                toast("任务分类查看全部");
                break;
            case R.id.tv_order_receivable_all:
                toast("回款记录查看全部");
                break;
            case R.id.tv_order_prepaid_all:
                toast("已付费用查看全部");
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getOrderDetail(Map orderDetailMap) {
        List<OrderDetailAttachment> attachmentList = (List<OrderDetailAttachment>) orderDetailMap.get("attachment");
        List<OrderCost> costList = (List<OrderCost>) orderDetailMap.get("cost");
        List<OrderFollow> followList = (List<OrderFollow>) orderDetailMap.get("follow");
        List<OrderTask> taskList = (List<OrderTask>) orderDetailMap.get("task");
        List<OrderReceivable> receivableList = (List<OrderReceivable>) orderDetailMap.get("receivable");
        List<OrderPayed> payedList = (List<OrderPayed>) orderDetailMap.get("payed");
        List<FontAndFont> shoDataList = (List<FontAndFont>) orderDetailMap.get("showData");
        List<FontAndFont> hideDataList = (List<FontAndFont>) orderDetailMap.get("hideData");
        // 订单状态
        String orderStatus = (String) orderDetailMap.get("orderStatus");
        statusTV.setText(orderStatus);
        mDataAdapter.setList(shoDataList);
        mDataCostAdapter.setList(hideDataList);
        mAttachAdapter.setAttachmentList(attachmentList);
        mCostAdapter.setList(costList);
        mFollowAdapter.setList(followList);
        mTaskAdapter.setTaskList(taskList);
        mReceivableAdapter.setList(receivableList);
        mPayedAdapter.setPayedList(payedList);
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
