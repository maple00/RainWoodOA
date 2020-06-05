package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IArticleCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/5/25 19:24
 * @Desc: 文章详情页面
 */
public final class ArticleDetailActivity extends BaseActivity implements IArticleCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_title)
    private TextView systemTitle;
    @ViewInject(R.id.tv_screen)
    private TextView screen;
    @ViewInject(R.id.tv_content)
    private TextView content;
    @ViewInject(R.id.tv_time)
    private TextView time;

    private IArticlePresenter mArticlePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_system_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mArticlePresenter = PresenterManager.getOurInstance().getArticlePresenter();
        mArticlePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String articleId = getIntent().getStringExtra("articleId");
        if (articleId != null) {
            mArticlePresenter.requestArticleDetailById(articleId);
        }
    }

    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getArticleDetail(Article article) {
        // LogUtils.d("sxs", article.getWord());
        systemTitle.setText(article.getTitle());
        screen.setText(article.getClickRate() + " 浏览");
        content.setText(Html.fromHtml(article.getArticle()));
        time.setText(article.getUpdateTime());
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
