package com.rainwood.oa.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.ui.activity.BlockLogDetailActivity;
import com.rainwood.oa.ui.adapter.BlockLogPagerAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IBlockLogCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment implements IBlockLogCallbacks, StatusAction {

    @ViewInject(R.id.rl_search_click)
    private RelativeLayout searchView;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageBack;
    // content
    @ViewInject(R.id.rv_block_log_list)
    private RecyclerView blockLogView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private BottomNavigationView mBottomNavigationView;
    private BlockLogPagerAdapter mBlockLogPagerAdapter;
    private IBlockLogPresenter mBlockLogPresenter;

    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        mBottomNavigationView = bottomNavigationView;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_blocklog;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        StatusBarUtils.immersive(getActivity());
        StatusBarUtils.setMargin(getContext(), searchView);
        pageBack.setVisibility(View.GONE);
        // 设置布局管理器
        blockLogView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        blockLogView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        // 创建适配器
        mBlockLogPagerAdapter = new BlockLogPagerAdapter();
        // 设置适配器
        blockLogView.setAdapter(mBlockLogPagerAdapter);
    }

    @Override
    protected void initPresenter() {
        mBlockLogPresenter = PresenterManager.getOurInstance().getBlockLogPresenter();
        mBlockLogPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        if (!NetworkUtils.isAvailable(getContext())) {
            onError(getString(R.string.common_network));
            return;
        }
        mBlockLogPresenter.requestStateData();
        mBlockLogPresenter.requestBlockLogList();
    }

    @Override
    protected void initListener() {
        mBlockLogPagerAdapter.setBlockListener((blockLog, position) -> {
            if ("已完成".equals(blockLog.getWorkFlow())) {
                PageJumpUtil.finishedBlock2Detail(getContext(), BlockLogDetailActivity.class, "详情", blockLog.getId());
            } else {
                toast(blockLog.getType());
            }
        });
    }

    @SingleClick
    @OnClick({R.id.ll_network_error_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                mBlockLogPresenter.requestBlockLogList();
                break;
          /*  case R.id.btn_dialog_pay:
                // 支付密码输入对话框
                new PayPasswordDialog.Builder(view.getContext())
                        .setTitle(getString(R.string.pay_title))
                        .setSubTitle(null)
                        .setAutoDismiss(false)
                        .setListener(new PayPasswordDialog.OnListener() {

                            @Override
                            public void onCompleted(BaseDialog dialog, String password) {
                                toast(password);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_date_timer:
                // 自定义日期时间控件
                LogUtils.d(TAG, "点击了日期时间组合控件");
                new TimerDialog.Builder(getContext(), true)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setIgnoreSecond()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new TimerDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minutes, int second) {
                                dialog.dismiss();
                                toast("选中了：" + year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + second);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_date:
                // 自定义日期控件
                new StartEndDateDialog.Builder(getContext(), false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new StartEndDateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                dialog.dismiss();
                                toast("选中的时间段：" + startTime + "至" + endTime);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;*/
        }
    }

    @Override
    public void getBlockState(List<String> stateList) {
        setUpState(State.LOADING);
    }

    @Override
    public void getBlockContent(List<BlockLog> blockLogList) {
        setUpState(State.SUCCESS);
        mBlockLogPagerAdapter.setBlockLogList(blockLogList);
        // 设置角标
        //获取整个的NavigationView
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) mBottomNavigationView.getChildAt(0);
        //这里就是获取所添加的每一个Tab(或者叫menu)，
        View tab = menuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
        //加载我们的角标View，新创建的一个布局
        View badge = LayoutInflater.from(getContext()).inflate(R.layout.badge_block_log, menuView, false);
        TextView msgCount = badge.findViewById(R.id.tv_msg_count);
        //添加到Tab上
        itemView.addView(badge);
        int size = ListUtils.getSize(blockLogList);
        msgCount.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
        msgCount.setText(size > 99 ? (size + "+") : String.valueOf(size));

        //
        if (ListUtils.getSize(blockLogList) == 0) {
            showEmpty();
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    protected void release() {
        if (mBlockLogPresenter != null) {
            mBlockLogPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
