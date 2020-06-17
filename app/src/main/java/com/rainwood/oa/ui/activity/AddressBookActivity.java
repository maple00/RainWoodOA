package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.contactslibrary.ContactAdapter;
import com.rainwood.contactslibrary.ContactsBean;
import com.rainwood.contactslibrary.IndexBar.widget.IndexBar;
import com.rainwood.contactslibrary.decoration.DividerItemDecoration;
import com.rainwood.contactslibrary.decoration.TitleItemDecoration;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.DepartStructureAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.PhoneCallUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/3 15:41
 * @Desc: 通讯录
 */
public final class AddressBookActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
    @ViewInject(R.id.gti_screen)
    private GroupTextIcon pageRight;
    // content
    @ViewInject(R.id.tv_depart)
    private TextView depart;
    @ViewInject(R.id.rv_organization_structure)
    private RecyclerView StructureView;
    @ViewInject(R.id.rv_contact)
    private RecyclerView contactView;
    @ViewInject(R.id.ib_index_bar)
    private IndexBar indexBar;
    @ViewInject(R.id.tv_sideBar_hint)
    private TextView sideBarHint;

    private LinearLayoutManager mManager;

    // 联系人
    private ContactAdapter mContactAdapter;
    // 组织结构
    private DepartStructureAdapter mStructureAdapter;

    private IMinePresenter mMinePresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        pageRight.setText("部门职位");
        searchTips.setText("输入关键词");
        // 设置部门布局管理器
        StructureView.setLayoutManager(new GridLayoutManager(this, 1));
        StructureView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 30f)));
        // 创建适配器
        mStructureAdapter = new DepartStructureAdapter();
        mContactAdapter = new ContactAdapter();
        // 设置适配器
        contactView.setAdapter(mContactAdapter);
        StructureView.setAdapter(mStructureAdapter);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求通讯录数据
        mMinePresenter.requestAddressBookData();
    }

    @Override
    protected void initEvent() {
        pageRight.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("部门职位");
            }
        });
        // 选中员工
        mContactAdapter.setContactListener(new ContactAdapter.OnClickContactListener() {
            @Override
            public void onClickItem(ContactsBean contact, int position) {
                toast("选中员工：" + contact.getName());
            }

            @Override
            public void onClickTel(ContactsBean contact, int position) {
                //toast("拨打" + contact.getName() + "的电话---- " + contact.getTel());
                PhoneCallUtil.callPhoneDump(getActivity(), contact.getTel());
            }

            @Override
            public void onClickQq(ContactsBean contact, int position) {
                ClipboardUtil.clipFormat2Board(getActivity(), contact.getName() + "qq", contact.getQq());
                toast("已复制" + contact.getName() + "的QQ：" + contact.getQq());
            }
        });
    }

    @SingleClick
    @OnClick({R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getMineAddressBookData(List<ContactsBean> contactsList) {
        /*
        员工列表
         */
        // 设置员工布局管理器
        contactView.setLayoutManager(mManager = new LinearLayoutManager(this));
        contactView.addItemDecoration(new TitleItemDecoration(this, contactsList));
        contactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        indexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(contactsList);//设置数据源
        mContactAdapter.setDatas(contactsList);
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
