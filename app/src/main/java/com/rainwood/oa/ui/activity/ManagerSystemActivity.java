package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Article;
import com.rainwood.oa.presenter.IArticlePresenter;
import com.rainwood.oa.ui.adapter.ManagerSystemAdapter;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IArticleCallbacks;
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
public final class ManagerSystemActivity extends BaseActivity implements IArticleCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.et_search_tips)
    private TextView mSearchContent;
    // content
    @ViewInject(R.id.rv_manager_content)
    private RecyclerView managerContent;

    private ManagerSystemAdapter mSystemAdapter;

    private IArticlePresenter mArticlePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_manager_system;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        mSearchContent.setHint("请输入标题");
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
    protected void initPresenter() {
        mArticlePresenter = PresenterManager.getOurInstance().getArticlePresenter();
        mArticlePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showDialog();
        mArticlePresenter.requestManagerSystemData("");
    }

    @Override
    protected void initEvent() {
        mSystemAdapter.setClickSystem(article -> {
            // toast("查看详情");
            // startActivity(getNewIntent(ManagerSystemActivity.this, ArticleDetailActivity.class, "管理制度"));
            PageJumpUtil.skillList2Detail(ManagerSystemActivity.this, ArticleDetailActivity.class, article.getId(), "管理制度");
        });
        // 监听
        mSearchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 请求数据
                String title = s.toString();
                mArticlePresenter.requestManagerSystemData(title);
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
    public void getManagerSystemData(List<Article> managerSysList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mSystemAdapter.setSystemList(managerSysList);
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
