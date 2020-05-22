package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.presenter.IPostPresenter;
import com.rainwood.oa.ui.adapter.PostManagerAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IPostCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:04
 * @Desc: 职位管理
 */
public final class PostManagerActivity extends BaseActivity implements IPostCallbacks, PostManagerAdapter.OnClickPostItem {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.gti_depart)
    private GroupTextIcon departGTI;
    @ViewInject(R.id.gti_role)
    private GroupTextIcon roleGTI;
    @ViewInject(R.id.rv_post_list)
    private RecyclerView postListView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    // 选择flag
    private boolean selectedDepartFlag = false;
    private boolean selectedRoleFlag = false;

    private PostManagerAdapter mPostManagerAdapter;
    private IPostPresenter mPostPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_post_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTV.setText(getString(R.string.text_common_search));
        // 设置布局管理器
        postListView.setLayoutManager(new GridLayoutManager(this, 1));
        postListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mPostManagerAdapter = new PostManagerAdapter();
        // 设置适配器
        postListView.setAdapter(mPostManagerAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initEvent() {
        mPostManagerAdapter.setClickPostItem(this);
        departGTI.setOnItemClick(text -> {
            selectedDepartFlag = !selectedDepartFlag;
            departGTI.setRightIcon(selectedDepartFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedDepartFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
        });
        roleGTI.setOnItemClick(text -> {
            selectedRoleFlag = !selectedRoleFlag;
            roleGTI.setRightIcon(selectedRoleFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedRoleFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
        });
    }

    @Override
    protected void loadData() {
        // 加载数据
        mPostPresenter.getAllPost();

    }

    @Override
    protected void initPresenter() {
        mPostPresenter = PresenterManager.getOurInstance().getPostPresenter();
        mPostPresenter.registerViewCallback(this);
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
    public void getALlPostData(List<Post> postList) {
        mPostManagerAdapter.setPostList(postList);
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
    public void onClickPost(Post post) {
        // item点击事件
        toast("查看详情");
        //startActivity(PostDetailActivity.class);
        startActivity(getNewIntent(this, PostDetailActivity.class, "职位详情"));
    }
}
