package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.AdminLeaveOut;
import com.rainwood.oa.model.domain.AdminOverTime;
import com.rainwood.oa.model.domain.CardRecord;
import com.rainwood.oa.model.domain.LeaveOutRecord;
import com.rainwood.oa.model.domain.LeaveRecord;
import com.rainwood.oa.model.domain.OvertimeRecord;
import com.rainwood.oa.model.domain.ReceivableRecord;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.AdminLeaveOutAdapter;
import com.rainwood.oa.ui.adapter.AdminOvertimeAdapter;
import com.rainwood.oa.ui.adapter.CardRecordAdapter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.LeaveAdapter;
import com.rainwood.oa.ui.adapter.LeaveOutAdapter;
import com.rainwood.oa.ui.adapter.OvertimeAdapter;
import com.rainwood.oa.ui.adapter.ReceivableRecordAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
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
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:39
 * @Desc: 记录管理----- 加班记录、请假记录、外出记录、补卡记录、回款记录
 */
public final class RecordManagerActivity extends BaseActivity implements IRecordCallbacks {

    @ViewInject(R.id.ll_parent_pager)
    private LinearLayout parentPager;
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.iv_menu)
    private ImageView topRightMenu;
    //content
    @ViewInject(R.id.ll_top_status)
    private LinearLayout topStatusLL;
    @ViewInject(R.id.gti_status)
    private GroupTextIcon topStatus;
    @ViewInject(R.id.gti_leave_type)
    private GroupTextIcon topLeaveType;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.ll_loading)
    private LinearLayout mLoading;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_record_list)
    private RecyclerView recordListView;

    private IRecordManagerPresenter mRecordManagerPresenter;
    /*
    record适配器
     */
    // 加班记录
    private OvertimeAdapter mOvertimeAdapter;
    private AdminOvertimeAdapter mAdminOvertimeAdapter;
    // 请假记录
    private LeaveAdapter mLeaveAdapter;
    // 外出记录
    private LeaveOutAdapter mOutAdapter;
    // 补卡记录
    private CardRecordAdapter mCardRecordAdapter;
    // 回款记录
    private ReceivableRecordAdapter mReceivableRecordAdapter;

    private int pageCount = 0;
    // 选择flag
    private boolean selectedTimeFlag = false;
    private boolean SELECTED_STATE_FLAG = false;
    private boolean SELECTED_LEAVE_TYPE_FLAG = false;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private AdminLeaveOutAdapter mAdminLeaveOutAdapter;
    private List<SelectedItem> mOverTimeStateList;
    private List<SelectedItem> mLeaveStateList;
    private List<SelectedItem> mLeaveTypeList;
    private List<SelectedItem> mLeaveOutList;
    private List<SelectedItem> mReissueStateList;
    private String mStartTime = "";
    private String mEndTime = "";
    private String mState;
    private String mStaffId;
    private String mLeaveType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_record_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        topLeaveType.setVisibility(title.contains("请假记录") ? View.VISIBLE : View.GONE);
        topStatusLL.setVisibility(Constants.CUSTOM_ID == null ? View.VISIBLE : View.GONE);
        topRightMenu.setVisibility(Constants.CUSTOM_ID == null ? View.GONE : View.VISIBLE);
        // 设置布局管理器
        recordListView.setLayoutManager(new GridLayoutManager(this, 1));
        recordListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 设置适配器属性
        if (title.equals("加班记录")) {
            mOvertimeAdapter = new OvertimeAdapter();
            mAdminOvertimeAdapter = new AdminOvertimeAdapter();
            recordListView.setAdapter(mOvertimeAdapter);
            recordListView.setAdapter(mAdminOvertimeAdapter);
        } else if ("请假记录".equals(title)) {
            mLeaveAdapter = new LeaveAdapter();
            recordListView.setAdapter(mLeaveAdapter);
        } else if (title.contains("外出")) {
            mOutAdapter = new LeaveOutAdapter();
            mAdminLeaveOutAdapter = new AdminLeaveOutAdapter();
            recordListView.setAdapter(mOutAdapter);
            recordListView.setAdapter(mAdminLeaveOutAdapter);
        } else if (title.contains("补卡")) {
            mCardRecordAdapter = new CardRecordAdapter();
            recordListView.setAdapter(mCardRecordAdapter);
        } else if (title.contains("回款")) {
            mReceivableRecordAdapter = new ReceivableRecordAdapter();
            recordListView.setAdapter(mReceivableRecordAdapter);
        }
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
        pageCount = 0;
    }

    @Override
    protected void loadData() {
        // 从这里request data
        if (title.contains("加班")) {
            if (Constants.CUSTOM_ID != null) {
                mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
            } else {
                mRecordManagerPresenter.requestOvertimeRecord("", "", "", "", pageCount);
                mRecordManagerPresenter.requestOverTimeStateData();
            }
        } else if (title.contains("请假")) {
            mRecordManagerPresenter.requestLeaveRecord("", "", "", "", "", pageCount);
            mRecordManagerPresenter.requestLeaveCondition();
        } else if (title.contains("外出")) {
            if (Constants.CUSTOM_ID != null) {
                mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
            } else {
                mRecordManagerPresenter.requestGoOutRecord("", "", "", "", pageCount);
                mRecordManagerPresenter.requestGoOutCondition();
            }
        } else if (title.contains("补卡")) {
            mRecordManagerPresenter.requestReissueRecord("", "", "", "", pageCount);
            mRecordManagerPresenter.requestReissueCondition();
        } else if (title.contains("回款")) {
            mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
        }
    }

    @Override
    protected void initPresenter() {
        mRecordManagerPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initEvent() {
        if (title.contains("加班")) {
            // 客户管理--加班详情
            mOvertimeAdapter.setItemOvertime(overtimeRecord -> {
                startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "加班详情", "加班详情"));
            });
            // 行政人事 -- 加班详情
            mAdminOvertimeAdapter.setItemOvertime(overtimeRecord -> {
                //startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "加班详情", "加班详情"));
                PageJumpUtil.overTimeList2Detail(RecordManagerActivity.this,
                        RecordDetailActivity.class, "加班详情", overtimeRecord.getId(), "overtime");
            });
            topStatus.setOnItemClick(text -> {
                if (ListUtils.getSize(mOverTimeStateList) == 0) {
                    toast("无状态可选择");
                    return;
                }
                SELECTED_STATE_FLAG = !SELECTED_STATE_FLAG;
                topStatus.setRightIcon(SELECTED_STATE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        SELECTED_STATE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                if (SELECTED_STATE_FLAG) {
                    stateConditionPopDialog(mOverTimeStateList, topStatus);
                }
            });
        } else if (title.contains("请假")) {
            mLeaveAdapter.setClickItemLeave(leaveRecord -> {
                // 请假详情
                PageJumpUtil.askLeaveList2Detail(RecordManagerActivity.this, RecordDetailActivity.class, "请假详情",
                        leaveRecord.getId());
            });
            topStatus.setOnItemClick(text -> {
                if (ListUtils.getSize(mLeaveStateList) == 0) {
                    toast("无状态可选择");
                    return;
                }
                SELECTED_STATE_FLAG = !SELECTED_STATE_FLAG;
                topStatus.setRightIcon(SELECTED_STATE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        SELECTED_STATE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                if (SELECTED_STATE_FLAG) {
                    stateConditionPopDialog(mLeaveStateList, topStatus);
                }
            });
            topLeaveType.setOnItemClick(text -> {
                if (ListUtils.getSize(mLeaveTypeList) == 0) {
                    toast("无状态可选择");
                    return;
                }
                SELECTED_LEAVE_TYPE_FLAG = !SELECTED_LEAVE_TYPE_FLAG;
                topLeaveType.setRightIcon(SELECTED_LEAVE_TYPE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        SELECTED_LEAVE_TYPE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                if (SELECTED_LEAVE_TYPE_FLAG) {
                    stateConditionPopDialog(mLeaveTypeList, topLeaveType);
                }
            });
        } else if (title.contains("外出")) {
            // 客户详情外出记录
            mOutAdapter.setItemGoOut(leaveOutRecord -> {
                // 外出详情
                // startActivity(getNewIntent(RecordManagerActivity.this, RecordDetailActivity.class, "外出详情", "外出详情"));
                PageJumpUtil.askLeaveList2Detail(RecordManagerActivity.this, RecordDetailActivity.class, "外出详情",
                        leaveOutRecord.getStid());
            });
            // 行政人事外出记录
            mAdminLeaveOutAdapter.setItemGoOut(leaveOutRecord ->
                    PageJumpUtil.askOutList2Detail(RecordManagerActivity.this,
                            RecordDetailActivity.class, "外出详情", leaveOutRecord.getId(), "leaveOut"));
            topStatus.setOnItemClick(text -> {
                if (ListUtils.getSize(mLeaveOutList) == 0) {
                    toast("无状态可选择");
                    return;
                }
                SELECTED_STATE_FLAG = !SELECTED_STATE_FLAG;
                topStatus.setRightIcon(SELECTED_STATE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        SELECTED_STATE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                if (SELECTED_STATE_FLAG) {
                    stateConditionPopDialog(mLeaveOutList, topStatus);
                }
            });
        } else if (title.contains("补卡")) {
            mCardRecordAdapter.setItemGoOut(cardRecord -> {
                // 补卡详情
                PageJumpUtil.ReissueCardList2Detail(RecordManagerActivity.this, RecordDetailActivity.class,
                        "补卡详情", cardRecord.getId());
            });
            topStatus.setOnItemClick(text -> {
                if (ListUtils.getSize(mReissueStateList) == 0) {
                    toast("无状态可选择");
                    return;
                }
                SELECTED_STATE_FLAG = !SELECTED_STATE_FLAG;
                topStatus.setRightIcon(SELECTED_STATE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        SELECTED_STATE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                if (SELECTED_STATE_FLAG) {
                    stateConditionPopDialog(mReissueStateList, topStatus);
                }
            });
        } else if (title.contains("回款")) {
            mReceivableRecordAdapter.setClickReceivable((record, position) ->
                    PageJumpUtil.receivable2Detail(RecordManagerActivity.this, ReceivableDetailActivity.class, record.getId()));
        }

        //选择时间段
        periodTime.setOnItemClick(text -> {
            selectedTimeFlag = !selectedTimeFlag;
            new StartEndDateDialog.Builder(RecordManagerActivity.this, false)
                    .setTitle(null)
                    .setConfirm(getString(R.string.common_text_confirm))
                    .setCancel(getString(R.string.common_text_clear_screen))
                    .setAutoDismiss(false)
                    //.setIgnoreDay()
                    .setCanceledOnTouchOutside(false)
                    .setListener(new StartEndDateDialog.OnListener() {
                        @Override
                        public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                            dialog.dismiss();
                            //toast("选中的时间段：" + startTime + "至" + endTime);
                            selectedTimeFlag = false;
                            mStartTime = startTime;
                            mEndTime = endTime;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                            if (title.contains("加班")) {
                                if (Constants.CUSTOM_ID != null) {
                                    mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
                                } else {
                                    mRecordManagerPresenter.requestOvertimeRecord("", "", startTime, endTime, pageCount = 0);
                                }
                            } else if (title.contains("请假")) {
                                mRecordManagerPresenter.requestLeaveRecord("", "", "", mStartTime, mEndTime, pageCount = 0);
                            } else if (title.contains("外出")) {
                                if (Constants.CUSTOM_ID != null) {
                                    mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
                                } else {
                                    mRecordManagerPresenter.requestGoOutRecord("", "", startTime, endTime, pageCount = 0);
                                }
                            } else if (title.contains("补卡")) {
                                mRecordManagerPresenter.requestReissueRecord("", "", startTime, endTime, pageCount = 0);
                            } else if (title.contains("回款")) {
                                mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
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

        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(RecordManagerActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));

        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (title.contains("加班")) {
                    if (Constants.CUSTOM_ID != null) {
                        mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
                    } else {
                        mRecordManagerPresenter.requestOvertimeRecord(mStaffId, mState, mStartTime, mEndTime, (++pageCount));
                    }
                } else if (title.contains("请假")) {
                    mRecordManagerPresenter.requestLeaveRecord(mStaffId, mLeaveType, mState, mStartTime, mEndTime, (++pageCount));
                } else if (title.contains("外出")) {
                    if (Constants.CUSTOM_ID != null) {
                        mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
                    } else {
                        mRecordManagerPresenter.requestGoOutRecord(mStaffId, mState, mStartTime, mEndTime, (++pageCount));
                    }
                } else if (title.contains("补卡")) {
                    mRecordManagerPresenter.requestReissueRecord(mStaffId, mState, mStartTime, mEndTime, (++pageCount));
                } else if (title.contains("回款")) {
                    mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
                }
            }
        });
    }

    @SuppressWarnings("all")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            String staff = data.getStringExtra("staff");
            mStaffId = data.getStringExtra("staffId");
            String position = data.getStringExtra("position");

            toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
            // TODO: 通过员工查询
            if (title.contains("加班")) {
                if (Constants.CUSTOM_ID != null) {
                    mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
                } else {
                    mRecordManagerPresenter.requestOvertimeRecord(mStaffId, "", "", "", pageCount = 0);
                }
            } else if (title.contains("请假")) {
                mRecordManagerPresenter.requestLeaveRecord(mStaffId, "", "", "", "", pageCount = 0);
            } else if (title.contains("外出")) {
                if (Constants.CUSTOM_ID != null) {
                    mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
                } else {
                    mRecordManagerPresenter.requestGoOutRecord(mStaffId, "", "", "", pageCount = 0);
                }
            } else if (title.contains("补卡")) {
                mRecordManagerPresenter.requestReissueRecord(mStaffId, "", "", "", pageCount = 0);
            } else if (title.contains("回款")) {
                mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                showQuickFunction(this, parentPager);
                break;
        }
    }

    @Override
    public void getOvertimeRecords(List<OvertimeRecord> overtimeRecords) {
        mLoading.setVisibility(View.GONE);
        // 客户管理---加班记录
        mOvertimeAdapter.setOvertimeRecordList(overtimeRecords);
    }

    @Override
    public void getGoOutRecords(List<LeaveOutRecord> leaveOutList) {
        mLoading.setVisibility(View.GONE);
        // 客户管理--- 外出记录
        mOutAdapter.setLeaveOutRecordList(leaveOutList);
    }

    @Override
    public void getCustomReceivableRecords(List<ReceivableRecord> receivableRecordList) {
        mLoading.setVisibility(View.GONE);
        //  客户管理---回款记录
        mReceivableRecordAdapter.setRecordList(receivableRecordList);
    }

    @Override
    public void getAdminOverTimeRecords(List<AdminOverTime> adminOverTimeList) {
        mLoading.setVisibility(View.GONE);
        // 行政人事-- 加班记录
        pagerRefresh.finishLoadmore();
        mAdminOvertimeAdapter.setLoaded(pageCount == 0);
        if (pageCount != 0) {
            toast("为您加载了" + ListUtils.getSize(adminOverTimeList) + "条数据");
        }
        mAdminOvertimeAdapter.setOvertimeRecordList(adminOverTimeList);
    }

    @Override
    public void getAdminOverTimeState(List<SelectedItem> overTimeStateList) {
        mLoading.setVisibility(View.GONE);
        // 行政人事 -- 加班记录condition
        mOverTimeStateList = overTimeStateList;
    }

    @Override
    public void getLeaveRecords(List<LeaveRecord> leaveRecordList) {
        mLoading.setVisibility(View.GONE);
        //  行政人事---请假记录
        pagerRefresh.finishLoadmore();
        // 行政人事-- 加班记录
        mLeaveAdapter.setLoaded(pageCount == 0);
        if (pageCount != 0) {
            toast("为您加载了" + ListUtils.getSize(leaveRecordList) + "条数据");
        }
        mLeaveAdapter.setLeaveList(leaveRecordList);
    }

    @Override
    public void getLeaveConditionData(List<SelectedItem> stateList, List<SelectedItem> leaveTypeList) {
        mLoading.setVisibility(View.GONE);
        // 行政人事 -- 请假记录condition
        mLeaveStateList = stateList;
        mLeaveTypeList = leaveTypeList;
    }

    @Override
    public void getAdminLeaveOutRecords(List<AdminLeaveOut> adminLeaveOutList) {
        mLoading.setVisibility(View.GONE);
        // 行政人事---外出记录
        pagerRefresh.finishLoadmore();
        mAdminLeaveOutAdapter.setLoaded(pageCount == 0);
        if (pageCount != 0) {
            toast("为您加载了" + ListUtils.getSize(adminLeaveOutList) + "条数据");
        }
        mAdminLeaveOutAdapter.setLeaveOutRecordList(adminLeaveOutList);
    }

    @Override
    public void getLeaveOutCondition(List<SelectedItem> leaveOutList) {
        mLoading.setVisibility(View.GONE);
        mLeaveOutList = leaveOutList;
    }

    @Override
    public void getReissueRecords(List<CardRecord> cardRecordList) {
        mLoading.setVisibility(View.GONE);
        //  行政人事---补卡记录
        pagerRefresh.finishLoadmore();
        mCardRecordAdapter.setLoaded(pageCount == 0);
        if (pageCount != 0) {
            toast("为您加载了" + ListUtils.getSize(cardRecordList) + "条数据");
        }
        mCardRecordAdapter.setLeaveOutRecordList(cardRecordList);
    }

    @Override
    public void getReissueCondition(List<SelectedItem> reissueStateList) {
        mLoading.setVisibility(View.GONE);
        //  行政人事---补卡记录condition
        mReissueStateList = reissueStateList;
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
            SELECTED_STATE_FLAG = false;
            selectedTimeFlag = false;
            SELECTED_LEAVE_TYPE_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SELECTED_STATE_FLAG = false;
                selectedTimeFlag = false;
                SELECTED_LEAVE_TYPE_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : stateList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            // 请假类型
            if (SELECTED_LEAVE_TYPE_FLAG) {
                mLeaveType = item.getName();
            } else {
                mLeaveType = "";
                mState = item.getName();
            }
            // TODO : 请求记录状态列表
            if (title.contains("加班")) {
                if (Constants.CUSTOM_ID != null) {
                    mRecordManagerPresenter.requestOvertimeRecord(Constants.CUSTOM_ID);
                } else {
                    mRecordManagerPresenter.requestOvertimeRecord("", item.getName(), "", "", pageCount = 0);
                }
            } else if (title.contains("请假")) {
                mRecordManagerPresenter.requestLeaveRecord("", mLeaveType, mState, "", "", pageCount = 0);
            } else if (title.contains("外出")) {
                if (Constants.CUSTOM_ID != null) {
                    mRecordManagerPresenter.requestGoOutRecord(Constants.CUSTOM_ID);
                } else {
                    mRecordManagerPresenter.requestGoOutRecord("", mState, "", "", pageCount = 0);
                }
            } else if (title.contains("补卡")) {
                mRecordManagerPresenter.requestReissueRecord("", mState, "", "", pageCount = 0);
            } else if (title.contains("回款")) {
                mRecordManagerPresenter.requestCustomReceivableRecords(Constants.CUSTOM_ID);
            }

            mStatusPopWindow.dismiss();
        });
    }

    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
