package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;
import static com.rainwood.oa.utils.Constants.PAGE_SEARCH_CODE;

/**
 * @Author: a797s
 * @Date: 2020/6/28 10:26
 * @Desc: 收支记录列表
 */
public final class BalanceRecordActivity extends BaseActivity implements IFinancialCallbacks {


    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

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

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // list
        mFinancialPresenter.requestBalanceRecords("", "", "", "",
                "", "", pageCount = 1);
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
                .setAutoDismiss(false)
                .setIgnoreDay()
                .setCanceledOnTouchOutside(false)
                .setListener(new StartEndDateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                        dialog.dismiss();
                        // TODO: 查询时间段
                        mStartTime = startTime;
                        mEndTime = endTime;
                        mFinancialPresenter.requestBalanceRecords("", "", "", mStartTime,
                                mEndTime, "", pageCount = 0);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show());

        mBalanceRecordAdapter.setOnClickItemListener(new BalanceRecordAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(BalanceRecord balanceRecord, int position) {
                toast("查看详情");
            }
        });

        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mFinancialPresenter.requestBalanceRecords(mStaffId, mOrigin, mSelectedType, mStartTime,
                        mEndTime, "", ++pageCount);
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

                toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                mFinancialPresenter.requestBalanceRecords(mStaffId, "", "", "",
                        "", "", pageCount = 1);
            }
            // 记录搜索
            if (requestCode == PAGE_SEARCH_CODE && resultCode == PAGE_SEARCH_CODE) {
                String keyWord = data.getStringExtra("keyWord");
                LogUtils.d("sxs", "---- keyWord -- " + keyWord);
                mFinancialPresenter.requestBalanceRecords("", "", "", "",
                        "", keyWord, pageCount = 1);
            }
        }

    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("pageFlag", "balanceRecord");
                intent.putExtra("title", "收支记录");
                intent.putExtra("tips", "请输入备注信息");
                startActivityForResult(intent, PAGE_SEARCH_CODE);
                break;
        }
    }

    @Override
    public void getBalanceRecords(List<BalanceRecord> balanceRecordList) {
        if (ListUtils.getSize(balanceRecordList) == 0) {
            toast("收支记录为空");
            return;
        }
        mBalanceRecordAdapter.setLoaded(pageCount == 1);
        if (pageCount != 1) {
            pageRefresh.finishLoadmore();
            toast("为您加载了" + ListUtils.getSize(balanceRecordList) + "条数据");
        }
        mBalanceRecordAdapter.setRecordList(balanceRecordList);
    }

    @Override
    public void getInOutComeData(List<SelectedItem> originList, List<ManagerMain> balanceTypeList) {
        mOriginList = originList;
        mBalanceTypeList = balanceTypeList;
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
                    contentList.setNumColumns(3);
                    mSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSelectedAdapter);
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
        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : stateList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            mStatusPopWindow.dismiss();
            // TODO: 查询来源
            mOrigin = item.getName();
            mFinancialPresenter.requestBalanceRecords("", mOrigin, "", "",
                    "", "", pageCount = 1);
        });
    }

    /**
     * initial popWindow
     */
    private void initPopDepartDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_role_screen)
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
            mFinancialPresenter.requestBalanceRecords("", "", "", "",
                    "", "", pageCount = 1);
        });
        mTextConfirm.setOnClickListener(v -> {
            mSelectedType = "";
            for (ManagerMain main : mBalanceTypeList) {
                if (main.isHasSelected()) {
                    for (IconAndFont iconAndFont : main.getArray()) {
                        if (iconAndFont.isSelected()) {
                            mSelectedType = iconAndFont.getName();
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(mSelectedType)) {
                toast("请选择分类");
                return;
            }
            mStatusPopWindow.dismiss();
            // TODO: 查询分类列表
            mFinancialPresenter.requestBalanceRecords("", "", mSelectedType, "",
                    "", "", pageCount = 1);
        });
    }

    /**
     * show popWindow
     */
    private void showData() {
        // 设置数据 -- 默认选中第一项
        mBalanceTypeList.get(tempPos == -1 ? 0 : tempPos).setHasSelected(true);
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
}
