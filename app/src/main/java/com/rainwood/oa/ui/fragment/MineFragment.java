package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
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
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.model.domain.IconAndFont;
import com.rainwood.oa.model.domain.MineData;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.activity.AccountFundsActivity;
import com.rainwood.oa.ui.activity.MineInfoActivity;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.adapter.MineAccountAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.views.RWNestedScrollView;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.SmartUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 我的fragment
 */
public final class MineFragment extends BaseFragment implements IMineCallbacks {

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
        //
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 从这里请求数据
        mMinePresenter.getMineData();
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
        // 个人中心账户
        mMineAccountAdapter.setPersonalAccount((item, position) -> {
            switch (position) {
                case 0:
                    // 结算账户
                    startActivity(getNewIntent(getContext(), AccountFundsActivity.class, "结算账户", "settlementAccount"));
                    break;
                case 1:
                    // 团队基金
                    startActivity(getNewIntent(getContext(), AccountFundsActivity.class, "团队基金", "personTeamAccount"));
                    break;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void getMenuData(MineData mineData, List<FontAndFont> accountList) {
        setUpState(State.SUCCESS);
        mMineAccountAdapter.setList(accountList);
        Glide.with(this).load(mineData.getIco())
                .error(R.mipmap.ic_logo_mdpi)
                .placeholder(R.mipmap.ic_logo_mdpi)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPhoto);
        accountBalance.setText(mineData.getMoney());
        name.setText(mineData.getStaffName());
        department.setText(mineData.getJob());
        noteLabel.setText(mineData.getEntryTime() + "入职 · " + mineData.getState() + " · 已工作" + mineData.getEntryDay() + "天");
        mModuleAdapter.setList(mineData.getButton());
    }

    @Override
    public void getMineModule(List<IconAndFont> iconAndFonts) {
        //TODO: 修改密码
    }

    @SingleClick
    @OnClick({R.id.ll_network_error_tips, R.id.btn_logout, R.id.rl_mine_data, R.id.rl_account_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                onRetryClick();
                break;
            case R.id.btn_logout:
                toast("退出登录");
                break;
            case R.id.rl_mine_data:
                //toast("我的个人信息");
                startActivity(getNewIntent(getContext(), MineInfoActivity.class, "个人资料", " 个人资料"));
                break;
            case R.id.rl_account_account:
                startActivity(getNewIntent(getContext(), AccountFundsActivity.class, "会计账户", "accountAccount"));
                break;
        }
    }

    @Override
    protected void onRetryClick() {
        if (mMinePresenter != null) {
            mMinePresenter.getMineData();
        }
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
