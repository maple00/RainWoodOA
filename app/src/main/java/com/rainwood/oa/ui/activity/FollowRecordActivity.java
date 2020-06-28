package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.KnowledgeFollowRecord;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.FollowRecordsAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/6/5 11:53
 * @Desc: 跟进记录
 */
public final class FollowRecordActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
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

    private IRecordManagerPresenter mRecordManagerPresenter;
    private FollowRecordsAdapter mRecordsAdapter;
    private CommonGridAdapter mSelectedAdapter;
    private View mMaskLayer;

    private boolean CHECKED_SECRET_FLAG = false;
    private List<SelectedItem> mTypeList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_follow_records;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
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
        mRecordManagerPresenter.requestKnowledgeFollowRecords();
        // 请求跟进记录
        mRecordManagerPresenter.requestRecordType();
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
                    toast("跳转到事务详情");
                    break;
            }
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
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getKnowledgeFollowRecords(List<KnowledgeFollowRecord> recordList) {
        mRecordsAdapter.setRecordList(recordList);
    }

    @Override
    public void getRecordsTypes(List<SelectedItem> typeList) {
        mTypeList = typeList;
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
        mSelectedAdapter.setTextList(stateList);
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
