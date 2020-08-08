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
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PickerLayoutManager;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.DateTimeUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.MyAdapter;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * author : a797s
 * time   : 2020/04/29
 * desc   : 自定义日期选择对话框
 */
public final class StartEndDateDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>
            implements PickerLayoutManager.OnPickerListener {
        // 日期开始时间
        private final int mStartYear = 2014;
        // 日期结束时间
        private final int mEndYear = Calendar.getInstance().get(Calendar.YEAR) + 100;
        private boolean hasIgnoreDay = false;       // 是否忽略了天

        private final RecyclerView mYearView;
        private final RecyclerView mMonthView;
        private final RecyclerView mDayView;

        private final PickerLayoutManager mYearManager;
        private final PickerLayoutManager mMonthManager;
        private final PickerLayoutManager mDayManager;

        private final PickerAdapter mYearAdapter;
        private final PickerAdapter mMonthAdapter;
        private final PickerAdapter mDayAdapter;

        @ViewInject(R.id.btn_clear_screen)
        private Button clearScreen;
        @ViewInject(R.id.btn_confirm)
        private Button confirm;
        @ViewInject(R.id.et_start_time)
        private EditText startTime;
        @ViewInject(R.id.et_end_time)
        private EditText endTime;

        private OnListener mListener;

        @SuppressLint("SetTextI18n")
        public Builder(Context context, boolean hasIgnoreDay) {
            super(context);
            setCustomView(R.layout.dialog_start_end_date);
            ViewBind.inject(this);
            setTitle(R.string.time_title);

            mYearView = findViewById(R.id.rv_date_year);
            mMonthView = findViewById(R.id.rv_date_month);
            mDayView = findViewById(R.id.rv_date_day);

            mYearAdapter = new PickerAdapter(context);
            mMonthAdapter = new PickerAdapter(context);
            mDayAdapter = new PickerAdapter(context);

            // 设置自动聚焦
            startTime.setFocusable(true);
            startTime.setFocusableInTouchMode(true);

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

            mYearAdapter.setData(yearData);
            mMonthAdapter.setData(monthData);
            mDayAdapter.setData(dayData);

            mYearManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mMonthManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mDayManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();

            mYearView.setLayoutManager(mYearManager);
            mMonthView.setLayoutManager(mMonthManager);
            mDayView.setLayoutManager(mDayManager);

            mYearView.setAdapter(mYearAdapter);
            mMonthView.setAdapter(mMonthAdapter);
            mDayView.setAdapter(mDayAdapter);

            setYear(calendar.get(Calendar.YEAR));
            setMonth(calendar.get(Calendar.MONTH) + 1);
            setDay(calendar.get(Calendar.DAY_OF_MONTH));

            mYearManager.setOnPickerListener(this);
            mMonthManager.setOnPickerListener(this);
            mDayManager.setOnPickerListener(this);
            clearScreen.setOnClickListener(this);
            confirm.setOnClickListener(this);

            // 设置EditText出现光标但是不调用软件盘
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(startTime, false);
                setSoftInputShownOnFocus.invoke(endTime, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 设置开始时间默认聚焦（UI初始化时设置默认的日期）
            int index = startTime.getHint().toString().length();
            if (index > 0) {
                startTime.requestFocus();
                // startTime.setSelection(index);
                startTime.setText(calendar.get(Calendar.YEAR) + "-"
                        + ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" + (calendar.get(Calendar.MONTH) + 1) : (calendar.get(Calendar.MONTH) + 1))
                        + (hasIgnoreDay ? "" : "-" + calendar.get(Calendar.DAY_OF_MONTH)));
            }
            // 结束时间焦点监听
            endTime.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    String endTimeStr = endTime.getText().toString();
                    endTime.setText(endTimeStr);
                    if (!TextUtils.isEmpty(endTime.getText().toString())) {
                        Date date = DateTimeUtils.stringToDate(endTimeStr,
                                hasIgnoreDay ? DateTimeUtils.DatePattern.ONLY_MONTH : DateTimeUtils.DatePattern.ONLY_DAY);
                        int year = date.getYear() + 1900;
                        int month = date.getMonth();
                        setYear(year);
                        setMonth(month + 1);
                        if (!hasIgnoreDay) {
                            // 没有忽略天
                            setDay(date.getDate());
                        }
                    } else {
                        String endTimeEmptyStr = endTime.hasFocus() ? (mStartYear + mYearManager.getPickedPosition()
                                + "-" + ((mMonthManager.getPickedPosition() + 1) < 10 ? "0"
                                + (mMonthManager.getPickedPosition() + 1) : (mMonthManager.getPickedPosition() + 1))
                                + (hasIgnoreDay ? "" : "-" + ((mDayManager.getPickedPosition() + 1) < 10 ? "0"
                                + (mDayManager.getPickedPosition() + 1) : (mDayManager.getPickedPosition() + 1))))
                                : (TextUtils.isEmpty(endTime.getText()) ? "" : endTime.getText().toString().trim());
                        endTime.setText(endTimeEmptyStr);
                    }
                    endTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    endTime.setTextColor(getResources().getColor(R.color.fontColor));
                }
            });
            // 开始时间焦点监听
            startTime.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    String startTimeStr = startTime.getText().toString();
                    startTime.setText(startTimeStr);
                    if (!TextUtils.isEmpty(startTimeStr)) {
                        Date date = DateTimeUtils.stringToDate(startTimeStr,
                                hasIgnoreDay ? DateTimeUtils.DatePattern.ONLY_MONTH : DateTimeUtils.DatePattern.ONLY_DAY);
                        int year = date.getYear() + 1900;
                        int month = date.getMonth();
                        setYear(year);
                        setMonth(month + 1);
                        if (!hasIgnoreDay) {
                            // 没有忽略天
                            setDay(date.getDate());
                        }
                        startTime.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        String startEmptyStr = startTime.hasFocus() ? (mStartYear + mYearManager.getPickedPosition()
                                + "-" + ((mMonthManager.getPickedPosition() + 1) < 10 ? "0"
                                + (mMonthManager.getPickedPosition() + 1) : (mMonthManager.getPickedPosition() + 1))
                                + (hasIgnoreDay ? "" : "-" + ((mDayManager.getPickedPosition() + 1) < 10 ? "0"
                                + (mDayManager.getPickedPosition() + 1) : (mDayManager.getPickedPosition() + 1))))
                                : (TextUtils.isEmpty(endTime.getText()) ? "" : endTime.getText().toString().trim());
                        startTime.setText(startEmptyStr);
                    }
                } else {
                    startTime.setTextColor(getResources().getColor(R.color.fontColor));
                }
            });

            // 按钮置灰

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
            hasIgnoreDay = true;
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
            return setYear(Integer.parseInt(year));
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
            return setMonth(Integer.parseInt(month));
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
            return setDay(Integer.parseInt(day));
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

        public Builder setStartTime(String startTime) {
            LogUtils.d("sxs", "----------- startTime ------- " + startTime);
            this.startTime.setText(startTime);
            if (!TextUtils.isEmpty(startTime)) {
                Date date = DateTimeUtils.stringToDate(startTime, DateTimeUtils.DatePattern.ONLY_MONTH);
                int year = date.getYear() + 1900;
                int month = date.getMonth();
                setYear(year);
                setMonth(month + 1);
            }
            return this;
        }

        public Builder setEndTime(String endTime) {
            this.endTime.setText(endTime);
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
            // 获取这个月最多有多少天
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (recyclerView == mYearView) {
                calendar.set(mStartYear + position, mMonthManager.getPickedPosition(), 1);
            } else if (recyclerView == mMonthView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), position, 1);
            } else if (recyclerView == mDayView) {
                calendar.set(mStartYear + mYearManager.getPickedPosition(), mMonthManager.getPickedPosition(), position);
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
            startTime.setText(startTime.hasFocus() ? (mStartYear + mYearManager.getPickedPosition()
                    + "-" + ((mMonthManager.getPickedPosition() + 1) < 10 ? "0"
                    + (mMonthManager.getPickedPosition() + 1) : (mMonthManager.getPickedPosition() + 1))
                    + (hasIgnoreDay ? "" : "-" + ((mDayManager.getPickedPosition() + 1) < 10 ? "0"
                    + (mDayManager.getPickedPosition() + 1) : (mDayManager.getPickedPosition() + 1))))
                    : (TextUtils.isEmpty(startTime.getText()) ? "" : startTime.getText().toString().trim()));
            endTime.setText(endTime.hasFocus() ? (mStartYear + mYearManager.getPickedPosition()
                    + "-" + ((mMonthManager.getPickedPosition() + 1) < 10 ? "0"
                    + (mMonthManager.getPickedPosition() + 1) : (mMonthManager.getPickedPosition() + 1))
                    + (hasIgnoreDay ? "" : "-" + ((mDayManager.getPickedPosition() + 1) < 10 ? "0"
                    + (mDayManager.getPickedPosition() + 1) : (mDayManager.getPickedPosition() + 1))))
                    : (TextUtils.isEmpty(endTime.getText()) ? "" : endTime.getText().toString().trim()));
        }


        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ui_confirm:
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), mStartYear + mYearManager.getPickedPosition(),
                                mMonthManager.getPickedPosition() + 1, mDayManager.getPickedPosition() + 1);
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
                    if (!TextUtils.isEmpty(startTime.getText())) {
                        startTime.setText("");
                    }
                    if (!TextUtils.isEmpty(endTime.getText())) {
                        endTime.setText("");
                    }
                    break;
                case R.id.btn_confirm:          // 确定选择
                    autoDismiss();
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), startTime.getText().toString().trim(), endTime.getText().toString().trim());
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
        default void onSelected(BaseDialog dialog, int year, int month, int day) {
        }

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

        /**
         * 选择完成后的日期段回调
         *
         * @param startTime 开始时间
         * @param endTime   结束时间
         */
        void onSelected(BaseDialog dialog, String startTime, String endTime);

    }
}