package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.RoleDetailModuleAdapter;
import com.rainwood.oa.utils.AppBarStateChangeListener;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:36
 * @Desc: 角色详情
 */
public final class RoleDetailActivity extends BaseActivity implements IAdministrativeCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_role_name)
    private TextView roleName;
    @ViewInject(R.id.tv_role_desc)
    private TextView roleDesc;
    @ViewInject(R.id.abl_role_permission)
    private AppBarLayout rolePermissionABL;
    @ViewInject(R.id.rv_permissions)
    private RecyclerView permissionsView;

    private RoleDetailModuleAdapter mDetailModuleAdapter;

    private IAdministrativePresenter mAdministrativePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_role_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.darkMode(this, false);
        StatusBarUtils.setMargin(this, pageTop);
        // 设置管理布局
        permissionsView.setLayoutManager(new GridLayoutManager(this, 1));
        permissionsView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mDetailModuleAdapter = new RoleDetailModuleAdapter();
        // 设置适配器
        permissionsView.setAdapter(mDetailModuleAdapter);
    }

    @Override
    protected void initEvent() {
        // 滑动监听
        rolePermissionABL.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    pageTitle.setText("");
                } else if (state == State.COLLAPSED) {
                    pageTitle.setText("角色权限详情");
                } else {
                    pageTitle.setText("角色权限详情");
                }
            }
        });
    }

    @Override
    protected void initPresenter() {
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String roleId = getIntent().getStringExtra("roleId");
        if (roleId != null) {
            mAdministrativePresenter.requestRoleDetailById(roleId);
        }
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
    public void getRoleXPermissionDetail(RolePermission rolePermission) {
        roleName.setText(rolePermission.getName());
        roleDesc.setText(rolePermission.getText());
        mDetailModuleAdapter.setPermissionList(rolePermission.getPower());
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
