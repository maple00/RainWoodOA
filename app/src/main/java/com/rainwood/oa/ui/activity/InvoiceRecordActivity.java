package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.FinancialInvoiceRecord;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.FinancialInvoiceRecordAdapter;
import com.rainwood.oa.ui.adapter.InvoiceRecordAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/5/30 17:41
 * @Desc: 客户开票记录
 */
public final class InvoiceRecordActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTopSearch;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.rv_invoice_records)
    private RecyclerView invoiceRecordView;
    // @ViewInject(R.id.nsv_invoice_record)
    // private NestedScrollView invoiceRecordNest;
    @ViewInject(R.id.ll_status_invoice)
    private LinearLayout invoiceStatus;
    @ViewInject(R.id.ll_type_invoice)
    private LinearLayout invoiceType;
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.line_allocated)
    private View lineAllocated;
    @ViewInject(R.id.line_un_allocated)
    private View lineUnAllocated;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_type)
    private GroupTextIcon typeView;
    @ViewInject(R.id.gti_pay_team)
    private GroupTextIcon payTeam;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.ll_apply_invoice)
    private LinearLayout applyInvoice;
    @ViewInject(R.id.btn_apply_open)
    private Button applyOpen;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;

    private IRecordManagerPresenter mRecordManagerPresenter;
    private InvoiceRecordAdapter mInvoiceRecordAdapter;
    private FinancialInvoiceRecordAdapter mFinancialInvoiceRecordAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_record;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        pageTitle.setText(title);
        // 设置布局管理器
        invoiceRecordView.setLayoutManager(new GridLayoutManager(this, 1));
        invoiceRecordView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        if (Constants.CUSTOM_ID == null) {
            pageTop.setVisibility(View.GONE);
            pageTopSearch.setVisibility(View.VISIBLE);
            StatusBarUtils.setPaddingSmart(this, pageTopSearch);
            applyInvoice.setVisibility(View.GONE);
            invoiceStatus.setVisibility(View.VISIBLE);
            invoiceType.setVisibility(View.VISIBLE);
            // 适配器
            mFinancialInvoiceRecordAdapter = new FinancialInvoiceRecordAdapter();
            invoiceRecordView.setAdapter(mFinancialInvoiceRecordAdapter);
        } else {
            pageTop.setVisibility(View.VISIBLE);
            pageTopSearch.setVisibility(View.GONE);
            StatusBarUtils.setPaddingSmart(this, pageTop);
            invoiceStatus.setVisibility(View.GONE);
            invoiceType.setVisibility(View.GONE);
            applyInvoice.setVisibility(View.VISIBLE);
            // 适配器
            mInvoiceRecordAdapter = new InvoiceRecordAdapter();
            invoiceRecordView.setAdapter(mInvoiceRecordAdapter);
        }
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 加载数据
        if (Constants.CUSTOM_ID != null) {
            // 从客户详情中点击
            mRecordManagerPresenter.requestCustomInvoiceRecords(Constants.CUSTOM_ID);
        } else {
            mRecordManagerPresenter.requestInvoiceRecords("");
        }
    }

    @Override
    protected void initEvent() {
        if (Constants.CUSTOM_ID != null) {
            int measuredHeight = applyOpen.getMinHeight();
            pageRefresh.setPadding(0, 0, 0, measuredHeight + 70);
        }

        departStaff.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("部门员工");
            }
        });
        typeView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("支付类型");
            }
        });
        payTeam.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("支付方");
            }
        });
        periodTime.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("时间段");
            }
        });

    }


    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu, R.id.btn_apply_open, R.id.tv_query_all, R.id.tv_allocated, R.id.tv_un_allocated})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                toast("menu");
                break;
            case R.id.btn_apply_open:
                //toast("申请开通");
                startActivity(getNewIntent(this, InvoiceApplyActivity.class, "开票记录", "开票记录"));
                break;
            case R.id.tv_query_all:
                //toast("全部");
                setStatusLine(true, false, false);
                if (Constants.CUSTOM_ID == null) {
                    mRecordManagerPresenter.requestInvoiceRecords("");
                }
                break;
            case R.id.tv_allocated:
                //toast("已拨付");
                setStatusLine(false, true, false);
                if (Constants.CUSTOM_ID == null) {
                    mRecordManagerPresenter.requestInvoiceRecords("是");
                }
                break;
            case R.id.tv_un_allocated:
                //toast("未拨付");
                setStatusLine(false, false, true);
                if (Constants.CUSTOM_ID == null) {
                    mRecordManagerPresenter.requestInvoiceRecords("否");
                }
                break;
        }
    }

    /**
     * 设置状态下划线
     *
     * @param selectedAll         全部
     * @param selectedAllocated   已拨付
     * @param selectedUnAllocated 未拨付
     */
    private void setStatusLine(boolean selectedAll, boolean selectedAllocated, boolean selectedUnAllocated) {
        lineAll.setVisibility(selectedAll ? View.VISIBLE : View.INVISIBLE);
        lineAllocated.setVisibility(selectedAllocated ? View.VISIBLE : View.INVISIBLE);
        lineUnAllocated.setVisibility(selectedUnAllocated ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * 获取客户开票记录
     *
     * @param invoiceRecordList
     */
    @Override
    public void getCustomInvoiceRecords(List<InvoiceRecord> invoiceRecordList) {
        mInvoiceRecordAdapter.setRecordList(invoiceRecordList);
    }

    /**
     * 财务管理下的开票记录
     *
     * @param financialInvoiceRecords
     */
    @Override
    public void getFinancialInvoiceRecords(List<FinancialInvoiceRecord> financialInvoiceRecords) {
        mFinancialInvoiceRecordAdapter.setRecordList(financialInvoiceRecords);
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
