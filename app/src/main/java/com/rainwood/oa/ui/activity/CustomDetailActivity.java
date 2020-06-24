package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CustomDetail;
import com.rainwood.oa.model.domain.CustomStaff;
import com.rainwood.oa.model.domain.CustomValues;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.AssociatesAdapter;
import com.rainwood.oa.ui.adapter.ContactAdapter;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.dialog.BottomCustomDialog;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.oa.utils.ClipboardUtil;
import com.rainwood.oa.utils.Constants;
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

/**
 * create by a797s in 2020/5/14 17:58
 *
 * @Description : &#x5ba2;&#x6237;&#x8be6;&#x60c5;
 * @Usage :
 **/
public final class CustomDetailActivity extends BaseActivity implements ICustomCallbacks {

    // actionBar
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.ll_order_detail_parent)
    public LinearLayout orderDetailParent;
    @ViewInject(R.id.iv_company_logo)
    private ImageView logoSrc;
    @ViewInject(R.id.tv_company_name)
    private TextView companyName;
    @ViewInject(R.id.mgv_custom_detail_module)
    private GridView customDetailModule;
    @ViewInject(R.id.iv_employees)
    private ImageView employeeHeadSrc;
    @ViewInject(R.id.tv_employees_name)
    private TextView employeeName;
    @ViewInject(R.id.tv_depart)
    private TextView depart;
    // 公客 start
    @ViewInject(R.id.iv_princess)
    private ImageView princess;
    @ViewInject(R.id.tv_princess_name)
    private TextView princessName;
    // 公客 end
    @ViewInject(R.id.iv_references)
    private ImageView referHeadSrc;
    @ViewInject(R.id.tv_references_name)
    private TextView referName;
    @ViewInject(R.id.tv_post)
    private TextView referPost;
    @ViewInject(R.id.tv_none_associates)
    private TextView noneAssociates;
    @ViewInject(R.id.mlv_associates_list)
    private ListView associatesMLV;
    @ViewInject(R.id.tv_none_contact)
    private TextView noneContact;
    @ViewInject(R.id.mlv_contact_list)
    private ListView contactMLV;
    // 客户需求
    @ViewInject(R.id.tv_follow_status)
    private TextView followStatus;
    @ViewInject(R.id.tv_custom_origin)
    private TextView customOrigin;
    @ViewInject(R.id.tv_item_budget)
    private TextView itemBudget;
    @ViewInject(R.id.tv_industry)
    private TextView industry;
    @ViewInject(R.id.tv_demand_detail)
    private TextView demand_detail;
    // 客户ID
    @ViewInject(R.id.tv_create_time)
    private TextView createTime;
    @ViewInject(R.id.tv_custom_id)
    private TextView customId;

    private ICustomPresenter mCustomPresenter;

    private ItemModuleAdapter mModuleAdapter;
    private ContactAdapter mContactAdapter;
    private AssociatesAdapter mAssociatesAdapter;

    // customId
    private String mCustomId;
    // 是转让还是添加协作人 -- 默认是转让
    private boolean plusFlag = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 创建适配器
        mModuleAdapter = new ItemModuleAdapter();
        mAssociatesAdapter = new AssociatesAdapter();
        mContactAdapter = new ContactAdapter();
        // 设置适配器
        customDetailModule.setAdapter(mModuleAdapter);
        customDetailModule.setNumColumns(5);
        associatesMLV.setAdapter(mAssociatesAdapter);
        //associatesMLV.setVisibility(View.GONE);
        contactMLV.setAdapter(mContactAdapter);
    }

    @Override
    protected void initData() {
        // 这里接收页面跳转数据
        mCustomId = getIntent().getStringExtra("customId");
        Constants.CUSTOM_ID = mCustomId;
        LogUtils.d("sxs", "客户详情----> " + mCustomId + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.CUSTOM_ID = null;
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mCustomPresenter.getDetailData();
        mCustomPresenter.requestCustomDetailById(mCustomId);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.btn_transfer_custom, R.id.tv_add_associates, R.id.iv_menu,
            R.id.iv_add_associates, R.id.tv_query_all_contact, R.id.btn_copy_custom_id, R.id.tv_requested_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                // 此处返回到客户列表
                // startActivity(getNewIntent(this, CustomListActivity.class, "客户列表"));
                finish();
                break;
            case R.id.btn_transfer_custom:
                // 转让客户
                plusFlag = false;
                mCustomPresenter.requestCustomStaff();
                break;
            case R.id.tv_add_associates:
            case R.id.iv_add_associates:
                // 添加协作人
                plusFlag = true;
                mCustomPresenter.requestCustomStaff();
                break;
            case R.id.tv_query_all_contact:
                // 查看全部联系人
                // startActivity(getNewIntent(this, CommonActivity.class, "联系人"));
                PageJumpUtil.customDetail2ContactList(this, CommonActivity.class, mCustomId);
                break;
            case R.id.tv_requested_edit:
                // 编辑客户需求-- 返回新增页面进行重新编辑
                //toast("编辑客户需求");
                startActivity(getNewIntent(this, CustomNewActivity.class, "新建客户", "新建客户"));
                break;
            case R.id.btn_copy_custom_id:
                // 复制客户id
                ClipboardUtil.clipFormat2Board(this, "customId", mCustomId);
                toast("已复制");
                break;
            case R.id.iv_menu:
                //toast("menu");
                showQuickFunction(this, orderDetailParent);
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getCustomDetailData(Map<String, List> contentMap) {
        List<IconAndFont> modules = contentMap.get("module");
        mModuleAdapter.setList(modules);
    }

    @Override
    public void getCustomDetailValues(Map dataMap) {
        CustomDetail customDetail = (CustomDetail) dataMap.get("custom");
        // setValues()
        if (customDetail != null) {
            setValues(customDetail);
        }
    }

    /**
     * setValue
     *
     * @param data
     */
    @SuppressLint("SetTextI18n")
    private void setValues(CustomDetail data) {
        Glide.with(this).load(data.getStaff().getIco())
                .error(R.drawable.ic_default_head)
                .placeholder(R.drawable.ic_default_head)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(employeeHeadSrc);
        employeeName.setText(data.getStaff().getName());
        depart.setText(data.getStaff().getJob());
        // 介绍人
        Glide.with(this).load(data.getShare().getIco())
                .error(R.drawable.ic_default_head)
                .placeholder(R.drawable.ic_default_head)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(referHeadSrc);
        referName.setText(data.getShare().getName());
        referPost.setText(data.getShare().getJob());
        noneAssociates.setVisibility(ListUtils.getSize(data.getEdit()) == 0 ? View.VISIBLE : View.GONE);
        //协作人
        mAssociatesAdapter.setList(data.getEdit());
        mAssociatesAdapter.setManagerId(data.getStaff().getManager());
        mAssociatesAdapter.setAssociatesListener(new AssociatesAdapter.OnItemAssociatesListener() {
            @Override
            @SingleClick
            public void onItemDelete(CustomValues associates, int position) {
                setAssociates(data.getEdit(), associates, position);
            }
        });
        // 联系人
        noneContact.setVisibility(ListUtils.getSize(data.getKehuStaff()) == 0 ? View.VISIBLE : View.GONE);
        mContactAdapter.setList(data.getKehuStaff());
        // 客户需求
        followStatus.setText(data.getDemand().getWorkFlow());
        customOrigin.setText(data.getDemand().getSource());
        itemBudget.setText(data.getDemand().getBudget());
        industry.setText(data.getDemand().getIndustry());
        demand_detail.setText(data.getDemand().getText());
        // 客户信息
        companyName.setText(data.getKehu().getCompanyName());
        createTime.setText(data.getKehu().getTime() + " 创建");
        customId.setText(data.getKehu().getKhid());
    }

    /**
     * 删除协作人
     */
    private void setAssociates(List<CustomValues> associateList, CustomValues associates, int position) {
        new MessageDialog.Builder(this)
                .setTitle("删除当前协作人")
                .setMessage("【 " + associates.getName() + " 】")
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(getString(R.string.common_cancel))
                .setShowConfirm(false)
                .setAutoDismiss(true)
                .setListener(new MessageDialog.OnListener() {

                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        mCustomPresenter.requestDeleteAssociates(Constants.CUSTOM_ID, associates.getId());
                        associateList.remove(position);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 客户员工列表
     *
     * @param customStaffList
     */
    @Override
    public void getCustomOfStaff(List<CustomStaff> customStaffList) {

        new BottomCustomDialog.Builder(this)
                .setTitle(plusFlag ? "添加协作人" : "转让给")
                .setList(customStaffList)
                .setGravity(Gravity.BOTTOM)
                .setAutoDismiss(false)
                .setTipsVisibility(plusFlag ? "客户所有权属于您，协作人则拥有与您同样的客户编辑权限。" : null)
                .setListener(new BottomCustomDialog.OnListener<CustomStaff>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, CustomStaff custom) {
                        if (position == -1) {
                            toast("请选择员工");
                            return;
                        }
                        dialog.dismiss();
                        if (plusFlag) {
                            // 添加协作人
                            mCustomPresenter.requestPlusAssociates(Constants.CUSTOM_ID, custom.getStid());
                        } else {
                            // 转让客户
                            mCustomPresenter.requestRevCustom(Constants.CUSTOM_ID, custom.getStid());
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }

                })
                .show();
    }

    @Override
    public void onError() {
        // 错误页面
    }

    @Override
    public void onLoading() {
        // 加载中
    }

    @Override
    public void onEmpty() {
        // 空数据
    }

    @Override
    protected void release() {
        if (mCustomPresenter != null) {
            mCustomPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            startActivity(getNewIntent(this, CustomListActivity.class, "客户列表"));
//            finish();
//        }
        return super.onKeyDown(keyCode, event);
    }
}
