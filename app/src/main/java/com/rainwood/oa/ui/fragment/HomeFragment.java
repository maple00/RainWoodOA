package com.rainwood.oa.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseFragment;
import com.rainwood.oa.model.domain.ChartEntity;
import com.rainwood.oa.model.domain.FontAndFont;
import com.rainwood.oa.presenter.IHomePresenter;
import com.rainwood.oa.ui.adapter.HomeSalaryAdapter;
import com.rainwood.oa.ui.widget.DateDialog;
import com.rainwood.oa.ui.widget.LineChartView;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.ui.widget.XCollapsingToolbarLayout;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.IHomeCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.ArrayList;
import java.util.Calendar;
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

        // 工资曲线
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
                    for (int i = 0; i < 20; i++) {
                        datas1.add(new ChartEntity("item" + String.valueOf(i), (float) (Math.random() * 1000)));
                    }
                    mLineChartSalary.setData(datas1, true, true);
                    mLineChartSalary.startAnimation(6000);
                    break;
            }
        }
    };

    @OnClick(R.id.iv_home_top)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home_top:
                LogUtils.d(TAG, "点击了头部");
                // 自定义日期控件
                // DateGroupView groupView = new DateGroupView(getContext());
                // groupView.show();
//                DateGroupView groupView = new DateGroupView(getContext());
//                groupView.setTouchOutCancel(true);
//                groupView.show();
                // TODO: 时间自定义控件
                new DateDialog.Builder(getActivity())
                        .setTitle(getString(R.string.date_title))
                        // 确定文本
                        .setConfirm(getString(R.string.common_text_confirm))
                        // 设置为null 时表示不显示取消按钮
                        .setCancel(getString(R.string.common_text_clear_screen))
                        .setGravity(Gravity.CENTER_VERTICAL)
                        // 设置日期(可支持2019-12-03， 20191203， 时间戳)
                        // .setDate(20191203)
                        // 设置年份
                        //.setYear(2019)
                        // 设置月份
                        //.setMonth(12)
                        // 设置天数
                        //.setDay(3)
                        // 不选择天数
                        //.setIgnoreDay()
                        .setListener(new DateDialog.OnListener() {
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                toast(year + "-" + "-" + month + "-" + day);

                                // 如果不指定时分秒则默认为现在的时间
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                // 月份从零开始，所以需要减 1
                                calendar.set(Calendar.MONTH, month - 1);
                                calendar.set(Calendar.DAY_OF_MONTH, day);
                                // toast("时间戳：" + calendar.getTimeInMillis());
                                //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                toast("取消了");
                            }
                        }).show();

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
}
