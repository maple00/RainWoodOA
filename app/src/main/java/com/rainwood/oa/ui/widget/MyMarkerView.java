package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.HomeSalaryDesc;
import com.rainwood.oa.ui.adapter.HomeSalaryDescAdapter;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/15 16:25
 * @Desc:
 */
public final class MyMarkerView extends MarkerView {


    private List<String> monthList;
    private TextView tvContent;
    private RecyclerView mSalaryDesc;
    private HomeSalaryDescAdapter mSalaryDescAdapter;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tv_content);
    }

    /**
     * 重写布局
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public MyMarkerView(Context context, int layoutResource, List<String> monthList) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tv_content);
        mSalaryDesc = findViewById(R.id.rv_desc_salary);
        initRecyclerView(context);
        this.monthList = monthList;
    }

    private void initRecyclerView(Context context) {
        mSalaryDesc.setLayoutManager(new GridLayoutManager(context, 1));
        mSalaryDesc.addItemDecoration(new SpacesItemDecoration(0,0,0,0));
        mSalaryDescAdapter = new HomeSalaryDescAdapter();
        mSalaryDesc.setAdapter(mSalaryDescAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        LogUtils.d("sxs", "--- entry --- " + e);
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvContent.setText("" + monthList.get((int) e.getX()));
            List<HomeSalaryDesc> descList = new ArrayList<>();
            descList.add(new HomeSalaryDesc(e.getY(), "工资："));
            mSalaryDescAdapter.setDescList(descList);
        }
    }

}