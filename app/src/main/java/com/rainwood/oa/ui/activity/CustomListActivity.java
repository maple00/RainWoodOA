package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.CustomArea;
import com.rainwood.oa.model.domain.CustomScreenAll;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.CustomAreaAdapter;
import com.rainwood.oa.ui.adapter.CustomListAdapter;
import com.rainwood.oa.ui.adapter.CustomListFilterAdapter;
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
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;

import static com.rainwood.oa.utils.Constants.PAGE_SEARCH_CODE;

/**
 * @Author: a797s
 * @Date: 2020/5/18 11:31
 * @Desc: 客户列表
 */
public final class CustomListActivity extends BaseActivity implements ICustomCallbacks,
        CustomListAdapter.OnItemClickListener, StatusAction {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchView;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTipsView;
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
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
    // 分页
    private int pageCount = 1;
    // 选中标记
    private boolean selectedStatusFlag = false;
    private boolean selectedHeadFlag = false;
    private boolean selectedAreaFlag = false;
    private boolean selectedFilterFlag = false;

    private CustomListAdapter mCustomAdapter;

    private ICustomPresenter mCustomListPresenter;
    private View mMaskLayer;
    private CommonGridAdapter mSelectedAdapter;
    private List<SelectedItem> mStateList;
    private RecyclerView mAddressListView;
    private TextView mClearScreenView;
    private TextView mConfirmView;
    private CustomAreaAdapter mCustomAreaAdapter;
    // 省数据
    private List<CustomArea> mProvinceList;
    private TextView mProvinceEt;
    private TextView mCityEt;
    private TextView mAreaEt;
    private RecyclerView mScreenAllView;
    private List<CustomScreenAll> mCustomListConditionList;
    private String mKeyWord;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_list;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        customView.setLayoutManager(new GridLayoutManager(this, 1));
        customView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
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

    @Override
    protected void loadData() {
        // 从这里请求数据 -------- 默认从第一页开始加载
        netRequestCustomList("", "", "", "",
                "", "", "");
        // request condition
        mCustomListPresenter.requestStateCondition();
        mCustomListPresenter.requestCustomCondition();
        mCustomListPresenter.requestProvinceCondition();
    }

    /**
     * 请求客户列表
     */
    private void netRequestCustomList(String headMan, String introduceMan, String origin, String sorting,
                                      String province, String city, String area) {
        showLoading();
        mCustomListPresenter.requestALlCustomData(mKeyWord, headMan, introduceMan, "",
                origin, province, city, area, sorting, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        // 筛选客户类型
        mStatus.setOnItemClick(text -> {
            selectedStatusFlag = !selectedStatusFlag;
            mStatus.setRightIcon(selectedStatusFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedStatusFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.fontColor));
            if (selectedStatusFlag) {
                stateConditionPopDialog(mStateList, mStatus);
            }
        });
        mHeadMan.setOnItemClick(text -> {
            selectedHeadFlag = !selectedHeadFlag;
            mHeadMan.setRightIcon(selectedHeadFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedHeadFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.fontColor));
            if (selectedHeadFlag) {
                toast(text);
            }
        });
        mArea.setOnItemClick(text -> {
            selectedAreaFlag = !selectedAreaFlag;
            mArea.setRightIcon(selectedAreaFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedAreaFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.fontColor));
            if (selectedAreaFlag) {
                chooseArea();
            }
        });
        selectedFilter.setOnItemClick(text -> {
            selectedFilterFlag = !selectedFilterFlag;
            selectedFilter.setRightIcon(selectedFilterFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedFilterFlag ? this.getColor(R.color.colorPrimary) : this.getColor(R.color.fontColor));
            if (selectedFilterFlag) {
                screenQueryAll();
            }
        });

        // 查看详情
        mCustomAdapter.setItemClickListener(this);
        // 去加载更多的内容
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mCustomListPresenter != null) {
                    mCustomListPresenter.requestALlCustomData("", "", "", "",
                            "", "", "", "", "", ++pageCount);
                }
            }
        });
        // 搜索内容监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    searchView.setVisibility(View.GONE);
                    mKeyWord = "";
                } else {
                    searchView.setVisibility(View.VISIBLE);
                    netRequestCustomList("", "", "", "", "", "", "");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == PAGE_SEARCH_CODE && resultCode == PAGE_SEARCH_CODE) {
                mKeyWord = data.getStringExtra("keyWord");
                searchTipsView.setText(mKeyWord);
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_search, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra("pageFlag", "staffManager");
                intent.putExtra("title", "客户列表");
                intent.putExtra("tips", "请输入公司名称");
                startActivityForResult(intent, PAGE_SEARCH_CODE);
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getAllCustomList(List customList) {
        showComplete();
        // 拿到客户列表
        if (pageCount != 1) {
            if (pagerRefresh != null) {
                pagerRefresh.finishLoadmore();
            }
            toast("加载了" + ListUtils.getSize(customList) + "条数据");
            mCustomAdapter.addData(customList);
        } else {
            mCustomAdapter.setList(customList);
        }
    }

    @Override
    public void getStateCondition(List<SelectedItem> stateList) {
        mStateList = stateList;
    }

    @Override
    public void getCustomListProvince(List<CustomArea> provinceList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mProvinceList = provinceList;
        if (selectedAreaFlag) {
            mCustomAreaAdapter.setAreaList(mProvinceList);
            mCustomAreaAdapter.setAreaListener((area, position) -> {
                for (CustomArea customArea : mProvinceList) {
                    customArea.setSelected(false);
                }
                area.setSelected(true);
                mProvinceEt.setText(area.getName());
            });
        }
    }

    @Override
    public void getCustomCity(List<CustomArea> cityList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mCustomAreaAdapter.setAreaList(cityList);
        mCustomAreaAdapter.setAreaListener((area, position) -> {
            for (CustomArea customArea : cityList) {
                customArea.setSelected(false);
            }
            area.setSelected(true);
            mCityEt.setText(area.getName());
        });
    }

    @Override
    public void getCustomArea(List<CustomArea> areaList) {
        if (isShowDialog()) {
            hideDialog();
        }
        mCustomAreaAdapter.setAreaList(areaList);
        mCustomAreaAdapter.setAreaListener((area, position) -> {
            for (CustomArea customArea : areaList) {
                customArea.setSelected(false);
            }
            area.setSelected(true);
            mAreaEt.setText(area.getName());
            mAreaEt.setHint(area.getId());
        });
    }

    /**
     * 列表筛选条件
     *
     * @param customListConditionList
     */
    @Override
    public void getCustomListCondition(List<CustomScreenAll> customListConditionList) {
        // TODO : 客户筛选条件
        mCustomListConditionList = customListConditionList;
    }

    /**
     * 类型选择
     */
    private void stateConditionPopDialog(List<SelectedItem> stateList, GroupTextIcon targetGTI) {
        CommonPopupWindow mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_grid_list)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    MeasureGridView contentList = view.findViewById(R.id.mgv_content);
                    contentList.setNumColumns(4);
                    mSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSelectedAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            selectedStatusFlag = false;
            targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                selectedStatusFlag = false;
                targetGTI.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mSelectedAdapter.setTextList(stateList);
        mSelectedAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : stateList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            mStatusPopWindow.dismiss();
            // TODO: 状态查询
            showLoading();
            mCustomListPresenter.requestALlCustomData(mKeyWord, "", "", item.getName(),
                    "", "", "", "", "", pageCount = 1);
        });
    }

    /**
     * 区域选择 -- TODO：需要做数据缓存
     */
    private void chooseArea() {
        CommonPopupWindow areaPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_area_choose)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mProvinceEt = view.findViewById(R.id.tv_province);
                    mCityEt = view.findViewById(R.id.tv_city);
                    mAreaEt = view.findViewById(R.id.tv_area);
                    mAddressListView = view.findViewById(R.id.rv_region_list);
                    mClearScreenView = view.findViewById(R.id.tv_clear_screen);
                    mConfirmView = view.findViewById(R.id.tv_confirm);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 设置布局管理器
                    mAddressListView.setLayoutManager(new GridLayoutManager(CustomListActivity.this, 1));
                    mAddressListView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
                    // 点击地区
                    mProvinceEt.setOnClickListener(v -> {
                        LogUtils.d("sxs", "点击了");
                        mProvinceEt.setText(null);
                        mCityEt.setText(null);
                        mAreaEt.setText(null);
                        mAreaEt.setHint(null);
                    });
                    mCityEt.setOnClickListener(v -> {
                        mCityEt.setText(null);
                        mAreaEt.setText(null);
                        mAreaEt.setHint(null);
                    });
                })
                .create();
        areaPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        // 创建适配器
        mCustomAreaAdapter = new CustomAreaAdapter();
        mAddressListView.setAdapter(mCustomAreaAdapter);
        // 设置数据(注意加上EditText的判断) -- 默认展示省
        if (TextUtils.isEmpty(mProvinceEt.getText())) {
            mCityEt.setVisibility(View.GONE);
            mAreaEt.setVisibility(View.GONE);
            mCustomAreaAdapter.setAreaList(mProvinceList);
            mCustomAreaAdapter.setAreaListener((area, position) -> {
                for (CustomArea customArea : mProvinceList) {
                    customArea.setSelected(false);
                }
                area.setSelected(true);
                mProvinceEt.setText(area.getName());
            });
            for (int i = 0; i < ListUtils.getSize(mProvinceList); i++) {
                if (mProvinceList.get(i).isSelected()) {
                    mProvinceEt.setText(mProvinceList.get(i).getName());
                    mAddressListView.scrollToPosition(i);
                    break;
                }
            }
        }
        // 区域选择监听
        mProvinceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mCityEt.setVisibility(View.VISIBLE);
                    mAreaEt.setVisibility(View.GONE);
                    // TODO: 请求市
                    mCustomListPresenter.requestCityByProvince(s.toString());
                } else {
                    // TODO：请求省
                    mCustomListPresenter.requestProvinceCondition();
                    mCityEt.setVisibility(View.GONE);
                    mAreaEt.setVisibility(View.GONE);
                }
                showDialog();
            }
        });
        mCityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mAreaEt.setVisibility(View.VISIBLE);
                    // TODO: 请求区
                    mCustomListPresenter.requestAreaByProvinceCity(mProvinceEt.getText().toString().trim(), s.toString());
                } else {
                    // TODO: 请求市 -- 点击了市-- 去除点击省的情况
                    if (!TextUtils.isEmpty(mProvinceEt.getText())) {
                        mCustomListPresenter.requestCityByProvince(mProvinceEt.getText().toString().trim());
                        mAreaEt.setVisibility(View.GONE);
                    }
                }
                showDialog();
            }
        });

        mAreaEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 此处的省市区数据都齐全
                if (!TextUtils.isEmpty(mProvinceEt.getText()) && !TextUtils.isEmpty(mCityEt.getText())
                        && !TextUtils.isEmpty(mAreaEt.getText())) {
                    LogUtils.d("sxs", mProvinceEt.getText() + "-" + mCityEt.getText() + "-" + mAreaEt.getText());
                }
            }
        });

        mMaskLayer.setOnClickListener(v -> {
            areaPopWindow.dismiss();
            selectedAreaFlag = false;
            mArea.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        areaPopWindow.setOnDismissListener(() -> {
            areaPopWindow.dismiss();
            selectedAreaFlag = false;
            mArea.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mClearScreenView.setOnClickListener(v -> {
            mProvinceEt.setText(null);
            mCityEt.setText(null);
            mAreaEt.setText(null);
            mAreaEt.setHint(null);
            // 清空筛选
            if (isShowDialog()){
                hideDialog();
            }
            netRequestCustomList("", "", "", "", "", "", "");
            areaPopWindow.dismiss();
        });
        mConfirmView.setOnClickListener(v -> {
            // TODO: 查询客户列表
            if (TextUtils.isEmpty(mProvinceEt.getText()) || TextUtils.isEmpty(mCityEt.getText())
                    || TextUtils.isEmpty(mAreaEt.getText())) {
                toast("请选择区域");
                return;
            }
            //toast(mProvinceEt.getText() + "-" + mCityEt.getText() + "-" + mAreaEt.getText());
            showLoading();
            netRequestCustomList("", "", "", "", mProvinceEt.getText().toString().trim(),
                    mCityEt.getText().toString().trim(), mAreaEt.getHint().toString().trim());
            areaPopWindow.dismiss();
        });
    }

    /**
     * 全部筛选
     */
    private void screenQueryAll() {
        CommonPopupWindow screenAllPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_custom_screen_all)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mScreenAllView = view.findViewById(R.id.rv_screen_all);
                    mClearScreenView = view.findViewById(R.id.tv_clear_screen);
                    mConfirmView = view.findViewById(R.id.tv_confirm);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 设置布局管理器
                    mScreenAllView.setLayoutManager(new GridLayoutManager(CustomListActivity.this, 1));
                })
                .create();
        screenAllPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            screenAllPopWindow.dismiss();
            selectedFilterFlag = false;
            selectedFilter.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        screenAllPopWindow.setOnDismissListener(() -> {
            screenAllPopWindow.dismiss();
            selectedFilterFlag = false;
            selectedFilter.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mClearScreenView.setOnClickListener(v -> {
            for (CustomScreenAll screenAll : mCustomListConditionList) {
                for (CustomScreenAll.InnerCustomScreen screen : screenAll.getArray()) {
                    screen.setSelected(false);
                }
            }
            screenAllPopWindow.dismiss();
            // TODO: 清空筛选
            netRequestCustomList("", "", "", "", "", "", "");
        });
        mConfirmView.setOnClickListener(v -> {
            String headMan = "";
            String introduceMan = "";
            String origin = "";
            String sorting = "";
            for (CustomScreenAll screenAll : mCustomListConditionList) {
                for (CustomScreenAll.InnerCustomScreen customScreen : screenAll.getArray()) {
                    if (customScreen.isSelected()) {
                        switch (screenAll.getTitle()) {
                            case "负责人":
                                headMan = customScreen.getName();
                                break;
                            case "介绍人":
                                introduceMan = customScreen.getName();
                                break;
                            case "客户来源":
                                origin = customScreen.getName();
                                break;
                            case "排序方式":
                                sorting = customScreen.getName();
                                break;
                        }
                    }
                }
            }
            screenAllPopWindow.dismiss();
            // TODO: 请求接口
            netRequestCustomList(headMan, introduceMan, origin, sorting, "", "", "");
        });
        // 设置数据
        CustomListFilterAdapter customListFilterAdapter = new CustomListFilterAdapter();
        mScreenAllView.setAdapter(customListFilterAdapter);
        customListFilterAdapter.setScreenAllList(mCustomListConditionList);
        customListFilterAdapter.setOnClickListener((item, position, screenAll, parentPos) -> {
            for (CustomScreenAll.InnerCustomScreen customScreen : screenAll.getArray()) {
                customScreen.setSelected(false);
            }
            screenAll.getArray().get(position).setSelected(true);
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
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onEmpty() {
        showEmpty();
        mCustomAdapter.setList(null);
        if (pagerRefresh != null) {
            pagerRefresh.finishLoadmore();
        }
    }

    @Override
    public void onItemClick(Custom custom) {
        PageJumpUtil.listJump2CustomDetail(this, CustomDetailActivity.class, custom.getKhid());
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
