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
import com.rainwood.tools.annotation.ViewBind;
import com.rainwood.tools.annotation.ViewInject;

/**
 * @Author: a797s
 * @Date: 2020/5/18 14:05
 * @Desc: 图片文字组合
 */
public final class GroupIconText extends LinearLayout {

    @ViewInject(R.id.iv_icon)
    private ImageView iconIV;
    @ViewInject(R.id.tv_values)
    private TextView values;

    private String value;

    public GroupIconText(Context context) {
        this(context, null);
    }

    public GroupIconText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint({"CustomViewStyleable", "Recycle"})
    public GroupIconText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.group_text_icon, this, true);
        ViewBind.inject(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GroupIconText);
        String valueStr = typedArray.getString(R.styleable.GroupIconText_text);
        if (TextUtils.isEmpty(valueStr)) {
            values.setText(valueStr);
        }
        int icon = typedArray.getResourceId(R.styleable.GroupIconText_icon, R.drawable.ic_custom_head);
        if (icon != -1) {
            iconIV.setImageResource(icon);
        }
        // 回收
        typedArray.recycle();
    }

    public void setValue(String value) {
        this.value = value;
        if (!TextUtils.isEmpty(value)){
            values.setText(value);
        }
    }
}
