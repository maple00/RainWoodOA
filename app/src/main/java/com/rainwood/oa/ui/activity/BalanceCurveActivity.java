package com.rainwood.oa.ui.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.network.aop.SingleClick;
import com.rainwood.oa.presenter.IFinancialPresenter;
import com.rainwood.oa.ui.widget.MeasureListView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IFinancialCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

/**
 * @Author: a797s
 * @Date: 2020/6/28 16:04
 * @Desc: 收支曲线
 */
public final class BalanceCurveActivity extends BaseActivity implements IFinancialCallbacks {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;

    @ViewInject(R.id.tv_date)
    private TextView date;

    @ViewInject(R.id.lc_salary_chart)
    private LineChart mSalaryChart;
    @ViewInject(R.id.mlv_salary_list)
    private MeasureListView salaryListView;

    private IFinancialPresenter mFinancialPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_cruve;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
    }

    @Override
    protected void initPresenter() {
        mFinancialPresenter = PresenterManager.getOurInstance().getFinancialPresenter();
        mFinancialPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        mFinancialPresenter.requestBalanceByMonth();
    }

    @SingleClick
    @OnClick({R.id.iv_page_back, R.id.tv_month, R.id.tv_year})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.tv_year:
                toast("按年");
                break;
            case R.id.tv_month:
                toast("按月");
                break;
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
