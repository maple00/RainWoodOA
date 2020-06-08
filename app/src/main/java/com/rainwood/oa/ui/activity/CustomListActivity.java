package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.CustomListAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 11:31
 * @Desc: 客户列表
 */
public final class CustomListActivity extends BaseActivity implements ICustomCallbacks, CustomListAdapter.OnItemClickListener {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.gti_status)
    private GroupTextIcon mStatus;
    @ViewInject(R.id.gti_head_man)
    private GroupTextIcon mHeadMan;
    @ViewInject(R.id.gti_area)
    private GroupTextIcon mArea;
    @ViewInject(R.id.gti_selected_filter)
    private GroupTextIcon selectedFilter;
    @ViewInject(R.id.divider)
    private View divider;
    @ViewInject(R.id.rv_custom_list)
    private RecyclerView customView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    // 选中标记
    private boolean selectedStatusFlag = false;
    private boolean selectedHeadFlag = false;
    private boolean selectedAreaFlag = false;
    private boolean selectedFilterFlag = false;

    private CustomListAdapter mCustomAdapter;

    private ICustomPresenter mCustomListPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchContent.setOnFocusChangeListener((v, hasFocus) -> searchTV.setText(hasFocus ? getString(R.string.text_common_search) : getString(R.string.custom_text_manager)));
        // 设置布局管理器
        customView.setLayoutManager(new GridLayoutManager(this, 1));
        customView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mCustomAdapter = new CustomListAdapter();
        // 设置适配器
        customView.setAdapter(mCustomAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mCustomListPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomListPresenter.registerViewCallback(this);
    }

    private int pageCount = 0;

    @Override
    protected void loadData() {
        // 从这里请求数据 -------- 默认从第一页开始加载
        mCustomListPresenter.getALlCustomData(pageCount);
    }

    @Override
    protected void initEvent() {
        // 筛选客户类型
        mStatus.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            mStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            LogUtils.d("sxs", "选中状态---- " + selectedStatusFlag);
            // TODO: 查询状态
            if (selectedStatusFlag)
                mCustomListPresenter.getStatus();
        });
        mHeadMan.setOnItemClick(text -> {
            selectedHeadFlag = !selectedHeadFlag;
            mHeadMan.setRightIcon(selectedHeadFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedHeadFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            // TODO：查询负责人
        });
        mArea.setOnItemClick(text -> {
            selectedAreaFlag = !selectedAreaFlag;
            mArea.setRightIcon(selectedAreaFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedAreaFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            // TODO: 查询区域
        });
        selectedFilter.setOnItemClick(text -> {
            selectedFilterFlag = !selectedFilterFlag;
            selectedFilter.setRightIcon(selectedFilterFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedFilterFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
            // TODO: 全部筛选
        });

        // 查看详情
        mCustomAdapter.setItemClickListener(this);

        // 去加载更多的内容
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mCustomListPresenter != null) {
                    mCustomListPresenter.getALlCustomData(++pageCount);
                }
            }
        });
    }

    @SingleClick
    @OnClick({R.id.tv_search_tips, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_search_tips:
                toast("搜索");
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getAllCustomList(List customList) {
        // 拿到客户列表
        // 设置数据
        if (pageCount != 0) {
            if (pagerRefresh != null) {
                pagerRefresh.finishLoadmore();
            }
            toast("加载了" + ListUtils.getSize(customList) + "条数据");
            mCustomAdapter.addData(customList);
        } else {
            mCustomAdapter.setList(customList);
        }
    }

    private View mMaskLayer;

    @SuppressWarnings("all")
    @Override
    public void getALlStatus(Map statusMap) {
        // 获取到状态信息
        List<SelectedItem> selectedItems = (List<SelectedItem>) statusMap.get("selectedItem");
        // popWindow
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                        contentList.setNumColumns(4);
                        CommonGridAdapter gridAdapter = new CommonGridAdapter();
                        contentList.setAdapter(gridAdapter);
                        gridAdapter.setTextList(selectedItems);

                        mMaskLayer = view.findViewById(R.id.mask_layer);
                        TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    }
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatusPopWindow.dismiss();
                selectedStatusFlag = false;
                mStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                        selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            }
        });
        mStatusPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mStatusPopWindow.dismiss();
                if (!mStatusPopWindow.isShowing()) {
                    selectedStatusFlag = false;
                    mStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                            selectedStatusFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
                }
            }
        });
    }

    @Override
    public void onError() {
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onLoading() {
        // toast("");
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onEmpty() {
        toast("没有更多的数据了");
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onItemClick(Custom custom) {
        PageJumpUtil.listJump2CustomDetail(this, CustomDetailActivity.class, custom.getKhid());
    }

}
