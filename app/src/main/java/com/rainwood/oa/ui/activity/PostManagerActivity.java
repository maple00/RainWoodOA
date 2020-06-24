package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.DepartGroupScreenAdapter;
import com.rainwood.oa.ui.adapter.DepartScreenAdapter;
import com.rainwood.oa.ui.adapter.PostManagerAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:04
 * @Desc: 职位管理activity
 */
public final class PostManagerActivity extends BaseActivity implements IAdministrativeCallbacks,
        PostManagerAdapter.OnClickPostItem {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.divider)
    private View divider;
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

    private IAdministrativePresenter mAdministrativePresenter;
    private PostManagerAdapter mPostManagerAdapter;
    private View mMaskLayer;
    private List<Depart> departConditionList = new ArrayList<>();
    private DepartScreenAdapter mDepartScreenAdapter;
    private DepartGroupScreenAdapter mDepartGroupScreenAdapter;
    private RecyclerView mModule;
    private RecyclerView mSecondModule;
    private CommonPopupWindow mStatusPopWindow;
    private TextView mTextClearScreen;
    private TextView mTextConfirm;
    private CommonGridAdapter mGridAdapter;
    private List<SelectedItem> mSelectedList;

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
        // initial popWindow
        initPopDepartDialog();
    }


    @Override
    protected void initPresenter() {
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 职位列表
        mAdministrativePresenter.requestPostListData();
        // 所属部门数据
        mAdministrativePresenter.requestAllDepartData();
        // 角色查询
        mAdministrativePresenter.requestPostRoleCondition();
    }

    @Override
    public void onClickPost(Post post) {
        PageJumpUtil.postList2Detail(this, PostDetailActivity.class, post.getId());
    }

    @Override
    protected void initEvent() {
        mPostManagerAdapter.setClickPostItem(this);
        departGTI.setOnItemClick(text -> {
            selectedDepartFlag = !selectedDepartFlag;
            departGTI.setRightIcon(selectedDepartFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedDepartFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            if (selectedDepartFlag) {
                showPopDepartScreen();
            }
        });
        roleGTI.setOnItemClick(text -> {
            if (ListUtils.getSize(mSelectedList) == 0) {
                toast("当前没有角色");
                return;
            }
            selectedRoleFlag = !selectedRoleFlag;
            roleGTI.setRightIcon(selectedRoleFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedRoleFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            if (selectedRoleFlag) {
                // initial role popWindow
                roleConditionPopDialog();
                showRoleData();
            }
        });
        //
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

    /**
     * 职位列表
     *
     * @param postList
     */
    @Override
    public void getPostListData(List<Post> postList) {
        mPostManagerAdapter.setPostList(postList);
    }

    /**
     * 所属部门
     *
     * @param departList
     */
    @Override
    public void getDepartListData(List<Depart> departList) {
        departConditionList.clear();
        departConditionList.addAll(departList);
    }

    /**
     * 角色信息
     *
     * @param selectedList
     */
    @Override
    public void getPostRoleData(List<SelectedItem> selectedList) {

        mSelectedList = selectedList;
    }

    private int tempPos = -1;

    /**
     * 初始化所属部门
     */
    private void initPopDepartDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_role_screen)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mModule = view.findViewById(R.id.rv_module);
                    mSecondModule = view.findViewById(R.id.rv_second_module);
                    // 设置布局管理器
                    mModule.setLayoutManager(new GridLayoutManager(PostManagerActivity.this, 1));
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    mSecondModule.setLayoutManager(new GridLayoutManager(PostManagerActivity.this, 1));
                    mSecondModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    // 创建适配器
                    mDepartScreenAdapter = new DepartScreenAdapter();
                    mDepartGroupScreenAdapter = new DepartGroupScreenAdapter();
                    // 设置适配器
                    mModule.setAdapter(mDepartScreenAdapter);
                    mSecondModule.setAdapter(mDepartGroupScreenAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 确定、清空
                    mTextClearScreen = view.findViewById(R.id.tv_clear_screen);
                    mTextConfirm = view.findViewById(R.id.tv_confirm);
                })
                .create();
        mTextClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("清空筛选");
            }
        });
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("确定");
            }
        });
    }

    /**
     * 展示所属部门
     */
    private void showPopDepartScreen() {
        // 设置数据 -- 默认选中第一项
        departConditionList.get(tempPos == -1 ? 0 : tempPos).setHasSelected(true);
        mDepartScreenAdapter.setList(departConditionList);
        mDepartGroupScreenAdapter.setList(departConditionList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);

        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            tempPos = -1;
            selectedDepartFlag = false;
            departGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                tempPos = -1;
                selectedDepartFlag = false;
                departGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
        // 点击事件
        mDepartScreenAdapter.setRoleCondition(((condition, position) -> {
            for (Depart departCondition : departConditionList) {
                departCondition.setHasSelected(false);
            }
            condition.setHasSelected(true);
            tempPos = position;
            mDepartGroupScreenAdapter.setList(departConditionList.get(position).getArray());
        }));
        mDepartGroupScreenAdapter.setSecondRoleCondition((secondCondition, position) -> {
            for (ProjectGroup group : departConditionList.get(tempPos == -1 ? 0 : tempPos).getArray()) {
                group.setSelected(false);
            }
            secondCondition.setSelected(true);
        });
    }

    /**
     * 角色条件
     */
    private void roleConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mGridAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mGridAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedRoleFlag = false;
            roleGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedRoleFlag = false;
                roleGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
    }

    private void showRoleData() {
        mGridAdapter.setTextList(mSelectedList);
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
