package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CustomFollowRecord;
import com.rainwood.oa.presenter.IRecordManagerPresenter;
import com.rainwood.oa.ui.adapter.CustomFollowRecordAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IRecordCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/29 12:56
 * @Desc: 跟进记录
 */
public final class CustomFollowRecordActivity extends BaseActivity implements IRecordCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView rightPageTitle;
    //content
    @ViewInject(R.id.fab_create_follow)
    private FloatingActionButton floatFollow;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_follow_records)
    private RecyclerView followRecordsView;

    @ViewInject(R.id.ll_follow_selected_all)
    private LinearLayout selectedAllLL;
    @ViewInject(R.id.tv_selected_all)
    private TextView selectedAllTV;
    @ViewInject(R.id.cb_checked)
    private CheckBox mCheckBox;


    private CustomFollowRecordAdapter mCustomFollowRecordAdapter;
    private IRecordManagerPresenter mRecordPresenter;
    // 全选flag
    private boolean managerControlFlag = false;
    private List<CustomFollowRecord> mRecordList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_follow_records;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        rightPageTitle.setText("管理");
        // 设置布局管理器
        followRecordsView.setLayoutManager(new GridLayoutManager(this, 1));
        followRecordsView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mCustomFollowRecordAdapter = new CustomFollowRecordAdapter();
        // 设置适配器
        followRecordsView.setAdapter(mCustomFollowRecordAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mRecordPresenter = PresenterManager.getOurInstance().getRecordManagerPresenter();
        mRecordPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 跟进记录有问题- 没有传客户id的时候查询出来的数据跟传了客户id的时候一致
        if (Constants.CUSTOM_ID != null) {
            mRecordPresenter.requestCustomFollowRecords(Constants.CUSTOM_ID);
        }
    }

    @Override
    protected void initEvent() {
        mCheckBox.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                LogUtils.d("sxs", "选择了");
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                LogUtils.d("sxs", "取消了");
            }
        });
        mCustomFollowRecordAdapter.setClickChecked((record, selected) -> {
            record.setSelected(selected);
            getSelectedValues(selected);
        });
    }

    @SuppressLint("SetTextI18n")
    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title, R.id.fab_create_follow, R.id.cb_checked, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.fab_create_follow:
                // LogUtils.d("sxs", "点击了--- 悬浮窗");
                toast("新增跟进记录");
                break;
            //管理
            case R.id.tv_page_right_title:
                managerControlFlag = !managerControlFlag;
                mCustomFollowRecordAdapter.setShowSelector(managerControlFlag);
                selectedAllLL.setVisibility(managerControlFlag ? View.VISIBLE : View.GONE);
                rightPageTitle.setText(managerControlFlag ? "完成" : "管理");
                if (!managerControlFlag) {
                    // 重置全选状态
                    mCheckBox.setChecked(false);
                    isSelectedAll(mCheckBox.isChecked());
                }
                break;
            // 全选
            case R.id.cb_checked:
                boolean checked = mCheckBox.isChecked();
                isSelectedAll(checked);
                break;
            case R.id.btn_delete:
                toast("删除");
                break;
        }
    }

    /**
     * 是否全选了
     */
    private void isSelectedAll(boolean checked) {
        if (checked) {
            for (CustomFollowRecord record : mRecordList) {
                record.setSelected(true);
            }
        } else {
            for (CustomFollowRecord record : mRecordList) {
                record.setSelected(false);
            }
        }
        mCustomFollowRecordAdapter.setRecordList(mRecordList);
        getSelectedValues(checked);
    }

    /**
     * 是否选中
     *
     * @param checked
     */
    private void getSelectedValues(boolean checked) {
        int selectedCount = 0;
        for (CustomFollowRecord record : mRecordList) {
            if (record.isSelected()) {
                selectedCount++;
            }
        }
        selectedAllTV.setText(Html.fromHtml("<font color=" + this.getColor(R.color.colorMiddle)
                + " size=" + FontSwitchUtil.dip2px(this, 15f) + ">全选</font>" +
                "<font color=" + this.getColor(R.color.labelColor) + " size= "
                + FontSwitchUtil.dip2px(this, 12f) + ">("
                + selectedCount + ")</font>"));
    }

    @Override
    public void getCustomFollowRecords(List<CustomFollowRecord> recordList) {
        mRecordList = recordList;
        mCustomFollowRecordAdapter.setRecordList(recordList);
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
