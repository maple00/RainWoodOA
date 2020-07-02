package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.sqlite.SQLiteHelper;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.oa.ui.dialog.SelectorListDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;
import java.util.Map;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;
import static com.rainwood.oa.utils.Constants.CUSTOM_DEMAND_WRITE_SIZE;

/**
 * @Author: sxs
 * @Time: 2020/5/17 13:13
 * @Desc: 介绍客户
 */
public final class CustomIntroduceActivity extends BaseActivity implements ICustomCallbacks {

    // top action bar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout mPageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView mPageTitle;
    // 必填项样式
    @ViewInject(R.id.tv_company_name)
    private TextView companyNameTV;
    @ViewInject(R.id.tv_contact)
    private TextView contactTV;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumberTV;
    @ViewInject(R.id.tv_demand_desc)
    private TextView demandDescTV;
    @ViewInject(R.id.tv_follow_status)
    private TextView followStatusTV;
    @ViewInject(R.id.tv_custom_origin)
    private TextView customOriginTV;
    @ViewInject(R.id.tv_introduce_to)
    private TextView introduceToTV;
    // content
    @ViewInject(R.id.cet_company_name)
    private EditText companyName;
    @ViewInject(R.id.cet_contact)
    private EditText contact;
    @ViewInject(R.id.cet_tel_number)
    private EditText telNumber;
    @ViewInject(R.id.cet_follow_status)
    private EditText followStatus;
    @ViewInject(R.id.cet_custom_origin_note)
    private EditText customOrigin;
    @ViewInject(R.id.cet_demand_desc)
    private EditText demandDesc;
    @ViewInject(R.id.cet_introduce_to)
    private EditText introduceObj;

    private ICustomPresenter mCustomPresenter;
    private List<String> mFollows;
    private List<String> mOriginList;
    private String mWarnPrompt;
    private String mStaffId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_introduce_custom;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, mPageTop);
        mPageTitle.setText(title);
        setRequiredValue(companyNameTV, "公司名称");
        setRequiredValue(contactTV, "联系人");
        setRequiredValue(telNumberTV, "手机号");
        setRequiredValue(demandDescTV, "需求描述");
        setRequiredValue(followStatusTV, "跟进状态");
        setRequiredValue(customOriginTV, "客户来源");
        setRequiredValue(introduceToTV, "介绍给");
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mCustomPresenter.requestFollowStatus();
        mCustomPresenter.requestCustomOrigin();
        //温馨提示
        mCustomPresenter.requestWarmPrompt();
    }

    @SingleClick
    @OnClick({R.id.iv_warn_prompt, R.id.tv_warm_prompt, R.id.cet_demand_desc, R.id.iv_page_back,
            R.id.cet_follow_status, R.id.cet_custom_origin_note, R.id.cet_introduce_to, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_warn_prompt:
            case R.id.tv_warm_prompt:
                // 温馨提示
                setWarnPrompt(mWarnPrompt);
                break;
            case R.id.cet_demand_desc:
                // 填写需求详情
                startActivityForResult(getNewIntent(this, DemandWriteActivity.class, "需求详情",
                        TextUtils.isEmpty(demandDesc.getText())
                                ? "" : demandDesc.getText().toString().trim()), CUSTOM_DEMAND_WRITE_SIZE);
                break;
            case R.id.cet_follow_status:
                // 选择跟进状态
                if (ListUtils.getSize(mFollows) == 0) {
                    toast("当前没有状态");
                    return;
                }
                setBottomDialog(mFollows, followStatus);
                break;
            case R.id.cet_custom_origin_note:
                // 选择客户来源
                if (ListUtils.getSize(mOriginList) == 0) {
                    toast("当前客户来源无数据");
                    return;
                }
                setBottomDialog(mOriginList, customOrigin);
                break;
            case R.id.cet_introduce_to:
                // toast("介绍客户给....");
                startActivityForResult(getNewIntent(this, ContactsActivity.class, "通讯录", ""),
                        CHOOSE_STAFF_REQUEST_SIZE);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(contact.getText())) {
                    toast("请输入联系人姓名");
                    return;
                }
                if (TextUtils.isEmpty(telNumber.getText())) {
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(followStatus.getText())) {
                    toast("请选择跟进状态");
                    return;
                }
                if (TextUtils.isEmpty(customOrigin.getText())) {
                    toast("请选择客户来源");
                    return;
                }
                if (TextUtils.isEmpty(demandDesc.getText())) {
                    toast("请填写需求");
                    return;
                }
                if (TextUtils.isEmpty(introduceObj.getText())) {
                    toast("请选择被介绍员工");
                    return;
                }
                String companyNameStr = companyName.getText().toString().trim();
                String contactStr = contact.getText().toString().trim();
                String telNumberStr = telNumber.getText().toString().trim();
                String followStateStr = followStatus.getText().toString().trim();
                String originStr = customOrigin.getText().toString().trim();
                String demandDescStr = demandDesc.getText().toString().trim();
                mCustomPresenter.createIntroduceCustom(companyNameStr, contactStr, telNumberStr, demandDescStr,
                        originStr, followStateStr, mStaffId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_DEMAND_WRITE_SIZE && resultCode == CUSTOM_DEMAND_WRITE_SIZE) {
            // 需求
            if (data != null) {
                String demand = data.getStringExtra("demand");
                demandDesc.setText(demand);
            }
        } else if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            // 选择被介绍员工
            if (data != null) {
                String staff = data.getStringExtra("staff");
                mStaffId = data.getStringExtra("staffId");
                introduceObj.setText(staff);
            }
        }
    }

    @Override
    public void getFollowData(List<String> followList) {
        mFollows = followList;
    }

    @Override
    public void getCustomOrigin(List<String> originList) {
        mOriginList = originList;
    }

    /**
     * 选择弹窗
     *
     * @param dataList 数据源
     * @param textView tv
     */
    private void setBottomDialog(List<String> dataList, EditText textView) {
        new SelectorListDialog.Builder(this)
                .setCancel(getString(R.string.common_clear))
                // .setAutoDismiss(false)
                .setList(dataList)
                .setGravity(Gravity.BOTTOM)
                .setListener(new SelectorListDialog.OnListener<String>() {

                    @Override
                    public void onSelected(BaseDialog dialog, int position, String s) {
                        textView.setText(s);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        // dialog.dismiss();
                        textView.setText("");
                    }

                })
                .show();
    }

    @Override
    public void getWarnPrompt(String warnPrompt) {
        // 查询数据库
        String sql = "SELECT module, desc,state FROM rainWoodTips";
        List<Map<String, String>> list = SQLiteHelper.with(this).query(sql);
        LogUtils.d("sxs", "--- 查询数据库-----" + list);
        mWarnPrompt = warnPrompt;
        if ("notLoaded".equals(list.get(0).get("state"))) {
            setWarnPrompt(mWarnPrompt);
            // 第一次弹窗之后设置为已读
            String updateSql = "update rainWoodTips set state= 'loaded' where module = 'customIntroduce'";
            SQLiteHelper.with(this).update(updateSql);
        }
    }

    /**
     * 温馨提示
     *
     * @param warnPrompt
     */
    private void setWarnPrompt(String warnPrompt) {
        new MessageDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage(warnPrompt)
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(null)
                .setAutoDismiss(false)
                .setShowConfirm(false)
                .setAutoDismiss(true)
                .setListener(new MessageDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void getIntroduceCreateData(Map<String, String> createMap) {
        PageJumpUtil.jump2SuccessPage(this, CustomSuccessActivity.class, "提示", createMap);
    }

    @Override
    public void onError(String tips) {
        toast(tips);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
