package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.BaseDialogFragment;
import com.rainwood.tools.wheel.LoopView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: shearson
 * @time: 2019/12/3 11:51
 * @des: 日期对话框
 */
public final class DateDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder>
            implements LoopView.LoopScrollListener,
            View.OnClickListener {

        private final int mStartYear = 1920;
        private final int mEndYear = 2019;

        private final Button mCancelView;
        private final Button mConfirmView;
        private final EditText mStartTime;
        private final EditText mEntTime;

        private final LoopView mYearView;
        private final LoopView mMonthView;
        private final LoopView mDayView;

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        @SuppressWarnings("all")
        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_date);

            mStartTime = findViewById(R.id.et_start_time);
            mEntTime = findViewById(R.id.et_end_time);

            mCancelView = findViewById(R.id.btn_clear_screen);
            mConfirmView = findViewById(R.id.btn_confirm);

            mYearView = findViewById(R.id.lv_date_year);
            mMonthView = findViewById(R.id.lv_date_month);
            mDayView = findViewById(R.id.lv_date_day);

            // 设置EditText出现光标但是不调用软件盘
            try {
                Class<EditText> cls = EditText.class;
                Method setSoftInputShownOnFocus;
                setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setSoftInputShownOnFocus.setAccessible(true);
                setSoftInputShownOnFocus.invoke(mStartTime, false);
                setSoftInputShownOnFocus.invoke(mEntTime, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

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

            mYearView.setData(yearData);
            mMonthView.setData(monthData);

            mYearView.setLoopListener(this);
            mMonthView.setLoopListener(this);

            mCancelView.setOnClickListener(this);
            mConfirmView.setOnClickListener(this);

            Calendar calendar = Calendar.getInstance();
            setYear(calendar.get(Calendar.YEAR));
            setMonth(calendar.get(Calendar.MONTH) + 1);
            setDay(calendar.get(Calendar.DAY_OF_MONTH));
        }

        public Builder setTitle(@StringRes int id) {
            return setTitle(getString(id));
        }

        public Builder setTitle(CharSequence text) {
            // mTitleView.setText(text);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);
            return this;
        }

        public Builder setConfirm(@StringRes int id) {
            return setConfirm(getString(id));
        }

        public Builder setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
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
            } else if (index > mYearView.getSize() - 1) {
                index = mYearView.getSize() - 1;
            }
            mYearView.setInitPosition(index);
            return this;
        }

        public Builder setMonth(String month) {
            return setMonth(Integer.valueOf(month));
        }

        public Builder setMonth(int month) {
            int index = month - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mMonthView.getSize() - 1) {
                index = mMonthView.getSize() - 1;
            }
            mMonthView.setInitPosition(index);
            return this;
        }

        public Builder setDay(String day) {
            return setDay(Integer.valueOf(day));
        }

        public Builder setDay(int day) {
            int index = day - 1;
            if (index < 0) {
                index = 0;
            } else if (index > mDayView.getSize() - 1) {
                index = mDayView.getSize() - 1;
            }
            mDayView.setInitPosition(index);
            return this;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onItemSelect(LoopView loopView, int position) {
            // 获取这个月最多有多少天
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (loopView == mYearView) {
                calendar.set(mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem(), 1);
            } else if (loopView == mMonthView) {
                calendar.set(mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem(), 1);
            }

            int day = calendar.getActualMaximum(Calendar.DATE);

            ArrayList<String> dayData = new ArrayList<>(day);
            for (int i = 1; i <= day; i++) {
                dayData.add(i + " " + getString(R.string.common_day));
            }
            mDayView.setData(dayData);
            // 根据焦点赋值
            // 判断聚焦的EditText
            if (mStartTime.hasFocus()) {
                LogUtils.d("sxs", "开始时间有焦点");
                mStartTime.setText(mStartYear + mYearView.getSelectedItem() + "-" + (mMonthView.getSelectedItem() + 1) + "-" + (mDayView.getSelectedItem() + 1));
            } else {
                LogUtils.d("sxs", "没有焦点");
            }
            if (mEntTime.hasFocus()) {
                LogUtils.d("sxs", "结束时间有焦点");
                mEntTime.setText(mStartYear + mYearView.getSelectedItem() + "-" + (mMonthView.getSelectedItem() + 1) + "-" + (mDayView.getSelectedItem() + 1));
            } else {
                LogUtils.d("sxs", "结束没有焦点");
            }
        }

        /**
         * {@link View.OnClickListener}
         */

        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                if (v == mConfirmView) {
                    mListener.onSelected(getDialog(), mStartYear + mYearView.getSelectedItem(), mMonthView.getSelectedItem() + 1, mDayView.getSelectedItem() + 1);
                } else if (v == mCancelView) {
                    mListener.onCancel(getDialog());
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
        void onSelected(BaseDialog dialog, int year, int month, int day);

        /**
         * 点击取消时回调
         */
        void onCancel(BaseDialog dialog);
    }
}
