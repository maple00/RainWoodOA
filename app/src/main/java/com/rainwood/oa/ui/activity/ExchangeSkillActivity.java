package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CommunicationSkills;
import com.rainwood.oa.presenter.ICommunicationPresenter;
import com.rainwood.oa.ui.adapter.CommunicationAdapter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ICommunicationCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:21
 * @Desc: 沟通技巧
 */
public final class ExchangeSkillActivity extends BaseActivity implements ICommunicationCallbacks {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_communication_list)
    private RecyclerView communicationList;

    private CommunicationAdapter mCommunicationAdapter;

    private ICommunicationPresenter mCommunicationPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_exchange_skills;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTV.setText(getString(R.string.text_common_search));
        //设置布局管理器
        communicationList.setLayoutManager(new GridLayoutManager(this, 1));
        communicationList.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 20f)));
        // 创建适配器
        mCommunicationAdapter = new CommunicationAdapter();
        // 设置适配器
        communicationList.setAdapter(mCommunicationAdapter);
        // 设置刷新相关属性
        pagerRefresh.setEnableLoadmore(true);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mCommunicationPresenter.getAllData();
    }

    @Override
    protected void initPresenter() {
        mCommunicationPresenter = PresenterManager.getOurInstance().getSkillPresenter();
        mCommunicationPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search:
                toast("搜索");
                break;
        }
    }

    @Override
    public void getAllData(List<CommunicationSkills> dataList) {
        // 得到返回数据
        mCommunicationAdapter.setList(dataList);
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