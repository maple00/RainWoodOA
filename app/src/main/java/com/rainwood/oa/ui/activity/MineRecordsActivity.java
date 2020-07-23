package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineRecordTime;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.MineOverTimeAdapter;
import com.rainwood.oa.ui.adapter.MineRecordsAdapter;
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
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 15:27
 * @Desc: 我的记录Activity(请假记录 、 加班记录 、 外出记录)
 */
public final class MineRecordsActivity extends BaseActivity implements IMineCallbacks, StatusAction {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
    @ViewInject(R.id.gti_status)
    private GroupTextIcon stateView;
    @ViewInject(R.id.gti_leave_type)
    private GroupTextIcon leaveType;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_mine_records)
    private RecyclerView mineRecordsView;
    @ViewInject(R.id.divider)
    private View divider;

    private IMinePresenter mMinePresenter;
    private MineRecordsAdapter mRecordsAdapter;
    private MineOverTimeAdapter mOverTimeAdapter;
    private List<SelectedItem> mStateList;
    private List<SelectedItem> mLeaveTypeList;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;
    private boolean SELECTED_STATE_FLAG = false;
    private boolean SELECTED_TYPE_FLAG = false;
    private int pageCount = 1;
    private String mStartTime;
    private String mEndTime;
    private String mStateText = "";
    private String mLeaveType = "";
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_ask_leave;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setTextSize(14f);
        pageRightTitle.setTextColor(getColor(R.color.white));
        pageRightTitle.setBackgroundResource(R.drawable.selector_apply_button);
        // 设置布局管理器
        mineRecordsView.setLayoutManager(new GridLayoutManager(this, 1));
        mineRecordsView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mRecordsAdapter = new MineRecordsAdapter();
        mOverTimeAdapter = new MineOverTimeAdapter();
        switch (moduleMenu) {
            case "mywork":
                pageRightTitle.setText("请假申请");
                leaveType.setVisibility(View.VISIBLE);
                mineRecordsView.setAdapter(mRecordsAdapter);
                break;
            case "myworkAdd":
                pageRightTitle.setText("加班申请");
                leaveType.setVisibility(View.GONE);
                mineRecordsView.setAdapter(mOverTimeAdapter);
                break;
            case "myworkOut":
                pageRightTitle.setText("外出申请");
                leaveType.setVisibility(View.GONE);
                mineRecordsView.setAdapter(mOverTimeAdapter);
                break;
        }
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showDialog();
        // 查询条件
        mMinePresenter.requestMineLeaveCondition();
        switch (moduleMenu) {
            case "mywork":
                mMinePresenter.requestAskLeaveRecords("", "", "", "", pageCount);
                break;
            case "myworkAdd":
                mMinePresenter.requestMineOverTimeRecords();
                break;
            case "myworkOut":
                mMinePresenter.requestMineLeaveOutRecords();
                break;
        }
    }


    @Override
    protected void initEvent() {
        stateView.setOnItemClick(text -> {
            SELECTED_STATE_FLAG = !SELECTED_STATE_FLAG;
            stateView.setRightIcon(SELECTED_STATE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_STATE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_STATE_FLAG) {
                stateConditionPopDialog(mStateList, stateView);
            }
        });
        leaveType.setOnItemClick(text -> {
            SELECTED_TYPE_FLAG = !SELECTED_TYPE_FLAG;
            leaveType.setRightIcon(SELECTED_TYPE_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SELECTED_TYPE_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SELECTED_TYPE_FLAG) {
                stateConditionPopDialog(mLeaveTypeList, leaveType);
            }
        });
        // 请假记录
        mRecordsAdapter.setItemReissue(new MineRecordsAdapter.OnClickItemReissue() {
            @Override
            public void onClickItem(MineRecords reissue, int position) {
                PageJumpUtil.askLeaveList2Detail(MineRecordsActivity.this, RecordDetailActivity.class, "请假详情",
                        reissue.getId());
            }

            @Override
            public void onClickEdit(MineRecords reissue, int position) {
                toast("编辑");
            }

            @Override
            public void onClickDelete(MineRecords reissue, int position) {
                toast("删除");
            }
        });
        // 加班、外出记录
        mOverTimeAdapter.setOnClickOverTime(new MineOverTimeAdapter.OnClickOverTime() {
            @Override
            public void onClickItem(MineRecordTime record, int position) {
                switch (moduleMenu) {
                    case "myworkAdd":
                        PageJumpUtil.overTimeList2Detail(MineRecordsActivity.this,
                                RecordDetailActivity.class, "加班详情", record.getId(), "mineOvertime");
                        break;
                    case "myworkOut":
                        PageJumpUtil.askOutList2Detail(MineRecordsActivity.this, RecordDetailActivity.class, "外出详情",
                                record.getId(), "mineLeaveOut");
                        break;
                }

            }

            @Override
            public void onClickDelete(MineRecordTime record, int position) {
                toast("删除");
            }

            @Override
            public void onClickEdit(MineRecordTime record, int position, String clickContent) {
                switch (clickContent) {
                    case "编辑":
                        toast("编辑");
                        break;
                    case "提交成果":
                        startActivity(getNewIntent(MineRecordsActivity.this, CommitMineAchievementAty.class,
                                "提交加班成果", "审核记录"));
                        break;
                }

            }
        });
        // 选择时间段
        periodTime.setOnItemClick(text -> new StartEndDateDialog.Builder(MineRecordsActivity.this, false)
                .setTitle(null)
                .setConfirm(getString(R.string.common_text_confirm))
                .setCancel(getString(R.string.common_text_clear_screen))
                .setAutoDismiss(false)
                .setStartTime(mStartTime)
                .setEndTime(mEndTime)
                .setCanceledOnTouchOutside(false)
                .setListener(new StartEndDateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                        dialog.dismiss();
                        //toast("选中的时间段：" + startTime + "至" + endTime);
                        mStartTime = startTime;
                        mEndTime = endTime;
                        switch (moduleMenu) {
                            case "mywork":
                                mMinePresenter.requestAskLeaveRecords("", "", mStartTime, mEndTime, pageCount = 1);
                                mMinePresenter.requestMineLeaveCondition();
                                break;
                            case "myworkAdd":
                                mMinePresenter.requestMineOverTimeRecords();
                                break;
                            case "myworkOut":
                                mMinePresenter.requestMineLeaveOutRecords();
                                break;
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                        mStartTime = null;
                        mEndTime = null;
                        periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
                    }
                })
                .show());
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                switch (moduleMenu) {
                    case "mywork":
                        mMinePresenter.requestAskLeaveRecords(mStateText, mLeaveType, mStartTime, mEndTime, ++pageCount);
                        break;
                    case "myworkAdd":
                        mMinePresenter.requestMineOverTimeRecords();
                        break;
                    case "myworkOut":
                        mMinePresenter.requestMineLeaveOutRecords();
                        break;
                }
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_page_right_title:
                switch (moduleMenu) {
                    case "mywork":
                        //toast("请假申请");
                        startActivity(getNewIntent(this, AskLeaveActivity.class, "请假申请", "askLeave"));
                        break;
                    case "myworkAdd":
                        // toast("加班申请");
                        startActivity(getNewIntent(this, MineOverTimeApplyActivity.class, "加班申请", "askOvertime"));
                        break;
                    case "myworkOut":
                        // toast("外出申请");
                        startActivity(getNewIntent(this, MineOverTimeApplyActivity.class, "外出申请", "askLeaveOut"));
                        break;
                }
                break;
        }
    }

    /**
     * 请假记录
     *
     * @param askLeaveList
     */
    @Override
    public void getMineAskLeaveRecords(List<MineRecords> askLeaveList) {
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        pageRefresh.finishLoadmore();
        if (pageCount != 1) {
            if (ListUtils.getSize(askLeaveList) == 0) {
                pageCount--;
                return;
            }
            toast("加载了" + ListUtils.getSize(askLeaveList) + "条数据");
            mRecordsAdapter.addData(askLeaveList);
        } else {
            if (ListUtils.getSize(askLeaveList) == 0) {
                showEmpty();
                return;
            }
            mRecordsAdapter.setReissueList(askLeaveList);
        }
    }

    /**
     * 请假记录 -- condition
     *
     * @param stateList
     * @param leaveTypeList
     */
    @Override
    public void getMineLeaveRecords(List<SelectedItem> stateList, List<SelectedItem> leaveTypeList) {
        Collections.reverse(stateList);
        Collections.reverse(leaveTypeList);
        mStateList = stateList;
        mLeaveTypeList = leaveTypeList;
    }

    /**
     * 加班记录
     *
     * @param overTimeList
     */
    @Override
    public void getMineOverTimeRecords(List<MineRecordTime> overTimeList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mOverTimeAdapter.setList(overTimeList);
    }

    /**
     * 条件状态
     *
     * @param stateList
     * @param targetGTI
     */
    private void stateConditionPopDialog(List<SelectedItem> stateList, GroupTextIcon targetGTI) {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    contentList.setVisibility(View.GONE);
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
            SELECTED_STATE_FLAG = false;
            SELECTED_TYPE_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SELECTED_STATE_FLAG = false;
                SELECTED_TYPE_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mItemFlowLayout.setTextList(stateList);
        mItemFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : stateList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            if (SELECTED_STATE_FLAG) {
                mStateText = "全部".equals(selectedItem.getName()) ? "" : selectedItem.getName();
            }
            if (SELECTED_TYPE_FLAG) {
                mLeaveType = "全部".equals(selectedItem.getName()) ? "" : selectedItem.getName();
            }
            switch (moduleMenu) {
                case "mywork":
                    showDialog();
                    mMinePresenter.requestAskLeaveRecords(mStateText, mLeaveType, mStartTime, mEndTime, pageCount = 1);
                    break;
                case "myworkAdd":
                    mMinePresenter.requestMineOverTimeRecords();
                    break;
                case "myworkOut":
                    mMinePresenter.requestMineLeaveOutRecords();
                    break;
            }
            mStatusPopWindow.dismiss();
        });
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

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
