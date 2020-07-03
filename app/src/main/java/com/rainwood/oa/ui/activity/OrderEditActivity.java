package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.os.Environment;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Examination;
import com.rainwood.oa.model.domain.FollowRecord;
import com.rainwood.oa.model.domain.Provision;
import com.rainwood.oa.model.domain.TempData;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.ExaminationAdapter;
import com.rainwood.oa.ui.adapter.FollowAdapter;
import com.rainwood.oa.ui.adapter.OrderAttachAdapter;
import com.rainwood.oa.ui.adapter.ProvisionAdapter;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.oa.ui.widget.GroupTextText;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;
import static com.rainwood.oa.utils.Constants.COST_OF_PROVISION;
import static com.rainwood.oa.utils.Constants.CUSTOM_DEMAND_WRITE_SIZE;
import static com.rainwood.oa.utils.Constants.FILE_SELECT_CODE;
import static com.rainwood.oa.utils.Constants.FOLLOW_OF_RECORDS;

/**
 * @Author: a797s
 * @Date: 2020/5/19 9:10
 * @Desc: 订单编辑
 */
public final class OrderEditActivity extends BaseActivity implements OrderAttachAdapter.OnClickWasteClear,
        ProvisionAdapter.OnClickWaste, ExaminationAdapter.OnClickClear,
        IOrderCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
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

    private IOrderPresenter mOrderEditPresenter;

    private OrderAttachAdapter mAttachAdapter;
    private ProvisionAdapter mProvisionAdapter;
    private FollowAdapter mFollowAdapter;
    private ExaminationAdapter mExaminationAdapter;

    private List<Provision> mProvisionList;
    private List<FollowRecord> mFollowRecordList;
    private List<Examination> mExaminationList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_edit;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        hideSoftKeyboard();
        pageTitle.setText(title);
        pageRightTitle.setText("保存");
        /*
         * 创建适配器
         */
        // 附件
        mAttachAdapter = new OrderAttachAdapter();
        // 费用计提
        mProvisionAdapter = new ProvisionAdapter();
        // 跟进记录
        mFollowAdapter = new FollowAdapter();
        // 审批流程
        mExaminationAdapter = new ExaminationAdapter();
        /*
        设置适配器
         */
        contractListView.setAdapter(mAttachAdapter);
        provisionListView.setAdapter(mProvisionAdapter);
        followRecordListView.setAdapter(mFollowAdapter);
        examinationView.setAdapter(mExaminationAdapter);

        mProvisionList = new ArrayList<>();
        mFollowRecordList = new ArrayList<>();
        mExaminationList = new ArrayList<>();

        // TODO: 新建订单的编辑信息
    }

    @Override
    protected void initEvent() {
        mAttachAdapter.setClickWasteClear(this);
        mProvisionAdapter.setOnClickWaste(this);
        mFollowAdapter.setOnClickWaste(new FollowAdapter.OnClickFollowWaste() {
            @Override
            public void onClickFollowWaste(int position) {
                toast("删除跟进记录");
            }
        });
        mExaminationAdapter.setClickClear(this);
    }

    @Override
    protected void initData() {
        TempData orderValues = (TempData) getIntent().getSerializableExtra("orderValues");
        if (orderValues != null) {
            // 创建订单数据
            customName.setText(orderValues.getTempMap().get("customName"));
            orderName.setText(orderValues.getTempMap().get("orderName"));
            contractMoney.setText(orderValues.getTempMap().get("money"));
            note.setText(orderValues.getTempMap().get("note"));
        }
    }

    @Override
    protected void initPresenter() {
        mOrderEditPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderEditPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mOrderEditPresenter.requestAllExaminationData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            // 添加备注信息
            if (requestCode == CUSTOM_DEMAND_WRITE_SIZE && resultCode == CUSTOM_DEMAND_WRITE_SIZE) {
                note.setText(data.getStringExtra("demand"));
            }
            // 添加费用计提
            if (requestCode == COST_OF_PROVISION && resultCode == COST_OF_PROVISION) {
                Provision provision = new Provision();
                String money = data.getStringExtra("money");
                String used = data.getStringExtra("used");
                provision.setMoney(money);
                provision.setUsed(used);
                mProvisionList.add(provision);
                mProvisionAdapter.setList(mProvisionList);
                noneProvision.setVisibility(ListUtils.getSize(mProvisionList) == 0 ? View.VISIBLE : View.GONE);
            }
            // 新增跟进记录
            if (requestCode == FOLLOW_OF_RECORDS && resultCode == FOLLOW_OF_RECORDS) {
                FollowRecord followRecord = new FollowRecord();
                String time = data.getStringExtra("time");
                String content = data.getStringExtra("follow");
                followRecord.setTime(time);
                followRecord.setRecord(content);
                mFollowRecordList.add(followRecord);
                if (ListUtils.getSize(mFollowRecordList) != 0) {
                    noneFollow.setVisibility(View.GONE);
                } else {
                    noneFollow.setVisibility(View.VISIBLE);
                }
                mFollowAdapter.setList(mFollowRecordList);
            }
            // 添加审批人
            if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
                Examination examination = new Examination();
                String staff = data.getStringExtra("staff");
                String staffId = data.getStringExtra("staffId");
                String position = data.getStringExtra("position");
                String headPhoto = data.getStringExtra("headPhoto");
                examination.setName(staff);
                examination.setHeadPhoto(headPhoto);
                examination.setPost(position);
                mExaminationList.add(examination);
                mExaminationAdapter.setList(mExaminationList);
            }
            // 上传附件
            if (requestCode == FILE_SELECT_CODE && resultCode == FILE_SELECT_CODE) {
                //高于API19版本
                String[] split = data.getData().getPath().split("\\:");
                String p = "";
                if (split.length >= 2) {
                    p = Environment.getExternalStorageDirectory() + "/" + split[1];
                    LogUtils.d("sxs --", p);
                    boolean mainthread = Looper.getMainLooper() == Looper.myLooper();
                    LogUtils.d("sxs", "mainthread " + mainthread);
                    // new ReadFileTask().execute(p);
                }
            }
        }
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title, R.id.cet_note, R.id.iv_upload_attach, R.id.tv_upload_attach,
            R.id.iv_provision, R.id.tv_provision, R.id.iv_follow_record, R.id.tv_follow_record,
            R.id.tv_add_attach, R.id.iv_add_attach, R.id.iv_add_follow, R.id.iv_add_provision,
            R.id.iv_add_approver, R.id.tv_add_approver, R.id.tv_add_follow, R.id.tv_add_provision})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                openActivity(OrderListActivity.class);
                finish();
                break;
            case R.id.tv_page_right_title:
                openActivity(OrderListActivity.class);
                toast("保存了");
                finish();
                break;
            case R.id.cet_note:
                startActivityForResult(getNewIntent(this, DemandWriteActivity.class, "备注信息", "备注信息"),
                        CUSTOM_DEMAND_WRITE_SIZE);
                break;
            case R.id.iv_upload_attach:
            case R.id.tv_upload_attach:
            case R.id.tv_add_attach:
            case R.id.iv_add_attach:
                // 上传附件
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择文件"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    LogUtils.d("sxs", "没有找到想要的文件");
                }
                break;
            case R.id.iv_add_provision:
            case R.id.tv_add_provision:
            case R.id.iv_provision:
            case R.id.tv_provision:
                // 添加费用计提
                startActivityForResult(getNewIntent(this, AddProvisionActivity.class, "添加费用计提", "添加费用计提"),
                        COST_OF_PROVISION);
                break;
            case R.id.iv_follow_record:
            case R.id.tv_follow_record:
            case R.id.iv_add_follow:
            case R.id.tv_add_follow:
                // 新增跟进
                startActivityForResult(getNewIntent(this, AddFollowRecordActivity.class, "新增跟进记录", "新增跟进记录"),
                        FOLLOW_OF_RECORDS);
                break;
            case R.id.iv_add_approver:
            case R.id.tv_add_approver:
                // 审批人
                startActivityForResult(getNewIntent(this, ContactsActivity.class, "通讯录", ""),
                        CHOOSE_STAFF_REQUEST_SIZE);
                break;
        }
    }

    @Override
    public void onClickClear(int position) {
        // 订单附件的删除
        toast("删除订单附件");
    }

    @Override
    public void onClickProvisionWaste(int position) {
        // 费用计提删除
        new MessageDialog.Builder(this)
                .setTitle("删除费用计提")
                .setMessage("是否确定删除改计提记录？")
                .setShowConfirm(false)
                .setShowImageClose(false)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        mProvisionList.remove(position);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClickExaminationClear(int position) {
        // 审批流程删除
        mExaminationList.remove(position);
    }

    @SuppressWarnings("all")
    @Override
    public void getAllExaminationData(Map examinationData) {
        // 得到所有的审批人
        List<Examination> examinationList = (List<Examination>) examinationData.get("examination");
        mExaminationList.addAll(examinationList);
        noneApprover.setVisibility(ListUtils.getSize(examinationList) == 0 ? View.VISIBLE : View.GONE);
        LogUtils.d("sxs", "审批数据-----> " + examinationList);
        mExaminationAdapter.setList(examinationList);
    }

    @Override
    public void onError(String tips) {
        toast(tips);
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
