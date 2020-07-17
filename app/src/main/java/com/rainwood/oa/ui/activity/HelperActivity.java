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
import com.rainwood.oa.ui.adapter.HelperAdapter;
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
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/2 9:55
 * @Desc: 帮助中心
 */
public final class HelperActivity extends BaseActivity implements IArticleCallbacks, StatusAction {

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

    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private HelperAdapter mHelperAdapter;
    private IArticlePresenter mArticlePresenter;

    private int pageCount = 1;
    private String mText;

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
        netRequestData("");
    }

    /**
     * 请求网络数
     */
    private void netRequestData(String SearchText) {
        showLoading();
        mArticlePresenter.requestHelperData(SearchText, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        mHelperAdapter.setClickItemHelper((article, position) -> {
            // 查看详情
            PageJumpUtil.skillList2Detail(HelperActivity.this, ArticleDetailActivity.class, article.getId(), "帮助中心");
        });
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mArticlePresenter.requestHelperData(mText, ++pageCount);
            }
        });
        searchTip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    netRequestData("");
                }
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
                if (TextUtils.isEmpty(searchTip.getText())) {
                    toast("请输入文章标题");
                    return;
                }
                mText = searchTip.getText().toString().trim();
                netRequestData(mText);
                break;
        }
    }

    @Override
    public void getHelperData(List<Article> helperList) {
        showComplete();
        if (pageCount != 1) {
            pageRefresh.finishLoadmore();
            toast("加载了" + ListUtils.getSize(helperList) + "条数据");
            mHelperAdapter.addData(helperList);
        } else {
            if (ListUtils.getSize(helperList) == 0) {
                showEmpty();
                return;
            }
            mHelperAdapter.setHelperList(helperList);
        }
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
