package com.rainwood.oa.ui.activity;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.RecordApproval;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.AuditRecordAdapter;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/7/23 9:28
 * @Desc: 客户记录详情
 */
public final class CustomRecordDetailActivity extends BaseActivity implements ICustomCallbacks {

    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    // content
    @ViewInject(R.id.ll_content)
    private LinearLayout contentParentView;
    @ViewInject(R.id.gv_audit_records)
    private MeasureGridView auditRecordsView;

    private ICustomPresenter mCustomPresenter;
    private AuditRecordAdapter mAuditRecordsAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_custom_record_detail;
    }

    @Override
    protected void initView() {
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        // 设置适配器适配器属性
        mAuditRecordsAdapter = new AuditRecordAdapter();
        auditRecordsView.setAdapter(mAuditRecordsAdapter);
    }

    @Override
    protected void initData() {
        //模拟数据
        for (int i = 0; i < titles.length; i++) {
            TextView titleView = new TextView(this);
            titleView.setText(titles[i]);
            titleView.setPadding(0, 0, 0, 0);
            titleView.setTextColor(getColor(R.color.labelColor));
            titleView.setTextSize(13f);
            TextView contentView = new TextView(this);
            contentView.setText(content[i]);
            contentView.setPadding(0, 14, 0, 25);
            contentView.setTextColor(getColor(R.color.fontColor));
            contentView.setLineSpacing(12f, 1f);
            contentView.setTextSize(15f);
            contentParentView.addView(titleView);
            contentParentView.addView(contentView);
        }
        List<RecordApproval> recordApprovals = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RecordApproval approval = new RecordApproval();
            approval.setText("韩梅梅驳回了蔡川的加班申请");
            approval.setTime("2020.03.20 17:02");
            recordApprovals.add(approval);
        }
        mAuditRecordsAdapter.setAuditRecordsList(recordApprovals);
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        String recordsId = getIntent().getStringExtra("recordsId");
        toast("暂无接口");
        if (recordsId != null) {
            //            mCustomPresenter.requestCustomRecordDetailById(recordsId);
        }
    }

    private String[] titles = {"实际加班时间", "加班成果", "申请人", "加班对象", "预计加班时间", "加班事由"};
    private String[] content = {"2020.03.20 18:00 - 2020.03.20 20:30", "宏巨精密企业管理系统的需求整理完毕已发给部\n" +
            "门经理审核，同周共计PC端原型图画了一半。", "韩梅梅", "客户 · 重庆宏巨精密", "2020.03.20 18:00 - 2020.03.20 20:00",
            "加班整理宏巨精密企业管理系统的需求，完成同\n" +
                    "周共计PC端原型图。"};


    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    protected void release() {
        if (mCustomPresenter != null) {
            mCustomPresenter.unregisterViewCallback(this);
        }
    }
}
