package com.rainwood.oa.ui.activity;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.base.BaseActivity;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.presenter.ICustomPresenter;
import com.rainwood.oa.ui.adapter.SelectedItemAdapter;
import com.rainwood.oa.ui.dialog.DateDialog;
import com.rainwood.oa.ui.widget.MeasureGridView;
import com.rainwood.oa.utils.Constants;
import com.rainwood.oa.utils.PresenterManager;
import com.rainwood.oa.utils.RandomUtil;
import com.rainwood.oa.view.ICustomCallbacks;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.tools.statusbar.StatusBarUtils;
import com.rainwood.tools.wheel.BaseDialog;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/5/19 17:56
 * @Desc: 新增跟进记录
 */
public final class AddFollowRecordActivity extends BaseActivity implements ICustomCallbacks {

    // actionBar
    @ViewInject(R.id.rl_page_top)
    private RelativeLayout pageTop;
    @ViewInject(R.id.tv_page_title)
    private TextView pageTitle;
    @ViewInject(R.id.tv_page_right_title)
    private TextView pageRightTitle;
    // content
    @ViewInject(R.id.cet_choose_time)
    private EditText chooseTime;
    @ViewInject(R.id.cet_follow_content)
    private EditText followContent;
    @ViewInject(R.id.mgv_labels)
    private MeasureGridView labelView;

    private ICustomPresenter mCustomPresenter;
    private SelectedItemAdapter mItemAdapter;
    private List<SelectedItem> mSelectedList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_follow_record;
    }

    @Override
    protected void initView() {
        StatusBarUtils.immersive(this);
        StatusBarUtils.setPaddingSmart(this, pageTop);
        pageTitle.setText(title);
        pageRightTitle.setVisibility(View.INVISIBLE);

        mItemAdapter = new SelectedItemAdapter();
        labelView.setAdapter(mItemAdapter);
        labelView.setNumColumns(4);
    }

    @Override
    protected void initPresenter() {
        mCustomPresenter = PresenterManager.getOurInstance().getCustomPresenter();
        mCustomPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        // 请求跟进记录的标签
        mCustomPresenter.requestFollowLabel();
    }

    @Override
    protected void initEvent() {
        mItemAdapter.setOnClickItem((item, position) -> {
            for (SelectedItem selectedItem : mSelectedList) {
                selectedItem.setHasSelected(false);
            }
            item.setHasSelected(true);
            followContent.append(item.getName() + "，");
        });

        followContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    for (SelectedItem selectedItem : mSelectedList) {
                        selectedItem.setHasSelected(false);
                    }
                    mItemAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick({R.id.iv_page_back, R.id.btn_confirm, R.id.cet_choose_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_page_back:
                finish();
                break;
            case R.id.cet_choose_time:
                new DateDialog.Builder(this)
                        .setTitle(getString(R.string.date_title))
                        .setConfirm(getString(R.string.common_confirm))
                        // 设置 null 表示不显示取消按钮
                        .setCancel(getString(R.string.common_cancel))
                        .setShowConfirm(true)
                        .setShowImageClose(false)
                        .setListener(new DateDialog.OnListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSelected(BaseDialog dialog, int year, int month, int day) {
                                chooseTime.setText(year + "-" + (month < 10 ? "0" + month : month)
                                        + "-" + (day < 10 ? "0" + day : day));
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(chooseTime.getText())) {
                    toast("请填写下次跟进时间");
                    return;
                }
                if (TextUtils.isEmpty(followContent.getText())) {
                    toast("请填写跟进记录");
                    return;
                }
                String content = followContent.getText().toString().trim();
                String time = chooseTime.getText().toString().trim();

                String recordId = RandomUtil.getItemID(20);
                // 新增客户跟进记录
                if (Constants.CUSTOM_ID != null) {
                    mCustomPresenter.createFollowRecord(recordId, "客户", Constants.CUSTOM_ID, content, time);
                }
                break;
        }
    }

    @Override
    public void getFollowLabels(List<SelectedItem> selectedList) {
        mSelectedList = selectedList;
        mItemAdapter.setList(mSelectedList);
    }

    @Override
    public void getFollowLabelResult(String warn) {
        if ("success".equals(warn)) {
            toast("新增成功");
            postDelayed(this::finish, 500);
        }
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError(String tips) {
        toast(tips);
    }

    @Override
    protected void release() {
        if (mCustomPresenter != null) {
            mCustomPresenter.unregisterViewCallback(this);
        }
    }
}
