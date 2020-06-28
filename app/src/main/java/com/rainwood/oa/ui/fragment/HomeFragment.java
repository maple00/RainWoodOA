package com.rainwood.oa.ui.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.network.okhttp.NetworkUtils;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.ui.adapter.HomeSalaryAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.MyMarkerView;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IHomeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.tools.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.List;

import cn.rawinwood.bgabanner.BGABanner;
import cn.rawinwood.bgabanner.transformer.TransitionEffect;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 首页
 */
public final class HomeFragment extends BaseFragment implements BGABanner.Adapter<ImageView, String>, IHomeCallbacks {

    @ViewInject(R.id.lc_salary_chart)
    private LineChart salaryChartView;
    @ViewInject(R.id.mgv_home_salary)
    private MeasureGridView homeSalary;
    @ViewInject(R.id.banner_home_top)
    private BGABanner mStackBanner;

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
        if (!NetworkUtils.isAvailable(getContext())) {
            StatusBarUtils.darkMode(getActivity(), true);
            onError(getString(R.string.text_network_state));
            return;
        } else {
            StatusBarUtils.darkMode(getActivity(), false);
        }
        // 加载数据 -- 当月1号到currentDay
        int nowYear = DateTimeUtils.getNowYear();
        int nowMonth = DateTimeUtils.getNowMonth();
        int nowDay = DateTimeUtils.getNowDay();
        mHomePresenter.requestSalaryData(nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-01",
                nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-" + (nowDay < 10 ? "0" + nowDay : nowDay));
    }

