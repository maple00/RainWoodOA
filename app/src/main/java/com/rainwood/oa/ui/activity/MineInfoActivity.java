package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineInfo;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/6/12 9:17
 * @Desc: 个人资料activity
 */
public final class MineInfoActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.gtt_name)
    private GroupTextText name;
    @ViewInject(R.id.gtt_post)
    private GroupTextText post;
    @ViewInject(R.id.gtt_super_manager)
    private GroupTextText superManager;
    @ViewInject(R.id.gtt_state)
    private GroupTextText state;
    @ViewInject(R.id.gtt_entry_time)
    private GroupTextText entryTime;
    @ViewInject(R.id.gtt_staff_id)
    private GroupTextText staffId;
    @ViewInject(R.id.gtt_tel)
    private GroupTextText tel;
    @ViewInject(R.id.gtt_qq)
    private GroupTextText qq;
    @ViewInject(R.id.gtt_graduation_school)
    private GroupTextText school;
    @ViewInject(R.id.gtt_major)
    private GroupTextText major;
    @ViewInject(R.id.gtt_graduation_date)
    private GroupTextText graduationDate;
    @ViewInject(R.id.gtt_bank_name)
    private GroupTextText bankName;
    @ViewInject(R.id.gtt_bank_num)
    private GroupTextText bankNo;

    private IMinePresenter mMinePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_info;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);

    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mMinePresenter.requestMineInfo();
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
    public void getMineInfo(MineInfo mineInfo) {
        name.setValue(mineInfo.getStaffName());
        post.setValue(mineInfo.getJob());
        superManager.setValue(mineInfo.getManager());
        state.setValue("state".equals(mineInfo.getState()) ? "在职" : "离职");
        entryTime.setValue(mineInfo.getEntryTime());
        staffId.setValue(mineInfo.getStid());
        tel.setValue(mineInfo.getTel());
        qq.setValue(mineInfo.getQq());
        school.setValue(mineInfo.getSchool());
        major.setValue(mineInfo.getSchoolMajor());
        graduationDate.setValue(mineInfo.getSchoolEnd());
        bankName.setValue(mineInfo.getBankName());
        bankNo.setValue(mineInfo.getBankNum());
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
