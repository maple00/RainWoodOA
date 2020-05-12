package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.necer.calendar.EmuiCalendar;
import com.necer.enumeration.CheckModel;
import com.necer.painter.InnerPainter;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ViewInject(R.id.ec_emui_calendar)
    private EmuiCalendar mEmuiCalendar;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initData() {
        super.initData();

        String selectedModel = getIntent().getStringExtra("selectedModel");
        String title = getIntent().getStringExtra("title");
        LogUtils.d("sxs", "selectedModel ----》 " + selectedModel);
        LogUtils.d("sxs", "title --- > " + title);
        mEmuiCalendar.setCheckMode(CheckModel.SINGLE_DEFAULT_CHECKED);
        //只在selectedMode==SINGLE_SELECTED有效
        mEmuiCalendar.setDefaultCheckedFirstDate(true);

        InnerPainter innerPainter = (InnerPainter) mEmuiCalendar.getCalendarPainter();
        Map<String, String> strMap = new HashMap<>();
        strMap.put("2020-05-25", "测试");
        strMap.put("2020-05-23", "测试1");
        strMap.put("2020-05-24", "测试2");
        innerPainter.setReplaceLunarStrMap(strMap);

        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("2020-05-01", Color.RED);

        colorMap.put("2020-05-10", Color.parseColor("#11CA8D"));
        innerPainter.setReplaceLunarColorMap(colorMap);

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
        //statusMap.put("加", jiaBanList);
        //statusMap.put("迟", chiDaoList);

        innerPainter.setTagDayCopy(statusMap);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);

    }

    @Override
    protected void initPresenter() {

    }


    @Override
    protected void initEvent() {
        super.initEvent();
        // 单选
        mEmuiCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate, dateChangeBehavior) -> {
            currentMonth.setText(year + "年" + month + "月");
            Log.d(TAG, "当前页面选中：：" + localDate);
        });
    }

    @OnClick({R.id.iv_lastMonth, R.id.iv_next_month})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_lastMonth:
                mEmuiCalendar.toLastPager();
                // 上个月
                break;
            case R.id.iv_next_month:
                // 下个月
                mEmuiCalendar.toNextPager();
                break;
        }
    }
}
