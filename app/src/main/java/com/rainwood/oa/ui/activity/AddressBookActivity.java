package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.rainwood.oa.model.domain.Depart;
import com.rainwood.oa.model.domain.DepartStructure;
import com.rainwood.oa.model.domain.ProjectGroup;
import com.rainwood.oa.model.domain.StaffStructure;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.DepartGroupScreenAdapter;
import com.rainwood.oa.ui.adapter.DepartScreenAdapter;
import com.rainwood.oa.ui.adapter.DepartStructureAdapter;
import com.rainwood.oa.ui.adapter.StaffListAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.PhoneCallUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.utils.TransactionUtil;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.ArrayList;
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

    // 联系人
    private ContactAdapter mContactAdapter;
    // 组织结构
    private DepartStructureAdapter mStructureAdapter;
    private View mMaskLayer;
    private List<Depart> departConditionList = new ArrayList<>();
    private DepartScreenAdapter mDepartScreenAdapter;
    private DepartGroupScreenAdapter mDepartGroupScreenAdapter;
    private RecyclerView mModule;
    private RecyclerView mSecondModule;
    private CommonPopupWindow mStatusPopWindow;
    private TextView mTextClearScreen;
    private TextView mTextConfirm;
    private IMinePresenter mMinePresenter;
    private boolean selectedDepartFlag = false;

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
        //
        initPopDepartDialog();
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
        // 请求部门职位列表
        mMinePresenter.requestAllDepartData();
    }

    @Override
    protected void initEvent() {
        pageRight.setOnItemClick(text -> {
            //toast("部门职位");
            selectedDepartFlag = !selectedDepartFlag;
            pageRight.setRightIcon(selectedDepartFlag ? R.drawable.ic_triangle_up : R.drawable.ic_triangle_down,
                    selectedDepartFlag ? getColor(R.color.colorPrimary) : getColor(R.color.labelColor));
            if (selectedDepartFlag) {
                showPopDepartScreen();
            }
        });
        // 通讯录员工
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
        // 部门员工
        mStructureAdapter.setItemStaffListener(new StaffListAdapter.OnClickItemStaffListener() {
            @Override
            public void onClickItem(StaffStructure structure, int position) {
                toast("选中员工--" + structure.getName());
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
        LinearLayoutManager manager;
        contactView.setLayoutManager(manager = new LinearLayoutManager(this));
        contactView.addItemDecoration(new TitleItemDecoration(this, contactsList));
        contactView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        //使用indexBar
        indexBar.setmPressedShowTextView(sideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(manager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(contactsList);//设置数据源
        mContactAdapter.setDatas(contactsList);
    }

    @Override
    public void getMineAddressBookDepartData(List<DepartStructure> departStructureList) {
          /*
        部门结构员工列表
         */
        mStructureAdapter.setStructureList(departStructureList);
    }

    @Override
    public void getDepartListData(List<Depart> departList) {
        departConditionList.clear();
        departConditionList.addAll(departList);
    }

    private int tempPos = -1;

    /**
     * 初始化所属部门
     */
    private void initPopDepartDialog() {
        mStatusPopWindow = new CommonPopupWindow.Builder(this)
                .setAnimationStyle(R.style.IOSAnimStyle)
                .setView(R.layout.pop_role_screen)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    mModule = view.findViewById(R.id.rv_module);
                    mSecondModule = view.findViewById(R.id.rv_second_module);
                    // 设置布局管理器
                    mModule.setLayoutManager(new GridLayoutManager(AddressBookActivity.this, 1));
                    mModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    mSecondModule.setLayoutManager(new GridLayoutManager(AddressBookActivity.this, 1));
                    mSecondModule.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 10));
                    // 创建适配器
                    mDepartScreenAdapter = new DepartScreenAdapter();
                    mDepartGroupScreenAdapter = new DepartGroupScreenAdapter();
                    // 设置适配器
                    mModule.setAdapter(mDepartScreenAdapter);
                    mSecondModule.setAdapter(mDepartGroupScreenAdapter);
                    mMaskLayer = view.findViewById(R.id.mask_layer);
                    TransactionUtil.setAlphaAllView(mMaskLayer, 0.7f);
                    // 确定、清空
                    mTextClearScreen = view.findViewById(R.id.tv_clear_screen);
                    mTextConfirm = view.findViewById(R.id.tv_confirm);
                })
                .create();
        mTextClearScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("清空筛选");
            }
        });
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempPos = -1;
                toast("确定");
            }
        });
    }

    /**
     * 展示所属部门
     */
    private void showPopDepartScreen() {
        // 设置数据 -- 默认选中第一项
        departConditionList.get(tempPos == -1 ? 0 : tempPos).setHasSelected(true);
        mDepartScreenAdapter.setList(departConditionList);
        mDepartGroupScreenAdapter.setList(departConditionList.get(tempPos == -1 ? 0 : tempPos).getArray());
        mStatusPopWindow.showAsDropDown(pageTop, Gravity.BOTTOM, 0, 0);

        mMaskLayer.setOnClickListener(v -> {
            mStatusPopWindow.dismiss();
            tempPos = -1;
            selectedDepartFlag = false;
            pageRight.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
        });
        mStatusPopWindow.setOnDismissListener(() -> {
            mStatusPopWindow.dismiss();
            if (!mStatusPopWindow.isShowing()) {
                tempPos = -1;
                selectedDepartFlag = false;
                pageRight.setRightIcon(R.drawable.ic_triangle_down, getColor(R.color.labelColor));
            }
        });
        // 点击事件
        mDepartScreenAdapter.setRoleCondition(((condition, position) -> {
            for (Depart departCondition : departConditionList) {
                departCondition.setHasSelected(false);
            }
            condition.setHasSelected(true);
            tempPos = position;
            mDepartGroupScreenAdapter.setList(departConditionList.get(position).getArray());
        }));
        mDepartGroupScreenAdapter.setSecondRoleCondition((secondCondition, position) -> {
            for (ProjectGroup group : departConditionList.get(tempPos == -1 ? 0 : tempPos).getArray()) {
                group.setSelected(false);
            }
            secondCondition.setSelected(true);
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
}
