package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.customchartview.chart.bean.ChartLable;
import com.rainwood.customchartview.chart.piechart.PieChartLayout;
import com.rainwood.customchartview.utils.DensityUtil;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.ClassificationStatics;
import com.rainwood.oa.model.domain.PieBean;
import com.rainwood.oa.network.action.StatusAction;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.dialog.StartEndDateDialog;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
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
        implements IFinancialCallbacks, StatusAction {

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
    private PieChartLayout incomeStaticsView;
    @ViewInject(R.id.pc_outcome_statics)
    private PieChartLayout outcomeStaticsView;

    private IFinancialPresenter mFinancialPresenter;
    private List<ClassificationStatics> mIncomeList;
    private List<ClassificationStatics> mClassificationOutcomeList;
    private String mStartTime;
    private String mEndTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_classfication_statics;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);

        // 统计收入
        incomeStaticsView.setRingWidth(DensityUtil.dip2px(this, 15));
        incomeStaticsView.setLineLenth(DensityUtil.dip2px(this, 8)); // //指示线长度
        incomeStaticsView.setTagModul(PieChartLayout.TAG_MODUL.MODUL_CHART);       //在扇形图上显示tag
        incomeStaticsView.setTagType(PieChartLayout.TAG_TYPE.TYPE_PERCENT);        // 显示百分比
        incomeStaticsView.setDebug(false);
        incomeStaticsView.setLoading(false);
        // 支出统计
        outcomeStaticsView.setRingWidth(DensityUtil.dip2px(this, 15));
        outcomeStaticsView.setLineLenth(DensityUtil.dip2px(this, 8)); // //指示线长度
        outcomeStaticsView.setTagType(PieChartLayout.TAG_TYPE.TYPE_PERCENT);        // 显示百分比
        outcomeStaticsView.setTagModul(PieChartLayout.TAG_MODUL.MODUL_CHART);       //在扇形图上显示tag
        outcomeStaticsView.setDebug(false);
        outcomeStaticsView.setLoading(false);
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
                        .setStartTime(mStartTime)
                        .setEndTime(mEndTime)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new StartEndDateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                                    toast("请选择时间");
                                    return;
                                }
                                if (DateTimeUtils.isDateOneBigger(startTime, endTime, DateTimeUtils.DatePattern.ONLY_DAY)) {
                                    toast("开始时间不能大于结束时间");
                                    return;
                                }
                                // TODO: 时间段查询
                                dialog.dismiss();
                                mStartTime = startTime;
                                mEndTime = endTime;
                                date.setText(mStartTime + "-" + mEndTime);
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
        inOutCome.setText(Html.fromHtml("<font color='" + getColor(R.color.colorPrimary) + "' size=='15dp'>收入：\t</font>"
                + "<font color='" + getColor(R.color.labelColor) + "' size=='15dp'>￥" + money + "</font>" +
                "\t\t<font color='" + getColor(R.color.red20) + "' size=='15dp'>支出：\t</font>" +
                "<font color='" + getColor(R.color.labelColor) + "' size=='15dp'>￥" + outcomeMoney + "</font>"));
    }

    /**
     * 收入统计
     *
     * @param moneyList
     */
    private void setIncomeData(List<ClassificationStatics> moneyList) {
        ArrayList<PieBean> datalist = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(moneyList); i++) {
            datalist.add(new PieBean(Float.parseFloat(moneyList.get(i).getMoney()),
                    moneyList.get(i).getType() + "：" + moneyList.get(i).getMoney() + "元"));
        }
        // label 标签
        List<ChartLable> tableList = new ArrayList<>();
        tableList.add(new ChartLable("收入统计", FontSwitchUtil.sp2px(this, 12),
                getColor(R.color.text_color_light_gray)));
        incomeStaticsView.setChartData(PieBean.class, "Numner", "Name", datalist, tableList);
    }

    /**
     * 支出统计
     *
     * @param moneyList
     */
    private void setOutcomeData(List<ClassificationStatics> moneyList) {
        ArrayList<PieBean> datalist = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(moneyList); i++) {
            datalist.add(new PieBean(Float.parseFloat(moneyList.get(i).getMoney()),
                    moneyList.get(i).getType() + "：" + moneyList.get(i).getMoney() + "元"));
        }
        // label 标签
        List<ChartLable> tableList = new ArrayList<>();
        tableList.add(new ChartLable("支出统计", FontSwitchUtil.sp2px(this, 12),
                getColor(R.color.text_color_light_gray)));
        outcomeStaticsView.setChartData(PieBean.class, "Numner", "Name", datalist, tableList);
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
