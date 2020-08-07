package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.adapter.CommonGridAdapter;
import com.rainwood.oa.ui.adapter.StaffDefaultSortAdapter;
import com.rainwood.oa.ui.adapter.StaffLeftAdapter;
import com.rainwood.oa.ui.adapter.StaffPostAdapter;
import com.rainwood.oa.ui.adapter.StaffRightAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.TextSelectedItemFlowLayout;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.Collections;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/22 10:35
 * @Desc: 员工管理
 */
public final class StaffManagerActivity extends BaseActivity implements IStaffCallbacks,
        StaffLeftAdapter.OnClickstaffDepart, StaffPostAdapter.OnClickPost,
        StaffRightAdapter.OnClickStaffRight, StatusAction {

    // action Bar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.ll_search_view)
    private LinearLayout searchTopView;
    @ViewInject(R.id.et_search_tips)
    private EditText searchTipsView;
    @ViewInject(R.id.tv_cancel)
    private TextView mTextCancel;
    @ViewInject(R.id.iv_search)
    private ImageView mImageSearch;
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
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IStaffPresenter mStaffPresenter;

    private StaffLeftAdapter mLeftAdapter;
    private List<StaffDepart> mDepartList;
    private StaffRightAdapter mRightAdapter;
    private String mPostId;
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
    private CommonPopupWindow mStatusPopWindow;
    private TextSelectedItemFlowLayout mTextFlowLayout;

    private int pageCount = 1;
    private String keyWord;
    private String mSex;
    private String mSortMethod;
    private String mSocialStr;
    private String mGateKeyStr;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_staff_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        departPostView.setLayoutManager(new GridLayoutManager(this, 1));
        departPostView.addItemDecoration(new SpacesItemDecoration(0, 0,
                0, FontSwitchUtil.dip2px(this, 15f)));
        staffRightView.setLayoutManager(new GridLayoutManager(this, 1));
        staffRightView.addItemDecoration(new SpacesItemDecoration(0, 0,
                0, FontSwitchUtil.dip2px(this, 4f)));
        // 创建适配器
        mLeftAdapter = new StaffLeftAdapter();
        mRightAdapter = new StaffRightAdapter();
        // 设置适配器
        departPostView.setAdapter(mLeftAdapter);
        staffRightView.setAdapter(mRightAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableLoadmore(true);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showDialog();
        mStaffPresenter.registerViewCallback(this);
        // 请求所有的部门数据
        mStaffPresenter.requestAllDepartData();
        // 请求查询条件(默认排序、性别、全部筛选)
        mStaffPresenter.requestQueryCondition();
    }

    /**
     * 员工列表
     *
     * @param keyWord
     */
    private void netRequestStaffList(String keyWord, String postId, String social, String gateKey) {
        mStaffPresenter.registerViewCallback(this);
        mStaffPresenter.requestAllStaff(postId, keyWord, mSex, social, gateKey, mSortMethod, pageCount = 1);
    }

    @Override
    protected void initEvent() {
        mLeftAdapter.setstaffDepart(this);
        mLeftAdapter.setPostClick(this);
        mRightAdapter.setClickStaffRight(this);
        defaultSort.setOnItemClick(text -> {
            DEFAULT_SORT_FLAG = !DEFAULT_SORT_FLAG;
            defaultSort.setRightIcon(DEFAULT_SORT_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    DEFAULT_SORT_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.fontColor));
            if (DEFAULT_SORT_FLAG) {
                defaultSortConditionPopDialog();
            }
        });
        sexScreen.setOnItemClick(text -> {
            SEX_FLAG = !SEX_FLAG;
            sexScreen.setRightIcon(SEX_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SEX_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.fontColor));
            if (SEX_FLAG) {
                sexConditionPopDialog();
            }
        });
        screenAll.setOnItemClick(text -> {
            SCREEN_ALL_FLAG = !SCREEN_ALL_FLAG;
            screenAll.setRightIcon(SCREEN_ALL_FLAG ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    SCREEN_ALL_FLAG ? getColor(R.color.colorPrimary) : getColor(R.color.fontColor));
            if (SCREEN_ALL_FLAG) {
                queryScreenAllConditionPopDialog();
            }
        });
        // UI变化监听
        searchTipsView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord = s.toString();
                netRequestStaffList(keyWord, mPostId, mSocialStr, mGateKeyStr);
            }
        });
        // 加载更多
        pagerRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mStaffPresenter.registerViewCallback(StaffManagerActivity.this);
                mStaffPresenter.requestAllStaff(mPostId, keyWord, mSex, mSocialStr, mGateKeyStr, mSortMethod, ++pageCount);
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.iv_search, R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_cancel:
                mTextCancel.setVisibility(View.GONE);
                searchTopView.setVisibility(View.GONE);
                pageTitle.setVisibility(View.VISIBLE);
                mImageSearch.setVisibility(View.VISIBLE);
                searchTipsView.setText("");
                // 向左边移出
                searchTopView.setAnimation(AnimationUtils.makeOutAnimation(this, false));
                break;
            case R.id.iv_search:
                mTextCancel.setVisibility(View.VISIBLE);
                searchTopView.setVisibility(View.VISIBLE);
                pageTitle.setVisibility(View.GONE);
                mImageSearch.setVisibility(View.GONE);
                searchTipsView.setHint("请输入员工姓名");
                // 向右边移入
                searchTopView.setAnimation(AnimationUtils.makeInAnimation(this, true));
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
        for (StaffDepart staffDepart : mDepartList) {
            staffDepart.setSelected(false);
            for (StaffPost staffPost : staffDepart.getArray()) {
                staffPost.setSelected(false);
            }
        }
        mDepartList.get(parentPos).getArray().get(position).setSelected(true);
        // 根据职位/部门id查询该所属员工
        showDialog();
        mPostId = mDepartList.get(parentPos).getArray().get(position).getId();
        netRequestStaffList(keyWord, mPostId, mSocialStr, mGateKeyStr);
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
        // 默认第一项展开
        mDepartList = departList;
        mLeftAdapter.setDepartList(mDepartList);
        // 查询部门下的员工-- 查询选中的职位员工
        for (StaffDepart depart : departList) {
            for (int i = 0; i < depart.getArray().size(); i++) {
                if (depart.getArray().get(i).isSelected()) {
                    mPostId = depart.getArray().get(i).getId();
                    netRequestStaffList(keyWord, mPostId, mSocialStr, mGateKeyStr);
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
        if (isShowDialog()) {
            hideDialog();
        }
        LogUtils.d("sxs", "11111111111111111111111111111");
        showComplete();
        pagerRefresh.finishLoadmore();
        if (pageCount != 1) {
            if (ListUtils.getSize(staffList) == 0) {
                pageCount--;
                return;
            }
            mRightAdapter.addData(staffList);
        } else {
            if (ListUtils.getSize(staffList) == 0) {
                toast("当前没有员工");
                showEmpty();
                return;
            }
            mRightAdapter.setStaffList(staffList);
        }
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
        Collections.reverse(mSexSelectedList);
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
                    contentList.setVisibility(View.GONE);
                    mSexSelectedAdapter = new CommonGridAdapter();
                    contentList.setAdapter(mSexSelectedAdapter);
                    mTextFlowLayout = view.findViewById(R.id.tfl_text);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.85f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SEX_FLAG = false;
            sexScreen.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SEX_FLAG = false;
                sexScreen.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });

        mTextFlowLayout.setTextList(mSexSelectedList);
        mTextFlowLayout.setOnFlowTextItemClickListener(selectedItem -> {
            for (SelectedItem item : mSexSelectedList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            // TODO : 通过性别筛选
            showDialog();
            mSex = selectedItem.getName().equals("不限") ? "" : selectedItem.getName();
            mStatusPopWindow.dismiss();
            mStaffPresenter.requestAllStaff(mPostId, keyWord, mSex,
                    mSocialStr, mGateKeyStr, mSortMethod, pageCount = 1);
        });
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
                    contentList.setVisibility(View.VISIBLE);
                    mDefaultSortAdapter = new StaffDefaultSortAdapter();
                    contentList.setAdapter(mDefaultSortAdapter);
                    mTextFlowLayout = view.findViewById(R.id.tfl_text);
                    mTextFlowLayout.setVisibility(View.GONE);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            DEFAULT_SORT_FLAG = false;
            defaultSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                DEFAULT_SORT_FLAG = false;
                defaultSort.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        // 筛选条件点击事件
        mDefaultSortAdapter.setOnClickListener((selectedItem, position) -> {
            for (SelectedItem item : mDefaultSortSelectedList) {
                item.setHasSelected(false);
            }
            selectedItem.setHasSelected(true);
            mStatusPopWindow.dismiss();
            //TODO: 通过排序查询
            mSortMethod = "默认排序".equals(selectedItem.getName()) ? "" : selectedItem.getName();
            showDialog();
            mStaffPresenter.requestAllStaff(mPostId, keyWord, mSex, mSocialStr, mGateKeyStr, mSortMethod
                    , pageCount = 1);
        });
        mDefaultSortAdapter.setItemList(mDefaultSortSelectedList);
    }


    /**
     * 全部筛选
     */
    private void queryScreenAllConditionPopDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
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
                    clearScreen.setOnClickListener(v -> {
                        // toast("清空筛选");
                        for (SelectedItem item : mSocialList) {
                            item.setHasSelected(false);
                        }
                        for (SelectedItem item : mGateList) {
                            item.setHasSelected(false);
                        }
                        mStatusPopWindow.dismiss();
                    });
                    confirm.setOnClickListener(v -> {
                        mStatusPopWindow.dismiss();
                        for (SelectedItem item : mSocialList) {
                            if (item.isHasSelected()) {
                                mSocialStr = "不限".equals(item.getName()) ? "" : item.getName();
                            }
                        }
                        for (SelectedItem item : mGateList) {
                            if (item.isHasSelected()) {
                                mGateKeyStr = "不限".equals(item.getName()) ? "" : item.getName();
                            }
                        }
                        // TODO : 查询全部筛选
                        showDialog();
                        netRequestStaffList(keyWord, mPostId, mSocialStr, mGateKeyStr);
                    });
                })
                .create();
        mStatusPopWindow.showAsDropDown(divider, Gravity.BOTTOM, 0, 0);
        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            SCREEN_ALL_FLAG = false;
            screenAll.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                SCREEN_ALL_FLAG = false;
                screenAll.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.fontColor));
            }
        });
        mSocialViewAdapter.setTextList(mSocialList);
        mGateViewAdapter.setTextList(mGateList);
        mSocialViewAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : mSocialList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            // TODO: 查询是否购买社保
        });
        mGateViewAdapter.setOnClickListener((item, position) -> {
            for (SelectedItem selectedItem : mGateList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            // TODO：查询是否有大门钥匙
        });
    }

    @Override
    public void onError(String tips) {
        if (isShowDialog()) {
            hideDialog();
        }
        toast(tips);
    }

    @Override
    public void onError() {
        if (isShowDialog()) {
            hideDialog();
        }
    }

    @Override
    public void onLoading() {
        if (isShowDialog()) {
            hideDialog();
        }
    }

    @Override
    public void onEmpty() {
        if (isShowDialog()) {
            hideDialog();
        }
    }

    @Override
    protected void release() {
        if (mStaffPresenter != null) {
            mStaffPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
