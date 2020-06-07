package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.presenter.IAttachmentPresenter;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IAttachmentCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

/**
 * @Author: sxs
 * @Time: 2020/6/7 16:10
 * @Desc: 知识管理--    附件管理
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attach_manager;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setMargin(this, pageTop);
        searchTips.setHint("输入文件名称");
    }

    @Override
    protected void initPresenter() {
        mAttachmentPresenter = PresenterManager.getOurInstance().getAttachmentPresenter();
        mAttachmentPresenter.registerViewCallback(this);
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
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
