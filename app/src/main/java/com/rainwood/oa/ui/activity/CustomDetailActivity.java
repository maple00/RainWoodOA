package com.rainwood.oa.ui.activity;

import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Associates;
import com.rainwood.oa.model.domain.Contact;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.presenter.ICustomDetailPresenter;
import com.rainwood.oa.ui.adapter.AssociatesAdapter;
import com.rainwood.oa.ui.adapter.ContactAdapter;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.dialog.BottomCustomDialog;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomDetailCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;
import java.util.Map;

/**
 * create by a797s in 2020/5/14 17:58
 *
 * @Description : &#x5ba2;&#x6237;&#x8be6;&#x60c5;
 * @Usage :
 **/
public final class CustomDetailActivity extends BaseActivity implements ICustomDetailCallbacks {

    // actionBar
    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
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
    @ViewInject(R.id.iv_princess)
    private ImageView princess;
    @ViewInject(R.id.tv_princess_name)
    private TextView princessName;
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

    private ICustomDetailPresenter mDetailPresenter;
    private ItemModuleAdapter mModuleAdapter;
    private AssociatesAdapter mAssociatesAdapter;
    private ContactAdapter mContactAdapter;

    private List<Custom> mCustomList;
    private List<Associates> mAssociates;

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
        contactMLV.setAdapter(mContactAdapter);

    }

    @Override
    protected void initPresenter() {
        mDetailPresenter = PresenterManager.getOurInstance().getCustomDetailPresenter();
        mDetailPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 从这里加载数据
        mDetailPresenter.getDetailData();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.btn_transfer_custom, R.id.tv_add_associates,
            R.id.iv_add_associates, R.id.tv_query_all_contact, R.id.btn_copy_custom_id, R.id.tv_requested_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                // 此处返回到客户列表
                finish();
                break;
            case R.id.btn_transfer_custom:
                // 转让客户
                // toast("转让客户");
                new BottomCustomDialog.Builder(this)
                        .setTitle("转让给")
                        .setAutoDismiss(false)
                        .setList(mCustomList)
                        .setGravity(Gravity.BOTTOM)
                        .setTipsVisibility(null)
                        .setListener(new BottomCustomDialog.OnListener<Custom>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, Custom custom) {
                                dialog.dismiss();
                                toast("选择了：" + position + "--- " + custom.getName());
                                // 请输入登录密码
                                new PayPasswordDialog.Builder(view.getContext())
                                        .setTitle(getString(R.string.pay_title))
                                        .setSubTitle(null)
                                        .setAutoDismiss(false)
                                        .setListener(new PayPasswordDialog.OnListener() {

                                            @Override
                                            public void onCompleted(BaseDialog dialog, String password) {
                                                dialog.dismiss();
                                                toast(password);
                                                // TODO: 执行逻辑
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }

                        })
                        .show();
                break;
            case R.id.tv_add_associates:
            case R.id.iv_add_associates:
                // 添加协作人
                new BottomCustomDialog.Builder(this)
                        .setTitle("添加协作人")
                        .setAutoDismiss(false)
                        .setList(mCustomList)
                        .setGravity(Gravity.BOTTOM)
                        .setTipsVisibility("客户所有权属于您，协作人则拥有与您同样的客户编辑权限。")
                        .setListener(new BottomCustomDialog.OnListener<Custom>() {

                            @Override
                            public void onSelected(BaseDialog dialog, int position, Custom custom) {
                                dialog.dismiss();
                                //toast("选择了：" + position + "--- " + custom.getName());
                                // 请输入登录密码
                                new PayPasswordDialog.Builder(view.getContext())
                                        .setTitle("添加协作人-" + custom.getName())
                                        .setSubTitle("请输入登录密码")
                                        .setAutoDismiss(false)
                                        .setListener(new PayPasswordDialog.OnListener() {

                                            @Override
                                            public void onCompleted(BaseDialog dialog, String password) {
                                                dialog.dismiss();
                                                toast(password);
                                                // TODO: 执行逻辑
                                                Associates associates = new Associates();
                                                associates.setHeadSrc("");
                                                associates.setDepart("研发部");
                                                associates.setPost("Android工程师");
                                                associates.setName(custom.getName());
                                                mAssociates.add(associates);
                                                mAssociatesAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancel(BaseDialog dialog) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }

                        })
                        .show();
                break;
            case R.id.tv_query_all_contact:
                // 查看全部联系人
                startActivity(getNewIntent(this, ContactActivity.class, "联系人"));
                break;
            case R.id.tv_requested_edit:
                // 编辑客户需求-- 返回新增页面进行重新编辑
                //toast("编辑客户需求");
                finish();
                break;
            case R.id.btn_copy_custom_id:
                // 复制客户id
                toast("复制");
                break;
        }
    }

    @SuppressWarnings("all")
    @Override
    public void getCustomDetailData(Map<String, List> contentMap) {
        // 从这里获取返回数据
        List<IconAndFont> modules = contentMap.get("module");
        mAssociates = contentMap.get("associates");
        List<Contact> contacts = contentMap.get("contact");
        mCustomList = contentMap.get("custom");
        // 设置adapter
        mModuleAdapter.setList(modules);
        // 协作人
        noneAssociates.setVisibility(ListUtils.getSize(mAssociates) == 0 ? View.VISIBLE : View.GONE);
        mAssociatesAdapter.setList(mAssociates);
        mContactAdapter.setList(contacts);
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
        if (mDetailPresenter != null) {
            mDetailPresenter.unregisterViewCallback(this);
        }
    }

}
