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
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IAdministrativePresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.DepartManagerAdapter;
import com.rainwood.oa.ui.adapter.ProjectGroupsAdapter;
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
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.Collections;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/21 16:59
 * @Desc: 部门管理
 */
public final class DepartManagerActivity extends BaseActivity implements IAdministrativeCallbacks,
        ProjectGroupsAdapter.OnClickGroup {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.et_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.gti_screen)
    private GroupTextIcon screenTitle;
    // content
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.rv_depart_list)
    private RecyclerView departList;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    private DepartManagerAdapter mDepartManagerAdapter;

    private IAdministrativePresenter mAdministrativePresenter;

    // 筛选
    private boolean selectedStatusFlag = false;
    private CommonPopupWindow mDepartTypePopWindow;
    private View mMaskLayer;
    private CommonGridAdapter mGridAdapter;
    private List<SelectedItem> mDepartTypeList;
    private TextSelectedItemFlowLayout mTextFlowLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_depart_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        // initView
        searchContent.setHint("输入部门名称");
        screenTitle.setText("部门分类");
        // 设置局部管理器
        departList.setLayoutManager(new GridLayoutManager(this, 1));
        departList.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 0f)));
        // 创建适配器
        mDepartManagerAdapter = new DepartManagerAdapter();
        // 设置适配器
        departList.setAdapter(mDepartManagerAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(false);

    }

    @Override
    protected void initEvent() {
        // 查看详情
        mDepartManagerAdapter.setOnClickGroup(this);
        // 筛选分类
        screenTitle.setOnItemClick(text -> {
            if (ListUtils.getSize(mDepartTypeList) == 0) {
                toast("当前没有分类数据");
                return;
            }
            selectedStatusFlag = !selectedStatusFlag;
            screenTitle.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (selectedStatusFlag) {
                // initial pop
                initPopDialog();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mAdministrativePresenter = PresenterManager.getOurInstance().getAdministrativePresenter();
        mAdministrativePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据---数据列表、筛选条件
        showDialog();
        mAdministrativePresenter.requestAllDepartData("", "");
        mAdministrativePresenter.requestDepartScreenCondition();
    }

    @Override
    public void onClickItem(int parentPos, ProjectGroup group) {
        // 查看详情
        PageJumpUtil.departList2Detail(this, DepartDetailActivity.class, group.getId());
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.et_search_tips, R.id.ll_search_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.et_search_tips:
            case R.id.ll_search_view:
                PageJumpUtil.page2SearchView(this, SearchActivity.class, "部门管理",
                        "departManager", "输入部门名称");
                break;
        }
    }

    @Override
    public void getDepartListData(List<Depart> departList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mDepartManagerAdapter.setDepartList(departList);
    }

    /**
     * 部门分类数据
     *
     * @param departTypeList
     */
    @Override
    public void getDepartTypeData(List<SelectedItem> departTypeList) {
        Collections.reverse(departTypeList);
        mDepartTypeList = departTypeList;
    }

    /**
     * 初始化pop筛选条件 --  Unable to add window -- token null is not valid; is your activity running?
     */
    private void initPopDialog() {
        mDepartTypePopWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.pop_grid_list)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mGridAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mGridAdapter);
                    contentList.setVisibility(View.GONE);
                    mTextFlowLayout = view.findViewById(R.id.tfl_text);

                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                }).create();

        mDepartTypePopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0);

        mMaskLayer.setOnClickListener(v -> {
            mDepartTypePopWindow.dismiss();
            selectedStatusFlag = false;
            screenTitle.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mDepartTypePopWindow.setOnDismissListener(() -> {
            mDepartTypePopWindow.dismiss();
            if (!mDepartTypePopWindow.isShowing()) {
                selectedStatusFlag = false;
                screenTitle.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });

        mTextFlowLayout.setTextList(mDepartTypeList);
        mTextFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : mDepartTypeList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            // TODO: 查询分类接口
            mAdministrativePresenter.requestAllDepartData("",
                    selectedItem.getName().equals("全部") ? "" : selectedItem.getName());
            mDepartTypePopWindow.dismiss();
        });
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
