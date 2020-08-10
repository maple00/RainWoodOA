package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
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
import com.rainwood.oa.model.domain.KnowledgeFollowRecord;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.FollowRecordsAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
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
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/6/5 11:53
 * @Desc: 跟进记录
 */
public final class FollowRecordActivity extends BaseActivity implements IRecordCallbacks, StatusAction {

    // actionBar
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
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_record_type)
    private GroupTextIcon recordType;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_follow_records)
    private RecyclerView followRecords;
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.ll_depart_staff)
    private LinearLayout mDepartStaff;

    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
    private IRecordManagerPresenter mRecordManagerPresenter;
    private FollowRecordsAdapter mRecordsAdapter;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;

    private boolean CHECKED_SECRET_FLAG = false;
    private List<SelectedItem> mTypeList;
    private int pageCount = 1;
    private String mStaffId;
    private String mTarget;
    private String mKeyWord;
    private TextSelectedItemFlowLayout mItemFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_follow_records;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        followRecords.setLayoutManager(new GridLayoutManager(this, 1));
        followRecords.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mRecordsAdapter = new FollowRecordsAdapter();
        // 设置适配器
        followRecords.setAdapter(mRecordsAdapter);
        mRecordsAdapter.setDefault_width(getWindowManager().getDefaultDisplay().getWidth()
                - FontSwitchUtil.dip2px(this, 20f));
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
        // 请求跟进记录数据
        netRequestData("", "");
        // 请求跟进记录
        mRecordManagerPresenter.requestRecordType();
    }

    /**
     * 请求网络数据
     */
    private void netRequestData(String staffId, String target) {
        showLoading();
        mRecordManagerPresenter.requestKnowledgeFollowRecords(staffId, target, mKeyWord, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(FollowRecordActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));
        recordType.setOnItemClick(text -> {
            CHECKED_SECRET_FLAG = !CHECKED_SECRET_FLAG;
            recordType.setRightIcon(CHECKED_SECRET_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    CHECKED_SECRET_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (CHECKED_SECRET_FLAG) {
                stateConditionPopDialog(mTypeList, recordType);
            }
        });
        //
        mRecordsAdapter.setTargetListener((record, position) -> {
            switch (record.getTarget()) {
                case "客户":
                    PageJumpUtil.listJump2CustomDetail(FollowRecordActivity.this,
                            CustomDetailActivity.class, record.getTargetId());
                    break;
                case "事务":
                    toast("事务类型");
                    break;
            }
        });
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mRecordManagerPresenter.requestKnowledgeFollowRecords(mStaffId, "", "", ++pageCount);
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
                mKeyWord = s.toString();
                mRecordManagerPresenter.requestKnowledgeFollowRecords(mStaffId, mTarget, mKeyWord, pageCount = 1);
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

                // toast("员工：" + staff + "\n员工编号：" + mStaffId + "\n 职位：" + position);
                netRequestData(mStaffId, mTarget);
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
                searchTipsView.setHint("请输入跟进内容");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
                break;
        }
    }

    @Override
    public void getKnowledgeFollowRecords(List<KnowledgeFollowRecord> recordList) {
        showComplete();
        if (pageCount != 1) {
            pageRefresh.finishLoadmore();
            toast("为您加载了" + ListUtils.getSize(recordList) + "条数据");
            mRecordsAdapter.addData(recordList);
        } else {
            if (ListUtils.getSize(recordList) == 0) {
                showEmpty();
                return;
            }
            mRecordsAdapter.setRecordList(recordList);
        }
    }

    @Override
    public void getRecordsTypes(boolean hasPermission, List<SelectedItem> typeList) {
        mDepartStaff.setVisibility(hasPermission ? View.VISIBLE : View.GONE);
        Collections.reverse(typeList);
        mTypeList = typeList;
    }

    /**
     * 记录类型
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
            CHECKED_SECRET_FLAG = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                CHECKED_SECRET_FLAG = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 流式布局
        mItemFlowLayout.setTextList(stateList);
        mItemFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : stateList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            mStatusPopWindow.dismiss();
            // TODO: 查询记录类型
            mTarget = selectedItem.getName();
            netRequestData("", "全部".equals(mTarget) ? "" : mTarget);
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
