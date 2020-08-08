package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.contactslibrary.IndexBar.widget.IndexBar;
import com.rainwood.contactslibrary.decoration.DividerItemDecoration;
import com.rainwood.contactslibrary.decoration.TitleItemDecoration;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.ContactsAdapter;
import com.rainwood.oa.ui.adapter.SearchContactAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
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
    @ViewInject(R.id.et_search_tips)
    private EditText searchTips;
    // content
    @ViewInject(R.id.rv_contact)
    private RecyclerView contactsView;
    @ViewInject(R.id.ib_index_bar)
    private IndexBar indexBar;
    @ViewInject(R.id.tv_sideBar_hint)
    private TextView sideBarHint;
    @ViewInject(R.id.fl_contact_container)
    private FrameLayout mFrameLayout;
    @ViewInject(R.id.tv_search)
    private TextView mTextSearch;
    // 搜索联系人
    @ViewInject(R.id.rl_contact_page)
    private RelativeLayout mContactPage;
    @ViewInject(R.id.tv_contact_num)
    private TextView mTextContactNum;
    @ViewInject(R.id.rv_search_contact)
    private RecyclerView searchContactView;

    private IMinePresenter mMinePresenter;
    private ContactsAdapter mContactsAdapter;
    private LinearLayoutManager mManager;
    private SearchContactAdapter mSearchContactAdapter;
    private List<ContactsBean> mContactsList;
    // 搜索框点击控制器 -- 默认不可聚焦
    private boolean clickController = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        searchTips.setHint("输入姓名/部门/职位名称");

        searchContactView.setLayoutManager(new GridLayoutManager(this, 1));
        searchContactView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 20f)));
        // 创建适配器
        mContactsAdapter = new ContactsAdapter();
        mSearchContactAdapter = new SearchContactAdapter();
        // 设置是适配器
        contactsView.setAdapter(mContactsAdapter);
        searchContactView.setAdapter(mSearchContactAdapter);
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
        // 员工选中
        mContactsAdapter.setContactListener((contact, position) -> UIBack(contact));
        // 搜索员工选中
        mSearchContactAdapter.setOnClickItemListener((contacts, position) -> UIBack(contacts));
        // 通讯录搜索
        List<ContactsBean> newContactList = new ArrayList<>();
        searchTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                // 输入了搜索字
                newContactList.clear();
                if (!TextUtils.isEmpty(s)) {
                    for (ContactsBean contactsBean : mContactsList) {
                        if (contactsBean.getName().contains(s.toString())) {
                            newContactList.add(contactsBean);
                        }
                    }
                    mTextContactNum.setVisibility(View.VISIBLE);
                    mTextContactNum.setText("找到" + ListUtils.getSize(newContactList) + "个联系人");
                } else {
                    mTextContactNum.setVisibility(View.GONE);
                }
                mSearchContactAdapter.setContactsList(newContactList, s.toString());
            }
        });
        // 设置聚焦
        if (!clickController) {
            searchTips.setFocusableInTouchMode(false);
            searchTips.setFocusable(false);
        }
    }

    /**
     * 页面返回
     */
    private void UIBack(ContactsBean contacts) {
        Intent intent = new Intent();
        intent.putExtra("staff", contacts.getName());
        intent.putExtra("staffId", contacts.getStid());
        intent.putExtra("position", contacts.getJob());
        intent.putExtra("headPhoto", contacts.getIco());
        setResult(Constants.CHOOSE_STAFF_REQUEST_SIZE, intent);
        finish();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_search, R.id.et_search_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.et_search_tips:
                clickController = true;
                // 设置显示
                mFrameLayout.setVisibility(View.GONE);
                mTextSearch.setVisibility(View.VISIBLE);
                mContactPage.setVisibility(View.VISIBLE);
                // 设置聚焦
                searchTips.setFocusableInTouchMode(clickController);
                searchTips.setFocusable(clickController);
                showSoftInputFromWindow(searchTips);
                break;
            case R.id.tv_search:
                clickController = false;
                mTextSearch.setVisibility(View.GONE);
                mFrameLayout.setVisibility(View.VISIBLE);
                mContactPage.setVisibility(View.GONE);
                searchTips.setText("");
                searchTips.setFocusableInTouchMode(clickController);
                searchTips.setFocusable(clickController);
                hideSoftKeyboard();
                break;
        }
    }

    @Override
    public void getMineAddressBookData(List<ContactsBean> contactsList) {
        mContactsList = contactsList;
        // 设置员工布局管理器
        contactsView.setLayoutManager(mManager = new LinearLayoutManager(this));
        contactsView.addItemDecoration(new TitleItemDecoration(this, contactsList));
        contactsView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST));
        mContactsAdapter.setDatas(mContactsList);
        //使用indexBar
        indexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(contactsList);//设置数据源
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
