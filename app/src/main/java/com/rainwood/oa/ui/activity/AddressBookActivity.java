package com.rainwood.oa.ui.activity;

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
import com.rainwood.oa.model.domain.DepartStructure;
import com.rainwood.oa.presenter.IContactPresenter;
import com.rainwood.oa.ui.adapter.DepartStructureAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IContactCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/3 15:41
 * @Desc: 通讯录
 */
public final class AddressBookActivity extends BaseActivity implements IContactCallbacks {

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
    private TitleItemDecoration mDecoration;
    private List<ContactsBean> mDatas;

    // 联系人
    private ContactAdapter mContactAdapter;
    // 组织结构
    private DepartStructureAdapter mStructureAdapter;

    private IContactPresenter mContactPresenter;

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
        // 设置布局管理器
        contactView.setLayoutManager(mManager = new LinearLayoutManager(this));
        // 设置数据
        initDatas(getResources().getStringArray(R.array.provinces));
        contactView.addItemDecoration(mDecoration = new TitleItemDecoration(this, mDatas));
        contactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        StructureView.setLayoutManager(new GridLayoutManager(this, 1));
        StructureView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, FontSwitchUtil.dip2px(this, 30f)));
        // 创建适配器
        mStructureAdapter = new DepartStructureAdapter();
        mContactAdapter = new ContactAdapter();
        // 设置适配器
        contactView.setAdapter(mContactAdapter);
        StructureView.setAdapter(mStructureAdapter);
        // 设置数据
        mContactAdapter.setDatas(mDatas);
        //使用indexBar
        indexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(true)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(mDatas);//设置数据源
    }

    /**
     * 组织数据源
     *
     * @param data
     * @return
     */
    private void initDatas(String[] data) {
        mDatas = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            ContactsBean contactsBean = new ContactsBean();
            contactsBean.setContact(data[i]);//设置城市名称
            mDatas.add(contactsBean);
        }
    }

    @Override
    protected void initPresenter() {
        mContactPresenter = PresenterManager.getOurInstance().getIContactPresenter();
        mContactPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mContactPresenter.requestAddressBookData();
    }

    @SuppressWarnings("all")
    @Override
    public void getAddressBookData(Map addressBookData) {
        List<DepartStructure> structureList = (List<DepartStructure>) addressBookData.get("structure");
        mStructureAdapter.setStructureList(structureList);
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
