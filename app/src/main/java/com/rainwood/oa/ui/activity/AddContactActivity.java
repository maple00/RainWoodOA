package com.rainwood.oa.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * create by a797s in 2020/5/15 16:41
 *
 * @Description : 添加联系人
 * @Usage :
 **/
public final class AddContactActivity extends BaseActivity implements ICustomCallbacks {

    @ViewInject(R.id.ll_parent_pager)
    private LinearLayout parentPager;
    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_menu_title)
    private TextView pageTitle;
    // 必填项
    @ViewInject(R.id.tv_post)
    private TextView post;
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.tv_tel_number)
    private TextView telNumber;
    // content
    @ViewInject(R.id.cet_post)
    private EditText postContent;
    @ViewInject(R.id.cet_name)
    private EditText nameContent;
    @ViewInject(R.id.cet_tel_number)
    private EditText tel;
    @ViewInject(R.id.cet_special_plane)
    private EditText lineTel;
    @ViewInject(R.id.cet_qqNumber)
    private EditText qqNum;
    @ViewInject(R.id.cet_wxNumber)
    private EditText wxNum;
    @ViewInject(R.id.cet_note)
    private EditText note;

    private Contact mContact;
    private String mCustomId;
    private ICustomPresenter mCustomPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_append_contact;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);

        setRequiredValue(post, "职位");
        setRequiredValue(name, "姓名");
        setRequiredValue(telNumber, "手机号");
    }

    @Override
    protected void initData() {
        mContact = (Contact) getIntent().getSerializableExtra("contact");
        mCustomId = getIntent().getStringExtra("customId");
        if (mContact != null) {
            LogUtils.d("sxs", "编辑联系人....");
            postContent.setText(mContact.getPosition());
            nameContent.setText(mContact.getName());
            tel.setText(mContact.getTel());
            lineTel.setText(mContact.getPhone());
            qqNum.setText(mContact.getQq());
            wxNum.setText(mContact.getWeChat());
            note.setText(mContact.getText());
        } else {
            LogUtils.d("sxs", "新建联系人....");
        }
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }


    @OnClick({R.id.btn_confirm, R.id.iv_page_back, R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.iv_menu:
                showQuickFunction(this, parentPager);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(postContent.getText())) {
                    toast("请输入职位");
                    return;
                }
                if (TextUtils.isEmpty(nameContent.getText())) {
                    toast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(tel.getText())) {
                    toast("请输入手机号");
                    return;
                }
                String position = postContent.getText().toString().trim();
                String name = nameContent.getText().toString().trim();
                String tel = this.tel.getText().toString().trim();
                String lineTel = this.lineTel.getText().toString().trim();
                String qqNumber = qqNum.getText().toString().trim();
                String wxNumber = wxNum.getText().toString().trim();
                String noteStr = note.getText().toString().trim();
                String contactId;
                if (mContact == null) {  // 说明是新建
                    contactId = RandomUtil.getItemID(20);
                } else {
                    contactId = mContact.getId();
                }
                mCustomPresenter.requestCreateContact(mCustomId, contactId, position, name, tel, lineTel, qqNumber,
                        wxNumber, noteStr);
                break;
        }
    }

    @Override
    public void createContactData(boolean success) {
        toast(success ? "提交成功" : "提交失败");
        postDelayed(this::finish, 500);
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
