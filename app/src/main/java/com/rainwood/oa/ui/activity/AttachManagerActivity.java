package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.KnowledgeAttach;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.adapter.AttachKnowledgeAdapter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.FileManagerUtil;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;
import com.rainwood.tools.wheel.view.RegexEditText;

import java.util.List;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:10
 * @Desc: 知识管理-- 附件管理
 */
public final class AttachManagerActivity extends BaseActivity implements IAttachmentCallbacks {

    //actionBar
    @ViewInject(R.id.rl_search_click)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_search_tips)
    private TextView searchTips;
    // content
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaff;
    @ViewInject(R.id.gti_secret)
    private GroupTextIcon attachSecret;
    @ViewInject(R.id.gti_obj_type)
    private GroupTextIcon objType;
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon attachSorting;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_attach_list)
    private RecyclerView attachView;

    private IAttachmentPresenter mAttachmentPresenter;
    private AttachKnowledgeAdapter mAttachKnowledgeAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attach_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTips.setHint("输入文件名称");
        // 设置布局管理器
        attachView.setLayoutManager(new GridLayoutManager(this, 1));
        attachView.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                FontSwitchUtil.dip2px(this, 10f)));
        // 创建适配器
        mAttachKnowledgeAdapter = new AttachKnowledgeAdapter();
        // 设置适配器
        attachView.setAdapter(mAttachKnowledgeAdapter);
        // 设置刷新属性
        pageRefresh.setEnableLoadmore(true);
        pageRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mAttachmentPresenter.requestKnowledgeAttach();
    }

    @Override
    protected void initEvent() {
        departStaff.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("部门员工");
            }
        });
        attachSecret.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("保密状态");
            }
        });
        objType.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("对象类型");
            }
        });
        attachSorting.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("默认排序");
            }
        });
        // itemClickListener
        mAttachKnowledgeAdapter.setClickAttach(new AttachKnowledgeAdapter.OnClickKnowledgeAttach() {
            @Override
            public void onClickTarget(KnowledgeAttach attach, int position) {
                if (attach.getTargetId().startsWith(RegexEditText.REGEX_CHINESE)
                        && attach.getTargetId().endsWith(RegexEditText.REGEX_CHINESE)) {
                    toast("数据异常");
                    LogUtils.d("sxs", "传入的id有问题");
                    return;
                }
                //toast("页面跳转");
                if ("客户".equals(attach.getTarget())) {
                    PageJumpUtil.listJump2CustomDetail(AttachManagerActivity.this, CustomDetailActivity.class, attach.getTargetId());
                } else {
                    PageJumpUtil.orderList2Detail(AttachManagerActivity.this, OrderDetailActivity.class, attach.getTargetId(), "");
                }
            }

            @Override
            public void onClickDownload(KnowledgeAttach attach, int position) {
                FileManagerUtil.fileDownload(AttachManagerActivity.this, TbsActivity.class, attach.getSrc(), attach.getName(), attach.getFormat());
            }

            @Override
            public void onClickPreview(KnowledgeAttach attach, int position) {
                FileManagerUtil.filePreview(AttachManagerActivity.this, TbsActivity.class, attach.getSrc(), attach.getName(), attach.getFormat());
            }
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
    public void getKnowledgeAttach(List<KnowledgeAttach> attachList) {
        mAttachKnowledgeAdapter.setAttachList(attachList);
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
