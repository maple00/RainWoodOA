package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.ui.adapter.HelperAdapter;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/2 9:55
 * @Desc: 帮助中心
 */
public final class HelperActivity extends BaseActivity {

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
        helperView.addItemDecoration(new SpacesItemDecoration(0,0,0,0));
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

    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_page_back:
                finish();
                break;

        }
    }
}
