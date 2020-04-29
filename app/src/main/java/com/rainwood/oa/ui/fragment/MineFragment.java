package com.rainwood.oa.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.TempAppMine;
import com.rainwood.oa.model.domain.TempMineAccount;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.adapter.MineAccountAdapter;
import com.rainwood.oa.ui.adapter.MineAppAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.ui.widget.RWNestedScrollView;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 我的fragment
 */
public final class MineFragment extends BaseFragment implements IMineCallbacks {

    @ViewInject(R.id.sxs_status_bar)
    private View statusBar;
    @ViewInject(R.id.trl_pager_refresh)
    private TwinklingRefreshLayout pagerRefresh;
    // 个人信息
    @ViewInject(R.id.tv_name)
    private TextView name;
    @ViewInject(R.id.tv_depart)
    private TextView department;
    @ViewInject(R.id.tv_label)
    private TextView noteLabel;
    @ViewInject(R.id.iv_head_photo)
    private ImageView headPhoto;
    @ViewInject(R.id.tv_balance)
    private TextView accountBalance;
    // 账户
    @ViewInject(R.id.mgv_account)
    private MeasureGridView accountGv;
    @ViewInject(R.id.mgv_mine_manager)
    private MeasureGridView mineManager;
    // app应用信息
    @ViewInject(R.id.mlv_app)
    private MeasureListView appData;
    @ViewInject(R.id.btn_logout)
    private Button logout;

    @ViewInject(R.id.ll_mine_parent)
    private LinearLayout mineParent;
    @ViewInject(R.id.rns_scroll)
    private RWNestedScrollView mScrollView;

    private IMinePresenter mMinePresenter;
    private MineAccountAdapter mMineAccountAdapter;
    private ItemModuleAdapter mModuleAdapter;
    private MineAppAdapter mAppAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        // 设置状态栏高度 140 - 96
        // LogUtils.d(this, "bar height ---- > " + StatusBarUtil.getStatusBarHeight(getContext()));
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                StatusBarUtil.getStatusBarHeight(getContext()));
        statusBar.setLayoutParams(layoutParams);
        // 创建适配器--- 账户管理、我的管理
        mMineAccountAdapter = new MineAccountAdapter();
        mModuleAdapter = new ItemModuleAdapter();
        mAppAdapter = new MineAppAdapter();
        // 设置适配器
        accountGv.setAdapter(mMineAccountAdapter);
        mineManager.setAdapter(mModuleAdapter);
        appData.setAdapter(mAppAdapter);
        // refresh
        pagerRefresh.setEnableRefresh(false);
        pagerRefresh.setEnableLoadmore(false);
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        mineParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int measuredHeight = mineParent.getMeasuredHeight();
                // 账户信息高度
                ViewGroup.LayoutParams layoutParams = accountGv.getLayoutParams();
                layoutParams.height = measuredHeight;
                accountGv.setLayoutParams(layoutParams);
                // 我的管理- 高度
                ViewGroup.LayoutParams managerLayoutParams = mineManager.getLayoutParams();
                managerLayoutParams.height = measuredHeight;
                mineManager.setLayoutParams(layoutParams);
                // app信息高度
                ViewGroup.LayoutParams appDataLayoutParams = appData.getLayoutParams();
                appDataLayoutParams.height = measuredHeight;
                appData.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    mineParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        // TODO: 处理滑动卡顿冲突问题；自定义组合控件
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mMinePresenter.getMineData();
    }

    @Override
    public void getMenuData(List<TempMineAccount> accounts, List<IconAndFont> iconAndFonts, List<TempAppMine> appMines) {
        // 从这里拿到数据
        // 账户信息
        mMineAccountAdapter.setList(accounts);
        // 我的module
        mModuleAdapter.setList(iconAndFonts);
        // app
        mAppAdapter.setAppMines(appMines);
        //
        Glide.with(this).load(R.mipmap.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPhoto);
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
        if (mMinePresenter != null) {
            mMinePresenter.unregisterViewCallback(this);
        }
    }

}
