package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.MineRecords;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.MineRecordsAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.RefreshListenerAdapter;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/12 10:58
 * @Desc: 我的补卡记录
 */
public final class MineReissueCardActivity extends BaseActivity implements IMineCallbacks, StatusAction {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;
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
    @ViewInject(R.id.tv_query_all)

    private TextView mTextQueryAll;
    @ViewInject(R.id.tv_through)
    private TextView mTextThrough;
    @ViewInject(R.id.tv_audit)
    private TextView mTextAudit;
    @ViewInject(R.id.tv_draft)
    private TextView mTextDraft;

    private IMinePresenter mMinePresenter;
    private MineRecordsAdapter mMineRecordsAdapter;
    private int pageCount = 1;
    private String mStartTime;
    private String mEndTime;
    private String mState = "";

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
        // 设置TextView字体加粗
        mTextQueryAll.getPaint().setFakeBoldText(true);
        mTextQueryAll.invalidate();
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        showDialog();
        mMinePresenter.requestMineReissueCards(mState, "", "", pageCount);
    }

    @Override
    protected void initEvent() {
        // item点击事件
        mMineRecordsAdapter.setItemReissue(new MineRecordsAdapter.OnClickItemReissue() {
            @Override
            public void onClickItem(MineRecords reissue, int position) {
                PageJumpUtil.ReissueCardList2Detail(MineReissueCardActivity.this, RecordDetailActivity.class,
                        "补卡详情", reissue.getId());
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
        // 加载更多
        pageRefresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mMinePresenter.requestMineReissueCards(mState, mStartTime, mEndTime, ++pageCount);
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
                new StartEndDateDialog.Builder(this, false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(true)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new StartEndDateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                dialog.dismiss();
                                mStartTime = startTime;
                                mEndTime = endTime;
                                mMinePresenter.requestMineReissueCards("", mStartTime, mEndTime, pageCount = 1);
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
                setTextBold(true, false, false, false);
                setLineVisible(View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                mState = "";
                showDialog();
                mMinePresenter.requestMineReissueCards(mState, "", "", pageCount = 1);
                break;
            case R.id.tv_through:
                //toast("已通过");
                setTextBold(false, true, false, false);
                setLineVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                mState = "已通过";
                showDialog();
                mMinePresenter.requestMineReissueCards(mState, "", "", pageCount = 1);
                break;
            case R.id.tv_audit:
                // toast("待审核");
                setTextBold(false, false, true, false);
                setLineVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                mState = "审核中";
                showDialog();
                mMinePresenter.requestMineReissueCards(mState, "", "", pageCount = 1);
                break;
            case R.id.tv_draft:
                // toast("草稿");
                setTextBold(false, false, false, true);
                setLineVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                mState = "草稿";
                showDialog();
                mMinePresenter.requestMineReissueCards("mState", "", "", pageCount = 1);
                break;
        }
    }

    /**
     * 设置字体加粗
     *
     * @param b
     * @param b2
     * @param b3
     * @param b4
     */
    private void setTextBold(boolean b, boolean b2, boolean b3, boolean b4) {
        mTextQueryAll.getPaint().setFakeBoldText(b);
        mTextThrough.getPaint().setFakeBoldText(b2);
        mTextAudit.getPaint().setFakeBoldText(b3);
        mTextDraft.getPaint().setFakeBoldText(b4);
        mTextQueryAll.invalidate();
        mTextThrough.invalidate();
        mTextAudit.invalidate();
        mTextDraft.invalidate();
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
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        pageRefresh.finishLoadmore();
        if (pageCount != 1) {
            if (ListUtils.getSize(reissueList) == 0) {
                pageCount--;
                return;
            }
            toast("加载了" + ListUtils.getSize(reissueList) + "条数据");
            mMineRecordsAdapter.addData(reissueList);
        } else {
            if (ListUtils.getSize(reissueList) == 0) {
                showEmpty();
                return;
            }
            mMineRecordsAdapter.setReissueList(reissueList);
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        pageRefresh.finishLoadmore();
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
