package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.ClassificationStatics;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
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

/**
 * @Author: a797s
 * @Date: 2020/6/28 13:08
 * @Desc: 分类统计
 */
public final class ClassificationStaticsActivity extends BaseActivity
        implements OnChartValueSelectedListener, IFinancialCallbacks, StatusAction {

    @ViewInject(R.id.hl_status_hint)
    private HintLayout mHintLayout;

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @ViewInject(R.id.tv_date)
    private TextView date;
    @ViewInject(R.id.tv_balance)
    private TextView balance;
    @ViewInject(R.id.tv_in_out_come)
    private TextView inOutCome;

    @ViewInject(R.id.pc_income_statics)
    private PieChart incomeStaticsView;
    @ViewInject(R.id.pc_outcome_statics)
    private PieChart outcomeStaticsView;

    private IFinancialPresenter mFinancialPresenter;
    private List<ClassificationStatics> mIncomeList;
    private List<ClassificationStatics> mClassificationOutcomeList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classfication_statics;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);

        setValues(incomeStaticsView, "收入统计");
        setValues(outcomeStaticsView, "支出统计");
    }

    /**
     * 饼图初始化
     * @param staticsView
     */
    private void setValues(PieChart staticsView, String tips) {
        staticsView.setUsePercentValues(true);
        staticsView.getDescription().setEnabled(false);
        staticsView.setExtraOffsets(5, 10, 5, 5);
        staticsView.setDragDecelerationFrictionCoef(0.95f);
        staticsView.setCenterText(new SpannableString(tips));
        staticsView.setDrawHoleEnabled(true);
        staticsView.setDrawEntryLabels(false);
        staticsView.setHoleColor(Color.WHITE);
        staticsView.setTransparentCircleColor(Color.WHITE);
        staticsView.setTransparentCircleAlpha(110);
        staticsView.setHoleRadius(58f);
        staticsView.setTransparentCircleRadius(61f);
        staticsView.setDrawCenterText(true);
        staticsView.setRotationAngle(0);
        // enable rotation of the chart by touch
        staticsView.setRotationEnabled(true);
        staticsView.setHighlightPerTapEnabled(true);
        // chart.setDrawUnitsInChart(true);
        // add a selection listener
        staticsView.setOnChartValueSelectedListener(this);
        staticsView.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);
        Legend legend = staticsView.getLegend();
        legend.setEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(7f);
        legend.setWordWrapEnabled(true);
        legend.setYOffset(0f);
        // entry label styling
        staticsView.setEntryLabelColor(Color.WHITE);
        staticsView.setEntryLabelTextSize(12f);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void loadData() {
        // 默认当月1号到当前日期
        int nowYear = DateTimeUtils.getNowYear();
        int nowMonth = DateTimeUtils.getNowMonth();
        int nowDay = DateTimeUtils.getNowDay();
        date.setText(nowYear + "." + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + ".01" + "-" +
                nowYear + "." + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "." + (nowDay < 10 ? "0" + nowDay : nowDay));
        mFinancialPresenter.requestClassStatics(nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-01",
                nowYear + "-" + (nowMonth < 10 ? "0" + nowMonth : nowMonth) + "-" + (nowDay < 10 ? "0" + nowDay : nowDay));
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_date, R.id.iv_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_date:
            case R.id.iv_arrow:
                new StartEndDateDialog.Builder(this, false)
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
                                // TODO: 时间段查询
                                mFinancialPresenter.requestClassStatics(startTime, endTime);
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
    public void getClassificationIncome(List<ClassificationStatics> incomeList, String money,
                                        List<ClassificationStatics> classificationOutcomeList, String outcomeMoney, String balance) {

        mIncomeList = incomeList;
        mClassificationOutcomeList = classificationOutcomeList;
        // setData
        setIncomeData(mIncomeList);
        setOutcomeData(mClassificationOutcomeList);
        this.balance.setText(balance);
        inOutCome.setText(Html.fromHtml("<font color='" + getColor(R.color.colorPrimary) + "' size=='15dp'>收入\t</font>"
                + "<font color='" + getColor(R.color.labelColor) + "' size=='15dp'>" + money + "</font>" +
                "\t\t<font color='" + getColor(R.color.red20) + "' size=='15dp'>支出\t</font>" +
                "<font color='" + getColor(R.color.labelColor) + "' size=='15dp'>" + outcomeMoney + "</font>"));
    }

    /**
     * 收入统计
     * @param moneyList
     */
    private void setIncomeData(List<ClassificationStatics> moneyList) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(moneyList); i++) {
            entries.add(new PieEntry(Float.parseFloat(moneyList.get(i).getMoney()),
                    moneyList.get(i).getType() + "：" + moneyList.get(i).getMoney() + "元"));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.TRANSPARENT);
        incomeStaticsView.setData(data);
        // undo all highlights
        incomeStaticsView.highlightValues(null);
        incomeStaticsView.invalidate();
    }

    /**
     * 支出统计
     * @param moneyList
     */
    private void setOutcomeData(List<ClassificationStatics> moneyList) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(moneyList); i++) {
            entries.add(new PieEntry(Float.parseFloat(moneyList.get(i).getMoney()),
                    moneyList.get(i).getType() + "：" + moneyList.get(i).getMoney() + "元"));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.TRANSPARENT);
        outcomeStaticsView.setData(data);
        // undo all highlights
        outcomeStaticsView.highlightValues(null);
        outcomeStaticsView.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        LogUtils.d("sxs",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.toString());

    }

    @Override
    public void onNothingSelected() {
        LogUtils.d("sxs", "nothing selected");
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
