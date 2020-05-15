package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.ui.adapter.ContactListAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IContactCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/15 14:55
 *
 * @Description : 客户联系人
 * @Usage :
 **/
public final class ContactActivity extends BaseActivity implements IContactCallbacks {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // non content
    @ViewInject(R.id.rl_empty_contact_page)
    private RelativeLayout emptyPage;

    // content
    @ViewInject(R.id.rv_contact_list)
    private RecyclerView contactList;

    private ContactListAdapter mContactListAdapter;

    private IContactPresenter mContactPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contact;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        contactList.setLayoutManager(new GridLayoutManager(this, 1));
        contactList.addItemDecoration(new SpacesItemDecoration(FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mContactListAdapter = new ContactListAdapter();
        // 设置适配器
        contactList.setAdapter(mContactListAdapter);
    }

    @Override
    protected void loadData() {
        // 请求数据
        mContactPresenter.getAllContact();
    }

    @Override
    protected void initPresenter() {
        mContactPresenter = PresenterManager.getOurInstance().getIContactPresenter();
        mContactPresenter.registerViewCallback(this);
    }

    @SingleClick
    @OnClick({R.id.btn_plus, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_plus:
                // 添加联系人
                startActivity(getNewIntent(this, AddContactActivity.class, "添加联系人"));
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getAllContact(Map<String, List> contactData) {
        List<Contact> contactList = contactData.get("contact");
        emptyPage.setVisibility(ListUtils.getSize(contactList) == 0 ? View.VISIBLE : View.GONE);
        // 设置数据
        LogUtils.d("sxs", "联系人数据:" + contactList);
        mContactListAdapter.setList(contactList);
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
        if (mContactPresenter != null) {
            mContactPresenter.unregisterViewCallback(this);
        }
    }
}
