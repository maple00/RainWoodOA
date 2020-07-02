package com.rainwood.oa.ui.activity;

import android.content.Intent;
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
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.Logcat;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ILogcatPresenter;
import com.rainwood.oa.ui.adapter.LogcatAdapter;
import com.rainwood.oa.ui.adapter.ModuleFirstAdapter;
import com.rainwood.oa.ui.adapter.ModuleSecondAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.ILogcatCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/5/28 9:25
 * @Desc: 系统日志
 */
public final class LogcatActivity extends BaseActivity implements ILogcatCallbacks {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
    // content
    @ViewInject(R.id.gti_logcat_type)
    private GroupTextIcon logcatType;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_time)
    private GroupTextIcon periodTime;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_logcat_content)
    private RecyclerView logcatContent;
    @ViewInject(R.id.divider)
    private View divider;

    private ILogcatPresenter mLogcatPresenter;
    private LogcatAdapter mLogcatAdapter;
    private boolean selectedTimeFlag = false;
    private boolean selectedLogcatTypeFlag = false;
    private List<ManagerMain> mMenuList;

    private View mMaskLayer;
    private ModuleFirstAdapter mModuleFirstAdapter;
    private ModuleSecondAdapter mModuleSecondAdapter;
    private RecyclerView mModule;
    private RecyclerView mSecondModule;
    private CommonPopupWindow mStatusPopWindow;
    private TextView mTextClearScreen;
    private TextView mTextConfirm;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_logcat;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTips.setText("输入详细说明");
        // 设置布局管理器
        logcatContent.setLayoutManager(new GridLayoutManager(this, 1));
        logcatContent.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mLogcatAdapter = new LogcatAdapter();
        // 设置适配器
        logcatContent.setAdapter(mLogcatAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
        // initial pop
        initPopDepartDialog();
    }

    @Override
    protected void initPresenter() {
        mLogcatPresenter = PresenterManager.getOurInstance().getLogcatPresenter();
        mLogcatPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求日志列表
        mLogcatPresenter.requestLogcatData();
        // 请求日志类型
        mLogcatPresenter.requestLogcatType();
    }

    @Override
    protected void initEvent() {
        logcatType.setOnItemClick(text -> {
            selectedLogcatTypeFlag = !selectedLogcatTypeFlag;
            logcatType.setRightIcon(selectedLogcatTypeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedLogcatTypeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (selectedLogcatTypeFlag) {
                showData();
            }
        });
        departStaff.setOnItemClick(text -> startActivityForResult(getNewIntent(LogcatActivity.this,
                ContactsActivity.class, "通讯录", ""),
                CHOOSE_STAFF_REQUEST_SIZE));
        periodTime.setOnItemClick(text -> {
            new StartEndDateDialog.Builder(LogcatActivity.this, false)
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
                            toast("选中的时间段：" + startTime + "至" + endTime);
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                            selectedTimeFlag = false;
                            periodTime.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
                        }
                    })
                    .show();
            periodTime.setRightIcon(selectedTimeFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedTimeFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
        });
        // 查看详情
        mLogcatAdapter.setClickLogcat((logcat, position) ->
                PageJumpUtil.logcatList2Detail(LogcatActivity.this, LogcatDetailActivity.class, "日志详情", logcat));
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
    public void getSystemLogcat(List<Logcat> logcatList) {
        mLogcatAdapter.setLogcatList(logcatList);
    }

    @Override
    public void getMainManagerData(List<ManagerMain> menuList) {
        mMenuList = menuList;
        for (ManagerMain managerMain : mMenuList) {
            managerMain.setHasSelected(false);
        }
        mMenuList.get(0).setHasSelected(true);
    }

    private int tempPos = -1;

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
                    mModule.setLayoutManager(new GridLayoutManager(LogcatActivity.this, 1));
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    mSecondModule.setLayoutManager(new GridLayoutManager(LogcatActivity.this, 1));
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
        mTextClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("清空筛选");
            }
        });
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("确定");
            }
        });
    }

    /**
     * show popWindow
     */
    private void showData() {
        // 设置数据 -- 默认选中第一项
        mMenuList.get(tempPos == -1 ? 0 : tempPos).setHasSelected(true);
        mModuleFirstAdapter.setList(mMenuList);
        mModuleSecondAdapter.setList(mMenuList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);

        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            tempPos = -1;
            selectedLogcatTypeFlag = false;
            logcatType.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                tempPos = -1;
                selectedLogcatTypeFlag = false;
                logcatType.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 点击事件
        mModuleFirstAdapter.setRoleCondition(((condition, position) -> {
            for (ManagerMain manager : mMenuList) {
                manager.setHasSelected(false);
            }
            condition.setHasSelected(true);
            tempPos = position;
            mModuleSecondAdapter.setList(mMenuList.get(position).getArray());
        }));
        mModuleSecondAdapter.setSecondRoleCondition((secondCondition, position) -> {
            for (IconAndFont group : mMenuList.get(tempPos == -1 ? 0 : tempPos).getArray()) {
                group.setSelected(false);
            }
            secondCondition.setSelected(true);
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
}
