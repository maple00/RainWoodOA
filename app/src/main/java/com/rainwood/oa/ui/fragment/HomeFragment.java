package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.ChartEntity;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.ui.adapter.HomeSalaryAdapter;
import com.rainwood.oa.ui.widget.LineChartView;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IHomeCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import cn.rawinwood.bgabanner.BGABanner;
import cn.rawinwood.bgabanner.transformer.TransitionEffect;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 首页
 */
public final class HomeFragment extends BaseFragment implements
        BGABanner.Delegate<ImageView, String>, BGABanner.Adapter<ImageView, String>, IHomeCallbacks {

    //    @ViewInject(R.id.ctl_test_bar)
//    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    public boolean mScrimsShown;

    @ViewInject(R.id.lcv_salary)
    private LineChartView mLineChartSalary;

    @ViewInject(R.id.mgv_home_salary)
    private MeasureGridView homeSalary;
    @ViewInject(R.id.banner_home_top)
    private BGABanner mStackBanner;

    // mHandler
    private final int INITIAL_LINE_SALARY_SIZE = 0x101;     // 工资曲线图
    private IHomePresenter mHomePresenter;
    private HomeSalaryAdapter mSalaryAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        // 创建工资曲线适配器
        mSalaryAdapter = new HomeSalaryAdapter();
        // 设置适配器
        homeSalary.setAdapter(mSalaryAdapter);
    }

    @Override
    protected void initPresenter() {
        mHomePresenter = PresenterManager.getOurInstance().getIHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mHomePresenter.getHomeSalaryData();
    }

    /**
     * 工资曲线数据
     *
     * @param salaries
     */
    @Override
    public void getSalariesData(List<FontAndFont> salaries) {
        // 加载返回的数据
        mSalaryAdapter.setList(salaries);
        // setUpState(State.ERROR);
        // 工资曲线 -- V1.0
        Message msg = new Message();
        msg.what = INITIAL_LINE_SALARY_SIZE;
        mHandler.sendMessage(msg);
        // TODO: 自定义线性表的属性(线条，颜色，间距等)

        // 模拟加载返回的图片
        mStackBanner.setTransitionEffect(TransitionEffect.Default);
        loadBannerData(mStackBanner, 3);
        // mStackBanner.setAnimation();
    }

    private void loadBannerData(final BGABanner banner, final int count) {
        // 模拟图片
        List<String> modelImages = new ArrayList<>();
        List<String> imageTips = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            modelImages.add("加载的banner");
            imageTips.add("这是第" + (i + 1) + "张图片");
        }
        /**
         * 设置是否开启自动轮播，需要在 setData 方法之前调用，并且调了该方法后必须再调用一次 setData 方法
         * 例如根据图片当图片数量大于 1 时开启自动轮播，等于 1 时不开启自动轮播
         */
        banner.setAutoPlayAble(modelImages.size() > 1);
        banner.setAdapter(HomeFragment.this);
        banner.setData(modelImages, imageTips);
        banner.setIsNeedShowIndicatorOnOnlyOnePage(false);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        LogUtils.d("sxs", "点击了 + " + position);
        toast("点击了第 " + (position + 1) + " + 页");
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
      //  LogUtils.d("sxs", "model---> " + model + " -- position ---> " + position);
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.bg_monkey_king)
                        .error(R.drawable.bg_monkey_king)
                        .dontAnimate().centerCrop())
                .into(itemView);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_LINE_SALARY_SIZE:
                    List<ChartEntity> datas1 = new ArrayList<>();
                    List<ChartEntity> datas2 = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        datas1.add(new ChartEntity("sxs" + String.valueOf(i), (float) (Math.random() * 1000)));
                        datas2.add(new ChartEntity("sxs" + String.valueOf(i), (float) (Math.random() * 1000)));
                    }
                    mLineChartSalary.setData(datas1, true, true);
                    mLineChartSalary.setData(datas2, true, true);
                    mLineChartSalary.startAnimation(6000);
                    break;
            }
        }
    };

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }


}
