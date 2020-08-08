package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.rainwood.oa.R;
import com.rainwood.oa.ui.adapter.HomeSalaryDescAdapter;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.utils.FontSwitchUtil;

/**
 * @Author: a797s
 * @Date: 2020/7/24 15:51
 * @Desc:
 */
@SuppressLint("ViewConstructor")
public final class MyChartMarkerView extends MarkerView {

    private final TextView tvPerson;
    private final RecyclerView valuesListView;
    private CallBack mCallBack;
    private Context context;
    private HomeSalaryDescAdapter mSalaryDescAdapter;

    public MyChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        tvPerson = findViewById(R.id.tv_content);
        valuesListView = findViewById(R.id.rv_desc_salary);
        // 设置布局管理器和适配器
        valuesListView.setLayoutManager(new GridLayoutManager(context, 1));
        valuesListView.addItemDecoration(new SpacesItemDecoration(0,0,0,0));
        mSalaryDescAdapter = new HomeSalaryDescAdapter();
        valuesListView.setAdapter(mSalaryDescAdapter);

    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String values;
        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            System.out.println(e.getY());
            values = "" + Utils.formatNumber(ce.getHigh(), 0, true);
        } else {
            values = "" + Utils.formatNumber(e.getY(), 0, true);
        }
        if (mCallBack != null) {
            mCallBack.onCallBack(e.getX(), values);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface CallBack {
        void onCallBack(float x, String value);
    }

    public TextView getTvPerson() {
        return tvPerson;
    }

    public HomeSalaryDescAdapter getTvFirm() {
        return mSalaryDescAdapter;
    }

    /**
     * 重写绘制方法
     */
    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        MPPointF mpPointF = new MPPointF();
        Chart chart = getChartView();
        float width = getWidth();
        float chartWidth = chart.getWidth();/////调用的时候这边可能会把空指针
        float chartHeight = chart.getHeight();
        //右边超出的情况，显示一个，隐藏一个，设置对应的偏移量
        if (chartWidth < posX + width) {
            mpPointF.x = FontSwitchUtil.dip2px(context, 5f) - getWidth();
            mpPointF.y = -getHeight() + FontSwitchUtil.dip2px(context, 5f);
        } else {//右边没有超出的情况，同上
            mpPointF.x = -FontSwitchUtil.dip2px(context, 5f);
            mpPointF.y = -getHeight() + FontSwitchUtil.dip2px(context, 5f);
        }
        //绘制方法是直接复制过来的，没动
        int saveId = canvas.save();
        // translate to the correct position and draw
        //canvas.translate(posX + mpPointF.x, posY + mpPointF.y);
        ////////////因为上面和下面也会出现遮挡的效果，所以我这边直接将M a r ke r Vi e w显示的Y轴的高度固定了///
        canvas.translate(posX + mpPointF.x, chartHeight / 4);
        draw(canvas);
        canvas.restoreToCount(saveId);
    }
}
