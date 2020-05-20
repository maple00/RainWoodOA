package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.ICustomListPresenter;
import com.rainwood.oa.ui.adapter.CustomListAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ICustomListCallbacks;
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
public final class CustomListActivity extends BaseActivity implements ICustomListCallbacks, CustomListAdapter.OnItemClickListener {

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

    // 选中标记
    private boolean selectedStatusFlag = false;
    private boolean selectedHeadFlag = false;
    private boolean selectedAreaFlag = false;
    private boolean selectedFilterFlag = false;

    private CustomListAdapter mCustomAdapter;

    private ICustomListPresenter mCustomListPresenter;

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
    }

    @Override
    protected void initEvent() {
        // 筛选客户类型
        mStatus.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            mStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.labelColor));
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
    }

    @SingleClick
    @OnClick(R.id.tv_search_tips)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_tips:
                toast("搜索");
                break;
        }
    }

    @Override
    protected void initPresenter() {
        mCustomListPresenter = PresenterManager.getOurInstance().getCustomListPresenter();
        mCustomListPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mCustomListPresenter.getALlCustomData();
    }

    @SuppressWarnings("all")
    @Override
    public void getAllCustomList(Map customListValues) {
        // 拿到返回数据
        List<Custom> customList = (List<Custom>) customListValues.get("customList");
        // LogUtils.d("sxs", "客户列表数据" + customList);
        // 设置数据
        mCustomAdapter.setList(customList);
    }

    @SuppressWarnings("all")
    @Override
    public void getALlStatus(Map statusMap) {
        // 获取到状态信息
        List<SelectedItem> selectedItems = (List<SelectedItem>) statusMap.get("selectedItem");
        toast("状态--> " + selectedItems);
        LogUtils.d("sxs", "状态-----> " + statusMap);
        // TODO: widget popwindow
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
    public void onItemClick(Custom custom) {
        PageJumpUtil.listJump2CustomDetail(this, CustomDetailActivity.class, custom);
    }
}
