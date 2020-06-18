package com.rainwood.oa.ui.fragment;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.presenter.IBlockLogPresenter;
import com.rainwood.oa.ui.adapter.BlockPagerAdapter;
import com.rainwood.oa.ui.dialog.PayPasswordDialog;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.dialog.TimerDialog;
import com.rainwood.oa.ui.widget.GlueTabLayout;
import com.rainwood.oa.ui.widget.GroupTextIcon;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IBlockLogCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.List;

import static com.rainwood.tools.utils.SmartUtil.dp2px;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 待办事项fragment
 */
public final class BlockLogFragment extends BaseFragment implements IBlockLogCallbacks {

    @ViewInject(R.id.rl_search_click)
    private RelativeLayout searchView;
    @ViewInject(R.id.iv_page_back)
    private ImageView pageBack;
    // content
    @ViewInject(R.id.tl_state)
    private GlueTabLayout stateLayout;
    @ViewInject(R.id.vp_block_log)
    private ViewPager blockLogPager;
    @ViewInject(R.id.gti_default_sort)
    private GroupTextIcon defaultSortView;
    @ViewInject(R.id.gti_depart_staff)
    private GroupTextIcon departStaffView;
    @ViewInject(R.id.gti_type)
    private GroupTextIcon typeView;
    @ViewInject(R.id.gti_period_time)
    private GroupTextIcon periodTimeView;

    private IBlockLogPresenter mBlockLogPresenter;
    private BlockPagerAdapter mBlockPagerAdapter;

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
        // 创建适配器
        mBlockPagerAdapter = new BlockPagerAdapter(getChildFragmentManager());
        // 设置适配器
        blockLogPager.setAdapter(mBlockPagerAdapter);
        blockLogPager.setOffscreenPageLimit(2);
        // 设置TabLayout属性
        stateLayout.setTabMode(GlueTabLayout.GRAVITY_CENTER);
        // 设置指示器下划线高度和颜色
        stateLayout.setSelectedTabIndicatorHeight(dp2px(3));
        stateLayout.setSelectedTabIndicatorColor(getContext().getColor(R.color.colorPrimary));
        //GlueTabLayout 设置下划线指示器圆角
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(dp2px(2));
        stateLayout.setSelectedTabIndicator(gradientDrawable);
        //GlueTabLayout 设置点击动画为水波纹扩散效果
        stateLayout.setUnboundedRipple(false);
        //GlueTabLayout 设置下划线指示器的宽度为原来的一半
        stateLayout.setTabIndicatorWidth(0.3f);
        // 设置点击时的背景
        stateLayout.setTabRippleColor(ColorStateList.valueOf(getContext().getColor(R.color.transparent)));
        // 设置字体颜色字体
        stateLayout.setTabTextColors(getContext().getColor(R.color.colorMiddle), getContext().getColor(R.color.fontColor));
        // 给ViewPager设置适配器
        stateLayout.setupWithViewPager(blockLogPager);
    }

    @Override
    protected void initPresenter() {
        mBlockLogPresenter = PresenterManager.getOurInstance().getBlockLogPresenter();
        mBlockLogPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求TabLayout状态
        mBlockLogPresenter.requestStateData();
    }

    @Override
    protected void initListener() {
        defaultSortView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("默认排序");
            }
        });
        departStaffView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("部门员工");
            }
        });
        typeView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("类型");
            }
        });
        periodTimeView.setOnItemClick(new GroupTextIcon.onItemClick() {
            @Override
            public void onItemClick(String text) {
                toast("时间段");
            }
        });
    }

    @SingleClick
    @OnClick({R.id.btn_dialog_pay, R.id.btn_date_timer, R.id.btn_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_pay:
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
                break;
        }
    }

    @Override
    public void getBlockState(List<String> stateList) {
        setUpState(State.SUCCESS);
        if (mBlockPagerAdapter != null) {
            mBlockPagerAdapter.setTitleList(stateList);
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
        if (mBlockLogPresenter != null){
            mBlockLogPresenter.unregisterViewCallback(this);
        }
    }
}
