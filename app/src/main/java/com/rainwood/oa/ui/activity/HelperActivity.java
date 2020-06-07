package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.ui.adapter.HelperAdapter;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IArticleCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/2 9:55
 * @Desc: 帮助中心
 */
public final class HelperActivity extends BaseActivity implements IArticleCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTip;
    // content
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_helper_content)
    private RecyclerView helperView;

    //
    private HelperAdapter mHelperAdapter;
    private IArticlePresenter mArticlePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_helper;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTip.setHint("输入文章标题");
        // 设置布局管理器
        helperView.setLayoutManager(new GridLayoutManager(this, 1));
        helperView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mHelperAdapter = new HelperAdapter();
        // 设置适配器
        helperView.setAdapter(mHelperAdapter);
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mArticlePresenter = PresenterManager.getOurInstance().getArticlePresenter();
        mArticlePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mArticlePresenter.requestHelperData();
    }

    @Override
    protected void initEvent() {
        mHelperAdapter.setClickItemHelper((article, position) -> {
            // 查看详情
            PageJumpUtil.skillList2Detail(HelperActivity.this, ArticleDetailActivity.class, article.getId(), "帮助中心");
        });
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;

        }
    }

    @Override
    public void getHelperData(List<Article> helperList) {
        mHelperAdapter.setHelperList(helperList);
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
