package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Month;
import com.rainwood.oa.presenter.ICalendarPresenter;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICalendarCallback;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/8 16:33
 * @Desc: 工作日
 */
public final class WorkDayActivity extends BaseActivity implements ICalendarCallback, CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_year)
    private TextView mTextYear;
    @ViewInject(R.id.tv_month)
    private TextView mTextMonth;
    // calendar
    @ViewInject(R.id.tv_current_day)
    private TextView mCurrentDay;
    @ViewInject(R.id.calendarLayout)
    private CalendarLayout mCalendarLayout;
    @ViewInject(R.id.calendarView)
    private CalendarView mCalendarView;
    @ViewInject(R.id.fl_current)
    private FrameLayout currentDay;

    @ViewInject(R.id.tv_calendar_info)
    private TextView calendarInfo;
    @ViewInject(R.id.tv_calendar_note)
    private TextView calendarNote;
    @ViewInject(R.id.tv_day_note)
    private TextView mTextDayNote;

    private int mYear;
    private ICalendarPresenter mCalendarPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_work_day;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 日历属性
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mYear = mCalendarView.getCurYear();

        mCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected void initPresenter() {
        mCalendarPresenter = PresenterManager.getOurInstance().getCalendarPresenter();
        mCalendarPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 获取月份
        mCalendarPresenter.begWorkDayMonth();
        // 当前月分的工作日
        mCalendarPresenter.requestCurrentWorkDay("202006");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        // 日历监听
    }

    @SuppressLint("SetTextI18n")
    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_page_right_title, R.id.fl_current, R.id.tv_year})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.fl_current:
                // 回到今天
                mCalendarView.scrollToCurrent();
                break;
            case R.id.tv_year:
            case R.id.tv_month:
                // 年视图
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextMonth.setVisibility(View.INVISIBLE);
                mTextYear.setVisibility(View.VISIBLE);
                mTextYear.setText(mYear + "年");
                currentDay.setVisibility(View.GONE);
                break;
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onYearChange(int year) {
        mTextYear.setText(year + "年");
        mTextMonth.setText(mCalendarView.getCurMonth() + "月");
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        currentDay.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonth.setVisibility(View.VISIBLE);
        mTextYear.setText(calendar.getYear() + "年");
        mTextMonth.setText(mCalendarView.getCurMonth() + "月");
        mYear = calendar.getYear();
    }

    @Override
    public void getWorkDayMonthData(List<Month> monthList) {

    }

    @Override
    public void getWorkDayData(List<String> dayList, String dayNote) {
        mTextDayNote.setText(dayNote);
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> dayMap = new HashMap<>();
        for (String day : dayList) {
            dayMap.put(getSchemeCalendar(year, month, Integer.parseInt(day), getColor(R.color.blue25), "班").toString(),
                    getSchemeCalendar(year, month, Integer.parseInt(day), getColor(R.color.blue25), "班"));
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        mCalendarView.setSchemeDate(dayMap);
    }

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
