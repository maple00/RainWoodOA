package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.widget.RelativeLayout;
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
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.BalanceByMonthOrYear;
import com.rainwood.oa.model.domain.HomeSalaryDesc;
import com.rainwood.oa.model.domain.StaffCurve;
import com.rainwood.oa.ui.adapter.HomeSalaryDescAdapter;
import com.rainwood.oa.ui.fragment.HomeFragment;
import com.rainwood.oa.utils.ListUtils;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/6/15 16:25
 * @Desc:
 */
@SuppressLint("ViewConstructor")
public final class MyMarkerView extends MarkerView {

    private List<StaffCurve> mStaffNumValues;
    private BaseFragment fragment;
    private List<BalanceByMonthOrYear> monthBalanceList;
    private List<String> monthList;
    private TextView tvContent;
    private RecyclerView mSalaryDesc;
    private HomeSalaryDescAdapter mSalaryDescAdapter;
    private Context mContext;
    private RelativeLayout markerParent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource   the layout resource to use for the MarkerView
     * @param balanceYearMonth
     * @param monthBalanceList
     */
    public MyMarkerView(Context context, int layoutResource, List<String> balanceYearMonth, List<BalanceByMonthOrYear> monthBalanceList,List<StaffCurve> yValues) {
        super(context, layoutResource);
        mContext = context;
        tvContent = findViewById(R.id.tv_content);
        mSalaryDesc = findViewById(R.id.rv_desc_salary);
        markerParent = findViewById(R.id.rl_marker_parent);
        initRecyclerView(context);
        this.monthList = balanceYearMonth;
        this.monthBalanceList = monthBalanceList;
        this.mStaffNumValues = yValues;
    }

    public MyMarkerView(Context context, int layoutResource, List<String> monthList, BaseFragment fragment) {
        super(context, layoutResource);
        mContext = context;
        tvContent = findViewById(R.id.tv_content);
        mSalaryDesc = findViewById(R.id.rv_desc_salary);
        markerParent = findViewById(R.id.rl_marker_parent);
        initRecyclerView(context);
        this.monthList = monthList;
        this.fragment = fragment;
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
        mContext = context;
        tvContent = findViewById(R.id.tv_content);
        mSalaryDesc = findViewById(R.id.rv_desc_salary);
        markerParent = findViewById(R.id.rl_marker_parent);
        initRecyclerView(context);
        this.monthList = monthList;
    }

    private void initRecyclerView(Context context) {
        mSalaryDesc.setLayoutManager(new GridLayoutManager(context, 1));
        mSalaryDesc.addItemDecoration(new SpacesItemDecoration(0, 0, 0, 0));
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
            // 收支曲线
            for (int i = 0; i < ListUtils.getSize(monthBalanceList); i++) {
                descList.add(new HomeSalaryDesc(monthBalanceList.get(i).getData().get((int) e.getX()).getMoney(),
                        monthBalanceList.get(i).getName() + "："));
            }
            // 首页工资
            if (fragment instanceof HomeFragment) {
                descList.add(new HomeSalaryDesc(e.getY(), "工资："));
            }
            // 员工数量曲线
            for (int i = 0; i < ListUtils.getSize(mStaffNumValues); i++) {
                descList.add(new HomeSalaryDesc(mStaffNumValues.get(i).getData().get((int) e.getX()).getNum(),
                        mStaffNumValues.get(i).getName() + "："));
            }
            mSalaryDescAdapter.setDescList(descList);
        }
    }

    //标记相对于折线图的偏移量
    @Override
    public MPPointF getOffset() {
        //偏移量(x,y)，y的话又看到我xml布局中圆球球是10dp的，这里就网上偏移5dp也就是半径
        // TODO: marker的左右偏移量设置
        return new MPPointF(-(markerParent.getMeasuredWidth() / 2), -FontSwitchUtil.dip2px(mContext, 5));
    }

    private MPPointF mOffset2 = new MPPointF();

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {

        MPPointF offset = getOffset();
        mOffset2.x = offset.x;
        mOffset2.y = offset.y;

        Chart chart = getChartView();

        float width = getWidth();
        float height = getHeight();

        if (posX + mOffset2.x < 0) {
            //第一个数据的时候，超出屏幕了，实现父类方法改一改
            mOffset2.x = offset.x;
        } else if (chart != null && posX + width + mOffset2.x > chart.getWidth()) {
            mOffset2.x = chart.getWidth() - posX - width;
        }

        if (posY + mOffset2.y < 0) {
            mOffset2.y = -posY;
        } else if (chart != null && posY + height + mOffset2.y > chart.getHeight()) {
            mOffset2.y = chart.getHeight() - posY - height;
        }

        return mOffset2;
    }

}