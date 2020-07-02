package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CustomDemand;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.dialog.SelectorListDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

import static com.rainwood.oa.utils.Constants.CUSTOM_DEMAND_WRITE_SIZE;

/**
 * create by a797s in 2020/5/14 11:38
 *
 * @Description : 新建客户activity
 * @Usage :
 **/
public final class CustomNewActivity extends BaseActivity implements ICustomCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // 必填信息
    @ViewInject(R.id.tv_contact)
    private TextView contactTV;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumber;
    @ViewInject(R.id.tv_follow_status)
    private TextView followStatus;
    @ViewInject(R.id.tv_demand_detail)
    private TextView demandDetail;
    @ViewInject(R.id.tv_company_name)
    private TextView companyNameTV;
    // 填写内容
    @ViewInject(R.id.cet_contact)
    private EditText contactCET;
    @ViewInject(R.id.cet_tel_number)
    private EditText telNumberCET;
    @ViewInject(R.id.cet_wxNumber)
    private EditText wxNumber;
    @ViewInject(R.id.cet_qqNumber)
    private EditText qqNumber;
    @ViewInject(R.id.cet_post)
    private EditText post;
    @ViewInject(R.id.tv_follow_status_content)
    private TextView followContent;
    @ViewInject(R.id.tv_custom_origin)
    private TextView customOrigin;
    @ViewInject(R.id.cet_custom_origin_note)
    private EditText customOriginNote;
    @ViewInject(R.id.cet_budget)
    private EditText budget;
    @ViewInject(R.id.cet_industry)
    private EditText industry;
    @ViewInject(R.id.tv_demand_detail_content)
    private TextView demandContent;
    @ViewInject(R.id.cet_company_name)
    private EditText companyName;
    @ViewInject(R.id.cet_tax_num)
    private EditText taxNum;
    @ViewInject(R.id.cet_mailing_address)
    private EditText mailingAddress;
    @ViewInject(R.id.cet_billing_address)
    private EditText billingAddress;
    @ViewInject(R.id.cet_special_plane)
    private EditText specialPlane;
    @ViewInject(R.id.cet_opening_bank)
    private EditText openingBank;
    @ViewInject(R.id.cet_brought_account)
    private EditText broughtAccount;

    private ICustomPresenter mCustomPresenter;

    private List<String> mFollows;
    private List<String> mOriginList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_new;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageTitle.setText(title);
        setRequiredValue(contactTV, "联系人");
        setRequiredValue(telNumber, "手机号");
        setRequiredValue(followStatus, "跟进状态");
        setRequiredValue(demandDetail, "需求详情");
        setRequiredValue(companyNameTV, "公司名称");
    }

    @Override
    protected void initData() {
        CustomDemand customDemand = (CustomDemand) getIntent().getSerializableExtra("demand");
        if (customDemand != null) {
            followStatus.setText(customDemand.getWorkFlow());
            customOrigin.setText(customDemand.getSource());
            budget.setText(customDemand.getBudget());
            industry.setText(customDemand.getIndustry());
            demandContent.setText(customDemand.getText());
        }
    }

    @Override
    protected void initEvent() {
        // 客户来源备注
        customOrigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customOriginNote.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void loadData() {
        // 从这里加载数据
        mCustomPresenter.requestFollowStatus();
        mCustomPresenter.requestCustomOrigin();
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_follow_status_content, R.id.btn_confirm,
            R.id.tv_custom_origin, R.id.tv_demand_detail_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_follow_status_content:
                // 选择跟进状态
                if (ListUtils.getSize(mFollows) == 0) {
                    toast("当前没有状态");
                    return;
                }
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(mFollows)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                followContent.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                // dialog.dismiss();
                                followContent.setText("");
                            }

                        })
                        .show();
                break;
            case R.id.tv_custom_origin:
                // 选择客户来源
                if (ListUtils.getSize(mOriginList) == 0) {
                    toast("当前客户来源无数据");
                    return;
                }
                new SelectorListDialog.Builder(this)
                        .setCancel(getString(R.string.common_clear))
                        // .setAutoDismiss(false)
                        .setList(mOriginList)
                        .setGravity(Gravity.BOTTOM)
                        .setListener(new SelectorListDialog.OnListener<String>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, String s) {
                                customOrigin.setText(s);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                // dialog.dismiss();
                                customOrigin.setText("");
                            }

                        })
                        .show();
                break;
            case R.id.tv_demand_detail_content:
                // 填写需求详情
                startActivityForResult(getNewIntent(this, DemandWriteActivity.class, "需求详情", "需求详情"),
                        CUSTOM_DEMAND_WRITE_SIZE);
                break;
            case R.id.btn_confirm:
                //确认新建
                if (TextUtils.isEmpty(contactCET.getText())) {
                    toast("请输入联系人姓名");
                    return;
                }
                if (TextUtils.isEmpty(telNumberCET.getText())) {
                    toast("请输入联系人手机号");
                    return;
                }
                if (TextUtils.isEmpty(followContent.getText())) {
                    toast("请选择跟进状态");
                    return;
                }
                if (TextUtils.isEmpty(customOrigin.getText())) {
                    toast("请选择客户来源");
                    return;
                }
                if (TextUtils.isEmpty(demandContent.getText())) {
                    toast("请填写需求");
                    return;
                }
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请输入公司名称");
                    return;
                }
                String contact = contactCET.getText().toString().trim();
                String tel = telNumberCET.getText().toString().trim();
                String wxNum = wxNumber.getText().toString().trim();
                String qqNum = qqNumber.getText().toString().trim();
                String postStr = post.getText().toString().trim();
                String followStr = followContent.getText().toString().trim();
                String customStr = customOrigin.getText().toString().trim();
                String customNote = customOriginNote.getText().toString().trim();
                String budgetStr = budget.getText().toString().trim();
                String industryStr = industry.getText().toString().trim();
                String demandStr = demandContent.getText().toString().trim();
                String companyNameStr = companyName.getText().toString().trim();
                String taxNumStr = taxNum.getText().toString().trim();
                String mailingAddressStr = mailingAddress.getText().toString().trim();
                String billingAddressStr = billingAddress.getText().toString().trim();
                String specialPlaneStr = specialPlane.getText().toString().trim();
                String openingBankStr = openingBank.getText().toString().trim();
                String broughtAccountStr = broughtAccount.getText().toString().trim();
                mCustomPresenter.createCustomData(contact, tel, wxNum, qqNum, postStr, followStr, customStr,
                        customNote, budgetStr, industryStr, demandStr, companyNameStr, taxNumStr,
                        mailingAddressStr, billingAddressStr, specialPlaneStr, openingBankStr,
                        broughtAccountStr);
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_DEMAND_WRITE_SIZE && resultCode == CUSTOM_DEMAND_WRITE_SIZE) {
            String demand = data.getStringExtra("demand");
            demandContent.setText(demand);
        }
    }

    /*
     * 网络请求result
     */
    @Override
    public void getFollowData(List<String> followList) {
        mFollows = followList;
    }

    @Override
    public void getCustomOrigin(List<String> originList) {
        mOriginList = originList;
    }

    @Override
    public void createCustomData(boolean isSuccess, String warn) {
        if (isSuccess) {
            startActivity(getNewIntent(this, CustomDetailActivity.class, "客户详情", "客户详情"));
        } else {
            toast(warn);
        }
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
