package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RoleCondition;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.model.domain.RoleSecondCondition;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.RoleManagerAdapter;
import com.rainwood.oa.ui.adapter.RoleScreenAdapter;
import com.rainwood.oa.ui.adapter.RoleSecondScreenAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 13:12
 * @Desc: 角色管理pager
 */
public final class RoleManagerActivity extends BaseActivity implements IAdministrativeCallbacks, RoleManagerAdapter.OnClickRoleItem {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.gti_screen)
    private GroupTextIcon screenTitle;
    // content
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.rv_role_list)
    private RecyclerView mRoleList;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private IAdministrativePresenter mRoleManagerPresenter;
    private RoleManagerAdapter mRoleManagerAdapter;
    // 筛选
    private boolean selectedStatusFlag = false;
    private View mMaskLayer;
    private List<RoleCondition> roleConditionList = new ArrayList<>();
    private RoleScreenAdapter mRoleScreenAdapter;
    private RoleSecondScreenAdapter mRoleSecondScreenAdapter;
    private RecyclerView mModule;
    private RecyclerView mSecondModule;
    private CommonPopupWindow mStatusPopWindow;
    private TextView mTextClearScreen;
    private TextView mTextConfirm;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_role_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // initView
        searchContent.setHint("输入角色名称");
        screenTitle.setText("筛选分类");
        // 设置局部管理器
        mRoleList.setLayoutManager(new GridLayoutManager(this, 1));
        mRoleList.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 0f)));
        // 创建适配器
        mRoleManagerAdapter = new RoleManagerAdapter();
        // 设置适配器
        mRoleList.setAdapter(mRoleManagerAdapter);
        // 设置刷新的属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
        // 初始化弹窗
        initPopDialog();
    }

    @Override
    protected void initEvent() {
        mRoleManagerAdapter.setClickRoleItem(this);
        // 筛选分类
        screenTitle.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            screenTitle.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (selectedStatusFlag) {
                showPopScreen();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mRoleManagerPresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mRoleManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据 -- 角色列表 -- 筛选条件
        mRoleManagerPresenter.requestAllRole();
        mRoleManagerPresenter.requestRoleScreenCondition();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void onClick(RolePermission role) {
        // 查看详情
        Intent intent = new Intent(this, RoleDetailActivity.class);
        intent.putExtra("roleId", role.getId());
        startActivity(intent);
    }

    /**
     * 筛选条件
     */
    private int tempPos = -1;

    private void showPopScreen() {
        // 设置数据 -- 默认选中第一项
        roleConditionList.get(tempPos == -1 ? 0 : tempPos).setSelected(true);
        mRoleScreenAdapter.setList(roleConditionList);
        mRoleSecondScreenAdapter.setList(roleConditionList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);

        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedStatusFlag = false;
            screenTitle.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedStatusFlag = false;
                screenTitle.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
        // 点击事件
        mRoleScreenAdapter.setRoleCondition((condition, position) -> {
            for (RoleCondition roleCondition : roleConditionList) {
                roleCondition.setSelected(false);
            }
            condition.setSelected(true);
            tempPos = position;
            mRoleSecondScreenAdapter.setList(roleConditionList.get(position).getArray());
        });
        mRoleSecondScreenAdapter.setSecondRoleCondition((secondCondition, position) -> {
            for (RoleSecondCondition roleSecondCondition : roleConditionList.get(tempPos == -1 ? 0 : tempPos).getArray()) {
                roleSecondCondition.setSelected(false);
            }
            secondCondition.setSelected(true);

        });
    }

    @Override
    public void getAllData2List(List<RolePermission> rolePermissions) {
        // 得到数据
        mRoleManagerAdapter.setPermissionList(rolePermissions);
    }

    @Override
    public void getRoleScreenData(List<RoleCondition> roleConditionList) {
        // 筛选分类条件
        this.roleConditionList = roleConditionList;
    }

    /**
     * 所属部门
     */
    private void initPopDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_role_screen)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mModule = view.findViewById(R.id.rv_module);
                    mSecondModule = view.findViewById(R.id.rv_second_module);
                    // 设置布局管理器
                    mModule.setLayoutManager(new GridLayoutManager(RoleManagerActivity.this, 1));
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    mSecondModule.setLayoutManager(new GridLayoutManager(RoleManagerActivity.this, 1));
                    mSecondModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    // 创建适配器
                    mRoleScreenAdapter = new RoleScreenAdapter();
                    mRoleSecondScreenAdapter = new RoleSecondScreenAdapter();
                    // 设置适配器
                    mModule.setAdapter(mRoleScreenAdapter);
                    mSecondModule.setAdapter(mRoleSecondScreenAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 确定、清空
                    mTextClearScreen = view.findViewById(R.id.tv_clear_screen);
                    mTextConfirm = view.findViewById(R.id.tv_confirm);
                })
                .create();
        mTextClearScreen.setOnClickListener(v -> {
            for (RoleCondition roleCondition : roleConditionList) {
                roleCondition.setSelected(false);
                for (RoleSecondCondition secondCondition : roleCondition.getArray()) {
                    secondCondition.setSelected(false);
                }
            }
            showPopScreen();
            tempPos = -1;
            //toast("清空筛选");
        });
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("确定");
            }
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
