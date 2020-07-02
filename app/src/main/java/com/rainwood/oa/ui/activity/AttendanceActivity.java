package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.necer.calendar.BaseCalendar;
import com.necer.calendar.Miui9Calendar;
import com.necer.enumeration.CheckModel;
import com.necer.enumeration.DateChangeBehavior;
import com.necer.listener.OnCalendarChangedListener;
import com.necer.painter.InnerPainter;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.AttendanceCalendarData;
import com.rainwood.oa.model.domain.AttendanceData;
import com.rainwood.oa.model.domain.CalendarStatics;
import com.rainwood.oa.presenter.ICalendarPresenter;
import com.rainwood.oa.ui.adapter.CalendarStaticsAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICalendarCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rainwood.oa.utils.Constants.CHOOSE_STAFF_REQUEST_SIZE;

/**
 * @Author: a797s
 * @Date: 2020/5/9 17:49
 * @Desc: 行政人事 --- 考勤记录
 */
public final class AttendanceActivity extends BaseActivity implements ICalendarCallback {

    // page
    @ViewInject(R.id.rl_pager_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView mPageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView mPageRightTitle;
    @ViewInject(R.id.iv_menu)
    private ImageView rightMenu;
    // content
    @ViewInject(R.id.tv_current_month)
    private TextView currentMonth;
    @ViewInject(R.id.mc_calendar_9)
    private Miui9Calendar mMiui9Calendar;
    // 本月平均工作小时数
    @ViewInject(R.id.tv_work_duration)
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
    /*
    个人信息
     */
    @ViewInject(R.id.iv_head_photo)
    private ImageView headPhoto;
    @ViewInject(R.id.tv_name)
    private TextView mTextName;
    @ViewInject(R.id.tv_position)
    private TextView mTextPosition;
    @ViewInject(R.id.tv_work_duration)
    private TextView yearAVGDuration;
    @ViewInject(R.id.tv_base_salary)
    private TextView baseSalary;
    @ViewInject(R.id.tv_post_allowance)
    private TextView postAllowance;
    @ViewInject(R.id.tv_account_account)
    private TextView accountAccount;
    @ViewInject(R.id.tv_settlement_account)
    private TextView settlementAccount;

    // 固定页面数据
    private String monthAvgWorkHours = "164.47";

    private CalendarStaticsAdapter mDayAdapter;
    private CalendarStaticsAdapter mMonthAdapterOne;
    private CalendarStaticsAdapter mMonthAdapterTwo;
    private CalendarStaticsAdapter mMonthSalaryAdapter;

    private List<CalendarStatics> mDayDescList;
    private List<CalendarStatics> mMonthDescOneList;
    private List<CalendarStatics> mMonthDescTwoList;
    private List<CalendarStatics> mMonthSalaryList;

    // 当日考勤
    private String[] currentDayDecs = {"打卡时间(上班)", "打卡时间(下班)", "今日打卡(次)", "今日工时(小时)"};
    // 本月考勤
    private String[] currentMonthDecs = {"本月实到(小时)", "本月应到(小时)", "本月加班(小时)",
            "超出法定(小时)"};
    private String[] currentMonthPunish = {"迟到次数(次)", "迟到处罚(元)", "矿工(小时)", "矿工处罚(元)"};
    // 本月工资
    private String[] currentMonthSalary = {"基本工资(元)", "岗位津贴(元)", "全勤奖(元)",
            "实得工资(元)"};

    private ICalendarPresenter mCalendarPresenter;
    private String mStaffId;

    private List<AttendanceCalendarData> mCurrentDayData;
    private InnerPainter mInnerPainter;
    private int tempMonth = 0;
    private int tempYear = 0;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attendance;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        rightMenu.setImageResource(R.drawable.ic_triangle_down);
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
        mPageRightTitle.setText("部门员工");
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
        mCalendarPresenter = PresenterManager.getOurInstance().getCalendarPresenter();
        mCalendarPresenter.registerViewCallback(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void loadData() {
        // 日历选择Listener
        mMiui9Calendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            currentMonth.setText(year + "年" + month + "月");
            // 不是同一个月份则请求
            if (tempMonth != month || year != tempYear) {
                mCalendarPresenter.requestCurrentDayAttendance(TextUtils.isEmpty(mStaffId) ? "" : mStaffId,
                        year + "-" + (month < 10 ? "0" + month : month));
            }
            // 展示选中天的考勤数据
            if (tempMonth == month && tempYear == year) {
                Map<String, List<String>> statusMap = new HashMap<>();
                List<String> holidayList = new ArrayList<>();
                List<String> jiaBanList = new ArrayList<>();
                List<String> chiDaoList = new ArrayList<>();
                List<String> kuangGongList = new ArrayList<>();
                List<String> buKaList = new ArrayList<>();
                for (AttendanceCalendarData currentDayDatum : mCurrentDayData) {
                    // 如果是当天
                    if (currentDayDatum.getDay().equals(String.valueOf(localDate.getDayOfMonth()))) {
                        for (CalendarStatics statics : mDayDescList) {
                            if (statics.getDescData().contains("打卡时间(上班)")) {
                                statics.setData(currentDayDatum.getStart().getTime());
                            } else if (statics.getDescData().contains("打卡时间(下班)")) {
                                statics.setData(currentDayDatum.getEnd().getTime());
                            } else if (statics.getDescData().contains("今日打卡")) {
                                statics.setData(String.valueOf(currentDayDatum.getSignNum()));
                            } else if (statics.getDescData().contains("今日工时")) {
                                statics.setData(String.valueOf(currentDayDatum.getHour()));
                            }
                        }
                        // 上班请假
                        setValues(holidayList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getHoliday()) ? "" : currentDayDatum.getStart().getTab().getHoliday());
                        // 上班补卡
                        setValues(buKaList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getSignAdd()) ? "" : currentDayDatum.getStart().getTab().getSignAdd());
                        // 上班矿工
                        setValues(kuangGongList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getLeave()) ? "" : currentDayDatum.getStart().getTab().getLeave());
                        // 上班迟到
                        setValues(chiDaoList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getLate()) ? "" : currentDayDatum.getStart().getTab().getLate());
                        // 下班请假
                        setValues(holidayList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getHoliday()) ? "" : currentDayDatum.getEnd().getTab().getHoliday());
                        // 下班补卡
                        setValues(buKaList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getSignAdd()) ? "" : currentDayDatum.getEnd().getTab().getSignAdd());
                        // 下班矿工
                        setValues(kuangGongList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getLeave()) ? "" : currentDayDatum.getEnd().getTab().getLeave());
                        // 下班加班
                        setValues(jiaBanList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getAdd()) ? "" : currentDayDatum.getEnd().getTab().getAdd());
                    }
                }
                mDayAdapter.setList(mDayDescList);

                mDayAdapter.setList(mDayDescList);
                statusMap.put("假", holidayList);
                statusMap.put("加", jiaBanList);
                statusMap.put("迟", chiDaoList);
                statusMap.put("旷", kuangGongList);
                statusMap.put("补", buKaList);
                // 设置日期的状态
                mInnerPainter.setTagDayCopy(statusMap, 4);
            }
            tempYear = year;
            tempMonth = month;
        });
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
        // 日历表标记
        mInnerPainter = (InnerPainter) mMiui9Calendar.getCalendarPainter();
        // 取消农历，显示今天
        Map<String, String> strMap = new HashMap<>();
        strMap.put(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_DAY), "今天");
        mInnerPainter.setReplaceLunarStrMap(strMap);
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put(DateTimeUtils.getNowDate(DateTimeUtils.DatePattern.ONLY_DAY), Color.parseColor("#11CA8D"));
        mInnerPainter.setReplaceLunarColorMap(colorMap);
        // TODO: 设置日历表的标记的背景drawable
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_STAFF_REQUEST_SIZE && resultCode == CHOOSE_STAFF_REQUEST_SIZE) {
            // 选择被介绍员工
            if (data != null) {
                String staff = data.getStringExtra("staff");
                String position = data.getStringExtra("position");
                mStaffId = data.getStringExtra("staffId");
                toast("员工：" + staff + "\n职位：" + position + "\n员工id：" + mStaffId);
            }
        }
    }

    @OnClick({R.id.iv_lastMonth, R.id.iv_next_month, R.id.tv_page_right_title,
            R.id.iv_page_back, R.id.iv_menu})
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
            case R.id.iv_menu:
                // 回到今天
                startActivityForResult(getNewIntent(this, ContactsActivity.class, "通讯录", ""),
                        CHOOSE_STAFF_REQUEST_SIZE);
                break;
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getAttendanceData(AttendanceData attendanceData) {
        /*
        个人信息
         */
        Glide.with(this).load(attendanceData.getIco())
                .error(R.drawable.ic_default_head)
                .placeholder(R.drawable.ic_default_head)
                .apply(new RequestOptions().circleCrop())
                .into(headPhoto);
        mTextName.setText(attendanceData.getName());
        mTextPosition.setText(attendanceData.getJob());
        yearAVGDuration.setText(attendanceData.getYmh());
        baseSalary.setText(attendanceData.getBasePay());
        postAllowance.setText(attendanceData.getSubsidy());
        accountAccount.setText(attendanceData.getMoney());
        settlementAccount.setText(attendanceData.getLastMoney());
        /*
        本月（考勤、工资）
         */
        for (CalendarStatics calendarStatics : mMonthDescOneList) {
            if (calendarStatics.getDescData().contains("实到")) {
                calendarStatics.setData(attendanceData.getHourAll());
            } else if (calendarStatics.getDescData().contains("应到")) {
                calendarStatics.setData(attendanceData.getLawHour());
            } else if (calendarStatics.getDescData().contains("加班")) {
                calendarStatics.setData(attendanceData.getWorkAddHour());
                calendarStatics.setNote(attendanceData.getWorkOvertimeText());
            } else if (calendarStatics.getDescData().contains("超出法定")) {
                calendarStatics.setData(attendanceData.getOutHour());
            }
        }
        mMonthAdapterOne.setList(mMonthDescOneList);
        for (CalendarStatics calendarStatics : mMonthDescTwoList) {
            if (calendarStatics.getDescData().contains("迟到次数")) {
                calendarStatics.setData(attendanceData.getLateNum());
            } else if (calendarStatics.getDescData().contains("迟到处罚")) {
                calendarStatics.setData(attendanceData.getLateMoney());
            } else if (calendarStatics.getDescData().contains("矿工(小时)")) {
                calendarStatics.setData(attendanceData.getLeave());
            } else if (calendarStatics.getDescData().contains("矿工处罚")) {
                calendarStatics.setData(attendanceData.getLeaveMoney());
            }
        }
        mMonthAdapterTwo.setList(mMonthDescTwoList);
        for (CalendarStatics statics : mMonthSalaryList) {
            if (statics.getDescData().contains("基本工资")) {
                statics.setData(attendanceData.getBasePayMoon());
            } else if (statics.getDescData().contains("岗位津贴")) {
                statics.setData(attendanceData.getSubsidyMoon());
            } else if (statics.getDescData().contains("全勤奖")) {
                statics.setData(attendanceData.getFullAttendance());
                statics.setNote(attendanceData.getFullAttendanceText());
            } else if (statics.getDescData().contains("实得工资")) {
                statics.setData(attendanceData.getPay());
            }
        }
        mMonthSalaryAdapter.setList(mMonthSalaryList);
        /*
        当日考勤
         */
        mCurrentDayData = attendanceData.getCalendar();
        // 初始化当天的数据
        // 日历表的标记
        Map<String, List<String>> statusMap = new HashMap<>();
        List<String> holidayList = new ArrayList<>();
        List<String> jiaBanList = new ArrayList<>();
        List<String> chiDaoList = new ArrayList<>();
        List<String> kuangGongList = new ArrayList<>();
        List<String> buKaList = new ArrayList<>();
        for (AttendanceCalendarData currentDayDatum : mCurrentDayData) {
            // 如果是当天
            if (currentDayDatum.getDay().contains(String.valueOf(DateTimeUtils.getNowDay()))) {
                for (CalendarStatics statics : mDayDescList) {
                    if (statics.getDescData().contains("打卡时间(上班)")) {
                        statics.setData(currentDayDatum.getStart().getTime());
                    } else if (statics.getDescData().contains("打卡时间(下班)")) {
                        statics.setData(currentDayDatum.getEnd().getTime());
                    } else if (statics.getDescData().contains("今日打卡")) {
                        statics.setData(String.valueOf(currentDayDatum.getSignNum()));
                    } else if (statics.getDescData().contains("今日工时")) {
                        statics.setData(String.valueOf(currentDayDatum.getHour()));
                    }
                }
                // 上班请假
                setValues(holidayList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getHoliday()) ? "" : currentDayDatum.getStart().getTab().getHoliday());
                // 上班补卡
                setValues(buKaList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getSignAdd()) ? "" : currentDayDatum.getStart().getTab().getSignAdd());
                // 上班矿工
                setValues(kuangGongList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getLeave()) ? "" : currentDayDatum.getStart().getTab().getLeave());
                // 上班迟到
                setValues(chiDaoList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getStart().getTab().getLate()) ? "" : currentDayDatum.getStart().getTab().getLate());
                // 下班请假
                setValues(holidayList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getHoliday()) ? "" : currentDayDatum.getEnd().getTab().getHoliday());
                // 下班补卡
                setValues(buKaList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getSignAdd()) ? "" : currentDayDatum.getEnd().getTab().getSignAdd());
                // 下班矿工
                setValues(kuangGongList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getLeave()) ? "" : currentDayDatum.getEnd().getTab().getLeave());
                // 下班加班
                setValues(jiaBanList, currentDayDatum, TextUtils.isEmpty(currentDayDatum.getEnd().getTab().getAdd()) ? "" : currentDayDatum.getEnd().getTab().getAdd());
            }
        }
        mDayAdapter.setList(mDayDescList);
        statusMap.put("假", holidayList);
        statusMap.put("加", jiaBanList);
        statusMap.put("迟", chiDaoList);
        statusMap.put("旷", kuangGongList);
        statusMap.put("补", buKaList);
        // 设置日期的状态
        mInnerPainter.setTagDayCopy(statusMap, 4);
    }

    /**
     * 设置日历的状态
     *
     * @param holidayList
     * @param currentDayDatum
     * @param holiday
     */
    private void setValues(List<String> holidayList, AttendanceCalendarData currentDayDatum, String holiday) {
        if (holiday.equals("是")) {
            holidayList.add(tempYear + "-" + tempMonth + "-" + currentDayDatum.getDay());
        }
    }

    @Override
    public void onError(String tips) {
        toast(tips);
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mCalendarPresenter != null) {
            mCalendarPresenter.unregisterViewCallback(this);
        }
    }
}
