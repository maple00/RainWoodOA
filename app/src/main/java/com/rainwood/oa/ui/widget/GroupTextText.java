package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rainwood.oa.R;
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:05
 * @Desc: 文字+文字描述组合
 */
public final class GroupTextText extends LinearLayout {

    @ViewInject(R.id.tv_title)
    private TextView titleTV;
    @ViewInject(R.id.tv_values)
    private TextView values;

    public GroupTextText(Context context) {
        this(context, null);
    }

    public GroupTextText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"CustomViewStyleable", "Recycle"})
    public GroupTextText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.group_text_text, this, true);
        ViewBind.inject(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GroupTextText);
        String valueStr = typedArray.getString(R.styleable.GroupTextText_values);
        if (!TextUtils.isEmpty(valueStr)) {
            values.setText(valueStr);
        }
        String title = typedArray.getString(R.styleable.GroupTextText_title);
        if (!TextUtils.isEmpty(title)) {
            titleTV.setText(title);
        }
        // 回收
        typedArray.recycle();
    }

    public void setValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            values.setText(value);
        }
    }
}
