package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.ExaminationAdapter;
import com.rainwood.oa.ui.adapter.FollowAdapter;
import com.rainwood.oa.ui.adapter.OrderAttachAdapter;
import com.rainwood.oa.ui.adapter.ProvisionAdapter;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/19 9:10
 * @Desc: 订单编辑
 */
public final class OrderActivity extends BaseActivity implements OrderAttachAdapter.OnClickWasteClear,
        ProvisionAdapter.OnClickWaste, FollowAdapter.OnClickFollowWaste, ExaminationAdapter.OnClickClear,
        IOrderCallbacks {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.cet_custom_name)
    private EditText customName;
    @ViewInject(R.id.cet_order_name)
    private EditText orderName;
    @ViewInject(R.id.cet_contract_money)
    private EditText contractMoney;
    @ViewInject(R.id.cet_note)
    private EditText note;
    @ViewInject(R.id.cet_order_desc)
    private EditText orderDesc;
    // ListView
    @ViewInject(R.id.mlv_contact_attach)
    private MeasureListView contractListView;
    @ViewInject(R.id.mlv_follow_record)
    private MeasureListView followRecordListView;
    @ViewInject(R.id.mlv_provision_list)
    private MeasureListView provisionListView;
    @ViewInject(R.id.mlv_examination)
    private MeasureListView examinationView;
    // 自定义组合控件
    @ViewInject(R.id.gtt_order_no)
    private GroupTextText orderNo;
    @ViewInject(R.id.gtt_create_time)
    private GroupTextText createTime;
    @ViewInject(R.id.gtt_pig_order_no)
    private GroupTextText pigOrderNo;
    @ViewInject(R.id.gtt_contract_money)
    private GroupTextText contractMoneyBottom;
    @ViewInject(R.id.gtt_provision_money)
    private GroupTextText provisionMoney;
    @ViewInject(R.id.gtt_contract_worth)
    private GroupTextText contractWorth;
    @ViewInject(R.id.gtt_contract_receivable)
    private GroupTextText contractReceivable;
    @ViewInject(R.id.gtt_remain_value)
    private GroupTextText remainValue;

    // tips
    @ViewInject(R.id.tv_none_contract_attach)
    private TextView noneAttach;
    @ViewInject(R.id.tv_none_follow)
    private TextView noneFollow;
    @ViewInject(R.id.tv_none_provision)
    private TextView noneProvision;
    @ViewInject(R.id.tv_none_approver)
    private TextView noneApprover;

    private OrderAttachAdapter mAttachAdapter;
    private ProvisionAdapter mProvisionAdapter;
    private FollowAdapter mFollowAdapter;
    private ExaminationAdapter mExaminationAdapter;

    private IOrderPresenter mOrderEditPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_edit;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setText("保存");
        /*
         * 创建适配器
         */
        // 附件
        mAttachAdapter = new OrderAttachAdapter();
        mProvisionAdapter = new ProvisionAdapter();
        mFollowAdapter = new FollowAdapter();
        mExaminationAdapter = new ExaminationAdapter();
        /*
        设置适配器
         */
        contractListView.setAdapter(mAttachAdapter);
        provisionListView.setAdapter(mProvisionAdapter);
        followRecordListView.setAdapter(mFollowAdapter);
        examinationView.setAdapter(mExaminationAdapter);
    }

    @Override
    protected void initEvent() {
        mAttachAdapter.setClickWasteClear(this);
        mProvisionAdapter.setOnClickWaste(this);
        mFollowAdapter.setOnClickWaste(this);
        mExaminationAdapter.setClickClear(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mOrderEditPresenter.requestAllExaminationData();
    }

    @Override
    protected void initPresenter() {
        mOrderEditPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderEditPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.tv_page_right_title, R.id.cet_note, R.id.iv_upload_attach, R.id.tv_upload_attach,
            R.id.iv_provision, R.id.tv_provision, R.id.iv_follow_record, R.id.tv_follow_record,
            R.id.tv_add_attach, R.id.iv_add_attach, R.id.iv_add_follow, R.id.iv_add_provision,
            R.id.iv_add_approver, R.id.tv_add_approver, R.id.tv_add_follow, R.id.tv_add_provision})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_page_right_title:
                // 保存
                toast("保存了");
                finish();
                break;
            case R.id.cet_note:
                startActivity(getNewIntent(this, DemandWriteActivity.class, "备注信息"));
                break;
            case R.id.iv_upload_attach:
            case R.id.tv_upload_attach:
            case R.id.tv_add_attach:
            case R.id.iv_add_attach:
                // 上传附件
                toast("上传附件");
                break;
            case R.id.iv_add_provision:
            case R.id.tv_add_provision:
            case R.id.iv_provision:
            case R.id.tv_provision:
                // 添加费用计提
                startActivity(getNewIntent(this, AddProvisionActivity.class, "添加费用计提"));
                break;
            case R.id.iv_follow_record:
            case R.id.tv_follow_record:
            case R.id.iv_add_follow:
            case R.id.tv_add_follow:
                // 新增跟进
                startActivity(getNewIntent(this, AddFollowRecordActivity.class, "新增跟进记录"));
                break;
            case R.id.iv_add_approver:
            case R.id.tv_add_approver:
                // 审批人
                toast("添加审批人");
                break;
        }
    }

    @Override
    public void onClickClear(int position) {
        // 订单附件的删除
    }

    @Override
    public void onClickProvisionWaste(int position) {
        // 费用计提删除
    }

    @Override
    public void onClickFollowWaste(int position) {
        // 跟进记录删除

    }

    @Override
    public void onClickExaminationClear(int position) {
        // 审批流程删除

    }

    @SuppressWarnings("all")
    @Override
    public void getAllExaminationData(Map examinationData) {
        // 得到所有的审批人
        List<Examination> examinationList = (List<Examination>) examinationData.get("examination");
        noneApprover.setVisibility(ListUtils.getSize(examinationList) == 0 ? View.VISIBLE : View.GONE);
        LogUtils.d("sxs", "审批数据-----> " + examinationList);
        mExaminationAdapter.setList(examinationList);
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
