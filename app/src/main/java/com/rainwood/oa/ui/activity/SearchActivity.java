package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.model.domain.RolePermission;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.presenter.ISearchPresenter;
import com.rainwood.oa.ui.adapter.DepartManagerAdapter;
import com.rainwood.oa.ui.adapter.RoleManagerAdapter;
import com.rainwood.oa.ui.adapter.SearchPostAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.oa.view.ISearchCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.ArrayList;
import java.util.List;

import static com.rainwood.oa.utils.Constants.PAGE_SEARCH_CODE;

/**
 * @Author: a797s
 * @Date: 2020/6/30 15:01
 * @Desc: 搜索activity
 */
public final class SearchActivity extends BaseActivity implements
        ISearchCallback, IAdministrativeCallbacks, StatusAction {

    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.et_search_tips)
    private TextView searchText;
    @ViewInject(R.id.rv_search_list)
    private RecyclerView searchListView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private ISearchPresenter mSearchPresenter;
    private IAdministrativePresenter mAdministrativePresenter;
    private String mPageFlag;
    private String mTips;
    // 角色管理adapter
    private RoleManagerAdapter mRoleManagerAdapter;
    // 部门管理adapter
    private DepartManagerAdapter mDepartManagerAdapter;
    // 职位管理
    private SearchPostAdapter mSearchPostAdapter;
    private String tempPostKey;

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
        mDepartManagerAdapter = new DepartManagerAdapter();
        mSearchPostAdapter = new SearchPostAdapter();
        // 设置适配器
        mPageFlag = getIntent().getStringExtra("pageFlag");
        if (mPageFlag != null)
            switch (mPageFlag) {
                case "roleManager":
                    searchListView.setAdapter(mRoleManagerAdapter);
                    break;
                case "departManager":
                    searchListView.setAdapter(mDepartManagerAdapter);
                    break;
                case "postManager":
                    searchListView.setAdapter(mSearchPostAdapter);
                    break;
            }
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getOurInstance().getSearchPresenter();
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mSearchPresenter.registerViewCallback(this);
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        //
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = searchText.getText().toString().trim();
                switch (mPageFlag) {
                    // 角色管理
                    case "roleManager":
                        mSearchPresenter.requestSearchRoleList(searchContent, "", "");
                        break;
                    // 部门管理
                    case "departManager":
                        mAdministrativePresenter.requestAllDepartData(searchContent, "");
                        break;
                    // 职位管理
                    case "postManager":
                        // 员工管理、跟进记录
                    case "staffManager":
                        // 收支记录
                    case "balanceRecord":
                        // 费用报销
                    case "reimbursement":
                        // 办公文件、附件管理
                    case "officeFile":
                        Intent intent = new Intent();
                        intent.putExtra("keyWord", searchContent);
                        setResult(PAGE_SEARCH_CODE, intent);
                        finish();
                        break;
                }
            }
        });
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
        //查看部门详情
        mDepartManagerAdapter.setOnClickGroup((parentPos, group) -> PageJumpUtil.departList2Detail(SearchActivity.this, DepartDetailActivity.class, group.getId()));
        // 选中职位
        mSearchPostAdapter.setOnClickSearchPost((clickText, position) -> searchText.setText(clickText));
        // 职位搜索监听
        if ("postManager".equals(mPageFlag)) {
            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!TextUtils.isEmpty(s)) {
                        tempPostKey = s.toString();
                        mAdministrativePresenter.requestPostListData(s.toString(), "", "", 1);
                    } else {
                        mSearchPostAdapter.setList(null);
                    }
                }
            });
        }
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
            // ToastUtils.setGravity(Gravity.CENTER, 0, 0);
            // toast("无当前角色");
            showEmpty();
            return;
        }
        showComplete();
        mRoleManagerAdapter.setPermissionList(rolePermissionList);
    }

    /**
     * 部门列表
     *
     * @param departList
     */
    @Override
    public void getDepartListData(List<Depart> departList) {
        if (ListUtils.getSize(departList) == 0) {
            // toast("未查询到该部门");
            showEmpty();
            return;
        }
        showComplete();
        mDepartManagerAdapter.setDepartList(departList);
    }

    /**
     * 职位列表
     *
     * @param postList
     */
    @Override
    public void getPostListData(List<Post> postList) {
        if (ListUtils.getSize(postList) == 0) {
            showEmpty();
            return;
        }
        searchListView.setItemViewCacheSize(6);
        List<String> postSearchList = new ArrayList<>();
        for (Post post : postList) {
            postSearchList.add(post.getName());
        }
        mSearchPostAdapter.setList(postSearchList);
        mSearchPostAdapter.setSearchKey(tempPostKey);
    }

    @Override
    public void onError(String tips) {
        toast(tips);
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
//        if (mAdministrativePresenter != null) {
//            mAdministrativePresenter.unregisterViewCallback(this);
//        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
