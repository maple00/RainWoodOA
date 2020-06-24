package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.tools.annotation.OnClick;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;
import com.rainwood.oa.network.aop.SingleClick;

/**
 * @Author: a797s
 * @Date: 2020/5/18 11:50
 * @Desc: 自定义TextGroup组合控件
 */
@SuppressLint("CustomViewStyleable")
public final class GroupTextIcon extends LinearLayout {

    private Context mContext;
    @ViewInject(R.id.tv_name)
    private TextView textName;
    @ViewInject(R.id.iv_icon)
    private ImageView icon;
    private onItemClick mOnItemClick;
    private int mRightIcon;

    private String text;

    public GroupTextIcon(Context context) {
        this(context, null);
    }

    public GroupTextIcon(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupTextIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.group_text_status, this, true);
        ViewBind.inject(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusTextGroup);
        // 状态
        String statusTitle = typedArray.getString(R.styleable.StatusTextGroup_status_text);
        if (!TextUtils.isEmpty(statusTitle)) {
            textName.setText(statusTitle);
        }
        int color = typedArray.getColor(R.styleable.StatusTextGroup_text_color, context.getResources().getColor(R.color.fontColor));
        textName.setTextColor(color);
        int textSize = typedArray.getInteger(R.styleable.StatusTextGroup_text_size, 15);
        textName.setTextSize(textSize);
        // 右边图标
        mRightIcon = typedArray.getResourceId(R.styleable.StatusTextGroup_right_icon, R.drawable.ic_triangle_down);
        if (mRightIcon != -1) {
            icon.setImageResource(mRightIcon);
        }
        // 回收
        typedArray.recycle();
    }

    @SingleClick
    @OnClick(R.id.ll_status_item)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_status_item:
                mOnItemClick.onItemClick(TextUtils.isEmpty(textName.getText()) ? "" : textName.getText().toString());
                break;
        }
    }

    /**
     * 说明被点击了
     *
     * @param rightIcon 右边图标、
     * @param color     字体颜色
     */
    public void setRightIcon(int rightIcon, int color) {
        mRightIcon = rightIcon;
        if (mRightIcon != -1) {
            icon.setImageResource(mRightIcon);
            textName.setTextColor(color);
        }
    }

    /**
     * 设置文本文字
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        if (!TextUtils.isEmpty(text)) {
            textName.setText(text);
        }
    }

    public void setOnItemClick(onItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }


    public interface onItemClick {
        void onItemClick(String text);
    }
}
