package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.presenter.IDepartPresenter;
import com.rainwood.oa.ui.adapter.DepartManagerAdapter;
import com.rainwood.oa.ui.adapter.ProjectGroupsAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IDepartCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 16:59
 * @Desc: 部门管理
 */
public final class DepartManagerActivity extends BaseActivity implements IDepartCallbacks, ProjectGroupsAdapter.OnClickGroup {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.gti_screen)
    private GroupTextIcon screenTitle;
    // content
    @ViewInject(R.id.rv_depart_list)
    private RecyclerView departList;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private DepartManagerAdapter mDepartManagerAdapter;

    private IDepartPresenter mDepartPresenter;
    // 筛选
    private boolean selectedStatusFlag = false;
    private List<Depart> mDepartList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_depart_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // initView
        searchContent.setHint("输入部门名称");
        screenTitle.setText("部门分类");
        // 设置局部管理器
        departList.setLayoutManager(new GridLayoutManager(this, 1));
        departList.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 0f)));
        // 创建适配器
        mDepartManagerAdapter = new DepartManagerAdapter();

        // 设置适配器
        departList.setAdapter(mDepartManagerAdapter);

        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(false);
    }

    @Override
    protected void initEvent() {
        // 查看详情
        mDepartManagerAdapter.setOnClickGroup(this);
        // 筛选分类
        screenTitle.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            screenTitle.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));

        });
    }

    @Override
    protected void loadData() {
        // 请求数据
        mDepartPresenter.getAllDepartData();
    }

    @Override
    protected void initPresenter() {
        mDepartPresenter = PresenterManager.getOurInstance().getDepartPresenter();
        mDepartPresenter.registerViewCallback(this);
    }

    @Override
    public void getAllDepartData(List<Depart> departList) {
        mDepartList = departList;
        mDepartManagerAdapter.setDepartList(mDepartList);
    }

    @Override
    public void onClickItem(int parentPos, ProjectGroup group) {
        // 查看详情
        toast("部门：" + mDepartList.get(parentPos).getDepart() + " ---- 项目组：" + group.getGroup());
        startActivity(getNewIntent(this, DepartDetailActivity.class, "部门详情"));
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


    @SingleClick
    @OnClick(R.id.iv_page_back)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }
}