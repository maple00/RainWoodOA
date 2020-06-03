package com.rainwood.oa.ui.fragment;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.lcodecore.tkrefreshlayout.views.RWNestedScrollView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.TempMineAccount;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.adapter.MineAccountAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.SmartUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 我的fragment
 */
public final class MineFragment extends BaseFragment implements IMineCallbacks {

    // @ViewInject(R.id.trl_pager_refresh)
    // private TwinklingRefreshLayout pagerRefresh;
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
    //@ViewInject(R.id.mlv_app)
    // private MeasureListView appData;
    @ViewInject(R.id.btn_logout)
    private Button logout;

    @ViewInject(R.id.ll_mine_parent)
    private LinearLayout mineParent;
    @ViewInject(R.id.rns_scroll)
    private RWNestedScrollView mScrollView;
    @ViewInject(R.id.tb_mine_bar)
    private Toolbar mineBar;
    @ViewInject(R.id.bl_mine_top_layout)
    private AppBarLayout mBarLayout;
    @ViewInject(R.id.fl_mine_content)
    private FrameLayout mineContentFL;

    private IMinePresenter mMinePresenter;
    private MineAccountAdapter mMineAccountAdapter;
    private ItemModuleAdapter mModuleAdapter;

    private int mOffset = 0;
    private int mScrollY = 0;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);

        StatusBarUtils.immersive(this.getActivity());
        StatusBarUtils.setPaddingSmart(this.getContext(), mineBar);
        StatusBarUtils.setMargin(this.getContext(), mScrollView);
        // 字体设置(苹方字体)
        Typeface typeface = Typeface.createFromAsset(rootView.getContext().getAssets(), "pingfang.ttf");
        accountBalance.setTypeface(typeface);
        // 创建适配器--- 账户管理、我的管理
        mMineAccountAdapter = new MineAccountAdapter();
        mModuleAdapter = new ItemModuleAdapter();
        // 设置适配器
        accountGv.setAdapter(mMineAccountAdapter);
        mineManager.setAdapter(mModuleAdapter);
        // refresh
        // pagerRefresh.setEnableRefresh(false);
        // pagerRefresh.setEnableLoadmore(false);
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
                // 滑动高度
                int mineFLHeight = mineContentFL.getMeasuredHeight();
                mScrollView.setHeaderHeight(mineFLHeight);
                if (measuredHeight != 0) {
                    mineParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        // 滑动监听
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = SmartUtil.dp2px(170);
            private int color = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.white) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    mBarLayout.setAlpha(0.9f * mScrollY / h);
                    mineBar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                lastScrollY = scrollY;
            }
        });
        mBarLayout.setAlpha(0);
        mineBar.setBackgroundColor(0);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mMinePresenter.getMineData();
    }

    @Override
    public void getMenuData(List<TempMineAccount> accounts, List<IconAndFont> iconAndFonts) {
        // 从这里拿到数据
        // 账户信息
        mMineAccountAdapter.setList(accounts);
        // 我的module
        mModuleAdapter.setList(iconAndFonts);
        Glide.with(this).load(R.drawable.bg_monkey_king)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPhoto);
        // 数据加载到了
        setUpState(State.SUCCESS);
    }

    @Override
    protected void onClickManager() {
        // 点击事件管理

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
