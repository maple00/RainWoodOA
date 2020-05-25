package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.Staff;
import com.rainwood.oa.model.domain.StaffDepart;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.activity.ExperienceDetailActivity;
import com.rainwood.oa.ui.adapter.StaffExperienceAdapter;
import com.rainwood.oa.ui.adapter.StaffPhotoAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/5/22 17:04
 * @Desc: 员工资料
 */
public final class StaffDataFragment extends BaseFragment implements IStaffCallbacks, StaffExperienceAdapter.OnItemClickExperience {

    // actionBar
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.ll_show_hide)
    private LinearLayout showHide;
    @ViewInject(R.id.gti_show_hide)
    private GroupTextIcon hideClick;
    @ViewInject(R.id.mgv_assist_photo)
    private MeasureGridView assistPhoto;
    @ViewInject(R.id.mlv_work_experience)
    private MeasureListView workExperience;
    // 展开隐藏flag、默认隐藏
    private boolean isShow = false;
    private StaffPhotoAdapter mStaffPhotoAdapter;
    private StaffExperienceAdapter mExperienceAdapter;

    private IStaffPresenter mStaffPresenter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_staff_data;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        pageTitle.setText("员工详情");

        // 创建适配器
        mStaffPhotoAdapter = new StaffPhotoAdapter();
        mExperienceAdapter = new StaffExperienceAdapter();
        // 设置适配器
        assistPhoto.setAdapter(mStaffPhotoAdapter);
        workExperience.setAdapter(mExperienceAdapter);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mStaffPresenter.requestStaffPhoto();
        mStaffPresenter.requestExperience();
    }

    @Override
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        mExperienceAdapter.setClickExperience(this);
        hideClick.setOnItemClick(text -> {
            hideClick.setText(isShow ? "展开" : "收起");
            hideClick.setRightIcon(isShow ? R.drawable.ic_down_arrow_blue : R.drawable.ic_up_arrow_blue, getContext().getColor(R.color.blue05));
            isShow = !isShow;
            showHide.setVisibility(isShow ? View.VISIBLE : View.GONE);
        });
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                Objects.requireNonNull(getActivity()).finish();
                break;
        }
    }

    @Override
    public void getStaffPhoto(List<StaffPhoto> photoList) {
        mStaffPhotoAdapter.setList(photoList);
    }

    @Override
    public void getStaffExperience(List<StaffExperience> experienceList) {
        mExperienceAdapter.setList(experienceList);
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
    public void onClickExperience(StaffExperience staffExperience) {
        // 查看工作经历详情
        startActivity(getNewIntent(getContext(), ExperienceDetailActivity.class, "工作经历"));
    }
}
