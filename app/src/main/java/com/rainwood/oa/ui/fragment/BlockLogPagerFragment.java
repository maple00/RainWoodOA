package com.rainwood.oa.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.presenter.IBlockLogPagerPresenter;
import com.rainwood.oa.ui.activity.BlockLogDetailActivity;
import com.rainwood.oa.ui.adapter.BlockLogPagerAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IBlockLogPagerCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

import static com.rainwood.oa.utils.Constants.POSITION_BLOCK_PAGER_State;

/**
 * @Author: a797s
 * @Date: 2020/6/17 15:01
 * @Desc: 待办事项子页面
 */
public final class BlockLogPagerFragment extends BaseFragment implements IBlockLogPagerCallbacks {

    private IBlockLogPagerPresenter mBlockLogPagerPresenter;
    private BlockLogPagerAdapter mBlockLogPagerAdapter;
    private String mTitleState;

    private static BottomNavigationView mBottomNavigationView;

    public static BlockLogPagerFragment getInstance(String title, int position, BottomNavigationView bottomNavigationView) {
        mBottomNavigationView = bottomNavigationView;
        BlockLogPagerFragment blockLogPagerFragment = new BlockLogPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BLOCK_PAGER_State, title);
        bundle.putInt(POSITION_BLOCK_PAGER_State, position);
        blockLogPagerFragment.setArguments(bundle);
        return blockLogPagerFragment;
    }

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    @ViewInject(R.id.rv_block_log_pager)
    private RecyclerView blockLogPagerView;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_block_pager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
        // 设置布局管理器
        blockLogPagerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        blockLogPagerView.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
        mBlockLogPagerAdapter = new BlockLogPagerAdapter();
        // 设置适配器
        blockLogPagerView.setAdapter(mBlockLogPagerAdapter);
        // 设置刷新属性
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(false);
    }

    @Override
    protected void initPresenter() {
        mBlockLogPagerPresenter = PresenterManager.getOurInstance().getBlockLogPagerPresenter();
        mBlockLogPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTitleState = bundle.getString(Constants.KEY_BLOCK_PAGER_State);
            int defaultPos = bundle.getInt(POSITION_BLOCK_PAGER_State);
            if (mBlockLogPagerPresenter != null && defaultPos != -1) {
                mBlockLogPagerPresenter.requestBlockData(mTitleState);
            }
        }
    }

    @Override
    public String getState() {
        return mTitleState;
    }

    @Override
    protected void initListener() {
        // 待办事项点击
        mBlockLogPagerAdapter.setBlockListener((blockLog, position) -> {
            if ("已完成".equals(blockLog.getWorkFlow())) {
                PageJumpUtil.finishedBlock2Detail(getContext(), BlockLogDetailActivity.class, "详情", blockLog.getId());
            } else {
                toast(blockLog.getType());
            }
        });
    }

    @SingleClick
    @OnClick(R.id.ll_network_error_tips)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                loadData();
                break;
        }
    }

    @Override
    public void getBlockContent(List<BlockLog> blockLogList) {
        setUpState(State.SUCCESS);
        // 设置数据源
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
        int unDelBlockCount = 0;
        for (BlockLog blockLog : blockLogList) {
            if ("待处理".equals(blockLog.getWorkFlow())) {
                unDelBlockCount++;
            }
        }
        msgCount.setVisibility(unDelBlockCount == 0 ? View.GONE : View.VISIBLE);
        msgCount.setText(unDelBlockCount > 99 ? (unDelBlockCount + "+") : String.valueOf(unDelBlockCount));
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
        if (mBlockLogPagerPresenter != null) {
            mBlockLogPagerPresenter.unregisterViewCallback(this);
        }
    }
}
