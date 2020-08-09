package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.FinancialInvoiceRecord;
import com.rainwood.oa.model.domain.InvoiceRecord;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.FinancialInvoiceRecordAdapter;
import com.rainwood.oa.ui.adapter.InvoiceRecordAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;
import static com.rainwood.oa.utils.Constants.PAGE_SEARCH_CODE;

/**
 * @Author: sxs
 * @Time: 2020/5/30 17:41
 * @Desc: 客户开票记录、财务管理开票记录
 */
public final class InvoiceRecordActivity extends BaseActivity implements IRecordCallbacks, StatusAction {

    @ViewInject(R.id.ll_parent_pager)
    private LinearLayout parentPager;
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTopSearch;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchTopView;
    @ViewInject(R.id.et_search_tips)
    private TextView searchTipsView;
    @ViewInject(R.id.tv_page_menu_title)
    private TextView pageMenuTitle;
    @ViewInject(R.id.tv_cancel)
    private TextView mTextCancel;
    @ViewInject(R.id.iv_search)
    private ImageView mImageSearch;
    // content
    @ViewInject(R.id.rv_invoice_records)
    private RecyclerView invoiceRecordView;
    @ViewInject(R.id.ll_status_invoice)
    private LinearLayout invoiceStatus;
    @ViewInject(R.id.ll_type_invoice)
    private LinearLayout invoiceType;
    @ViewInject(R.id.tv_query_all)
    private TextView mTextQueryAll;
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.tv_allocated)
    private TextView mTextAllocated;
    @ViewInject(R.id.line_allocated)
    private View lineAllocated;
    @ViewInject(R.id.tv_un_allocated)
    private TextView mTextUnAllocated;
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
    @ViewInject(R.id.divider)
    private View divider;

    @ViewInject(R.id.ll_apply_invoice)
    private LinearLayout applyInvoice;
    @ViewInject(R.id.btn_apply_open)
    private Button applyOpen;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IRecordManagerPresenter mRecordManagerPresenter;
    private InvoiceRecordAdapter mInvoiceRecordAdapter;
    private FinancialInvoiceRecordAdapter mFinancialInvoiceRecordAdapter;

    private boolean selectedTimeFlag = false;
    private boolean SELECTED_TYPE_FLAG = false;
    private boolean SELECTED_SALE_FLAG = false;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private List<SelectedItem> mFinancialSaleList;
    private List<SelectedItem> mFinancialTypeList;
    private int pageCount = 1;
    private String selectedAllocated = "";
    private String mStaffId;
    private String mSeller;
    private String mAllocated;
    private String mStartTime;
    private String mEndTime;
    private String mKeyWord;
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invoice_record;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        // 设置布局管理器
        invoiceRecordView.setLayoutManager(new GridLayoutManager(this, 1));
        invoiceRecordView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        if (Constants.CUSTOM_ID == null) {
            // 财务管理 --- 开票记录
            pageTitle.setText(title);
            pageTop.setVisibility(View.GONE);
            pageTopSearch.setVisibility(View.VISIBLE);
            applyInvoice.setVisibility(View.GONE);
            invoiceStatus.setVisibility(View.VISIBLE);
            invoiceType.setVisibility(View.VISIBLE);
            StatusBarUtils.setPaddingSmart(this, pageTopSearch);
            // 适配器
            mFinancialInvoiceRecordAdapter = new FinancialInvoiceRecordAdapter();
            invoiceRecordView.setAdapter(mFinancialInvoiceRecordAdapter);
        } else {
            // 客户详情 -- 开票记录
            pageMenuTitle.setText(title);
            pageTop.setVisibility(View.VISIBLE);
            pageTopSearch.setVisibility(View.GONE);
            invoiceStatus.setVisibility(View.GONE);
            invoiceType.setVisibility(View.GONE);
            applyInvoice.setVisibility(View.VISIBLE);
            StatusBarUtils.setPaddingSmart(this, pageTop);
            // 适配器
            mInvoiceRecordAdapter = new InvoiceRecordAdapter();
            invoiceRecordView.setAdapter(mInvoiceRecordAdapter);
        }
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
        // 设置TextView加粗
        mTextQueryAll.getPaint().setFakeBoldText(true);
        mTextQueryAll.invalidate();
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
            showLoading();
            mRecordManagerPresenter.requestCustomInvoiceRecords(Constants.CUSTOM_ID, pageCount = 1);
        } else {
            netRequestData("", "", "", "", "", "");
            // 请求condition
            mRecordManagerPresenter.requestInvoiceCondition();
        }
    }

    /**
     * 管理财务管理 ---  开票记录
     */
    private void netRequestData(String type, String staffId, String seller, String allocated, String startTime, String endTime) {
        showLoading();
        mRecordManagerPresenter.requestInvoiceRecords(type, staffId, seller, allocated, startTime, endTime, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        if (Constants.CUSTOM_ID != null) {
            // 客户详情开票记录
            int measuredHeight = applyOpen.getMinHeight();
            pageRefresh.setPadding(0, 0, 0, measuredHeight + 70);
        } else {
            // 管理 --- 开票记录
            mFinancialInvoiceRecordAdapter.setInvoiceListener((invoiceRecord, position) ->
                    PageJumpUtil.page2InvoiceDetail(InvoiceRecordActivity.this, InvoiceDetailActivity.class,
                            "开票详情", invoiceRecord.getId(), invoiceRecord.getOpen()));
        }
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(InvoiceRecordActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));

        typeView.setOnItemClick(text -> {
            SELECTED_TYPE_FLAG = !SELECTED_TYPE_FLAG;
            typeView.setRightIcon(SELECTED_TYPE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_TYPE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_TYPE_FLAG) {
                stateConditionPopDialog(mFinancialTypeList, typeView);
            }
        });
        payTeam.setOnItemClick(text -> {
            SELECTED_SALE_FLAG = !SELECTED_SALE_FLAG;
            payTeam.setRightIcon(SELECTED_SALE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_SALE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_SALE_FLAG) {
                stateConditionPopDialog(mFinancialSaleList, payTeam);
            }
        });
        periodTime.setOnItemClick(text -> {
            selectedTimeFlag = !selectedTimeFlag;
            new StartEndDateDialog.Builder(InvoiceRecordActivity.this, false)
                    .setTitle(null)
                    .setConfirm(getString(R.string.common_text_confirm))
                    .setCancel(getString(R.string.common_text_clear_screen))
                    .setAutoDismiss(false)
                    //.setIgnoreDay()
                    .setEndTime(mEndTime)
                    .setStartTime(mStartTime)
                    .setCanceledOnTouchOutside(false)
                    .setListener(new StartEndDateDialog.OnListener() {
                        @Override
                        public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                            dialog.dismiss();
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                            if (Constants.CUSTOM_ID == null) {
                                mStartTime = startTime;
                                mEndTime = endTime;
                                netRequestData("", "", "", "", mStartTime, mEndTime);
                            }
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                        }
                    })
                    .show();
            periodTime.setRightIcon(selectedTimeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedTimeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
        });
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (Constants.CUSTOM_ID == null) {
                    mRecordManagerPresenter.requestInvoiceRecords(selectedAllocated, mStaffId,
                            mSeller, mAllocated, mStartTime, mEndTime, (++pageCount));
                } else {
                    mRecordManagerPresenter.requestCustomInvoiceRecords(Constants.CUSTOM_ID, ++pageCount);
                }
            }
        });
        // 搜索条件监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {

                } else {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 选择员工
            if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
                String staff = data.getStringExtra("staff");
                mStaffId = data.getStringExtra("staffId");
                String position = data.getStringExtra("position");

                //toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                if (Constants.CUSTOM_ID == null) {
                    netRequestData("", mStaffId, "", "", "", "");
                }
            }
            // 搜索结果
            if (requestCode == PAGE_SEARCH_CODE && resultCode == PAGE_SEARCH_CODE) {
                mKeyWord = data.getStringExtra("keyWord");
                searchTipsView.setText(mKeyWord);

            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu, R.id.btn_apply_open, R.id.tv_query_all, R.id.tv_allocated,
            R.id.tv_un_allocated, R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                showQuickFunction(this, parentPager);
                break;
            case R.id.btn_apply_open:
                //toast("申请开通");
                startActivity(getNewIntent(this, InvoiceApplyActivity.class, "开票申请", "开票记录"));
                break;
            case R.id.tv_query_all:
                //toast("全部");
                selectedAllocated = "";
                setStatusLine(true, false, false);
                setTextViewBold(true, false, false);
                if (Constants.CUSTOM_ID == null) {
                    netRequestData("", "", "", "", "", "");
                }
                break;
            case R.id.tv_allocated:
                //toast("已拨付");
                selectedAllocated = "是";
                setStatusLine(false, true, false);
                setTextViewBold(false, true, false);
                if (Constants.CUSTOM_ID == null) {
                    netRequestData("是", "", "", "", "", "");
                }
                break;
            case R.id.tv_un_allocated:
                //toast("未拨付");
                selectedAllocated = "否";
                setStatusLine(false, false, true);
                setTextViewBold(false, false, true);
                if (Constants.CUSTOM_ID == null) {
                    netRequestData("否", "", "", "", "", "");
                }
                break;
            case R.id.iv_search:
                mTextCancel.setVisibility(View.VISIBLE);
                searchTopView.setVisibility(View.VISIBLE);
                pageTitle.setVisibility(View.GONE);
                mImageSearch.setVisibility(View.GONE);
                searchTipsView.setHint("请输入备注信息");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
            case R.id.tv_cancel:
                mTextCancel.setVisibility(View.GONE);
                searchTopView.setVisibility(View.GONE);
                pageTitle.setVisibility(View.VISIBLE);
                mImageSearch.setVisibility(View.VISIBLE);
                searchTipsView.setText("");
                // 向左边移出
                searchTopView.setAnimation(AnimationUtils.makeOutAnimation(this, false));
        }
    }

    /**
     * 设置字体加粗
     *
     * @param b
     * @param b2
     * @param b3
     */
    private void setTextViewBold(boolean b, boolean b2, boolean b3) {
        mTextQueryAll.getPaint().setFakeBoldText(b);
        mTextAllocated.getPaint().setFakeBoldText(b2);
        mTextUnAllocated.getPaint().setFakeBoldText(b3);
        mTextQueryAll.invalidate();
        mTextAllocated.invalidate();
        mTextUnAllocated.invalidate();
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
        showComplete();
        pageRefresh.finishLoadmore();
        if (pageCount != 1) {
            toast("为您加载了" + ListUtils.getSize(invoiceRecordList) + "条数据");
            mInvoiceRecordAdapter.addData(invoiceRecordList);
        } else {
            if (ListUtils.getSize(invoiceRecordList) == 0) {
                showEmpty();
            }
            mInvoiceRecordAdapter.setRecordList(invoiceRecordList);
        }
    }

    /**
     * 财务管理下的开票记录
     *
     * @param financialInvoiceRecords
     */
    @Override
    public void getFinancialInvoiceRecords(List<FinancialInvoiceRecord> financialInvoiceRecords) {
        pageRefresh.finishLoadmore();
        showComplete();
        if (pageCount != 1) {
            toast("为您加载了" + ListUtils.getSize(financialInvoiceRecords) + "条数据");
            mFinancialInvoiceRecordAdapter.addData(financialInvoiceRecords);
        } else {
            if (ListUtils.getSize(financialInvoiceRecords) == 0) {
                showEmpty();
            }
            mFinancialInvoiceRecordAdapter.setRecordList(financialInvoiceRecords);
        }
    }

    /**
     * 财务管理 --- 开票记录condition
     *
     * @param saleList
     * @param typeList
     */
    @Override
    public void getInvoiceCondition(List<SelectedItem> saleList, List<SelectedItem> typeList) {
        Collections.reverse(saleList);
        Collections.reverse(typeList);
        mFinancialSaleList = saleList;
        mFinancialTypeList = typeList;
    }

    /**
     * 类型选择
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
                    mItemFlowLayout = view.findViewById(R.id.tfl_text);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SELECTED_TYPE_FLAG = false;
            SELECTED_SALE_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SELECTED_TYPE_FLAG = false;
                SELECTED_SALE_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });

        mItemFlowLayout.setTextList(stateList);
        mItemFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : stateList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            mSeller = "";
            mAllocated = "";
            if (SELECTED_TYPE_FLAG) {
                mAllocated = selectedItem.getName();
            }
            if (SELECTED_SALE_FLAG) {
                mSeller = selectedItem.getName();
            }
            if (Constants.CUSTOM_ID == null) {
                netRequestData("", "", mSeller, mAllocated, "", "");
            }
            mStatusPopWindow.dismiss();
        });
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        showError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
