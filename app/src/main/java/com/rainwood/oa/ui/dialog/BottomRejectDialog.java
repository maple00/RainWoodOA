package com.rainwood.oa.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.Custom;
import com.rainwood.tools.wheel.BaseAdapter;
import com.rainwood.tools.wheel.BaseDialog;
import com.rainwood.tools.wheel.MyAdapter;
import com.rainwood.tools.wheel.action.AnimAction;
import com.rainwood.oa.network.aop.SingleClick;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by a797s in 2020/5/14 16:40
 *
 * @Description : 底部弹窗--驳回申请
 * @Usage :
 **/
public final class BottomRejectDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements BaseAdapter.OnItemClickListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final MenuAdapter mAdapter;

        private final EditText mRejectReason;
        private final TextView mTitle;
        private final TextView mDialogClose;
        private final TextView mCommit;

        // flag
        // 选择驳回的状态-默认为草稿状态
        private boolean selectedStatus = true;
        private final CheckBox mDraftCB;
        private final CheckBox mPassedCb;
        private final LinearLayout mRejectTo;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_reject_content);
            setAnimStyle(AnimAction.BOTTOM);

            mTitle = findViewById(R.id.tv_title);
            mDialogClose = findViewById(R.id.tv_close);
            mCommit = findViewById(R.id.tv_commit);
            mRejectReason = findViewById(R.id.cet_reject_reason);
            mDraftCB = findViewById(R.id.cb_draft);
            mPassedCb = findViewById(R.id.cb_passed);
            mRejectTo = findViewById(R.id.ll_reject_to);

            mAdapter = new MenuAdapter(getContext());
            mAdapter.setOnItemClickListener(this);
            // 设置自动聚焦
            postDelayed(() -> showKeyboard(mRejectReason), 50);

            setOnClickListener(R.id.tv_close, R.id.tv_commit, R.id.cb_draft, R.id.tv_draft,
                    R.id.cb_passed, R.id.tv_passed);
        }

        //弹出软键盘
        void showKeyboard(EditText editText) {
            //其中editText为dialog中的输入框的 EditText
            if (editText != null) {
                //设置可获得焦点
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                //请求获得焦点
                editText.requestFocus();
                //调用系统输入法
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }

        @Override
        public Builder setGravity(int gravity) {
            switch (gravity) {
                // 如果这个是在中间显示的
                case Gravity.CENTER:
                case Gravity.CENTER_VERTICAL:
                    // 不显示取消按钮
                    setCancel(null);
                    // 重新设置动画
                    setAnimStyle(AnimAction.SCALE);
                    break;
                default:
                    break;
            }
            return super.setGravity(gravity);
        }

        public Builder setList(int... ids) {
            List<String> data = new ArrayList<>(ids.length);
            for (int id : ids) {
                data.add(getString(id));
            }
            return setList(data);
        }

        public Builder setList(String... data) {
            return setList(Arrays.asList(data));
        }

        @SuppressWarnings("all")
        public Builder setList(List data) {
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public Builder setCancel(CharSequence text) {
            mDialogClose.setText(text);
            return this;
        }

        public Builder setAutoDismiss(boolean dismiss) {
            mAutoDismiss = dismiss;
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setTitle(CharSequence text) {
            mTitle.setText(text);
            return this;
        }

        public Builder setRejectToVisibility(boolean visibility) {
            mRejectTo.setVisibility(visibility ? View.VISIBLE : View.GONE);
            return this;
        }

        @SuppressWarnings("all")
        @SingleClick
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }
            switch (v.getId()) {
                case R.id.tv_close:
                    if (mListener != null) {
                        mListener.onCancel(getDialog());
                    }
                    break;
                case R.id.tv_commit:
                    if (mListener != null) {
                        if (mListener != null) {
                            mListener.onSelected(getDialog(), mRejectReason.getText().toString().trim());
                        }
                    }
                    break;
                // 选择驳回到的位置
                case R.id.cb_draft:
                case R.id.tv_draft:
                    // 选择了草稿
                    selectedStatus = true;
                    if (selectedStatus) {
                        mDraftCB.setChecked(true);
                        mPassedCb.setChecked(false);
                    }
                    break;
                case R.id.cb_passed:
                case R.id.tv_passed:
                    selectedStatus = false;
                    if (!selectedStatus) {
                        mDraftCB.setChecked(false);
                        mPassedCb.setChecked(true);
                    }
                    break;
            }

            // 取消弹窗
            if (v == mDialogClose) {

            }
            // 获取弹窗的值
            if (v == mCommit) {

            }
        }


        /**
         * {@link BaseAdapter.OnItemClickListener}
         */
        @Override
        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            if (mAutoDismiss) {
                dismiss();
            }

           /* for (Custom selectedItem : mData) {
                selectedItem.setSelected(false);
            }
            mData.get(position).setSelected(true);*/
            mAdapter.notifyDataSetChanged();
        }
    }

    private static final class MenuAdapter extends MyAdapter<Custom> {

        private MenuAdapter(Context context) {
            super(context);
        }

        @NonNull
        @Override
        public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MenuAdapter.ViewHolder();
        }

        final class ViewHolder extends MyAdapter.ViewHolder {

            private final TextView mTextView;

            ViewHolder() {
                super(R.layout.item_selected);
                mTextView = (TextView) findViewById(R.id.tv_content);
            }

            @Override
            public void onBindView(int position) {
                mTextView.setText(getItem(position).getStaff());
                if (getItem(position).isSelected()) {
                    mTextView.setBackgroundResource(R.drawable.selector_selected);
                    mTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    mTextView.setBackgroundResource(R.drawable.selector_uncheck_selected);
                    mTextView.setTextColor(getResources().getColor(R.color.colorMiddle));
                }
            }
        }
    }

    public interface OnListener<T> {

        /**
         * 选择条目时回调
         */
        void onSelected(BaseDialog dialog, String content);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

    }
}
