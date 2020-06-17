package com.rainwood.oa.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.BlockLog;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.ui.adapter.BlockLogPagerAdapter;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IBlockLogCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.ViewInject;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/17 15:01
 * @Desc: 待办事项子页面
 */
public final class BlockLogPagerFragment extends BaseFragment implements IBlockLogCallbacks {

    private IBlockLogPresenter mBlockLogPresenter;
    private BlockLogPagerAdapter mBlockLogPagerAdapter;

    public static BlockLogPagerFragment getInstance(String title) {
        BlockLogPagerFragment blockLogPagerFragment = new BlockLogPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_BLOCK_PAGER_State, title);
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
        pagerRefresh.setEnableLoadmore(true);
    }

    @Override
    protected void initPresenter() {
        mBlockLogPresenter = PresenterManager.getOurInstance().getBlockLogPresenter();
        mBlockLogPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        String title = bundle.getString(Constants.KEY_BLOCK_PAGER_State);
        if (mBlockLogPresenter != null) {
            mBlockLogPresenter.requestBlockData(title);
        }
    }

    @Override
    protected void initListener() {
        mBlockLogPagerAdapter.setBlockListener(new BlockLogPagerAdapter.OnClickItemBlockListener() {
            @Override
            public void onClickItem(BlockLog blockLog, int position) {
                toast("选中了--- " + blockLog.getContent() + "---- position---" + position);
            }
        });
    }

    @Override
    public void getBlockContent(List<BlockLog> blockLogList) {
        mBlockLogPagerAdapter.setBlockLogList(blockLogList);
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
}
