package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Attachment;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.AttachAdapter;
import com.rainwood.oa.ui.adapter.ContactListAdapter;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PhoneCallUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 14:55
 *
 * @Description : 共用module
 * @Usage : 联系人列表、客户附件
 **/
public final class CommonActivity extends BaseActivity implements ICustomCallbacks, IAttachmentCallbacks {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // non content
    @ViewInject(R.id.rl_empty_contact_page)
    private RelativeLayout emptyPage;
    @ViewInject(R.id.tv_tips)
    private TextView emptyTips;
    // content
    @ViewInject(R.id.rl_common_content)
    private RelativeLayout commonContent;
    @ViewInject(R.id.rv_common_list)
    private RecyclerView commonRView;
    @ViewInject(R.id.tv_selected_num)
    private TextView selectedNum;
    @ViewInject(R.id.cb_checked)
    private CheckBox selectedAllCB;
    @ViewInject(R.id.rl_bottom_selected_all)
    private RelativeLayout selectedAllRL;

    // 联系人adapter
    private ContactListAdapter mContactListAdapter;
    private ICustomPresenter mCustomPresenter;
    private List<Contact> mContactList;
    private boolean selectedAll = false;        // 设置全选，默认不全选
    // 附件adapter
    private AttachAdapter mAttachAdapter;
    private IAttachmentPresenter mAttachmentPresenter;
    private List<Attachment> mAttachmentList;
    private String mCustomId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_common_page;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setText("管理");
        selectedNum.setText("全选(0)");
        // 设置布局管理器
        commonRView.setLayoutManager(new GridLayoutManager(this, 1));
        commonRView.addItemDecoration(new SpacesItemDecoration(FontSwitchUtil.dip2px(this, 13f)));
        // 创建适配器
        mContactListAdapter = new ContactListAdapter();
        mAttachAdapter = new AttachAdapter();
        switch (title) {
            case "联系人":
                emptyTips.setText("还没有联系人");
                // 设置适配器
                commonRView.setAdapter(mContactListAdapter);
                break;
            case "客户附件":
                emptyTips.setText("还没有客户附件");
                commonContent.setBackgroundColor(getColor(R.color.white));
                // 设置适配器
                commonRView.setAdapter(mAttachAdapter);
                break;
        }
    }

    @Override
    protected void initData() {
        switch (title){
            case "联系人":
                mCustomId = getIntent().getStringExtra("customId");
                break;
            case "客户附件":
                mCustomId = Constants.CUSTOM_ID;
                break;
        }
    }

    @Override
    protected void loadData() {
        // 请求数据
        switch (title) {
            case "联系人":
                mCustomPresenter.requestContactListByCustomId(mCustomId);
                break;
            case "客户附件":
                mAttachmentPresenter.requestCustomAttachData(mCustomId);
                break;
        }
    }

    @Override
    protected void initPresenter() {
        // 联系人
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
        // 客户附件
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @SingleClick
    @OnClick({R.id.btn_plus, R.id.iv_page_back, R.id.iv_menu, R.id.tv_page_right_title, R.id.iv_plus,
            R.id.cb_checked, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_plus:
            case R.id.iv_plus:
                switch (title) {
                    case "联系人":
                        // 添加联系人
                        startActivity(getNewIntent(this, AddContactActivity.class, "添加联系人"));
                        break;
                    case "客户附件":
                        toast("添加客户附件");
                        break;
                }
                break;
            case R.id.iv_menu:
                toast("菜单快捷方式");
                break;
            case R.id.tv_page_right_title:
                // 管理
                selectedAll = !selectedAll;
                pageRightTitle.setText(selectedAll ? "完成" : "管理");
                selectedAllRL.setVisibility(selectedAll ? View.VISIBLE : View.GONE);
                switch (title) {
                    case "联系人":
                        for (Contact contact : mContactList) {
                            contact.setSelected(false);
                        }
                        mContactListAdapter.setList(mContactList);
                        mContactListAdapter.setCheckShow(selectedAll);
                        //mContactListAdapter.notifyDataSetChanged();
                        break;
                    case "客户附件":
                        for (Attachment attachment : mAttachmentList) {
                            attachment.setSelected(false);
                        }
                        mAttachAdapter.setList(mAttachmentList);
                        mAttachAdapter.setCheckBoxShow(selectedAll);
                        mAttachAdapter.notifyDataSetChanged();
                        break;
                }
                break;
            case R.id.cb_checked:
                // 全选
                switch (title) {
                    case "联系人":
                        boolean contactChecked = selectedAllCB.isChecked();
                        for (Contact contact : mContactList) {
                            contact.setSelected(contactChecked);
                        }
                        mContactListAdapter.setList(mContactList);
                        mContactListAdapter.setCheckShow(true);
                        mContactListAdapter.notifyDataSetChanged();
                        selectedNum.setText("全选(" + (contactChecked ? ListUtils.getSize(mContactList) : 0) + ")");
                        break;
                    case "客户附件":
                        boolean attachChecked = selectedAllCB.isChecked();
                        for (Attachment attachment : mAttachmentList) {
                            attachment.setSelected(attachChecked);
                        }
                        mAttachAdapter.setList(mAttachmentList);
                        mAttachAdapter.setCheckBoxShow(true);
                        mAttachAdapter.notifyDataSetChanged();
                        selectedNum.setText("全选(" + (attachChecked ? ListUtils.getSize(mAttachmentList) : 0) + ")");
                        break;
                }
                break;
            case R.id.btn_delete:
                // 删除选中
                toast("删除");
                break;
        }
    }

    /**
     * 获取所有的联系人
     *
     * @param contactList 联系人列表
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void getCustomContactList(List<Contact> contactList) {
        mContactList = contactList;
        // 空页面
        emptyPage.setVisibility(ListUtils.getSize(contactList) == 0 ? View.VISIBLE : View.GONE);
        pageRightTitle.setVisibility(ListUtils.getSize(contactList) != 0 ? View.VISIBLE : View.GONE);
        // 设置数据
        //LogUtils.d("sxs", "联系人数据:" + contactList);
        mContactListAdapter.setList(contactList);
        // 选中回调
        mContactListAdapter.setOnClickSelected(new ContactListAdapter.OnClickSelected() {
            @Override
            public void onSelectedSwitch(boolean status, int position, int selectedCount) {
                if (!status) {
                    // 如果有未选中的状态，则更改全选状态
                    selectedAllCB.setChecked(false);
                }
                selectedNum.setText("全选(" + (selectedAllCB.isChecked()
                        ? selectedCount : selectedCount - ListUtils.getSize(contactList)) + ")");
            }

            @Override
            public void copyWxNum2Board(String content) {
                ClipboardUtil.clipFormat2Board(CommonActivity.this, "wxNum", content);
                toast("已复制");
            }

            @Override
            public void copyQQNum2Board(String content) {
                ClipboardUtil.clipFormat2Board(CommonActivity.this, "QqNum", content);
                toast("已复制");
            }

            @Override
            public void playPhone(String tel) {
                PhoneCallUtil.callPhoneDump(CommonActivity.this, tel);
            }

            @Override
            public void editContactData(Contact contact) {
                // 编辑 AddContactActivity
                PageJumpUtil.contact2Edit(CommonActivity.this, AddContactActivity.class, contact, mCustomId);
            }
        });
    }

    /**
     * 客户附件列表
     * @param attachmentList 附件列表
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void getCustomAttachments(List<Attachment> attachmentList) {
        mAttachmentList = attachmentList;
        emptyPage.setVisibility(ListUtils.getSize(attachmentList) == 0 ? View.VISIBLE : View.GONE);
        pageRightTitle.setVisibility(ListUtils.getSize(attachmentList) != 0 ? View.VISIBLE : View.GONE);
        LogUtils.d("sxs", "所有客户附件========= " + attachmentList);
        // 设置适配器数据源
        mAttachAdapter.setList(attachmentList);
        mAttachAdapter.setClickCheckBox(new AttachAdapter.OnClickCheckBox() {
            @Override
            public void onSelectedSwitch(boolean status, int position, int selectedCount) {
                if (!status) {
                    // 如果有未选中的状态，则更改全选状态
                    selectedAllCB.setChecked(false);
                }
                selectedNum.setText("全选(" + (selectedAllCB.isChecked()
                        ? selectedCount : selectedCount - ListUtils.getSize(attachmentList)) + ")");
            }

            @Override
            public void fileDownload(Attachment attachment, int position) {

                FileManagerUtil.fileDownload(CommonActivity.this, TbsActivity.class, attachment.getSrc(),
                        attachment.getName(), attachment.getFormat());
            }

            @Override
            public void filePreview(Attachment attachment, int position) {
                FileManagerUtil.filePreview(CommonActivity.this, TbsActivity.class, attachment.getSrc(),
                        attachment.getName(), attachment.getFormat());
            }
        });
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
    protected void release() {
        if (mCustomPresenter != null) {
            mCustomPresenter.unregisterViewCallback(this);
        }
    }

}
