package com.rainwood.oa.ui.activity;

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
import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.ui.adapter.CommunicationAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IArticleCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 11:21
 * @Desc: 沟通技巧（业务技能）
 */
public final class ExchangeSkillActivity extends BaseActivity implements IArticleCallbacks, StatusAction {

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
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private CommunicationAdapter mCommunicationAdapter;

    private IArticlePresenter mArticlePresenter;
    private int pageCount = 1;
    private String mSearchContent = "";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_exchange_skills;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTV.setText(getString(R.string.text_common_search));
        searchContent.setHint("请输入文章标题");
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
    protected void initPresenter() {
        mArticlePresenter = PresenterManager.getOurInstance().getArticlePresenter();
        mArticlePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        showLoading();
        mArticlePresenter.requestCommunicationData("", pageCount);
    }

    @Override
    protected void initEvent() {
        mCommunicationAdapter.setItemCommunication((skills, position) -> {
            // 跳转到详情页面
            PageJumpUtil.skillList2Detail(ExchangeSkillActivity.this, ArticleDetailActivity.class, skills.getId(), "沟通技巧");
        });

        // 文本域监听
        searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mArticlePresenter.requestCommunicationData("", pageCount = 1);
                }
            }
        });
        // 加载更多
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mArticlePresenter.requestCommunicationData(mSearchContent, ++pageCount);
            }
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
                if (TextUtils.isEmpty(searchContent.getText())) {
                    toast("请输入搜索内容");
                    return;
                }
                mSearchContent = this.searchContent.getText().toString().trim();
                mArticlePresenter.requestCommunicationData(mSearchContent, pageCount = 1);
                break;
        }
    }

    @Override
    public void getCommunicationData(List<Article> skillsList) {
        showComplete();
        pagerRefresh.finishLoadmore();
        mCommunicationAdapter.setLoaded(pageCount == 1);
        if (pageCount != 1) {
            toast("加载了" + ListUtils.getSize(skillsList) + "条数据");
        }
        mCommunicationAdapter.setList(skillsList);
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

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
