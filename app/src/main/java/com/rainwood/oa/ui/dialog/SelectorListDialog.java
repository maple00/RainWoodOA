package com.rainwood.oa.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.utils.LogUtils;
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
 * @Description : 弹窗
 * @Usage :
 **/
public final class SelectorListDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements BaseAdapter.OnItemClickListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final MenuAdapter mAdapter;
        private final TextView mCancelView;
        private final TextView mConfirm;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_selector);
            setAnimStyle(AnimAction.BOTTOM);

            RecyclerView recyclerView = findViewById(R.id.rl_content_list);
            mCancelView = findViewById(R.id.tv_cancel);
            mConfirm = findViewById(R.id.tv_confirm);

            mAdapter = new MenuAdapter(getContext());
            mAdapter.setOnItemClickListener(this);
            recyclerView.setAdapter(mAdapter);

            setOnClickListener(R.id.tv_cancel, R.id.tv_confirm);
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
            mAdapter.setData(data);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public Builder setCancel(CharSequence text) {
            mCancelView.setText(text);
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

        @SingleClick
        @Override
        public void onClick(View v) {
            if (mAutoDismiss) {
                dismiss();
            }

            // 取消弹窗
            if (v == mCancelView) {
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
            // 获取弹窗的值
            if (v == mConfirm) {
                if (mListener != null) {
                    LogUtils.d("sxs", "点击了确认");
                }
            }
        }


        /**
         * {@link BaseAdapter.OnItemClickListener}
         */
        @SuppressWarnings("all")
        @Override
        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            if (mAutoDismiss) {
                dismiss();
            }

            if (mListener != null) {
                mListener.onSelected(getDialog(), position, mAdapter.getItem(position));
            }
            /*for (SelectedItem selectedItem : mData) {
                selectedItem.setHasSelected(false);
            }
            mData.get(position).setHasSelected(true);
            tempPos = position;

            mAdapter.notifyDataSetChanged();*/
        }
    }

    private static final class MenuAdapter extends MyAdapter<Object> {

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
            private final View mLineView;

            ViewHolder() {
                super(R.layout.item_menu);
                mTextView = (TextView) findViewById(R.id.tv_menu_text);
                mLineView = findViewById(R.id.v_menu_line);
            }

            @Override
            public void onBindView(int position) {
                mTextView.setText(getItem(position).toString());
                //mTextView.setTextColor(getItem(position).isHasSelected() ? (getResources().getColor(R.color.fontColor))
                //        : (getResources().getColor(R.color.labelColor)));

                if (position == 0) {
                    // 当前是否只有一个条目
                    if (getItemCount() == 1) {
                        mLineView.setVisibility(View.GONE);
                    } else {
                        mLineView.setVisibility(View.VISIBLE);
                    }
                } else if (position == getItemCount() - 1) {
                    mLineView.setVisibility(View.GONE);
                } else {
                    mLineView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public interface OnListener<T> {

        /**
         * 选择条目时回调
         */
        void onSelected(BaseDialog dialog, int position, T t);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

    }
}
