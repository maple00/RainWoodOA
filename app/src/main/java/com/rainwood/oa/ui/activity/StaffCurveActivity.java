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
import com.rainwood.oa.model.domain.HomeSalaryDesc;
import com.rainwood.oa.model.domain.StaffCurve;
import com.rainwood.oa.model.domain.StaffCurveList;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.adapter.StaffCurveAdapter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.ui.widget.MyChartMarkerView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.rainwood.tools.utils.DateTimeUtils.dateToString;

/**
 * @Author: a797s
 * @Date: 2020/6/29 13:38
 * @Desc: 员工数曲线
 */
public final class StaffCurveActivity extends BaseActivity implements IFinancialCallbacks, StatusAction {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_date)
    private TextView mTextDate;
    @ViewInject(R.id.lc_staff_chart)
    private LineChart staffCurveView;
    @ViewInject(R.id.mlv_staff_list)
    private MeasureListView staffListView;
    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    private IFinancialPresenter mFinancialPresenter;
    private StaffCurveAdapter mStaffCurveAdapter;
    private String mStartTime;
    private String mEndTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_staff_curve;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 创建创适配器
        mStaffCurveAdapter = new StaffCurveAdapter();
        // 设置适配器
        staffListView.setAdapter(mStaffCurveAdapter);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void loadData() {
        showLoading();
        int nowYear = DateTimeUtils.getNowYear();
        int nowMonth = DateTimeUtils.getNowMonth();
        int nowDay = DateTimeUtils.getNowDay();
        // -- 默认选择6个月
        Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
        ca.set(nowYear, nowMonth - 1, nowDay);// 月份是从0开始的，所以11表示12月
        ca.add(Calendar.YEAR, 0); // 年份减1
        ca.add(Calendar.MONTH, -6);// 月份减1
        ca.add(Calendar.DATE, 0);// 日期减1
        Date resultDate = ca.getTime(); // 结果
        String oldTime = dateToString(resultDate, DateTimeUtils.DatePattern.ONLY_MONTH);
        String nowDate = DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_MONTH);
        mTextDate.setText(oldTime + "-" + nowDate);
        mFinancialPresenter.requestStaffNum(oldTime, nowDate);
    }

    @SingleClick
    @OnClick({R.id.tv_date, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_date:
                new StartEndDateDialog.Builder(this, true)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        .setCanceledOnTouchOutside(false)
                        .setIgnoreDay()
                        .setStartTime(mStartTime)
                        .setEndTime(mEndTime)
                        .setListener(new StartEndDateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
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
                                mTextDate.setText(mStartTime + "-" + mEndTime);
                                showDialog();
                                mFinancialPresenter.requestStaffNum(startTime, endTime);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void getStaffNumByCurve(List<String> xValues, List<StaffCurve> staffNumList) {
        showComplete();
        if (isShowDialog()) {
            hideDialog();
        }
        if (ListUtils.getSize(staffNumList) == 0) {
            staffCurveView.setNoDataText("当前暂无收支数据");
            return;
        }
        mTextDate.setText(xValues.get(0) + "--" + xValues.get(ListUtils.getSize(xValues) - 1));
        List<ILineDataSet> lineDataSetList = new ArrayList<>();
        float yMinValues = initSalaryChartValues(xValues);
        LogUtils.d("sxs", "--- Y轴的最小值 ---- " + yMinValues);
        for (int i = 0; i < ListUtils.getSize(staffNumList); i++) {
            List<Entry> entryList = new ArrayList<>();
            for (int j = 0; j < ListUtils.getSize(staffNumList.get(i).getData()); j++) {
                entryList.add(new Entry(j, staffNumList.get(i).getData().get(j).getNum()));
            }
            //随机颜色
            Random random = new Random();
            int color = 0xff000000 | random.nextInt(0xffffff);
            LineDataSet lineDataSet = new LineDataSet(entryList, staffNumList.get(i).getName());
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
        staffCurveView.setData(new LineData(lineDataSetList));
        // 列表展示
        List<StaffCurveList> curveListData = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(xValues); i++) {
            StaffCurveList listData = new StaffCurveList();
            listData.setMonth(xValues.get(i));
            for (StaffCurve balanceByMonthOrYear : staffNumList) {
                listData.setNumber(String.valueOf(balanceByMonthOrYear.getData().get(i).getNum()));
            }
            curveListData.add(listData);
        }
        // 倒叙
        Collections.reverse(curveListData);
        mStaffCurveAdapter.setCurveList(curveListData);
        // 标注
        MyChartMarkerView chartMarkerView = new MyChartMarkerView(this, R.layout.marker_salary_marker);
        chartMarkerView.setChartView(staffCurveView);///如果没有加这句MyMarkView draw会报空指针
        List<HomeSalaryDesc> descList = new ArrayList<>();
        chartMarkerView.setCallBack((x, value) -> {
            int index = (int) (x);
            if (index < 0) {
                return;
            }
            if (index > ListUtils.getSize(xValues)) {
                return;
            }
            descList.clear();
            descList.add(new HomeSalaryDesc(Float.parseFloat(value), "员工数："));
            chartMarkerView.getTvPerson().setText(xValues.get(index));
            chartMarkerView.getTvFirm().setDescList(descList);
        });
        staffCurveView.setMarker(chartMarkerView);
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
        staffCurveView.setDescription(description);
        // 设置是否可以触摸
        staffCurveView.setTouchEnabled(true);
        // 设置是否可以拖拽
        staffCurveView.setDragEnabled(true);
        // 设置是否可以缩放
        staffCurveView.setScaleEnabled(true);
        // Y轴的值是否跟随图表缩放
        staffCurveView.setPinchZoom(false);
        // 是否允许双击进行缩放
        staffCurveView.setDoubleTapToZoomEnabled(true);
        // 是否以X轴进行缩放
        staffCurveView.setScaleXEnabled(false);
        // 是否显示表格颜色
        staffCurveView.setBackgroundColor(Color.TRANSPARENT);
        // 设置动画
        staffCurveView.animateY(10, Easing.Linear);
        // 防止底部数据显示不完整，设置底部偏移量
        staffCurveView.setExtraBottomOffset(5f);
        /*
         X轴配置
         */
        XAxis xAxis = staffCurveView.getXAxis();
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
        YAxis lyAxis = staffCurveView.getAxisLeft();
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
        YAxis ryAxis = staffCurveView.getAxisRight();
        ryAxis.setEnabled(false);//是否可用
        //标签配置
        Legend legend = staffCurveView.getLegend();
        legend.setEnabled(true);//是否可用
        return yMinValues;
    }

    @Override
    public void onError(String tips) {
        toast(tips);
        showError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
