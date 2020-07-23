package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.Post;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.DepartGroupScreenAdapter;
import com.rainwood.oa.ui.adapter.DepartScreenAdapter;
import com.rainwood.oa.ui.adapter.PostManagerAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IAdministrativeCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.rainwood.oa.utils.Constants.PAGE_SEARCH_CODE;

/**
 * @Author: a797s
 * @Date: 2020/5/22 9:04
 * @Desc: 职位管理activity
 */
public final class PostManagerActivity extends BaseActivity implements IAdministrativeCallbacks,
        PostManagerAdapter.OnClickPostItem, StatusAction {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchView;
    @ViewInject(R.id.et_search_tips)
    private EditText searchTipsView;
    @ViewInject(R.id.tv_cancel)
    private TextView mTextCancel;
    @ViewInject(R.id.iv_search)
    private ImageView mImageSearch;
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
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
    // 选择flag
    private boolean selectedDepartFlag = false;
    private boolean selectedRoleFlag = false;
    private String mDepartId;

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

    private int pageCount = 1;
    private String mPositionName;
    private String mRoleId;
    private TextSelectedItemFlowLayout mTextFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_post_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        postListView.setLayoutManager(new GridLayoutManager(this, 1));
        postListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mPostManagerAdapter = new PostManagerAdapter();
        // 设置适配器
        postListView.setAdapter(mPostManagerAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
        // initial popWindow
        initPopDepartDialog();
        // 设置动画
        // LayoutTransition transition = new LayoutTransition();
        // pageTop.setLayoutTransition(transition);
    }


    @Override
    protected void initPresenter() {
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 职位列表
        showDialog();
        netDataLoading("", "", "");
        // 所属部门数据
        mAdministrativePresenter.requestAllDepartData("", "");
        // 角色查询
        mAdministrativePresenter.requestPostRoleCondition();
    }

    /**
     * 请求数据
     *
     * @param keyWord
     */
    private void netDataLoading(String keyWord, String departId, String roleId) {
        mAdministrativePresenter.requestPostListData(keyWord, departId, roleId, (pageCount = 1));
    }

    @Override
    public void onClickPost(Post post) {
        // 查看详情
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
            }
        });
        // UI样式变化监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    searchView.setVisibility(View.VISIBLE);
                    mPositionName = s.toString();
                } else {
                    mPositionName = "";
                }
                netDataLoading(mPositionName, mDepartId, mRoleId);
            }
        });
        // 加载更多
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mAdministrativePresenter.requestPostListData(mPositionName, mDepartId, mRoleId, (++pageCount));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 职位搜索
            if (requestCode == PAGE_SEARCH_CODE && resultCode == PAGE_SEARCH_CODE) {
                mAdministrativePresenter.registerViewCallback(this);
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_search:
                // 搜索
                mTextCancel.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.VISIBLE);
                pageTitle.setVisibility(View.GONE);
                mImageSearch.setVisibility(View.GONE);
                searchTipsView.setHint("请输入职位");
                break;
            case R.id.tv_cancel:
                mTextCancel.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                pageTitle.setVisibility(View.VISIBLE);
                mImageSearch.setVisibility(View.VISIBLE);
                searchTipsView.setText("");
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
        pagerRefresh.finishLoadmore();
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        if (pageCount != 1) {
            if (ListUtils.getSize(postList) == 0) {
                toast("我是有底线滴哟");
                return;
            }
            mPostManagerAdapter.addData(postList);
        } else {
            if (ListUtils.getSize(postList) == 0) {
                showEmpty();
                return;
            }
            mPostManagerAdapter.setPostList(postList);
        }
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
        Collections.reverse(mSelectedList);
    }

    private static int tempPos = -1;

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
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 3));
                    mSecondModule.setLayoutManager(new GridLayoutManager(PostManagerActivity.this, 1));
                    mSecondModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 3));
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
        mMaskLayer.setOnClickListener(v -> mStatusPopWindow.dismiss());
        mTextClearScreen.setOnClickListener(v -> {
            for (Depart depart : departConditionList) {
                depart.setHasSelected(false);
                for (ProjectGroup group : depart.getArray()) {
                    group.setSelected(false);
                }
            }
            tempPos = -1;
            mStatusPopWindow.dismiss();
            // TODO: 清空筛选
            mDepartId = "";
            showDialog();
            netDataLoading("", "", "");
        });
        mTextConfirm.setOnClickListener(v -> {
            for (Depart depart : departConditionList) {
                if (depart.isHasSelected()) {
                    for (ProjectGroup group : depart.getArray()) {
                        if (group.isSelected()) {
                            mDepartId = group.getId();
                            break;
                        }
                    }
                    break;
                }
            }
            if (TextUtils.isEmpty(mDepartId)) {
                toast("请选择部门");
                return;
            }
            mStatusPopWindow.dismiss();
            // TODO：带部门条件查询
            showDialog();
            netDataLoading(mPositionName, mDepartId, mRoleId);
        });
    }

    /**
     * 展示所属部门
     */
    private void showPopDepartScreen() {
        // 设置数据 -- 不设置默认选中项
        mDepartScreenAdapter.setList(departConditionList);
        mDepartGroupScreenAdapter.setList(departConditionList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);

        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedDepartFlag = false;
            departGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedDepartFlag = false;
                departGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
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
                    contentList.setVisibility(View.GONE);
                    contentList.setNumColumns(4);
                    mGridAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mGridAdapter);
                    mTextFlowLayout = view.findViewById(R.id.tfl_text);

                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedRoleFlag = false;
            roleGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedRoleFlag = false;
                roleGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mTextFlowLayout.setTextList(mSelectedList);
        mTextFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : mSelectedList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            mStatusPopWindow.dismiss();
            mRoleId = TextUtils.isEmpty(selectedItem.getId()) ? "" : selectedItem.getId();
            showDialog();
            if ("全部".equals(selectedItem.getName())) {
                mRoleId = "";
            }
            netDataLoading(mPositionName, mDepartId, mRoleId);
        });
    }

    @Override
    public void onError(String tips) {
        toast(tips);
    }

    @Override
    public void onLoading() {
        showLoading();
    }

    @Override
    public void onEmpty() {
        showEmpty();
    }

    @Override
    protected void release() {
        if (mAdministrativePresenter != null) {
            mAdministrativePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
