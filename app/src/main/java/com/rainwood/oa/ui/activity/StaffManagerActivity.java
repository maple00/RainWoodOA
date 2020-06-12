package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffPost;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.adapter.StaffLeftAdapter;
import com.rainwood.oa.ui.adapter.StaffPostAdapter;
import com.rainwood.oa.ui.adapter.StaffRightAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

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

    private IStaffPresenter mStaffPresenter;

    private StaffLeftAdapter mLeftAdapter;
    private List<StaffDepart> mDepartList;
    private StaffRightAdapter mRightAdapter;

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
        pagerRefresh.setEnableLoadmore(true);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initEvent() {
        mLeftAdapter.setstaffDepart(this);
        mLeftAdapter.setPostClick(this);
        mRightAdapter.setClickStaffRight(this);
        defaultSort.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("默认排序");
            }
        });
        sexScreen.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("性别筛选");
            }
        });
        screenAll.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("全部筛选");
            }
        });
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
        //
        toast(mDepartList.get(parentPos).getArray().get(position).getName());
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
        //startActivity(getNewIntent(this, StaffMainActivity.class, "员工详情"));
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
    public void getAllDepart(List<StaffDepart> departList) {
        mDepartList = departList;
        mLeftAdapter.setDepartList(mDepartList);
        // 查询部门下的员工-- 查询选中的职位员工
        for (StaffDepart depart : departList) {
            for (int i = 0; i < depart.getArray().size(); i++) {
                if (depart.getArray().get(i).isSelected()){
                    mStaffPresenter.requestAllStaff(depart.getArray().get(i).getId());
                    break;
                }
            }
        }
    }

    @Override
    public void getAllStaff(List<Staff> staffList) {
        mRightAdapter.setStaffList(staffList);
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
