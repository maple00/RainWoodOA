package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.necer.calendar.Miui9Calendar;
import com.necer.enumeration.CheckModel;
import com.necer.painter.InnerPainter;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.CalendarStatics;
import com.rainwood.oa.ui.adapter.CalendarStaticsAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/9 17:49
 * @Desc: 我的考勤
 */
public final class MineAttendanceActivity extends BaseActivity {

    // page
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView mPageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView mPageRightTitle;
    // content
    @ViewInject(R.id.tv_current_month)
    private TextView currentMonth;
    @ViewInject(R.id.mc_calendar_9)
    private Miui9Calendar mMiui9Calendar;
    // 本月平均工作小时数
    @ViewInject(R.id.tv_current_average_hour)
    private TextView averageHour;
    // 当日考勤
    @ViewInject(R.id.mgv_day_attendance)
    private MeasureGridView dayAttendance;
    // 当月考勤
    @ViewInject(R.id.mgv_month_attendance)
    private MeasureGridView monthAttendance;
    @ViewInject(R.id.mgv_month_attendance_normal)
    private MeasureGridView monthAttendanceNormal;
    // 本月工资
    @ViewInject(R.id.mgv_month_salary)
    private MeasureGridView monthSalary;

    // 固定页面数据
    private String monthAvgWorkHours = "本年月平均工作(小时)：";

    private CalendarStaticsAdapter mDayAdapter;
    private CalendarStaticsAdapter mMonthAdapterOne;
    private CalendarStaticsAdapter mMonthAdapterTwo;
    private CalendarStaticsAdapter mMonthSalaryAdapter;

    private List<CalendarStatics> mDayDescList;
    private List<CalendarStatics> mMonthDescOneList;
    private List<CalendarStatics> mMonthDescTwoList;
    private List<CalendarStatics> mMonthSalaryList;

