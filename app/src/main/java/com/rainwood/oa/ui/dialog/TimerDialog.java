package com.rainwood.oa.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PickerLayoutManager;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.MyAdapter;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author : a797s
 * time   : 2020/04/29
 * desc   : 日期时间控件
 */
public final class TimerDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>
            implements PickerLayoutManager.OnPickerListener, Runnable {

        private final int mStartYear = Calendar.getInstance().get(Calendar.YEAR) - 100;
        private final int mEndYear = Calendar.getInstance().get(Calendar.YEAR) + 100;
        private boolean hasIgnoreSecond = false;       // 是否忽略了秒数

        // 年月日
        private final RecyclerView mYearView;
        private final RecyclerView mMonthView;
        private final RecyclerView mDayView;

        // 时分秒
        private final RecyclerView mHourView;
        private final RecyclerView mMinutesView;
        private final RecyclerView mSecondView;

        private final PickerLayoutManager mYearManager;
        private final PickerLayoutManager mMonthManager;
        private final PickerLayoutManager mDayManager;

        private final PickerLayoutManager mHourManager;
        private final PickerLayoutManager mMinuteManager;
        private final PickerLayoutManager mSecondManager;

        private final PickerAdapter mYearAdapter;
        private final PickerAdapter mMonthAdapter;
        private final PickerAdapter mDayAdapter;

        private final PickerAdapter mHourAdapter;
        private final PickerAdapter mMinuteAdapter;
        private final PickerAdapter mSecondAdapter;

        @ViewInject(R.id.btn_clear_screen)
        private Button clearScreen;
        @ViewInject(R.id.btn_confirm)
        private Button confirm;
        @ViewInject(R.id.tv_date_timer)
        private EditText dateAndTime;

        private OnListener mListener;

        @SuppressLint("SetTextI18n")
        @SuppressWarnings("all")
        public Builder(Context context, boolean hasIgnoreSecond) {
            super(context);
            setCustomView(R.layout.dialog_timer);
            ViewBind.inject(this);
            setTitle(R.string.time_title);

            mYearView = findViewById(R.id.rv_date_year);
            mMonthView = findViewById(R.id.rv_date_month);
            mDayView = findViewById(R.id.rv_date_day);
            mHourView = findViewById(R.id.rv_date_hour);
            mMinutesView = findViewById(R.id.rv_date_minutes);
            mSecondView = findViewById(R.id.rv_date_second);

            mYearAdapter = new PickerAdapter(context);
            mMonthAdapter = new PickerAdapter(context);
            mDayAdapter = new PickerAdapter(context);

            mHourAdapter = new PickerAdapter(context);
            mMinuteAdapter = new PickerAdapter(context);
            mSecondAdapter = new PickerAdapter(context);

            // 设置自动聚焦
            dateAndTime.setFocusable(true);
            dateAndTime.setFocusableInTouchMode(true);

            // 生产年份
            ArrayList<String> yearData = new ArrayList<>(10);
            for (int i = mStartYear; i <= mEndYear; i++) {
                yearData.add(i + " " + getString(R.string.common_year));
            }

            // 生产月份
            ArrayList<String> monthData = new ArrayList<>(12);
            for (int i = 1; i <= 12; i++) {
                monthData.add(i + " " + getString(R.string.common_month));
            }

            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            int day = calendar.getActualMaximum(Calendar.DATE);
            // 生产天数
            ArrayList<String> dayData = new ArrayList<>(day);
            for (int i = 1; i <= day; i++) {
                dayData.add(i + " " + getString(R.string.common_day));
            }

            // 生产小时
            ArrayList<String> hourData = new ArrayList<>(24);
            for (int i = 0; i <= 23; i++) {
                hourData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_hour));
            }

            // 生产分钟
            ArrayList<String> minuteData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                minuteData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_minute));
            }

            // 生产秒钟
            ArrayList<String> secondData = new ArrayList<>(60);
            for (int i = 0; i <= 59; i++) {
                secondData.add((i < 10 ? "0" : "") + i + " " + getString(R.string.common_second));
            }

            mYearAdapter.setData(yearData);
            mMonthAdapter.setData(monthData);
            mDayAdapter.setData(dayData);
            mHourAdapter.setData(hourData);
            mMinuteAdapter.setData(minuteData);
            mSecondAdapter.setData(secondData);

            mYearManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mMonthManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mDayManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mHourManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mMinuteManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mSecondManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();

            mYearView.setLayoutManager(mYearManager);
            mMonthView.setLayoutManager(mMonthManager);
            mDayView.setLayoutManager(mDayManager);
            mHourView.setLayoutManager(mHourManager);
            mMinutesView.setLayoutManager(mMinuteManager);
            mSecondView.setLayoutManager(mSecondManager);

            mYearView.setAdapter(mYearAdapter);
            mMonthView.setAdapter(mMonthAdapter);
            mDayView.setAdapter(mDayAdapter);
            mHourView.setAdapter(mHourAdapter);
            mMinutesView.setAdapter(mMinuteAdapter);
            mSecondView.setAdapter(mSecondAdapter);

            setYear(calendar.get(Calendar.YEAR));
            setMonth(calendar.get(Calendar.MONTH) + 1);
            setDay(calendar.get(Calendar.DAY_OF_MONTH));
            setHour(calendar.get(Calendar.HOUR_OF_DAY));
            setMinute(calendar.get(Calendar.MINUTE));
            setSecond(calendar.get(Calendar.SECOND));

            postDelayed(this, 1000);

            mYearManager.setOnPickerListener(this);
            mMonthManager.setOnPickerListener(this);
            mDayManager.setOnPickerListener(this);
            mHourManager.setOnPickerListener(this);
            mMinuteManager.setOnPickerListener(this);
            mSecondManager.setOnPickerListener(this);

            clearScreen.setOnClickListener(this);
            confirm.setOnClickListener(this);

            // 设置EditText出现光标但是不调用软件盘
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(dateAndTime, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 设置开始时间默认聚焦（UI初始化时设置默认的日期）
            int index = dateAndTime.getHint().toString().length();
            if (index > 0) {
                dateAndTime.requestFocus();
                // dateAndTime.setSelection(index);
                dateAndTime.setText(calendar.get(Calendar.YEAR) + "-"
                        + (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                        + "-" + calendar.get(Calendar.MINUTE)
                        + (hasIgnoreSecond ? "" : "-" + calendar.get(Calendar.SECOND)));
            }
            // 开始时间焦点监听
            dateAndTime.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    dateAndTime.setText(calendar.get(Calendar.YEAR) + "-"
                            + (calendar.get(Calendar.MONTH) + 1)
                            + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                            + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                            + "-" + calendar.get(Calendar.MINUTE)
                            + (hasIgnoreSecond ? "" : "-" + calendar.get(Calendar.SECOND)));
                    dateAndTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    dateAndTime.setTextColor(getResources().getColor(R.color.fontColor));
                }
            });
        }

        @Override
        public void run() {
            if (mHourView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mMinutesView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE &&
                    mSecondView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, mHourManager.getPickedPosition());
                calendar.set(Calendar.MINUTE, mMinuteManager.getPickedPosition());
                calendar.set(Calendar.SECOND, mSecondManager.getPickedPosition());
                if (System.currentTimeMillis() - calendar.getTimeInMillis() < 3000) {
                    calendar = Calendar.getInstance();
                    setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    setMinute(calendar.get(Calendar.MINUTE));
                    setSecond(calendar.get(Calendar.SECOND));
                    postDelayed(this, 1000);
                }
            }
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * 不选择天数
         */
        public Builder setIgnoreDay() {
            mDayView.setVisibility(View.GONE);
            return this;
        }

        public Builder setDate(long date) {
            LogUtils.d(this, "-----> " + date);
            if (date > 0) {
                setDate(new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date(date)));
            }
            return this;
        }

        public Builder setDate(String date) {
            // 20190519
            if (date.matches("\\d{8}")) {
                setYear(date.substring(0, 4));
                setMonth(date.substring(4, 6));
                setDay(date.substring(6, 8));
                // 2019-05-19
            } else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                setYear(date.substring(0, 4));
                setMonth(date.substring(5, 7));
                setDay(date.substring(8, 10));
            }
            return this;
        }

        public Builder setYear(String year) {
            return setYear(Integer.valueOf(year));
        }

        public Builder setYear(int year) {
            int index = year - mStartYear;
            if (index < 0) {
                index = 0;
            } else if (index > mYearAdapter.getItemCount() - 1) {
                index = mYearAdapter.getItemCount() - 1;
            }
            mYearView.scrollToPosition(index);
            return this;
        }

        public Builder setMonth(String month) {
            return setMonth(Integer.valueOf(month));
        }

        public Builder setMonth(int month) {
            int index = month - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mMonthAdapter.getItemCount() - 1) {
                index = mMonthAdapter.getItemCount() - 1;
            }
            mMonthView.scrollToPosition(index);
            return this;
        }

        public Builder setDay(String day) {
            return setDay(Integer.valueOf(day));
        }

        public Builder setDay(int day) {
            int index = day - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mDayAdapter.getItemCount() - 1) {
                index = mDayAdapter.getItemCount() - 1;
            }
            mDayView.scrollToPosition(index);
            return this;
        }

        /**
         * 不选择秒数
         */
        public Builder setIgnoreSecond() {
            mSecondView.setVisibility(View.GONE);
            hasIgnoreSecond = true;
            return this;
        }

        public Builder setTime(String time) {
            // 102030
            if (time.matches("\\d{6}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(2, 4));
                setSecond(time.substring(4, 6));
                // 10:20:30
            } else if (time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                setHour(time.substring(0, 2));
                setMinute(time.substring(3, 5));
                setSecond(time.substring(6, 8));
            }
            return this;
        }

        public Builder setHour(String hour) {
            return setHour(Integer.valueOf(hour));
        }

        public Builder setHour(int hour) {
            int index = hour;
            if (index < 0 || hour == 24) {
                index = 0;
            } else if (index > mHourAdapter.getItemCount() - 1) {
                index = mHourAdapter.getItemCount() - 1;
            }
            mHourView.scrollToPosition(index);
            return this;
        }

        public Builder setMinute(String minute) {
            return setMinute(Integer.valueOf(minute));
        }

        public Builder setMinute(int minute) {
            int index = minute;
            if (index < 0) {
                index = 0;
            } else if (index > mMinuteAdapter.getItemCount() - 1) {
                index = mMinuteAdapter.getItemCount() - 1;
            }
            mMinutesView.scrollToPosition(index);
            return this;
        }

        public Builder setSecond(String second) {
            return setSecond(Integer.valueOf(second));
        }

        public Builder setSecond(int second) {
            int index = second;
            if (index < 0) {
                index = 0;
            } else if (index > mSecondAdapter.getItemCount() - 1) {
                index = mSecondAdapter.getItemCount() - 1;
            }
            mSecondView.scrollToPosition(index);
            return this;
        }

        /**
         * {@link PickerLayoutManager.OnPickerListener}
         * recyclerView滑动监听
         *
         * @param recyclerView RecyclerView 对象
         * @param position     当前滚动的位置
         */
        @SuppressLint("SetTextI18n")
        @Override
        public void onPicked(RecyclerView recyclerView, int position) {
            Calendar calendar = Calendar.getInstance();
            if (recyclerView == mYearView) {
                calendar.set(mStartYear + position, mMonthManager.getPickedPosition(), 1);
            } else if (recyclerView == mMonthView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), position, 1);
            } else if (recyclerView == mDayView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(), position);
            } else if (recyclerView == mHourView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(), mDayManager.getPickedPosition(), position, 1);
            } else if (recyclerView == mMinutesView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(),
                        mDayManager.getPickedPosition(), mHourManager.getPickedPosition(), position);
            } else if (recyclerView == mSecondView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(),
                        mDayManager.getPickedPosition(), mHourManager.getPickedPosition(), mMinuteManager.getPickedPosition(), position);
            }

            int day = calendar.getActualMaximum(Calendar.DATE);
            if (mDayAdapter.getItemCount() != day) {
                ArrayList<String> dayData = new ArrayList<>(day);
                for (int i = 1; i <= day; i++) {
                    dayData.add(i + " " + getString(R.string.common_day));
                }
                mDayAdapter.setData(dayData);
            }
            // 有焦点则说明值需更改，没有焦点的时候保留之前的（滚动改变值）
            dateAndTime.setText(dateAndTime.hasFocus() ? (mStartYear + mYearManager.getPickedPosition()
                    + "-" + (mMonthManager.getPickedPosition() + 1)
                    + "-" + (mDayManager.getPickedPosition() + 1))
                    + "-" + mHourManager.getPickedPosition()
                    + "-" + mMinuteManager.getPickedPosition()
                    + (hasIgnoreSecond ? "" : "-" + calendar.get(Calendar.SECOND))
                    : (TextUtils.isEmpty(dateAndTime.getText()) ? "" : dateAndTime.getText().toString().trim()));
        }


        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(), mDayManager.getPickedPosition(),
                                mHourManager.getPickedPosition(), mMinuteManager.getPickedPosition(), mSecondManager.getPickedPosition());
                    }
                    break;
                case R.id.tv_ui_cancel:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                case R.id.btn_clear_screen:         // 清空筛选
                    autoDismiss();
                    LogUtils.d("sxs", "清空筛选....");
                    if (!TextUtils.isEmpty(dateAndTime.getText())) {
                        dateAndTime.setText("");
                    }
                    break;
                case R.id.btn_confirm:          // 确定选择
                    autoDismiss();
                    if (mListener != null) {
                        if (TextUtils.isEmpty(dateAndTime.getText())){
                            ToastUtils.show("当前没有选择时间");
                            return;
                        }
                        mListener.onSelected(getDialog(), mStartYear + mYearManager.getPickedPosition(),
                                mMonthManager.getPickedPosition() + 1,
                                mDayManager.getPickedPosition() + 1,
                                mHourManager.getPickedPosition(),
                                mMinuteManager.getPickedPosition(),
                                mSecondManager.getPickedPosition());
                    }
                    break;
                case R.id.iv_dialog_close:      // dialog关闭
                    // autoDismiss();
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                default:
                    break;
            }
        }

        private static final class PickerAdapter extends MyAdapter<String> {

            private PickerAdapter(Context context) {
                super(context);
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ViewHolder();
            }

            final class ViewHolder extends MyAdapter.ViewHolder {

                private final TextView mPickerView;

                ViewHolder() {
                    super(R.layout.item_picker);
                    mPickerView = (TextView) findViewById(R.id.tv_picker_name);
                }

                @Override
                public void onBindView(int position) {
                    mPickerView.setText(getItem(position));
                }
            }
        }
    }

    public interface OnListener {

        /**
         * 选择完日期后回调
         *
         * @param year  年
         * @param month 月
         * @param day   日
         */
        default void onSelected(BaseDialog dialog, int year, int month, int day, int hour, int minutes, int second) {
        }

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

    }
}