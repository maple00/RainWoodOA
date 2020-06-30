package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ISearchPresenter;
import com.rainwood.oa.ui.adapter.RoleManagerAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ISearchCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/30 15:01
 * @Desc: 搜索activity
 */
public final class SearchActivity extends BaseActivity implements
        ISearchCallback {

    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchText;
    @ViewInject(R.id.rv_search_list)
    private RecyclerView searchListView;

    private ISearchPresenter mSearchPresenter;
    private String mPageFlag;
    private String mTips;
    // 角色管理adapter
    private RoleManagerAdapter mRoleManagerAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        mTips = getIntent().getStringExtra("tips");
        searchText.setHint(mTips);
        searchText.requestFocus();
        // 设置布局管理器
        searchListView.setLayoutManager(new GridLayoutManager(this, 1));
        searchListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));

        mRoleManagerAdapter = new RoleManagerAdapter();
        // 设置适配器
        mPageFlag = getIntent().getStringExtra("pageFlag");
        if (mPageFlag != null)
            switch (mPageFlag) {
                case "roleManager":
                    searchListView.setAdapter(mRoleManagerAdapter);
                    break;
                case "departManager":

                    break;
            }
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getOurInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initEvent() {
        // 角色列表
        mRoleManagerAdapter.setClickRoleItem(rolePermission -> {
            // 查看详情
            Intent intent = new Intent(SearchActivity.this, RoleDetailActivity.class);
            intent.putExtra("roleId", rolePermission.getId());
            startActivity(intent);
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search:
                if (TextUtils.isEmpty(searchText.getText())) {
                    toast(mTips);
                    return;
                }
                String searchContent = searchText.getText().toString().trim();

                switch (mPageFlag) {
                    case "roleManager":
                        mSearchPresenter.requestSearchRoleList(searchContent, "", "");
                        break;
                }
                break;
        }
    }

    /**
     * 角色列表
     *
     * @param rolePermissionList
     */
    @Override
    public void getRoleManagerList(List<RolePermission> rolePermissionList) {
        if (ListUtils.getSize(rolePermissionList) == 0) {
            toast("无当前角色");
            return;
        }
        mRoleManagerAdapter.setPermissionList(rolePermissionList);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }
}