    @SingleClick
    @OnClick({R.id.tv_query_all, R.id.ll_query_salary_all, R.id.ll_network_error_tips})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_network_error_tips:
                onRetryClick();
                break;
            case R.id.tv_query_all:
            case R.id.ll_query_salary_all:
                toast("查看全部");
                break;
        }
    }

    @Override
    protected void onRetryClick() {
        if (!NetworkUtils.isAvailable(getContext())) {
            StatusBarUtils.darkMode(getActivity(), true);
            onError(getString(R.string.text_network_state));
            return;
        } else {
            StatusBarUtils.darkMode(getActivity(), false);
        }
        if (mHomePresenter != null) {
            mHomePresenter.requestSalaryData("2020-01", "2020-05");
        }
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
        banner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                LogUtils.d("sxs", "点击了某个bannerItem ---- " + position);
            }
        });
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        //  LogUtils.d("sxs", "model---> " + model + " -- position ---> " + position);
        Glide.with(itemView.getContext())
                .load(model)
                .apply(new RequestOptions().placeholder(R.drawable.ic_banner)
                        .error(R.drawable.ic_banner)
                        .dontAnimate().centerCrop())
                .into(itemView);
    }

    /**
     * 工资曲线数据
     */
    @Override
    public void getSalariesData(List<String> incomeList, List<String> monthList, List<FontAndFont> baseSalaryList) {
        if (mHomePresenter != null) {
            setUpState(State.SUCCESS);
        }
        // 加载返回的数据
        mSalaryAdapter.setList(baseSalaryList);
        // 工资曲线 -- V1.0
        // initial 统计图
        initSalaryChartValues(monthList);
        List<Entry> salaryList = new ArrayList<>();
        for (int i = 0; i < monthList.size(); i++) {
            salaryList.add(new Entry(i, Float.parseFloat(incomeList.get(i))));
        }
        salaryChartView.setNoDataText("您当前还没有工资数据哦~~~");
        salaryChartView.setData(new LineData(setDrawLine(salaryList, 1, "工资曲线")));
        // banner---模拟加载返回的图片
        mStackBanner.setTransitionEffect(TransitionEffect.Accordion);
        loadBannerData(mStackBanner, 3);
        // mStackBanner.setAnimation();
    }

    /**
     * 曲线图属性
     *
     * @param monthList X轴坐标值
     */
    private void initSalaryChartValues(List<String> monthList) {
        // 描述信息
        Description description = new Description();
        description.setEnabled(false);
        description.setText("工资曲线图");
        salaryChartView.setDescription(description);
        // 设置是否可以触摸
        salaryChartView.setTouchEnabled(true);
        // 设置是否可以拖拽
        salaryChartView.setDragEnabled(true);
        // 设置是否可以缩放
        salaryChartView.setScaleEnabled(true);
        // Y轴的值是否跟随图表缩放
        salaryChartView.setPinchZoom(false);
        // 是否允许双击进行缩放
        salaryChartView.setDoubleTapToZoomEnabled(true);
        // 是否以X轴进行缩放
        salaryChartView.setScaleXEnabled(false);
        // 是否显示表格颜色
        salaryChartView.setBackgroundColor(Color.TRANSPARENT);
        // 设置动画
        salaryChartView.animateY(1000, Easing.Linear);
        // 防止底部数据显示不完整，设置底部偏移量
        salaryChartView.setExtraBottomOffset(5f);
        /*
         X轴配置
         */
        XAxis xAxis = salaryChartView.getXAxis();
        // 是否可用
        xAxis.setEnabled(true);
        // 是否显示数值
        xAxis.setDrawLabels(true);
        // 是否显示坐标
        xAxis.setDrawAxisLine(true);
        // 设置坐标轴颜色
        xAxis.setAxisLineColor(getContext().getColor(R.color.labelColor));
        // 设置坐标轴线宽度
        xAxis.setAxisLineWidth(0f);
        // 是否显示竖直风格线
        xAxis.setDrawGridLines(false);
        // X轴文字颜色
        xAxis.setTextColor(getContext().getColor(R.color.labelColor));
        // X轴文字大小
        xAxis.setTextSize(10f);
        // X轴文字显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 左右空白区大小-- 坐标系从零开始
        xAxis.setSpaceMax(0f);
        xAxis.setSpaceMin(0f);
        // count显示数量
        xAxis.setLabelCount(6, true);
        // 设置X轴显示值
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        // 偏移量---
        xAxis.setLabelRotationAngle(0f);
        // 设置占用宽度
        xAxis.setGranularity(monthList.size());
        // 设置第一个item和最后一个item都可见
        xAxis.setAvoidFirstLastClipping(true);
        /*
        左Y轴配置
         */
        YAxis lyAxis = salaryChartView.getAxisLeft();
        lyAxis.setEnabled(true);//是否可用
        lyAxis.setDrawLabels(true);//是否显示数值
        lyAxis.setDrawAxisLine(false);//是否显示坐标线
        lyAxis.setDrawGridLines(true);//是否显示水平网格线
        lyAxis.setDrawZeroLine(false);//是否绘制零线
        lyAxis.setZeroLineColor(getContext().getColor(R.color.labelColor));
        lyAxis.setZeroLineWidth(0.8f);
        lyAxis.enableGridDashedLine(10f, 10f, 0f);//网格虚线
        lyAxis.setGridColor(getContext().getColor(R.color.labelColor));//网格线颜色
        lyAxis.setGridLineWidth(0.8f);//网格线宽度
        lyAxis.setAxisLineColor(getContext().getColor(R.color.labelColor));//坐标线颜色
        lyAxis.setTextColor(getContext().getColor(R.color.labelColor));//左侧文字颜色
        lyAxis.setTextSize(12f);//左侧文字大小
        //this replaces setStartAtZero(true)
        lyAxis.setAxisMinimum(0f);
        lyAxis.setLabelCount(6, true);
        lyAxis.setLabelXOffset(-10f);
        // Y轴单位
        /*
        右Y轴配置
         */
        YAxis ryAxis = salaryChartView.getAxisRight();
        ryAxis.setEnabled(false);//是否可用
        //标签配置
        Legend legend = salaryChartView.getLegend();
        legend.setEnabled(true);//是否可用
        // 标注
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.marker_salary_marker, monthList);
        mv.setOffset(-mv.getMeasuredWidth() >> 1, -mv.getMeasuredHeight());
        salaryChartView.setMarkerView(mv);
    }

    /**
     * 画线
     *
     * @param salaryList
     * @param flag
     * @param name
     */
    private LineDataSet setDrawLine(List<Entry> salaryList, int flag, String name) {
        //设置baby的成长曲线
        LineDataSet setData = new LineDataSet(salaryList, name);
        setData.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER); //设置曲线为圆滑的线
        setData.setCubicIntensity(0.1f);
        setData.setDrawFilled(true); //设置包括的范围区域填充颜色
        setData.setDrawCircles(true); //设置有圆点
        setData.setLineWidth(1f); //设置线的宽度
        setData.setCircleSize(5f); //设置小圆的大小
        setData.setHighLightColor(Color.rgb(244, 117, 117));
        //设置曲线颜色
        if (flag == 1)
            setData.setColor(Color.rgb(104, 241, 175));
        else if (flag == 2)
            setData.setColor(Color.rgb(255, 0, 0));
        return setData; //返回曲线
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
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }
}
