package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.necer.calendar.Miui10Calendar;
import com.necer.painter.InnerPainter;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.Month;
import com.rainwood.oa.presenter.ICalendarPresenter;
import com.rainwood.oa.ui.adapter.HorizontalMonthAdapter;
import com.rainwood.oa.ui.widget.LooperLayoutManager;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.ICalendarCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/6/8 16:33
 * @Desc: 工作日
 */
public final class WorkDayActivity extends BaseActivity implements ICalendarCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_year)
    private TextView yearTV;
    @ViewInject(R.id.rv_work_month)
    private RecyclerView workMonthView;
    // @ViewInject(R.id.vp_month)
    // private ViewPager workMonthView;
    @ViewInject(R.id.mc10_calendar)
    private Miui10Calendar mCalendarView;
    @ViewInject(R.id.tv_calendar_info)
    private TextView calendarInfo;
    @ViewInject(R.id.tv_calendar_note)
    private TextView calendarNote;

    private ICalendarPresenter mCalendarPresenter;
    private HorizontalMonthAdapter mMonthAdapter;
    private List<Month> mMonthList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_work_day;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置月份的布局管理器
        LooperLayoutManager layoutManager = new LooperLayoutManager(this);
        layoutManager.setLooperEnable(true);
        workMonthView.setLayoutManager(layoutManager);
        workMonthView.addItemDecoration(new SpacesItemDecoration(20, 20, 0, 0));
        // 创建适配器
        mMonthAdapter = new HorizontalMonthAdapter();
        // 设置适配器
        workMonthView.setAdapter(mMonthAdapter);
        // TODO: 工作日效果---小米日历
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
    protected void initEvent() {
        // 滑动监听
        workMonthView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                LooperLayoutManager layoutManager = (LooperLayoutManager) recyclerView.getLayoutManager();
               /* if (dx > 0){
                    // 向左滑动
                    LogUtils.d("sxs","向左滑动");
                    int lastItem = layoutManager.findLastVisibleItemPosition();
                    LogUtils.d("sxs", " ---- lastItem --- " + lastItem);
                    if (lastItem == 0) {
                        setYearTV(1);
                    }
                }else {
                    LogUtils.d("sxs","右边");
                    int firstItem = layoutManager.findFirstVisibleItemPosition();
                    LogUtils.d("sxs", " ---- firstItem --- " + firstItem);
                    if (firstItem == 11) {
                        setYearTV(0);
                    }
                }*/
            }
        });
        setMaxFlingVelocity(workMonthView, 500);
    }

    /**
     * 设定RecyclerView最大滑动速度
     *
     * @param recyclerView view
     * @param velocity     3000
     */
    private void setMaxFlingVelocity(RecyclerView recyclerView, int velocity) {
        try {
            Field field = recyclerView.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recyclerView, velocity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听年份
     *
     * @param flag 左边还是右边--0:左边；1：右边
     */
    @SuppressLint("SetTextI18n")
    private void setYearTV(int flag) {
        String currentYear = yearTV.getText().toString().trim().split("年")[0];
        int currentYearInt = Integer.parseInt(currentYear);
        LogUtils.d("sxs", "年份--- " + currentYearInt);
        if (flag == 0) {
            yearTV.setText((--currentYearInt) + "年");
        } else {
            yearTV.setText((++currentYearInt) + "年");
        }
    }

    @SingleClick
    @OnClick(R.id.iv_page_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
        }
    }

    @Override
    public void getWorkDayMonthData(List<Month> monthList) {
        mMonthList = monthList;
        mMonthAdapter.setYear(yearTV.getText().toString().trim());
        mMonthAdapter.setMonthList(mMonthList);
    }

    @Override
    public void getWorkDayData(List<String> dayList) {
        Map<String, List<String>> dayMap = new HashMap<>();
        dayMap.put("班", dayList);
        InnerPainter calendarPainter = (InnerPainter) mCalendarView.getCalendarPainter();
        calendarPainter.setTagDayCopy(dayMap, 2);
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
