package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.ManagerSystem;
import com.rainwood.oa.presenter.IManagerSystemPresenter;
import com.rainwood.oa.ui.adapter.ManagerSystemAdapter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IManagerSystemCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/25 18:51
 * @Desc: 管理制度activity
 */
public final class ManagerSystemActivity extends BaseActivity implements IManagerSystemCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    // content
    @ViewInject(R.id.rv_manager_content)
    private RecyclerView managerContent;

    private ManagerSystemAdapter mSystemAdapter;

    private IManagerSystemPresenter mSystemPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_manager_system;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // 设置布局管理
        managerContent.setLayoutManager(new GridLayoutManager(this, 1));
        managerContent.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mSystemAdapter = new ManagerSystemAdapter();
        // 设置适配器
        managerContent.setAdapter(mSystemAdapter);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mSystemPresenter.requestAllData();
    }

    @Override
    protected void initPresenter() {
        mSystemPresenter = PresenterManager.getOurInstance().getSystemPresenter();
        mSystemPresenter.registerViewCallback(this);
    }

    @Override
    protected void initEvent() {
        mSystemAdapter.setClickSystem(new ManagerSystemAdapter.OnClickSystem() {
            @Override
            public void onClickItem(ManagerSystem item) {
                // toast("查看详情");
                startActivity(getNewIntent(ManagerSystemActivity.this, SystemDetailActivity.class, "管理制度"));
            }
        });
    }

    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getAllData(List<ManagerSystem> systemList) {
        mSystemAdapter.setSystemList(systemList);
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