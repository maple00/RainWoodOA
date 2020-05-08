package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.ChartEntity;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.ui.adapter.HomeSalaryAdapter;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.oa.ui.widget.LineChartView;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.XCollapsingToolbarLayout;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IHomeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 首页
 */
public final class HomeFragment extends BaseFragment implements XCollapsingToolbarLayout.OnScrimsListener, IHomeCallbacks {

    @ViewInject(R.id.ctl_test_bar)
    private XCollapsingToolbarLayout mCollapsingToolbarLayout;
    public boolean mScrimsShown;

    @ViewInject(R.id.lcv_salary)
    private LineChartView mLineChartSalary;

    @ViewInject(R.id.mgv_home_salary)
    private MeasureGridView homeSalary;

    // mHandler
    private final int INITIAL_LINE_SALARY_SIZE = 0x101;     // 工资曲线图
    private IHomePresenter mHomePresenter;
    private HomeSalaryAdapter mSalaryAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        super.initView(rootView);
        //设置渐变监听
        mCollapsingToolbarLayout.setOnScrimsListener(this);
        mScrimsShown = mCollapsingToolbarLayout.isScrimsShown();
       /* if (mScrimsShown){
            LogUtils.d(TAG, "11111111");
        }else {
            LogUtils.d(TAG, "22222222");
        }*/
        // 创建工资曲线适配器
        mSalaryAdapter = new HomeSalaryAdapter();
        // 设置适配器
        homeSalary.setAdapter(mSalaryAdapter);
    }

    @Override
    protected void initPresenter() {
        mHomePresenter = PresenterManager.getOurInstance().getIHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 加载数据
        mHomePresenter.getHomeSalaryData();
    }

    /**
     * 工资曲线数据
     *
     * @param salaries
     */
    @Override
    public void getSalariesData(List<FontAndFont> salaries) {
        // 加载返回的数据
        mSalaryAdapter.setList(salaries);
        // setUpState(State.ERROR);
        // 工资曲线 -- V1.0
        Message msg = new Message();
        msg.what = INITIAL_LINE_SALARY_SIZE;
        mHandler.sendMessage(msg);
    }

    /**
     * @param layout
     * @param shown  渐变开关
     */
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        } else {
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_LINE_SALARY_SIZE:
                    List<ChartEntity> datas1 = new ArrayList<>();
                    List<ChartEntity> datas2 = new ArrayList<>();
                    for (int i = 0; i < 20; i++) {
                        datas1.add(new ChartEntity("sxs" + String.valueOf(i), (float) (Math.random() * 1000)));
                        datas2.add(new ChartEntity("sxs" + String.valueOf(i), (float) (Math.random() * 1000)));
                    }
                    mLineChartSalary.setData(datas1, true, true);
                    mLineChartSalary.setData(datas2, true, true);
                    mLineChartSalary.startAnimation(6000);
                    break;
            }
        }
    };

    @SingleClick
    @OnClick(R.id.iv_home_top)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_top:
                // 自定义日期控件
                new DateDialog.Builder(getContext(), false)
                        .setTitle(null)
                        .setConfirm(getString(R.string.common_text_confirm))
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setAutoDismiss(false)
                        //.setIgnoreDay()
                        .setCanceledOnTouchOutside(false)
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, String startTime, String endTime) {
                                dialog.dismiss();
                                toast("选中的时间段：" + startTime + "至" + endTime);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
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

    public class StatisticsBean {

        private double mValue;


        public double getmValue() {
            return mValue;
        }

        public void setmValue(double mValue) {
            this.mValue = mValue;
        }
    }
}
