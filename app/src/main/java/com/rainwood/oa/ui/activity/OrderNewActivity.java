package com.rainwood.oa.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.PrimaryKey;
import com.rainwood.oa.model.domain.TempData;
import com.rainwood.oa.presenter.IOrderPresenter;
import com.rainwood.oa.ui.adapter.PrimaryKeyAdapter;
import com.rainwood.oa.ui.pop.CommonPopupWindow;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.oa.utils.PageJumpUtil;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.oa.view.IOrderCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.utils.FontSwitchUtil;
import com.rainwood.tools.wheel.aop.SingleClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/5/18 18:01
 * @Desc: 新建订单
 */
public final class OrderNewActivity extends BaseActivity implements IOrderCallbacks {
    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.tv_custom_name)
    private TextView customNameTV;
    @ViewInject(R.id.cet_custom_name)
    private EditText customName;
    @ViewInject(R.id.tv_order_name)
    private TextView orderNameTV;
    @ViewInject(R.id.cet_order_name)
    private EditText orderName;
    @ViewInject(R.id.tv_money)
    private TextView moneyTV;
    @ViewInject(R.id.cet_money)
    private EditText money;
    @ViewInject(R.id.tv_note)
    private TextView noteTV;
    @ViewInject(R.id.cet_note)
    private TextView note;

    private IOrderPresenter mOrderPresenter;
    private PrimaryKeyAdapter mKeyAdapter;
    private long mStartTime;
    private CommonPopupWindow mPrimaryPop;

    private TempData mTempData;
    // flag
    // 请求客户名称接口flag-- 默认请求
    private String tempCustomName = "";
    private PrimaryKey mPrimaryKey;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_new;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        setRequiredValue(customNameTV, "客户名称");
        setRequiredValue(orderNameTV, "订单名称");
        setRequiredValue(moneyTV, "合同金额");
        setRequiredValue(noteTV, "备注");
    }

    @Override
    protected void initPresenter() {
        mOrderPresenter = PresenterManager.getOurInstance().getOrderPresenter();
        mOrderPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        customName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempCustomName = String.valueOf(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // LogUtils.d("sxs", "afterTextChanged value---- " + s);
                // 请求接口 -- 当选择了之后则不请求接口
                mStartTime = System.currentTimeMillis();
                if (!TextUtils.isEmpty(s) && !"".contentEquals(s)) {
                    // 请求接口得条件
                    if (!tempCustomName.contentEquals(s)) {
                        mOrderPresenter.requestCustomName(s.toString());
                    } else {
                        LogUtils.d("sxs", "没有了焦点");
                    }
                } else {
                    if (mPrimaryPop != null) {
                        mPrimaryPop.dismiss();
                    }
                }
            }
        });

    }

    @SingleClick
    @OnClick({R.id.btn_next_step, R.id.iv_page_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.btn_next_step:
                if (TextUtils.isEmpty(customName.getText())) {
                    toast("请输入客户名称");
                    return;
                }
                if (TextUtils.isEmpty(orderName.getText())) {
                    toast("请输入订单名称");
                    return;
                }
                if (TextUtils.isEmpty(money.getText())) {
                    toast("请输入合同金额");
                    return;
                }
                if (TextUtils.isEmpty(note.getText())) {
                    toast("请输入备注");
                }
                String customNameStr = customName.getText().toString().trim();
                String orderNameStr = orderName.getText().toString().trim();
                String moneyStr = money.getText().toString().trim();
                String noteStr = note.getText().toString().trim();
                // 新建订单
                String customId = mPrimaryKey.getKhid();
                mOrderPresenter.CreateNewOrder(customId, orderNameStr, moneyStr, noteStr);
                mTempData = new TempData();
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put("customName", customName.getText().toString().trim());
                tempMap.put("orderName", orderName.getText().toString().trim());
                tempMap.put("money", money.getText().toString().trim());
                tempMap.put("note", note.getText().toString().trim());
                mTempData.setTempMap(tempMap);
                break;
        }
    }

    /**
     * 预想输入
     *
     * @param customDataList
     */
    @Override
    public void getCustomDataByKey(List<PrimaryKey> customDataList) {
        int customWidth = customName.getMeasuredWidth();
        mPrimaryPop = new CommonPopupWindow.Builder(this)
                // .setAnimationStyle(R.style.ScaleAnimStyle)
                .setView(R.layout.pop_primary_list)
                .setOutsideTouchable(true)
                .setWidthAndHeight(FontSwitchUtil.dip2px(this, customWidth), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setViewOnclickListener((view, layoutResId) -> {
                    RecyclerView contentList = view.findViewById(R.id.rv_item_list);
                    // 设置布局管理器
                    contentList.setLayoutManager(new GridLayoutManager(OrderNewActivity.this, 1));
                    contentList.addItemDecoration(new SpacesItemDecoration(0, 0, 0,
                            FontSwitchUtil.dip2px(OrderNewActivity.this, 20f)));
                    // 创建适配器
                    mKeyAdapter = new PrimaryKeyAdapter();
                    // 设置适配
                    contentList.setAdapter(mKeyAdapter);
                    // 设置数据
                    mKeyAdapter.setKeyList(customDataList);
                    mKeyAdapter.setKeyWord(customName.getText().toString().trim());

                    long endTime = System.currentTimeMillis();
                    LogUtils.d("sxs", "耗时-----" + (endTime - mStartTime));
                })
                .create();
        mPrimaryPop.showAsDropDown(customName, Gravity.BOTTOM, 0, 0);
        // keyAdapter点击事件
        mKeyAdapter.setClickPrimary((primaryKey, position) -> {
            mPrimaryKey = primaryKey;
            customName.setText(primaryKey.getCompanyName());
            mPrimaryPop.dismiss();
        });
        mPrimaryPop.setOnDismissListener(mPrimaryPop::dismiss);
    }

    @Override
    public void getCreateResult(boolean success) {
        if (!success) {
            toast("创建失败");
            return;
        }
        toast("创建成功");
        PageJumpUtil.OrderNew2OrderEditPage(this, OrderActivity.class, mTempData);
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
