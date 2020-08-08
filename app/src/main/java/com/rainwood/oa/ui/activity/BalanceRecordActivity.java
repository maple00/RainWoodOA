package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.BalanceRecord;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.adapter.BalanceRecordAdapter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.ModuleFirstAdapter;
import com.rainwood.oa.ui.adapter.ModuleSecondAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/6/28 10:26
 * @Desc: 收支记录列表
 */
public final class BalanceRecordActivity extends BaseActivity implements IFinancialCallbacks, StatusAction {

    //ActionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchTopView;
    @ViewInject(R.id.et_search_tips)
    private EditText searchTipsView;
    @ViewInject(R.id.tv_cancel)
    private TextView mTextCancel;
    @ViewInject(R.id.iv_search)
    private ImageView mImageSearch;
    // content
    @ViewInject(R.id.gti_balance_origin)
    private GroupTextIcon mOriginView;
    @ViewInject(R.id.gti_balance_type)
    private GroupTextIcon mTypeView;
    @ViewInject(R.id.gti_balance_depart_staff)
    private GroupTextIcon mStaffView;
    @ViewInject(R.id.gti_balance_per_time)
    private GroupTextIcon mTimeView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_balance_list)
    private RecyclerView mBalanceListView;
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IFinancialPresenter mFinancialPresenter;
    private BalanceRecordAdapter mBalanceRecordAdapter;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private List<SelectedItem> mOriginList;
    private static boolean SELECTED_ORIGIN_FLAG = false;
    private boolean selectedTypeFlag = false;

    private ModuleFirstAdapter mModuleFirstAdapter;
    private ModuleSecondAdapter mModuleSecondAdapter;
    private RecyclerView mModule;
    private RecyclerView mSecondModule;
    private CommonPopupWindow mStatusPopWindow;
    private TextView mTextClearScreen;
    private TextView mTextConfirm;
    private int tempPos = -1;
    private List<ManagerMain> mBalanceTypeList;
    private int pageCount = 1;
    private String mSelectedType;
    private String mStaffId;
    private String mOrigin;
    private String mStartTime;
    private String mEndTime;

    private TextSelectedItemFlowLayout mTextFlowLayout;
    private String mSearchText;
    private String mDirection;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_payment;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        mBalanceListView.setLayoutManager(new GridLayoutManager(this, 1));
        // 创建适配器
        mBalanceRecordAdapter = new BalanceRecordAdapter();
        // 设置适配器
        mBalanceListView.setAdapter(mBalanceRecordAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
        // initial popWindow
        initPopDepartDialog();
    }

    /**
     * 请求数据
     */
    private void netRequestData() {
        showLoading();
        mFinancialPresenter.requestBalanceRecords(mStaffId, mOrigin, mDirection, mSelectedType, mStartTime,
                mEndTime, mSearchText, pageCount = 1);
    }


    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // list
        netRequestData();
        // condition
        mFinancialPresenter.requestBalanceCondition();
    }

    @Override
    protected void initEvent() {
        mOriginView.setOnItemClick(text -> {
            //toast("来源");
            SELECTED_ORIGIN_FLAG = !SELECTED_ORIGIN_FLAG;
            mOriginView.setRightIcon(SELECTED_ORIGIN_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_ORIGIN_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.fontColor));
            if (SELECTED_ORIGIN_FLAG) {
                stateConditionPopDialog(mOriginList, mOriginView);
            }
        });
        mTypeView.setOnItemClick(text -> {
            if (ListUtils.getSize(mBalanceTypeList) == 0) {
                toast("当前没有分类");
                return;
            }
            selectedTypeFlag = !selectedTypeFlag;
            mTypeView.setRightIcon(selectedTypeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedTypeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (selectedTypeFlag) {
                showData();
            }
        });
        mStaffView.setOnItemClick(text -> startActivityForResult(getNewIntent(BalanceRecordActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));
        mTimeView.setOnItemClick(text -> new StartEndDateDialog.Builder(BalanceRecordActivity.this, false)
                .setTitle(null)
                .setConfirm(getString(R.string.common_text_confirm))
                .setCancel(getString(R.string.common_text_clear_screen))
                .setStartTime(mStartTime)
                .setEndTime(mEndTime)
                .setAutoDismiss(false)
                //.setIgnoreDay()
                .setCanceledOnTouchOutside(false)
                .setListener(new StartEndDateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                            toast("请选择时间");
                            return;
                        }
                        if (DateTimeUtils.isDateOneBigger(startTime, endTime, DateTimeUtils.DatePattern.ONLY_DAY)) {
                            toast("开始时间不能大于结束时间");
                            return;
                        }
                        dialog.dismiss();
                        // TODO: 查询时间段
                        mStartTime = startTime;
                        mEndTime = endTime;
                        netRequestData();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show());

        mBalanceRecordAdapter.setOnClickItemListener((balanceRecord, position) ->
                PageJumpUtil.page2BalanceDetail(BalanceRecordActivity.this,
                        BalanceRecordDetailAty.class, "收支记录详情", balanceRecord.getId()));
        // 搜索内容监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchText = s.toString();
                //
                mFinancialPresenter.requestBalanceRecords(mStaffId, mOrigin, mDirection, mSelectedType, mStartTime,
                        mEndTime, mSearchText, pageCount = 1);
            }
        });
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mFinancialPresenter.requestBalanceRecords(mStaffId, mOrigin, mDirection, mSelectedType, mStartTime,
                        mEndTime, mSearchText, ++pageCount);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 部门员工
            if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
                String staff = data.getStringExtra("staff");
                mStaffId = data.getStringExtra("staffId");
                String position = data.getStringExtra("position");

                //toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                netRequestData();
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_cancel:
                mTextCancel.setVisibility(View.GONE);
                searchTopView.setVisibility(View.GONE);
                pageTitle.setVisibility(View.VISIBLE);
                mImageSearch.setVisibility(View.VISIBLE);
                searchTipsView.setText("");
                // 向左边移出
                searchTopView.setAnimation(AnimationUtils.makeOutAnimation(this, false));
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
        }
    }

    @Override
    public void getBalanceRecords(List<BalanceRecord> balanceRecordList) {
        showComplete();
        pageRefresh.finishLoadmore();
        if (pageCount != 1) {
            toast("为您加载了" + ListUtils.getSize(balanceRecordList) + "条数据");
            mBalanceRecordAdapter.addData(balanceRecordList);
        } else {
            if (ListUtils.getSize(balanceRecordList) == 0) {
                showEmpty();
            }
            mBalanceRecordAdapter.setRecordList(balanceRecordList);
        }
    }

    @Override
    public void getInOutComeData(List<SelectedItem> originList, List<ManagerMain> balanceTypeList, String showDepart) {
        mOriginList = originList;
        mBalanceTypeList = balanceTypeList;
        Collections.reverse(mOriginList);
        //
        mStaffView.setVisibility("是".equals(showDepart) ? View.VISIBLE : View.GONE);
    }

    /**
     * 来源
     */
    private void stateConditionPopDialog(List<SelectedItem> stateList, GroupTextIcon targetGTI) {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setVisibility(View.GONE);
                    contentList.setNumColumns(3);
                    mSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSelectedAdapter);
                    mTextFlowLayout = view.findViewById(R.id.tfl_text);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });

        //
        mTextFlowLayout.setTextList(stateList);
        mTextFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            mStatusPopWindow.dismiss();
            for (SelectedItem item : stateList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            // TODO: 查询来源
            mOrigin = selectedItem.getName();
            netRequestData();
        });

        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : stateList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
        });
    }

    /**
     * initial popWindow
     */
    private void initPopDepartDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_role_screen)
                .setOutsideTouchable(true)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mModule = view.findViewById(R.id.rv_module);
                    mSecondModule = view.findViewById(R.id.rv_second_module);
                    // 设置布局管理器
                    mModule.setLayoutManager(new GridLayoutManager(BalanceRecordActivity.this, 1));
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    mSecondModule.setLayoutManager(new GridLayoutManager(BalanceRecordActivity.this, 1));
                    mSecondModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    // 创建适配器
                    mModuleFirstAdapter = new ModuleFirstAdapter();
                    mModuleSecondAdapter = new ModuleSecondAdapter();
                    // 设置适配器
                    mModule.setAdapter(mModuleFirstAdapter);
                    mSecondModule.setAdapter(mModuleSecondAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 确定、清空
                    mTextClearScreen = view.findViewById(R.id.tv_clear_screen);
                    mTextConfirm = view.findViewById(R.id.tv_confirm);
                })
                .create();
        mMaskLayer.setOnClickListener(v -> mStatusPopWindow.dismiss());
        mTextClearScreen.setOnClickListener(v -> {
            for (ManagerMain main : mBalanceTypeList) {
                main.setHasSelected(false);
                for (IconAndFont andFont : main.getArray()) {
                    andFont.setSelected(false);
                }
            }
            tempPos = -1;
            mStatusPopWindow.dismiss();
            // TODO: 清空筛选
            netRequestData();
        });
        mTextConfirm.setOnClickListener(v -> {
            mSelectedType = "";
            for (ManagerMain main : mBalanceTypeList) {
                if (main.isHasSelected()) {
                    mDirection = main.getName();
                    if (ListUtils.getSize(main.getArray()) != 0)
                        for (IconAndFont iconAndFont : main.getArray()) {
                            if (iconAndFont.isSelected()) {
                                mSelectedType = iconAndFont.getName();
                            }
                        }
                }
            }
            if (TextUtils.isEmpty(mDirection)) {
                toast("请选择分类");
                return;
            }
            mStatusPopWindow.dismiss();
            // TODO: 查询分类列表
            netRequestData();
        });
    }

    /**
     * show popWindow
     */
    private void showData() {
        // 设置数据 -- 默认选中第一项
        mModuleFirstAdapter.setList(mBalanceTypeList);
        mModuleSecondAdapter.setList(mBalanceTypeList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            tempPos = -1;
            selectedTypeFlag = false;
            SELECTED_ORIGIN_FLAG = false;
            mTypeView.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedTypeFlag = false;
                SELECTED_ORIGIN_FLAG = false;
                mTypeView.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 点击事件
        mModuleFirstAdapter.setRoleCondition(((condition, position) -> {
            for (ManagerMain manager : mBalanceTypeList) {
                manager.setHasSelected(false);
            }
            condition.setHasSelected(true);
            mDirection = condition.getName();
            tempPos = position;
            mModuleSecondAdapter.setList(mBalanceTypeList.get(position).getArray());
        }));
        mModuleSecondAdapter.setSecondRoleCondition((secondCondition, position) -> {
            for (IconAndFont group : mBalanceTypeList.get(tempPos == -1 ? 0 : tempPos).getArray()) {
                group.setSelected(false);
            }
            secondCondition.setSelected(true);
        });
    }

    @Override
    public void onError() {

    }

    @Override
    public void onError(String tips) {

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
