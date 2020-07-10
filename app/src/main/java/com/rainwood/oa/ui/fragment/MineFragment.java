package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.rainwood.oa.model.domain.MineData;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.presenter.IMinePresenter;
import com.rainwood.oa.ui.activity.AccountFundsActivity;
import com.rainwood.oa.ui.activity.ChangePwdActivity;
import com.rainwood.oa.ui.activity.LoginActivity;
import com.rainwood.oa.ui.activity.MineInfoActivity;
import com.rainwood.oa.ui.adapter.ItemModuleAdapter;
import com.rainwood.oa.ui.adapter.MineAccountAdapter;
import com.rainwood.oa.ui.dialog.MessageDialog;
import com.rainwood.oa.ui.dialog.UpdateDialog;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.ActivityStackManager;
import com.rainwood.oa.utils.AppCacheDataManager;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IMineCallbacks;
import com.rainwood.tkrefreshlayout.views.RWNestedScrollView;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.SmartUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.tools.wheel.widget.SettingBar;

import java.util.List;
import java.util.Objects;

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
    @ViewInject(R.id.sb_clear_cache)
    private SettingBar clearCacheView;
    // 账户
    @ViewInject(R.id.mgv_account)
    private MeasureGridView accountGv;
    @ViewInject(R.id.mgv_mine_manager)
    private MeasureGridView mineManager;
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
    private String telNumber;

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

        // 设置缓存
        clearCacheView.setRightText(AppCacheDataManager.getTotalCacheSize(Objects.requireNonNull(getContext())));
    }

    @Override
    protected void initPresenter() {
        mMinePresenter = PresenterManager.getOurInstance().getIMinePresenter();
        mMinePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        if (!NetworkUtils.isAvailable(getContext())) {
            onError(getString(R.string.text_network_state));
            return;
        }
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

    @SingleClick
    @OnClick({R.id.ll_network_error_tips, R.id.btn_logout, R.id.rl_mine_data, R.id.rl_account_account,
            R.id.sb_mine_changed_pwd, R.id.sb_clear_cache, R.id.sb_update_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                onRetryClick();
                break;
            case R.id.rl_mine_data:
                //toast("我的个人信息");
                startActivity(getNewIntent(getContext(), MineInfoActivity.class, "个人资料", " 个人资料"));
                break;
            case R.id.rl_account_account:
                startActivity(getNewIntent(getContext(), AccountFundsActivity.class, "会计账户", "accountAccount"));
                break;
            case R.id.sb_mine_changed_pwd:
                // toast("修改密码");
                startActivity(getNewIntent(getContext(), ChangePwdActivity.class, "修改密码",
                        TextUtils.isEmpty(telNumber) ? "" : telNumber));
                break;
            case R.id.sb_clear_cache:
                //toast("清除缓存");
                AppCacheDataManager.clearAllCache(getContext());
                postDelayed(() -> {
                    // 重新获取应用缓存大小
                    clearCacheView.setRightText(AppCacheDataManager.getTotalCacheSize(getContext()));
                }, 500);
                break;
            case R.id.sb_update_version:
                // toast("版本更新");
                new UpdateDialog.Builder(getContext())
                        // 版本名
                        .setVersionName("2.0")
                        // 是否强制更新
                        .setForceUpdate(false)
                        // 更新日志
                        .setUpdateLog("到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥\n到底更新了啥")
                        // 下载 URL
                        .setDownloadUrl("")
                        // 文件 MD5
                        .setFileMD5("56A5A5712D1856BDBD4C2AECA9B1FFE7")
                        .show();
                break;
            case R.id.btn_logout:
                //toast("退出登录");
                new MessageDialog.Builder(getContext())
                        .setTitle(null)
                        .setMessage("确定要退出登录吗？")
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setShowConfirm(false)
                        .setAutoDismiss(true)
                        .setListener(new MessageDialog.OnListener() {

                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                // 返回到登录页面
                                if (true) {
                                    return;
                                }
                                mMinePresenter.requestLogout();
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @SingleClick
    @SuppressLint("SetTextI18n")
    @Override
    public void getMenuData(MineData mineData, List<FontAndFont> accountList) {
        setUpState(State.SUCCESS);
        mMineAccountAdapter.setList(accountList);
        Glide.with(this).load(mineData.getIco())
                .error(R.mipmap.ic_logo)
                .placeholder(R.mipmap.ic_logo)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(headPhoto);
        telNumber = mineData.getTel();
        accountBalance.setText(mineData.getMoney());
        name.setText(mineData.getStaffName());
        department.setText(mineData.getJob());
        noteLabel.setText(mineData.getEntryTime() + "入职 · " + mineData.getState() + " · 已工作" + mineData.getEntryDay() + "天");
        mModuleAdapter.setList(mineData.getButton());
    }

    @Override
    public void getLogout(boolean success) {
        toast(success ? "登出成功" : "登出失败");
        // 销毁所有的栈--返回到登录页面
        ActivityStackManager.getInstance().finishAllActivities();
        openActivity(LoginActivity.class);
    }

    @Override
    protected void onRetryClick() {
        if (!NetworkUtils.isAvailable(getContext())) {
            onError(getString(R.string.text_network_state));
            return;
        }
        if (mMinePresenter != null) {
            mMinePresenter.getMineData();
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        //错误状态
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
