package com.rainwood.oa.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.CustomStaff;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.utils.SpacesItemDecoration;
import com.rainwood.tools.utils.FontSwitchUtil;
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
 * @Description : 底部弹窗--选择客户
 * @Usage :
 **/
public final class BottomCustomDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder>
            implements BaseAdapter.OnItemClickListener {

        private OnListener mListener;
        private boolean mAutoDismiss = true;

        private final MenuAdapter mAdapter;
        private final ImageView mCancelView;
        private final Button mConfirmView;
        private final TextView mTitle;
        private final TextView mTips;

        private List<SelectedItem> mData;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.dialog_custom_selector);
            setAnimStyle(AnimAction.BOTTOM);

            RecyclerView recyclerView = findViewById(R.id.rl_content_list);
            mCancelView = findViewById(R.id.iv_cancel);
            mConfirmView = findViewById(R.id.btn_confirm);
            mTitle = findViewById(R.id.tv_title);
            mTips = findViewById(R.id.tv_tips);

            mAdapter = new MenuAdapter(getContext());
            mAdapter.setOnItemClickListener(this);
            //    GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            recyclerView.addItemDecoration(new SpacesItemDecoration(FontSwitchUtil.dip2px(context, 10f)));
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));

            setOnClickListener(R.id.iv_cancel, R.id.btn_confirm);
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
            this.mData = data;
            mAdapter.setData(data);
            return this;
        }

        public Builder setCancel(@StringRes int id) {
            return setCancel(getString(id));
        }

        public Builder setCancel(CharSequence text) {
            //mCancelView.setText(text);
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

        public Builder setTipsVisibility(CharSequence text) {
            mTips.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
            mTips.setText(text);
            return this;
        }

        public Builder setTitle(CharSequence text) {
            mTitle.setText(text);
            return this;
        }

        @SuppressWarnings("all")
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
            if (v == mConfirmView) {
                if (mListener != null) {
                    if (mListener != null) {
                        mListener.onSelected(getDialog(), tempPos, mAdapter.getItem(tempPos == -1 ? 0 : tempPos));
                    }
                }
            }
        }

        private int tempPos = -1;

        /**
         * {@link BaseAdapter.OnItemClickListener}
         */
        @Override
        public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
            if (mAutoDismiss) {
                dismiss();
            }

            for (SelectedItem selectedItem : mData) {
                selectedItem.setHasSelected(false);
            }
            mData.get(position).setHasSelected(true);
            tempPos = position;
            mAdapter.notifyDataSetChanged();
        }
    }

    private static final class MenuAdapter extends MyAdapter<CustomStaff> {

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
                mTextView.setText(getItem(position).getName());
                mTextView.setHint(getItem(position).getStid());
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
        void onSelected(BaseDialog dialog, int position, T t);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {
        }

    }
}
