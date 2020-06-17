package com.rainwood.oa.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.contactslibrary.IndexBar.widget.IndexBar;
import com.rainwood.contactslibrary.decoration.DividerItemDecoration;
import com.rainwood.contactslibrary.decoration.TitleItemDecoration;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.ContactsAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 11:55
 * @Desc: 联系人 activity
 */
public final class ContactsActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
    // content
    @ViewInject(R.id.rv_contact)
    private RecyclerView contactsView;
    @ViewInject(R.id.ib_index_bar)
    private IndexBar indexBar;
    @ViewInject(R.id.tv_sideBar_hint)
    private TextView sideBarHint;

    private IMinePresenter mMinePresenter;
    private ContactsAdapter mContactsAdapter;
    private LinearLayoutManager mManager;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        searchTips.setHint("输入姓名/部门/职位名称");
        // 创建适配器
        mContactsAdapter = new ContactsAdapter();
        // 设置是适配器
        contactsView.setAdapter(mContactsAdapter);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mMinePresenter.requestAddressBookData();
    }

    @Override
    protected void initEvent() {
        mContactsAdapter.setContactListener((contact, position) -> {
            Intent intent = new Intent();
            intent.putExtra("staff", contact.getName());
            setResult(Constants.CHOOSE_STAFF_REQUEST_SIZE, intent);
            finish();
        });
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
    public void getMineAddressBookData(List<ContactsBean> contactsList) {
        // 设置员工布局管理器
        contactsView.setLayoutManager(mManager = new LinearLayoutManager(this));
        contactsView.addItemDecoration(new TitleItemDecoration(this, contactsList));
        contactsView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        indexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(contactsList);//设置数据源
        mContactsAdapter.setDatas(contactsList);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