    /*
     * 本Activity数据
     * + this.getDrawable(R.drawable.ic_doubt)
     */
    // 当日考勤
    private String[] currentDayDecs = {"打卡时间(上班)", "打卡时间(下班)", "今日打卡(次)", "今日工时(小时)"};
    // 本月考勤
    private String[] currentMonthDecs = {"本月实到(小时)", "本月应到(小时)", "本月加班(小时)",
            "超出法定(小时)"};
    private String[] currentMonthPunish = {"迟到次数(次)", "迟到处罚(元)", "矿工(小时)", "矿工处罚(元)"};
    // 本月工资
    private String[] currentMonthSalary = {"基本工资(元)", "岗位津贴(元)", "全勤奖(元)",
            "实得工资(元)"};

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_attendance;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        //StatusBarUtil.setStatusBarColor(this, this.getColor(R.color.assistColor15));
        // 设置日历选中当前日期
        mMiui9Calendar.setCheckMode(CheckModel.SINGLE_DEFAULT_CHECKED);  // 设置当前选中
        //只在selectedMode==SINGLE_SELECTED有效
        mMiui9Calendar.setDefaultCheckedFirstDate(true);
        //
        mDayDescList = new ArrayList<>();
        mMonthDescOneList = new ArrayList<>();
        mMonthDescTwoList = new ArrayList<>();
        mMonthSalaryList = new ArrayList<>();
        // 创建适配器
        mDayAdapter = new CalendarStaticsAdapter();
        mMonthAdapterOne = new CalendarStaticsAdapter();
        mMonthAdapterTwo = new CalendarStaticsAdapter();
        mMonthSalaryAdapter = new CalendarStaticsAdapter();
        // 设置适配器
        dayAttendance.setAdapter(mDayAdapter);
        dayAttendance.setNumColumns(2);
        dayAttendance.setVerticalSpacing(FontSwitchUtil.dip2px(this, 30f));
        monthAttendance.setAdapter(mMonthAdapterOne);
        monthAttendance.setNumColumns(2);
        monthAttendance.setVerticalSpacing(FontSwitchUtil.dip2px(this, 30f));
        monthAttendanceNormal.setAdapter(mMonthAdapterTwo);
        monthAttendanceNormal.setNumColumns(4);
        monthAttendanceNormal.setVerticalSpacing(FontSwitchUtil.dip2px(this, 30f));
        monthSalary.setNumColumns(2);
        monthSalary.setAdapter(mMonthSalaryAdapter);
        monthSalary.setVerticalSpacing(FontSwitchUtil.dip2px(this, 30f));
        // 设置适配器属性
    }

    @Override
    protected void initData() {
        mPageTitle.setText(title);
        mPageRightTitle.setText(this.getString(R.string.back_today));
        averageHour.setText(monthAvgWorkHours);
        // 当日考勤
        for (String currentDayDec : currentDayDecs) {
            CalendarStatics dayDesc = new CalendarStatics();
            dayDesc.setDescData(currentDayDec);
            mDayDescList.add(dayDesc);
        }
        // 本月考勤描述
        for (String currentMonthDec : currentMonthDecs) {
            CalendarStatics monthDescOne = new CalendarStatics();
            if (currentMonthDec.contains("本月加班")) {
                monthDescOne.setDescIcon(R.drawable.ic_doubt);
            }
            monthDescOne.setDescData(currentMonthDec);
            mMonthDescOneList.add(monthDescOne);
        }
        for (String monthPunish : currentMonthPunish) {
            CalendarStatics monthDescTwo = new CalendarStatics();
            monthDescTwo.setDescData(monthPunish);
            mMonthDescTwoList.add(monthDescTwo);
        }
        // 本月工资描述
        for (String salary : currentMonthSalary) {
            CalendarStatics monthSalary = new CalendarStatics();
            if (salary.contains("全勤奖")) {
                monthSalary.setDescIcon(R.drawable.ic_doubt);
            }
            monthSalary.setDescData(salary);
            mMonthSalaryList.add(monthSalary);
        }
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void loadData() {
         /*
        统计信息
         */
        // 当天统计
        mDayAdapter.setList(mDayDescList);
        mDayAdapter.setLineCount(2);
        // 本月统计
        mMonthAdapterOne.setLineCount(2);
        mMonthAdapterOne.setList(mMonthDescOneList);
        mMonthAdapterTwo.setLineCount(4);
        mMonthAdapterTwo.setList(mMonthDescTwoList);
        // 本月工资
        mMonthSalaryAdapter.setLineCount(2);
        mMonthSalaryAdapter.setList(mMonthSalaryList);

        /*
        模拟日历信息
         */
        InnerPainter innerPainter = (InnerPainter) mMiui9Calendar.getCalendarPainter();
        Map<String, List<String>> statusMap = new HashMap<>();
        List<String> holidayList = new ArrayList<>();
        holidayList.add("2020-05-10");
        holidayList.add("2020-05-11");
        holidayList.add("2020-05-12");
        List<String> workdayList = new ArrayList<>();
        workdayList.add("2020-05-10");
        workdayList.add("2020-05-12");
        workdayList.add("2020-05-13");
        List<String> jiaBanList = new ArrayList<>();
        jiaBanList.add("2020-05-13");
        jiaBanList.add("2020-05-10");
        jiaBanList.add("2020-05-15");
        List<String> chiDaoList = new ArrayList<>();
        chiDaoList.add("2020-05-16");
        chiDaoList.add("2020-05-12");
        chiDaoList.add("2020-05-18");
        chiDaoList.add("2020-05-10");
        statusMap.put("假", holidayList);
        statusMap.put("班", workdayList);
        statusMap.put("加", jiaBanList);
        statusMap.put("事", chiDaoList);
        statusMap.put("病", chiDaoList);
        statusMap.put("早", chiDaoList);
        // 设置日期的状态
        innerPainter.setTagDayCopy(statusMap, 4);
        // 取消农历，显示今天
        Map<String, String> strMap = new HashMap<>();
        strMap.put(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_DAY), "今天");
        innerPainter.setReplaceLunarStrMap(strMap);
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_DAY), Color.parseColor("#11CA8D"));
        innerPainter.setReplaceLunarColorMap(colorMap);

        // TODO: 设置日历表的标记的背景drawable
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initEvent() {
        super.initEvent();
        // 设置日历单选
        mMiui9Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            currentMonth.setText(year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + localDate);
        });

    }

    @OnClick({R.id.iv_lastMonth, R.id.iv_next_month, R.id.tv_page_right_title,
            R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_lastMonth:
                mMiui9Calendar.toLastPager();
                // 上个月
                break;
            case R.id.iv_next_month:
                // 下个月
                mMiui9Calendar.toNextPager();
                break;
            case R.id.tv_page_right_title:
                // 回到今天
                mMiui9Calendar.toToday();
                break;
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

}
