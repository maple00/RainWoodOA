package com.rainwood.oa.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PickerLayoutManager;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.MyAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: a797s
 * @Date: 2020/7/22 13:17
 * @Desc:
 */
public final class BottomYearDialog {

    public static final class Builder
            extends UIDialog.Builder<Builder>
            implements PickerLayoutManager.OnPickerListener {
        // 日期开始时间
        private final int mStartYear = 2014;
        // 日期结束时间
        private final int mEndYear = Calendar.getInstance().get(Calendar.YEAR) + 100;

        private final RecyclerView mYearStartView;
        private final RecyclerView mYearEndView;

        private final PickerLayoutManager mYearStartManager;
        private final PickerLayoutManager mYearEndManager;

        private final PickerAdapter mYearAdapter;

        @ViewInject(R.id.tv_cancel)
        private TextView mTextCancel;
        @ViewInject(R.id.tv_confirm)
        private TextView mTextConfirm;

        private OnListener mListener;

        @SuppressLint("SetTextI18n")
        public Builder(Context context, boolean hasIgnoreDay) {
            super(context);
            setCustomView(R.layout.dialog_bottom_year);
            ViewBind.inject(this);
            setTitle(R.string.time_title);

            mYearStartView = findViewById(R.id.rv_start_year);
            mYearEndView = findViewById(R.id.rv_end_year);

            mYearAdapter = new PickerAdapter(context);

            // 生产年份
            ArrayList<String> yearData = new ArrayList<>(10);
            for (int i = mStartYear; i <= mEndYear; i++) {
                yearData.add(i + " " + getString(R.string.common_year));
            }

            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            mYearAdapter.setData(yearData);
            mYearStartManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mYearEndManager = new PickerLayoutManager.Builder(context)
                    .setMaxItem(5)
                    .build();
            mYearStartView.setLayoutManager(mYearStartManager);
            mYearEndView.setLayoutManager(mYearEndManager);

            mYearStartView.setAdapter(mYearAdapter);
            mYearEndView.setAdapter(mYearAdapter);
            setYear(calendar.get(Calendar.YEAR));

            mYearStartManager.setOnPickerListener(this);
            mYearEndManager.setOnPickerListener(this);
            setOnClickListener(R.id.tv_cancel, R.id.tv_confirm);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
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
                // 2019-05-19
            } else if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                setYear(date.substring(0, 4));
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
            mYearStartView.scrollToPosition(index);
            mYearEndView.scrollToPosition(index);
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
            if (recyclerView == mYearStartView) {

            }
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancel:
                    if (getDialog() != null) {
                        getDialog().dismiss();
                    }
                    break;
                case R.id.tv_confirm:
                    mListener.onSelected(getDialog(), String.valueOf(mStartYear + mYearStartManager.getPickedPosition()),
                            String.valueOf(mStartYear + mYearEndManager.getPickedPosition()));
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
         * 选择完成后的日期段回调
         *
         * @param startTime 开始时间
         * @param endTime   结束时间
         */
        void onSelected(BaseDialog dialog, String startTime, String endTime);

    }
}