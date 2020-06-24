package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.MineRecordsAdapter;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 10:58
 * @Desc: 我的补卡记录
 */
public final class MineReissueCardActivity extends BaseActivity implements IMineCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.line_all)
    private View lineAll;
    @ViewInject(R.id.line_through)
    private View lineThrough;
    @ViewInject(R.id.line_audit)
    private View lineAudit;
    @ViewInject(R.id.line_draft)
    private View lineDraft;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pageRefresh;
    @ViewInject(R.id.rv_reissue_records)
    private RecyclerView reissueRecordsView;

    private IMinePresenter mMinePresenter;
    private MineRecordsAdapter mMineRecordsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_reissue_card;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置布局管理器
        reissueRecordsView.setLayoutManager(new GridLayoutManager(this, 1));
        reissueRecordsView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mMineRecordsAdapter = new MineRecordsAdapter();
        // 设置适配器
        reissueRecordsView.setAdapter(mMineRecordsAdapter);
        // 设置刷新属性
        pageRefresh.setEnableRefresh(false);
        pageRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mMinePresenter.requestMineReissueCards("");
    }

    @Override
    protected void initEvent() {
        mMineRecordsAdapter.setItemReissue(new MineRecordsAdapter.OnClickItemReissue() {
            @Override
            public void onClickItem(MineRecords reissue, int position) {
                toast("查看详情");
            }

            @Override
            public void onClickEdit(MineRecords reissue, int position) {
                //toast("编辑");
                PageJumpUtil.mineReissueList2Apply(MineReissueCardActivity.this, ReissueApplyActivity.class,
                        "补卡申请", reissue);
            }

            @Override
            public void onClickDelete(MineRecords reissue, int position) {
                toast("删除");
            }
        });
    }

    @SingleClick
    @OnClick({R.id.ll_top_right, R.id.iv_page_back, R.id.tv_query_all, R.id.tv_through, R.id.tv_audit,
            R.id.tv_draft})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.ll_top_right:
                //toast("选择时间");
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        .setShowConfirm(true)
                        .setShowImageClose(false)
                        .setListener(new DateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + getString(R.string.common_year) + month + getString(R.string.common_month) + day + getString(R.string.common_day));
                                // chooseTime.setText(year + "-" + month + "-" + day);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tv_query_all:
                //toast("查看全部");
                setLineVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                mMinePresenter.requestMineReissueCards("");
                break;
            case R.id.tv_through:
                //toast("已通过");
                setLineVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                mMinePresenter.requestMineReissueCards("已通过");
                break;
            case R.id.tv_audit:
                // toast("待审核");
                setLineVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                mMinePresenter.requestMineReissueCards("审核中");
                break;
            case R.id.tv_draft:
                // toast("草稿");
                setLineVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                mMinePresenter.requestMineReissueCards("草稿");
                break;
        }
    }

    /**
     * 设置横线可见
     *
     * @param queryAll
     * @param queryThrough
     * @param queryAudit
     * @param queryDraft
     */
    private void setLineVisible(int queryAll, int queryThrough, int queryAudit, int queryDraft) {
        lineAll.setVisibility(queryAll);
        lineThrough.setVisibility(queryThrough);
        lineAudit.setVisibility(queryAudit);
        lineDraft.setVisibility(queryDraft);
    }

    @Override
    public void getMineReissueRecords(List<MineRecords> reissueList) {
        mMineRecordsAdapter.setReissueList(reissueList);
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
