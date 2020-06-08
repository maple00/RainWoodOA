package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.RoleManagerAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

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
    @ViewInject(R.id.rv_role_list)
    private RecyclerView mRoleList;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private RoleManagerAdapter mRoleManagerAdapter;
    private IAdministrativePresenter mRoleManagerPresenter;
    // 筛选
    private boolean selectedStatusFlag = false;

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
    }

    @Override
    protected void initEvent() {
        mRoleManagerAdapter.setClickRoleItem(this);
        // 筛选分类
        screenTitle.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            screenTitle.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
        });

    }

    @Override
    protected void initPresenter() {
        mRoleManagerPresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mRoleManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mRoleManagerPresenter.requestAllRole();
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
    public void onClick(RolePermission rolePermission) {
        // 查看详情
        Intent intent = new Intent(this, RoleDetailActivity.class);
        intent.putExtra("permissions", rolePermission);
        startActivity(intent);
    }

    @Override
    public void getAllData2List(List<RolePermission> rolePermissions) {
        // 得到数据
        mRoleManagerAdapter.setPermissionList(rolePermissions);
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
