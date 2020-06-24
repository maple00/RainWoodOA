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
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffPost;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.adapter.StaffLeftAdapter;
import com.rainwood.oa.ui.adapter.StaffPostAdapter;
import com.rainwood.oa.ui.adapter.StaffRightAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:35
 * @Desc: 员工管理
 */
public final class StaffManagerActivity extends BaseActivity implements IStaffCallbacks,
        StaffLeftAdapter.OnClickstaffDepart, StaffPostAdapter.OnClickPost,
        StaffRightAdapter.OnClickStaffRight {

    // action Bar
    @ViewInject(value = R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchContent;
    @ViewInject(R.id.tv_search)
    private TextView searchTV;
    // content
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon defaultSort;
    @ViewInject(R.id.gti_sex)
    private GroupTextIcon sexScreen;
    @ViewInject(R.id.gti_screen)
    private GroupTextIcon screenAll;
    @ViewInject(R.id.rv_depart_post_list)
    private RecyclerView departPostView;
    @ViewInject(R.id.rv_staff_list)
    private RecyclerView staffRightView;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.divider)
    private View divider;

    private IStaffPresenter mStaffPresenter;

    private StaffLeftAdapter mLeftAdapter;
    private List<StaffDepart> mDepartList;
    private StaffRightAdapter mRightAdapter;

    //
    private boolean DEFAULT_SORT_FLAG = false;
    private boolean SEX_FLAG = false;
    private boolean SCREEN_ALL_FLAG = false;
    private CommonGridAdapter mSexSelectedAdapter;
    private List<SelectedItem> mSexSelectedList;
    private List<SelectedItem> mDefaultSortSelectedList;
    private View mMaskLayer;
    private StaffDefaultSortAdapter mDefaultSortAdapter;
    private CommonGridAdapter mSocialViewAdapter;
    private CommonGridAdapter mGateViewAdapter;
    private List<SelectedItem> mSocialList;
    private List<SelectedItem> mGateList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_staff_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTV.setText(getString(R.string.text_common_search));
        // 设置布局管理器
        departPostView.setLayoutManager(new GridLayoutManager(this, 1));
        departPostView.addItemDecoration(new SpacesItemDecoration(0, 0,
                0, FontSwitchUtil.dip2px(this, 30f)));
        staffRightView.setLayoutManager(new GridLayoutManager(this, 1));
        staffRightView.addItemDecoration(new SpacesItemDecoration(0, 0,
                0, FontSwitchUtil.dip2px(this, 8f)));
        // 创建适配器
        mLeftAdapter = new StaffLeftAdapter();
        mRightAdapter = new StaffRightAdapter();
        // 设置适配器
        departPostView.setAdapter(mLeftAdapter);
        staffRightView.setAdapter(mRightAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableLoadmore(false);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求所有的部门数据
        mStaffPresenter.requestAllDepartData();
        // 请求查询条件(默认排序、性别、全部筛选)
        mStaffPresenter.requestQueryCondition();
    }

    @Override
    protected void initEvent() {
        mLeftAdapter.setstaffDepart(this);
        mLeftAdapter.setPostClick(this);
        mRightAdapter.setClickStaffRight(this);
        defaultSort.setOnItemClick(text -> {
            DEFAULT_SORT_FLAG = !DEFAULT_SORT_FLAG;
            defaultSort.setRightIcon(DEFAULT_SORT_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    DEFAULT_SORT_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (DEFAULT_SORT_FLAG) {
                defaultSortConditionPopDialog();
            }
        });
        sexScreen.setOnItemClick(text -> {
            SEX_FLAG = !SEX_FLAG;
            sexScreen.setRightIcon(SEX_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SEX_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SEX_FLAG) {
                sexConditionPopDialog();
                setSexShowPop();
            }
        });
        screenAll.setOnItemClick(text -> {
            SCREEN_ALL_FLAG = !SCREEN_ALL_FLAG;
            screenAll.setRightIcon(SCREEN_ALL_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SCREEN_ALL_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (SCREEN_ALL_FLAG) {
                queryScreenAllConditionPopDialog();
            }
        });
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
    public void onClickDepart(int position) {
        // 选择部门 -- 如果已经是被选中状态，则折叠，反之展开
        if (!mDepartList.get(position).isSelected()) {
            for (StaffDepart depart : mDepartList) {
                depart.setSelected(false);
            }
            mLeftAdapter.setTempSelected(true);
            mDepartList.get(position).setSelected(!mDepartList.get(position).isSelected());
        }
    }

    /**
     * 查询职位下的员工
     *
     * @param parentPos
     * @param position
     */
    @Override
    public void onClickPost(int parentPos, int position) {
        // 选择职位
        for (StaffPost staffPost : mDepartList.get(parentPos).getArray()) {
            staffPost.setSelected(false);
        }
        mDepartList.get(parentPos).getArray().get(position).setSelected(true);
        // 根据职位/部门id查询该所属员工
        mStaffPresenter.requestAllStaff(mDepartList.get(parentPos).getArray().get(position).getId());
    }

    /**
     * 查看员工详情
     *
     * @param position
     */
    @Override
    public void onClickStaff(Staff staff, int position) {
        PageJumpUtil.staffList2Detail(this, StaffMainActivity.class, staff.getId());
    }

    /**
     * 获得部门列表
     *
     * @param departList
     */
    @Override
    public void getAllDepart(List<StaffDepart> departList) {
        mDepartList = departList;
        mLeftAdapter.setDepartList(mDepartList);
        // 查询部门下的员工-- 查询选中的职位员工
        for (StaffDepart depart : departList) {
            for (int i = 0; i < depart.getArray().size(); i++) {
                if (depart.getArray().get(i).isSelected()) {
                    mStaffPresenter.requestAllStaff(depart.getArray().get(i).getId());
                    break;
                }
            }
        }
    }

    /**
     * 获得员工列表
     *
     * @param staffList
     */
    @Override
    public void getAllStaff(List<Staff> staffList) {
        mRightAdapter.setStaffList(staffList);
    }

    /**
     * 查询条件
     *
     * @param defaultSortList
     * @param sexList
     * @param socialList
     * @param gateList
     */
    @Override
    public void getQueryCondition(List<SelectedItem> defaultSortList, List<SelectedItem> sexList,
                                  List<SelectedItem> socialList, List<SelectedItem> gateList) {
        mDefaultSortSelectedList = defaultSortList;
        mSexSelectedList = sexList;
        mSocialList = socialList;
        mGateList = gateList;
    }

    /**
     * 性别筛选
     */
    private void sexConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mSexSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSexSelectedAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SEX_FLAG = false;
            sexScreen.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SEX_FLAG = false;
                sexScreen.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
    }

    private void setSexShowPop() {
        mSexSelectedAdapter.setTextList(mSexSelectedList);
    }

    /**
     * 默认排序
     */
    private void defaultSortConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(1);
                    mDefaultSortAdapter = new StaffDefaultSortAdapter();
                    contentList.setAdapter(mDefaultSortAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            DEFAULT_SORT_FLAG = false;
            defaultSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                DEFAULT_SORT_FLAG = false;
                defaultSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
        // 筛选条件点击事件
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mDefaultSortSelectedList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
        });
        mDefaultSortAdapter.setItemList(mDefaultSortSelectedList);
    }


    /**
     * 全部筛选
     */
    private void queryScreenAllConditionPopDialog() {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_screen_query_staff_all)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView socialView = view.findViewById(R.id.mgv_social);
                    socialView.setNumColumns(4);
                    MeasureGridView gateView = view.findViewById(R.id.mgv_gate_key);
                    gateView.setNumColumns(4);
                    mSocialViewAdapter = new CommonGridAdapter();
                    mGateViewAdapter = new CommonGridAdapter();
                    socialView.setAdapter(mSocialViewAdapter);
                    gateView.setAdapter(mGateViewAdapter);

                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    TextView clearScreen = view.findViewById(R.id.tv_clear_screen);
                    TextView confirm = view.findViewById(R.id.tv_confirm);
                    clearScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toast("清空筛选");
                        }
                    });
                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toast("确认");
                        }
                    });
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SCREEN_ALL_FLAG = false;
            screenAll.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SCREEN_ALL_FLAG = false;
                screenAll.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
        mSocialViewAdapter.setTextList(mSocialList);
        mGateViewAdapter.setTextList(mGateList);
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
