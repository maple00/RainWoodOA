package com.rainwood.oa.ui.fragment;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.ManagerMain;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.presenter.IManagerPresenter;
import com.rainwood.oa.ui.adapter.ManagerMainAdapter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IManagerCallbacks;
import com.rainwood.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 管理fragment
 */
public final class ManagerFragment extends BaseFragment implements IManagerCallbacks {

    @ViewInject(R.id.sxs_status_bar)
    private View statusBar;

    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;

    @ViewInject(R.id.rv_main_manager)
    private RecyclerView managerMain;

    @ViewInject(R.id.ll_manager_parent)
    private LinearLayout managerParent;

    private static IManagerPresenter mManagerPresenter;
    private ManagerMainAdapter mMainAdapter;

    @Override
    public void onHiddenChanged(boolean hidden) {
        this.onDetach();
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_manager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        // 设置状态栏高度
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(getContext()));
        statusBar.setLayoutParams(layoutParams);
        // 设置布局管理器
        managerMain.setLayoutManager(new GridLayoutManager(getContext(), 1));
        managerMain.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = FontSwitchUtil.dip2px(getContext(), 0f);
                outRect.bottom = FontSwitchUtil.dip2px(getContext(), 8f);
            }
        });
        // 创建适配器
        mMainAdapter = new ManagerMainAdapter();
        // 设置适配器
        managerMain.setAdapter(mMainAdapter);
        // 设置适配器动画
        managerMain.setItemAnimator(new DefaultItemAnimator());
        // 设置refresh相关属性
        pagerRefresh.setEnableLoadmore(false);
        pagerRefresh.setEnableRefresh(false);
    }

    @Override
    protected void initPresenter() {
        mManagerPresenter = PresenterManager.getOurInstance().getIManagerPresenter();
        mManagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        // recyclerView绘制卡顿优化
        managerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 状态栏高度
                int measuredHeight = managerParent.getMeasuredHeight();

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) managerMain.getLayoutParams();
                //  LogUtils.d(HomePagerFragment.this,"layoutParams.height -=== > " + layoutParams.height);
                layoutParams.height = measuredHeight;
                managerMain.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    managerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 从这里加载数据
        mManagerPresenter.getManagerData();
    }

    @SingleClick
    @OnClick(R.id.ll_network_error_tips)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                if (!NetworkUtils.isAvailable(getContext())) {
                    toast("当前网络不可用");
                    onError(getString(R.string.text_network_state));
                    return;
                }
                mManagerPresenter.getManagerData();
                break;
        }
    }

    @Override
    public void getMainManagerData(List<ManagerMain> mainList) {
        // 从这里拿到数据
        mMainAdapter.setData(mainList);
        // 数据加载到了
        setUpState(State.SUCCESS);
    }

    @Override
    public void onError() {
        //网络错误
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
        if (mManagerPresenter != null) {
            mManagerPresenter.unregisterViewCallback(this);
        }
    }
}
