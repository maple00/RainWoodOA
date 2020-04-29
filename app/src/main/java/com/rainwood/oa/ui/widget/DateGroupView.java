package com.rainwood.oa.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.PickerLayoutManager;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @Author: a797s
 * @Date: 2020/4/29 16:08
 * @Desc: 自定义日期控件
 */
public final class DateGroupView extends Dialog implements PickerLayoutManager.OnPickerListener {

    private Context mContext;

    /**
     * 点击外部是否消失
     */
    private boolean isTouchOutCancel;

    /**
     * 开始时间、结束时间
     */
    private static final int mStartYear = Calendar.getInstance().get(Calendar.YEAR) - 100;
    private static final int mEndYear = Calendar.getInstance().get(Calendar.YEAR);

    @ViewInject(R.id.rv_date_year)
    private static RecyclerView mYearView;
    @ViewInject(R.id.rv_date_month)
    private static RecyclerView mMonthView;
    @ViewInject(R.id.rv_date_day)
    private static RecyclerView mDayView;

    private static DateSelectedAdapter mYearAdapter;
    private static DateSelectedAdapter mMonthAdapter;
    private static DateSelectedAdapter mDayAdapter;

    private PickerLayoutManager mYearManager;
    private PickerLayoutManager mMonthManager;
    private PickerLayoutManager mDayManager;

    public void setTouchOutCancel(boolean touchOutCancel) {
        isTouchOutCancel = touchOutCancel;
    }

    public DateGroupView(@NonNull Context context) {
        this(context, 0);
    }

    public DateGroupView(@NonNull Context context, int themeResId) {
        this(context, true, null);
    }

    protected DateGroupView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View itemView = View.inflate(mContext, R.layout.dialog_date_group, null);
        ViewBind.inject(this, itemView);
        setContentView(itemView);
        //
        setCanceledOnTouchOutside(isTouchOutCancel);
        Window win = getWindow();
        WindowManager.LayoutParams winAttributes = win.getAttributes();
        winAttributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        winAttributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(winAttributes);
        // initial时间
        initView();
    }

    private void initView() {
        mYearManager = new PickerLayoutManager.Builder(mContext)
                .build();
        mMonthManager = new PickerLayoutManager.Builder(mContext)
                .build();
        mDayManager = new PickerLayoutManager.Builder(mContext)
                .build();

        // 生产年份
        ArrayList<String> yearData = new ArrayList<>(10);
        for (int i = mStartYear; i <= mEndYear; i++) {
            yearData.add(i + " " + mContext.getResources().getString(R.string.common_year));
        }

        // 生产月份
        ArrayList<String> monthData = new ArrayList<>(12);
        for (int i = 1; i <= 12; i++) {
            monthData.add(i + " " + mContext.getResources().getString(R.string.common_month));
        }

        Calendar calendar = Calendar.getInstance(Locale.CHINA);

        int day = calendar.getActualMaximum(Calendar.DATE);
        // 生产天数
        ArrayList<String> dayData = new ArrayList<>(day);
        for (int i = 1; i <= day; i++) {
            dayData.add(i + " " + mContext.getResources().getString(R.string.common_day));
        }
        initAdapter(yearData, monthData, dayData);

        // 数据监听
        setYear(calendar.get(Calendar.YEAR));
        setMonth(calendar.get(Calendar.MONTH) + 1);
        setDay(calendar.get(Calendar.DAY_OF_MONTH));

        mYearManager.setOnPickerListener(this);
        mMonthManager.setOnPickerListener(this);
    }

    private void setDay(int day) {
        int index = day - 1;
        if (index < 0) {
            index = 0;
        } else if (index > mDayAdapter.getItemCount() - 1) {
            index = mDayAdapter.getItemCount() - 1;
        }
        mDayView.scrollToPosition(index);
    }

    private void setMonth(int month) {
        int index = month - 1;
        if (index < 0) {
            index = 0;
        } else if (index > mMonthAdapter.getItemCount() - 1) {
            index = mMonthAdapter.getItemCount() - 1;
        }
        mMonthView.scrollToPosition(index);
    }

    private void setYear(int year) {
        int index = year - mStartYear;
        if (index < 0) {
            index = 0;
        } else if (index > mYearAdapter.getItemCount() - 1) {
            index = mYearAdapter.getItemCount() - 1;
        }
        mYearView.scrollToPosition(index);
    }

    private void initAdapter(ArrayList<String> yearData, ArrayList<String> monthData, ArrayList<String> dayData) {
        // 设置布局管理器
        mYearView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mYearView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = FontSwitchUtil.dip2px(mContext, 0f);
                outRect.bottom = FontSwitchUtil.dip2px(mContext, 15f);
            }
        });
        mMonthView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mMonthView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = FontSwitchUtil.dip2px(mContext, 0f);
                outRect.bottom = FontSwitchUtil.dip2px(mContext, 15f);
            }
        });
        mDayView.setLayoutManager(new GridLayoutManager(mContext, 1));
        mDayView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = FontSwitchUtil.dip2px(mContext, 0f);
                outRect.bottom = FontSwitchUtil.dip2px(mContext, 15f);
            }
        });
        // 创建适配器
        mYearAdapter = new DateSelectedAdapter();
        mMonthAdapter = new DateSelectedAdapter();
        mDayAdapter = new DateSelectedAdapter();
        // 设置适配器
        mYearView.setAdapter(mYearAdapter);
        mMonthView.setAdapter(mMonthAdapter);
        mDayView.setAdapter(mDayAdapter);
        // 设置数据
        mYearAdapter.setDateList(yearData);
        mMonthAdapter.setDateList(monthData);
        mDayAdapter.setDateList(dayData);
    }

    @Override
    public void onPicked(RecyclerView recyclerView, int position) {
        // 获取这个月最多有多少天
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        if (recyclerView == mYearView) {
            calendar.set(mStartYear + position, mMonthManager.getPickedPosition(), 1);
        } else if (recyclerView == mMonthView) {
            calendar.set(mStartYear + mYearManager.getPickedPosition(), position, 1);
        }

        int day = calendar.getActualMaximum(Calendar.DATE);
        if (mDayAdapter.getItemCount() != day) {
            ArrayList<String> dayData = new ArrayList<>(day);
            for (int i = 1; i <= day; i++) {
                dayData.add(i + " " + mContext.getResources().getString(R.string.common_day));
            }
            mDayAdapter.setDateList(dayData);
        }
    }

    /**
     * 时间选择器
     */
    private static final class DateSelectedAdapter extends RecyclerView.Adapter<DateSelectedAdapter.ViewHolder> {

        private List<String> dateList;

        public void setDateList(List<String> dateList) {
            this.dateList = dateList;
        }

        @NonNull
        @Override
        public DateSelectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View dateView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_picker, parent, false);
            return new DateSelectedAdapter.ViewHolder(dateView);
        }

        @Override
        public void onBindViewHolder(@NonNull DateSelectedAdapter.ViewHolder holder, int position) {
            holder.pickerName.setText(dateList.get(position));
        }

        @Override
        public int getItemCount() {
            return dateList == null ? 0 : dateList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.tv_picker_name)
            private TextView pickerName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ViewBind.inject(this, itemView);
            }
        }
    }


    public interface OnDailogListener {
        /**
         * 完成回调
         */
        void onSelected(Dialog dialog);

        /**
         * 取消时回调
         */
        default void onCancel() {
        }
    }

}
