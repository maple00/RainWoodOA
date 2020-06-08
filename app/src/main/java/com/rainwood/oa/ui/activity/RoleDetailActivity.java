package com.rainwood.oa.ui.activity;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.ui.adapter.RoleDetailModuleAdapter;
import com.rainwood.oa.utils.AppBarStateChangeListener;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * @Author: a797s
 * @Date: 2020/5/21 14:36
 * @Desc: 角色详情
 */
public final class RoleDetailActivity extends BaseActivity {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageReturn;
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
                    LogUtils.d("sxs", "展开状态");
                    pageTitle.setText("");
                } else if (state == State.COLLAPSED) {
                    LogUtils.d("sxs", "折叠状态");
                    pageTitle.setText("超级管理员");
                } else {
                    LogUtils.d("sxs", "中间状态");
                    pageTitle.setText("超级管理员");
                }
            }
        });
    }

    @Override
    protected void loadData() {
        RolePermission rolePermission = (RolePermission) getIntent().getSerializableExtra("permissions");
        mDetailModuleAdapter.setPermissionList(rolePermission.getRoleXModules());
    }

    @Override
    protected void initPresenter() {

    }


}
