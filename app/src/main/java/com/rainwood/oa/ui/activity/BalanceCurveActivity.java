package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.BalanceByMonthOrYear;
import com.rainwood.oa.model.domain.BalanceCurveListData;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.adapter.BalanceCurveAdapter;
import com.rainwood.oa.ui.dialog.BottomYearDialog;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.ui.widget.MyMarkerView;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.widget.HintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: a797s
 * @Date: 2020/6/28 16:04
 * @Desc: 收支曲线
 */
public final class BalanceCurveActivity extends BaseActivity implements IFinancialCallbacks, StatusAction {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @ViewInject(R.id.tv_date)
    private TextView mTextDate;
    @ViewInject(R.id.tv_year)
    private TextView mTextYear;
    @ViewInject(R.id.tv_month)
    private TextView mTextMonth;

    @ViewInject(R.id.lc_salary_chart)
    private LineChart mSalaryChart;
    @ViewInject(R.id.mlv_salary_list)
    private MeasureListView salaryListView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IFinancialPresenter mFinancialPresenter;
    private BalanceCurveAdapter mBalanceCurveAdapter;
    //flag
    private boolean SELECTED_MONTH = true;
    private boolean SELECTED_YEAR = false;
    private String mStartTime;
    private String mEndTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_cruve;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 创建适配器
        mBalanceCurveAdapter = new BalanceCurveAdapter();
        // 设置适配器
        salaryListView.setAdapter(mBalanceCurveAdapter);
    }

    @Override
    protected void initData() {
        // 默认按月 -- 当前月的前6个月
        // mTextDate.setText();
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 工资曲线，默认查询月---默认一年的数据
        showDialog();
        int nowYear = DateTimeUtils.getNowYear();
        int nowMonth = DateTimeUtils.getNowMonth();
        int nowDay = DateTimeUtils.getNowDay();
        netLoadingData((nowYear - 1) + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-01",
                nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-" + (nowDay < 10 ? "0" + nowDay : nowDay));
        mStartTime = (nowYear - 1) + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth);
        mEndTime = nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth);
    }

    /**
     * 查询收支曲线
     */
    private void netLoadingData(String startMonth, String endTime) {
        showLoading();
        mFinancialPresenter.requestBalanceByMonth(startMonth, endTime);
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_month, R.id.tv_year, R.id.tv_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_year:
                setUI(R.drawable.shape_selectted_right_button_bg, R.drawable.shape_unselectted_left_button_bg,
                        R.color.white, R.color.colorPrimary);
                // 查询年的数据 -- 默认展示5年数据
                showDialog();
                SELECTED_MONTH = false;
                SELECTED_YEAR = true;
                mFinancialPresenter.requestBalanceByYear(String.valueOf(DateTimeUtils.getNowYear() - 5),
                        String.valueOf(DateTimeUtils.getNowYear()));
                break;
            case R.id.tv_month:
                setUI(R.drawable.shape_unselectted_right_button_bg, R.drawable.shape_selectted_left_button_bg,
                        R.color.colorPrimary, R.color.white);
                //
                int nowYear = DateTimeUtils.getNowYear();
                int nowMonth = DateTimeUtils.getNowMonth();
                int nowDay = DateTimeUtils.getNowDay();
                showDialog();
                SELECTED_MONTH = true;
                SELECTED_YEAR = false;
                netLoadingData(mStartTime, mEndTime);
                break;
            case R.id.tv_date:
                // 默认选择月
                if (SELECTED_MONTH) {
                    new StartEndDateDialog.Builder(this, true)
                            .setTitle(null)
                            .setConfirm(getString(R.string.common_text_confirm))
                            .setCancel(getString(R.string.common_text_clear_screen))
                            .setAutoDismiss(false)
                            .setStartTime(mStartTime)
                            .setEndTime(mEndTime)
                            .setCanceledOnTouchOutside(false)
                            .setIgnoreDay()
                            .setListener(new StartEndDateDialog.OnListener() {
                                @Override
                                public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                    if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                                        toast("请选择时间");
                                        return;
                                    }
                                    if (DateTimeUtils.isDateOneBigger(startTime, endTime, DateTimeUtils.DatePattern.ONLY_MONTH)) {
                                        toast("开始时间不能大于结束时间");
                                        return;
                                    }
                                    dialog.dismiss();
                                    // toast("选中的时间段：" + startTime + "至" + endTime);
                                    mStartTime = startTime;
                                    mEndTime = endTime;
                                    netLoadingData(mStartTime, mEndTime);
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                if (SELECTED_YEAR) {
                    new BottomYearDialog.Builder(this, true)
                            .setYear(DateTimeUtils.getNowYear())
                            .setShowImageClose(false)
                            .setAutoDismiss(false)
                            .setListener((dialog, startTime, endTime) -> {
                                if (DateTimeUtils.isDateOneBigger(startTime, endTime, DateTimeUtils.DatePattern.ONLY_YEAR)) {
                                    toast("开始年份不能大于结束年份");
                                    return;
                                }
                                dialog.dismiss();
                                // TODO: 查询年
                                showDialog();
                                mFinancialPresenter.requestBalanceByYear(startTime, endTime);
                            }).show();
                }
                break;
        }
    }

    private void setUI(int p, int p2, int p3, int p4) {
        mTextYear.setBackgroundResource(p);
        mTextMonth.setBackgroundResource(p2);
        mTextYear.setTextColor(getColor(p3));
        mTextMonth.setTextColor(getColor(p4));
    }

    /**
     * 收支曲线 -- 按月
     *
     * @param balanceYearMonth X轴
     * @param monthBalanceList Y轴
     */
    @Override
    public void getBalanceMonthData(List<String> balanceYearMonth, List<BalanceByMonthOrYear> monthBalanceList) {
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        // 统计图展示
        setStaticsChart(balanceYearMonth, monthBalanceList);
    }


    @Override
    public void getBalanceYearData(List<String> balanceYearMonth, List<BalanceByMonthOrYear> yearBalanceList) {
        if (isShowDialog()) {
            hideDialog();
        }
        showComplete();
        // 统计图展示
        setStaticsChart(balanceYearMonth, yearBalanceList);
    }

    /**
     * 统计图展示
     *
     * @param balanceYearMonth X轴数据
     * @param monthBalanceList Y轴数据
     */
    @SuppressLint("SetTextI18n")
    private void setStaticsChart(List<String> balanceYearMonth, List<BalanceByMonthOrYear> monthBalanceList) {
        if (ListUtils.getSize(monthBalanceList) == 0) {
            mSalaryChart.setNoDataText("当前暂无收支数据");
            return;
        }
        mTextDate.setText(balanceYearMonth.get(0) + "--" + balanceYearMonth.get(ListUtils.getSize(balanceYearMonth) - 1));
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        float yMinValues = initSalaryChartValues(balanceYearMonth);
        LogUtils.d("sxs", "--- Y轴的最小值 ---- " + yMinValues);
        for (int i = 0; i < ListUtils.getSize(monthBalanceList); i++) {
            List<Entry> entryList = new ArrayList<>();
            for (int j = 0; j < ListUtils.getSize(monthBalanceList.get(i).getData()); j++) {
                entryList.add(new Entry(j, monthBalanceList.get(i).getData().get(j).getMoney()));
            }
            //随机颜色
            Random random = new Random();
            int color = 0xff000000 | random.nextInt(0xffffff);
            LineDataSet lineDataSet = new LineDataSet(entryList, monthBalanceList.get(i).getName());
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setCircleRadius(4.5f);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setHighLightColor(color);
            lineDataSet.setColor(color);
            lineDataSet.setCircleColor(color);
            lineDataSet.setDrawValues(true);
            lineDataSet.setDrawFilled(false);
            // 设置显示最小值
            lineDataSet.setFillFormatter((dataSet, dataProvider) -> yMinValues);
            lineDataSetList.add(lineDataSet);
        }
        // 标注
        MyMarkerView mv = new MyMarkerView(this, R.layout.marker_salary_marker, balanceYearMonth, monthBalanceList, null);
        mv.setOffset(-mv.getMeasuredWidth() >> 1, -mv.getMeasuredHeight());
        mSalaryChart.setMarker(mv);
        mSalaryChart.setData(new LineData(lineDataSetList));

        // 列表展示
        List<BalanceCurveListData> curveListData = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(balanceYearMonth); i++) {
            BalanceCurveListData listData = new BalanceCurveListData();
            listData.setMonth(balanceYearMonth.get(i));
            for (BalanceByMonthOrYear balanceByMonthOrYear : monthBalanceList) {
                if (balanceByMonthOrYear.getName().contains("收入")) {
                    listData.setIncome(String.valueOf(balanceByMonthOrYear.getData().get(i).getMoney()));
                }
                if (balanceByMonthOrYear.getName().contains("支出")) {
                    listData.setOutcome(String.valueOf(balanceByMonthOrYear.getData().get(i).getMoney()));
                }
                if (balanceByMonthOrYear.getName().contains("工资")) {
                    listData.setSalary(String.valueOf(balanceByMonthOrYear.getData().get(i).getMoney()));
                }
                if (balanceByMonthOrYear.getName().contains("结余")) {
                    listData.setSettlement(String.valueOf(balanceByMonthOrYear.getData().get(i).getMoney()));
                }
            }
            curveListData.add(listData);
        }
        mBalanceCurveAdapter.setCurveListData(curveListData);
    }

    /**
     * 曲线属性设置
     *
     * @param monthList
     * @return
     */
    private float initSalaryChartValues(List<String> monthList) {
        // 描述信息
        Description description = new Description();
        description.setEnabled(false);
        description.setText("收支曲线图");
        mSalaryChart.setDescription(description);
        // 设置是否可以触摸
        mSalaryChart.setTouchEnabled(true);
        // 设置是否可以拖拽
        mSalaryChart.setDragEnabled(true);
        // 设置是否可以缩放
        mSalaryChart.setScaleEnabled(true);
        // Y轴的值是否跟随图表缩放
        mSalaryChart.setPinchZoom(false);
        // 是否允许双击进行缩放
        mSalaryChart.setDoubleTapToZoomEnabled(true);
        // 是否以X轴进行缩放
        mSalaryChart.setScaleXEnabled(false);
        // 是否显示表格颜色
        mSalaryChart.setBackgroundColor(Color.TRANSPARENT);
        // 设置动画
        mSalaryChart.animateY(1000, Easing.Linear);
        // 防止底部数据显示不完整，设置底部偏移量
        mSalaryChart.setExtraBottomOffset(5f);
        /*
         X轴配置
         */
        XAxis xAxis = mSalaryChart.getXAxis();
        // 是否可用
        xAxis.setEnabled(true);
        // 是否显示数值
        xAxis.setDrawLabels(true);
        // 是否显示坐标
        xAxis.setDrawAxisLine(true);
        // 设置坐标轴颜色
        xAxis.setAxisLineColor(getColor(R.color.labelColor));
        // 设置坐标轴线宽度
        xAxis.setAxisLineWidth(0f);
        // 是否显示竖直风格线
        xAxis.setDrawGridLines(false);
        // X轴文字颜色
        xAxis.setTextColor(getColor(R.color.labelColor));
        // X轴文字大小
        xAxis.setTextSize(10f);
        // X轴文字显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 左右空白区大小-- 坐标系从零开始
        xAxis.setSpaceMax(0f);
        xAxis.setSpaceMin(0f);
        // count显示数量
        xAxis.setLabelCount(5, true);
        // 设置X轴显示值
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthList));
        // 偏移量---
        xAxis.setLabelRotationAngle(0f);
        // 设置占用宽度
        // xAxis.setGranularity(monthList.size());
        // 设置第一个item和最后一个item都可见
        xAxis.setAvoidFirstLastClipping(true);
        /*
        左Y轴配置
         */
        YAxis lyAxis = mSalaryChart.getAxisLeft();
        lyAxis.setEnabled(true);//是否可用
        lyAxis.setDrawLabels(true);//是否显示数值
        lyAxis.setDrawAxisLine(false);//是否显示坐标线
        lyAxis.setDrawGridLines(true);//是否显示水平网格线
        lyAxis.setDrawZeroLine(false);//是否绘制零线
        float yMinValues = lyAxis.getAxisMinimum();
        lyAxis.setZeroLineColor(getColor(R.color.labelColor));
        lyAxis.setZeroLineWidth(0.8f);
        lyAxis.enableGridDashedLine(10f, 10f, 0f);//网格虚线
        lyAxis.setGridColor(getColor(R.color.labelColor));//网格线颜色
        lyAxis.setGridLineWidth(0.8f);//网格线宽度
        lyAxis.setAxisLineColor(getColor(R.color.labelColor));//坐标线颜色
        lyAxis.setTextColor(getColor(R.color.labelColor));//左侧文字颜色
        lyAxis.setTextSize(12f);//左侧文字大小
        //this replaces setStartAtZero(true)
        // lyAxis.setAxisMinimum(0f);
        lyAxis.setLabelCount(6, true);
        lyAxis.setLabelXOffset(-10f);
        // Y轴单位
        /*
        右Y轴配置
         */
        YAxis ryAxis = mSalaryChart.getAxisRight();
        ryAxis.setEnabled(false);//是否可用
        //标签配置
        Legend legend = mSalaryChart.getLegend();
        legend.setEnabled(true);//是否可用
        return yMinValues;
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public HintLayout getHintLayout() {
        return mHintLayout;
    }
}
