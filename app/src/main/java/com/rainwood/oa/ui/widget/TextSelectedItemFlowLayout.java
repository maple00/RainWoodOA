package com.rainwood.oa.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.SelectedItem;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 流式布局
 */
public class TextSelectedItemFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;
    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;
    private float DEFAULT_TEXT_SIZE = 12.0f;
    private float textSize;
    private int textColor;
    private OnFlowTextItemClickListener mItemClickListener = null;
    private Context mContext;

    public int getContentSize() {
        return mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    private List<SelectedItem> mTextList = new ArrayList<>();

    public TextSelectedItemFlowLayout(Context context) {
        this(context, null);
    }

    public TextSelectedItemFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public TextSelectedItemFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿到相关属性
        @SuppressLint("CustomViewStyleable")
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);
        mItemHorizontalSpace = ta.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = ta.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);
        textSize = ta.getDimension(R.styleable.FlowTextStyle_textFlowSize, DEFAULT_TEXT_SIZE);
        textColor = ta.getColor(R.styleable.FlowTextStyle_textFlowColor, context.getColor(R.color.colorMiddle));
        ta.recycle();
        LogUtils.d(this, "mItemHorizontalSpace == > " + mItemHorizontalSpace);
        LogUtils.d(this, "mItemVerticalSpace == > " + mItemVerticalSpace);
    }

    /**
     * 对外暴露添加数据方法
     *
     * @param textList
     */
    public void setTextList(List<SelectedItem> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        //遍历内容
        for (SelectedItem text : mTextList) {
            //添加子view
            //LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view,this,true);
            // 等价于
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text.getName());
            item.setPadding(FontSwitchUtil.sp2px(mContext, 8f), FontSwitchUtil.sp2px(mContext, 8f),
                    FontSwitchUtil.sp2px(mContext, 8f), FontSwitchUtil.sp2px(mContext, 8f));
            item.setTextSize(textSize);
            item.setIncludeFontPadding(false);
            item.setFreezesText(true);
            if (text.isHasSelected()) {
                item.setBackgroundResource(R.drawable.selector_selected);
                item.setTextColor(mContext.getColor(R.color.colorPrimary));
               /* holder.content.setBackground(getItem(position).isHasSelected()
                        ? parent.getContext().getDrawable(R.drawable.selector_selected)
                        : parent.getContext().getDrawable(R.drawable.selector_uncheck_selected));
                holder.content.setTextColor(getItem(position).isHasSelected()
                        ? parent.getContext().getColor(R.color.colorPrimary)
                        : parent.getContext().getColor(R.color.colorMiddle));     */
            } else {
                item.setBackgroundResource(R.drawable.selector_uncheck_selected);
                item.setTextColor(textColor);
            }
            item.setOnClickListener(v -> {
                if (mItemClickListener != null) {
                    mItemClickListener.onFlowItemClick(text);
                }
            });
            addView(item);
        }
    }

    //这个是描述所有的行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        //这个是描述单行
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LogUtils.d(this, "mSelfWidth == > " + mSelfWidth);
        //测量
        LogUtils.d(this, "onMeasure -- > " + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要进行测量
                continue;
            }
            //测量前
            LogUtils.d(this, "before height -- > " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //测量后
            LogUtils.d(this, "after height -- > " + itemView.getMeasuredHeight());
            if (line == null) {
                //说明当前行为空，可以添加进来
                line = createNewLine(itemView);
            } else {
                //判断是否可以再继续添加
                if (canBeAdd(itemView, line)) {
                    //可以添加，添加去
                    line.add(itemView);
                } else {
                    //新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        //测量自己
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以再继续添加新数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有已经添加的子view宽度相加+(line.size()+1)*mItemHorizontalSpace + itemView.getMeasuredWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWith = itemView.getMeasuredWidth();
        for (View view : line) {
            //叠加所有已经添加控件的宽度
            totalWith += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWith += mItemHorizontalSpace * (line.size() + 1);
        //LogUtils.d(this,"totalWith -- > " + totalWith);
        //LogUtils.d(this,"mSelfWidth -- > " + mSelfWidth);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWith <= mSelfWidth;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        LogUtils.d(this, "onLayout -- > " + getChildCount());
        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                //每一行里的每个item
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                //
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemHorizontalSpace;
        }
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(SelectedItem selectedItem);
    }
}