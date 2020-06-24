package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.StaffDetail;
import com.rainwood.oa.model.domain.StaffExperience;
import com.rainwood.oa.model.domain.StaffPhoto;
import com.rainwood.oa.presenter.IStaffPresenter;
import com.rainwood.oa.ui.activity.ExperienceDetailActivity;
import com.rainwood.oa.ui.adapter.StaffExperienceAdapter;
import com.rainwood.oa.ui.adapter.StaffPhotoAdapter;
import com.rainwood.oa.ui.widget.GroupIconText;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IStaffCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.oa.network.aop.SingleClick;

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
    @ViewInject(R.id.tv_staff)
    private TextView staffName;
    @ViewInject(R.id.tv_depart_post)
    private TextView departPost;
    @ViewInject(R.id.tv_note_data)
    private TextView noteData;
    @ViewInject(R.id.git_staff_tel)
    private GroupIconText staffTel;
    @ViewInject(R.id.git_staff_qq)
    private GroupIconText staffQq;
    @ViewInject(R.id.gtt_staff_id)
    private GroupTextText staffId;
    @ViewInject(R.id.gtt_staff_username)
    private GroupTextText staffLoginName;
    @ViewInject(R.id.gtt_direct_supervisor)
    private GroupTextText supervisor;
    @ViewInject(R.id.gtt_school)
    private GroupTextText school;
    @ViewInject(R.id.gtt_major)
    private GroupTextText major;
    @ViewInject(R.id.gtt_graduation_date)
    private GroupTextText graduationTime;
    @ViewInject(R.id.gtt_staff_bank)
    private GroupTextText salaryBank;
    @ViewInject(R.id.gtt_back_no)
    private GroupTextText bankNo;
    @ViewInject(R.id.gtt_social_security)
    private GroupTextText socialSecurityNo;
    @ViewInject(R.id.gtt_entry_time)
    private GroupTextText entryTime;
    @ViewInject(R.id.gtt_ding_no)
    private GroupTextText dingTalkNo;
    @ViewInject(R.id.gtt_staff_note)
    private GroupTextText staffNote;
    //
    @ViewInject(R.id.ll_show_hide)
    private LinearLayout showHide;
    @ViewInject(R.id.gti_show_hide)
    private GroupTextIcon hideClick;
    @ViewInject(R.id.mgv_assist_photo)
    private MeasureGridView assistPhoto;
    @ViewInject(R.id.mlv_work_experience)
    private MeasureListView workExperience;
    @ViewInject(R.id.tv_none_job_experience)
    private TextView noneJobExperience;
    // 展开隐藏flag、默认隐藏
    private boolean isShow = false;
    private StaffPhotoAdapter mStaffPhotoAdapter;
    private StaffExperienceAdapter mExperienceAdapter;

    private IStaffPresenter mStaffPresenter;

    private String mStaffId;

    public StaffDataFragment(String staffId) {
        mStaffId = staffId;
    }

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
    protected void initPresenter() {
        mStaffPresenter = PresenterManager.getOurInstance().getStaffPresenter();
        mStaffPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mStaffPresenter.requestStaffData(mStaffId);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void getStaffDetailData(StaffDetail staffDetail) {
        staffName.setText(staffDetail.getName());
        departPost.setText(staffDetail.getJob());
        noteData.setText(staffDetail.getSex() + " · " + staffDetail.getEntryTime()
                + " · " + staffDetail.getState() + " · "
                + ("是".equals(staffDetail.getSocialSecurity()) ? "有社保" : "没有社保") + " · "
                + ("是".equals(staffDetail.getGateKey()) ? "有钥匙" : "没有钥匙"));
        staffTel.setValue(staffDetail.getTel());
        staffQq.setValue(staffDetail.getQq());
        staffId.setValue(staffDetail.getStid());
        staffLoginName.setValue(staffDetail.getLoginName());
        supervisor.setValue(staffDetail.getManager());
        school.setValue(staffDetail.getSchool());
        major.setValue(staffDetail.getSchoolMajor());
        graduationTime.setValue(staffDetail.getSchoolEnd());
        bankNo.setValue(staffDetail.getBankNum());
        socialSecurityNo.setValue(staffDetail.getSocialSecurityNum());
        entryTime.setValue(staffDetail.getEntryTime());
        dingTalkNo.setValue(staffDetail.getDingtalkId());
        staffNote.setValue(staffDetail.getText());
        noneJobExperience.setVisibility(ListUtils.getSize(staffDetail.getJobHistory()) == 0 ? View.VISIBLE : View.GONE);
        mExperienceAdapter.setList(staffDetail.getJobHistory());
    }

    @Override
    public void getStaffPhoto(List<StaffPhoto> photoList) {
        mStaffPhotoAdapter.setList(photoList);
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
        //startActivity(getNewIntent(getContext(), ExperienceDetailActivity.class, "工作经历"));
        PageJumpUtil.staffJobExperience2Detail(getContext(), ExperienceDetailActivity.class, staffExperience.getId());
    }
}
