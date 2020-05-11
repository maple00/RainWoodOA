package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.DateTimeUtils;

import java.util.HashMap;

/**
 * @Author: a797s
 * @Date: 2020/5/9 17:49
 * @Desc: 考勤
 */
public final class AttendanceActivity extends BaseActivity {

    @ViewInject(R.id.rl_title_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_current_month)
    private TextView currentMonth;
    @ViewInject(R.id.cv_calendar)
    private CalendarView calendarView;


    private int[] cDate = CalendarUtil.getCurrentDate();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attendance;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);

        HashMap<String, String> map = new HashMap<>();
        map.put(DateTimeUtils.getNowYear() + "." + DateTimeUtils.getNowMonth() + "." + DateTimeUtils.getNowDay(), "今天");

        calendarView.setSpecifyMap(map)
                // 设置开始时间和结束时间
                .setStartEndDate("2016.1", "2028.12")
                // 设置时间的禁用范围
                .setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

        currentMonth.setText(cDate[0] + "年" + cDate[1] + "月");
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                currentMonth.setText(date[0] + "年" + date[1] + "月");
            }
        });

        // 选中变化监听
        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                currentMonth.setText(date.getSolar()[0] + "年" + date.getSolar()[1] + "月");
                if (date.getType() == 1) {
                    // chooseDate.setText("当前选中的日期：" + date.getSolar()[0] + "年" + date.getSolar()[1] + "月" + date.getSolar()[2] + "日");
                }
            }
        });
        // 设置选中的样式
    }

    @Override
    protected void initPresenter() {

    }

    public void lastMonth(View view) {
        calendarView.lastMonth();
    }

    public void nextMonth(View view) {
        calendarView.nextMonth();
    }
}
