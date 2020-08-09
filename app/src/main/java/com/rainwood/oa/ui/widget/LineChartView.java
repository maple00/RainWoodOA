package com.rainwood.oa.ui.widget;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;

import com.rainwood.oa.R;
import com.rainwood.oa.model.domain.ChartEntity;
import com.rainwood.oa.utils.LogUtils;
import com.rainwood.tools.utils.CalculateUtil;
import com.rainwood.tools.utils.FontSwitchUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: a797s
 * @Date: 2020/4/27 17:45
 * @Desc: 自定义绘制线性图表
 */
public class LineChartView extends View {

    private Context mContext;
    /**
     * 背景的颜色
     */
    private static final int BG_COLOR = Color.WHITE;
    /**
     * 视图的宽和高
     */
    private int mTotalWidth, mTotalHeight;
    private int mPaddingRight, mPaddingBottom, mPaddingTop;
    /**
     * x轴 y轴 起始坐标
     */
    private float mStartX, mStartY;
    /**
     * 图表绘制区域的顶部和底部  图表绘制区域的最大高度
     */
    private float maxHeight;
    private int mBottomMargin;
    private int mTopMargin;
    private int mLeftMargin, mRightMargin;
    /**
     * 画笔 背景，轴 ，线 ，text ,点 提示线
     */
    private Paint mBgPaint, mAxisPaint, mLinePaint, mTextPaint, mPointPaint, mHintPaint;
    /**
     * 原点的半径
     */
    private static final float RADIUS = 10;
    // 一条线
    private List<ChartEntity> mData;
    // 每条线
    private List<List<ChartEntity>> mLineDataList = new ArrayList<>();
    /**
     * 右边的最大和最小值
     */
    private int mMaxRight, mMinRight;
    /**
     * item中的Y轴最大值
     */
    private float mMaxYValue;
    /**
     * 最大分度值
     */
    private float mMaxYDivisionValue;
    /**
     * 线的路径
     */
    Path mLinePath;
    /**
     * 向右边滑动的距离
     */
    private float mLeftMoving;
    private String mLeftAxisUnit = "单位";
    /**
     * 两个点之间的距离
     */
    private int mSpace;
    /**
     * 绘制的区域
     */
    private RectF mDrawArea, mHintArea;
    private Rect mLeftWhiteRect, mRightWhiteRect;
    /**
     * 保存点的x坐标
     */
    private List<List<Point>> mLinePoints = new ArrayList<>();
    /**
     * 左后一次的x坐标
     */
    private float mLastPointX;
    /**
     * 当前移动的距离
     */
    private float mMovingThisTime = 0.0f;

    /**
     * 速度跟踪器
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 滑动
     */
    private Scroller mScroller;
    /**
     * fling最大速度
     */
    private int mMaxVelocity;
    /**
     * 是不是绘制曲线
     */
    private boolean isCurve = false;
    /**
     * 中间的横线是否是虚线
     */
    private boolean isMiddleDottedLine = false;

    /**
     * 点击的点的位置
     */
    private int mSelectIndex;
    /**
     * 点击的线
     */
    private int selectedLine;
    /**
     * 是否绘制提示文字
     */
    private boolean isDrawHint = false;
    private int mHintColor = getResources().getColor(R.color.colorStockLine);
    private EdgeEffect mEdgeEffectLeft, mEdgeEffectRight;
    /**
     * 优化fling状态下的边缘效果绘制
     */
    private boolean mHasAbsorbLeft, mHasAbsorbRight;
    /**
     * 是否需要边缘反馈效果
     */
    private boolean mNeedEdgeEffect = true;

    public LineChartView(Context context) {
        super(context);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        setWillNotDraw(false);
        mContext = context;
        mEdgeEffectLeft = new EdgeEffect(context);
        mEdgeEffectRight = new EdgeEffect(context);
        mSpace = FontSwitchUtil.dip2px(getContext(), 30);
        mBottomMargin = FontSwitchUtil.dip2px(getContext(), 30);
        mTopMargin = FontSwitchUtil.dip2px(context, 30);
        mRightMargin = FontSwitchUtil.dip2px(getContext(), 20);
        mLeftMargin = FontSwitchUtil.dip2px(getContext(), 10);

        mScroller = new Scroller(context);
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.WHITE);

        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(FontSwitchUtil.dip2px(context, 1));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(FontSwitchUtil.dip2px(getContext(), 9));
        mTextPaint.setColor(getResources().getColor(R.color.labelColor));

        // 线
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(FontSwitchUtil.dip2px(context, 2));
        Random random = new Random();
        int color = 0xff000000 | random.nextInt(0xffffff);
        mLinePaint.setColor(color);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.STROKE);
        mPointPaint.setStrokeWidth(FontSwitchUtil.dip2px(context, 2));

        float txtSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                12, context.getResources().getDisplayMetrics());
        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);
        mHintPaint.setTextSize(txtSize);
        mHintPaint.setStyle(Paint.Style.FILL);
        mHintPaint.setAlpha(100);
        mHintPaint.setStrokeWidth(2);
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mHintPaint.setColor(mHintColor);

        mLinePath = new Path();
    }

    private int tempCount = 0;

    public void setData(List<ChartEntity> list, boolean isCurve, boolean isDottedLine) {
        mLineDataList.add(tempCount++, list);
        this.mData = list;
        this.isCurve = isCurve;
        this.isMiddleDottedLine = isDottedLine;
        //计算最大值
        if (list.size() > 0) {
            mMaxYValue = list.get(0).getyValue();
            mMaxYValue = calculateMax(list);
            getRange(mMaxYValue);
        }
    }

    /**
     * 计算出Y轴最大值
     *
     * @return
     */
    private float calculateMax(List<ChartEntity> list) {
        float start = list.get(0).getyValue();
        for (ChartEntity entity : list) {
            if (entity.getyValue() > start) {
                start = entity.getyValue();
            }
        }
        return start;
    }

    /**
     * 是否滑动到了左边缘
     *
     * @return
     */
    private boolean isArriveAtLeftEdge() {
        return mLeftMoving <= 0;
    }

    /**
     * 是否滑动到了右边缘
     *
     * @return
     */
    private boolean isArriveAtRightEdge() {
        return mLeftMoving >= mMaxRight - mMinRight;
    }

    /**
     * 得到图标的Y轴刻度
     *
     * @param maxValueInItems
     */
    private void getRange(float maxValueInItems) {
        int scale = CalculateUtil.getScale(maxValueInItems);//获取这个最大数 数总共有几位
        float unScaleValue = (float) (maxValueInItems / Math.pow(10, scale));//最大值除以位数之后剩下的值  比如1200/1000 后剩下1.2

        mMaxYDivisionValue = (float) (CalculateUtil.getRangeTop(unScaleValue) * Math.pow(10, scale));//获取Y轴的最大的分度值
        mStartX = CalculateUtil.getDivisionTextMaxWidth(mMaxYDivisionValue, mContext) + 20;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTotalWidth = w - getPaddingLeft() - getPaddingRight();
        mTotalHeight = h - getPaddingTop() - getPaddingBottom();
        maxHeight = h - getPaddingTop() - getPaddingBottom() - mBottomMargin - mTopMargin;
        mPaddingBottom = getPaddingBottom();
        mPaddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();
        mLeftWhiteRect = new Rect(0, 0, 0, mTotalHeight);
        mRightWhiteRect = new Rect(mTotalWidth - mLeftMargin * 2, 0, mTotalWidth, mTotalHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    //获取滑动范围和指定区域
    private void getArea() {
        for (List<ChartEntity> entityList : mLineDataList) {
            if (entityList != null) {
                mMaxRight = (int) (mStartX + mSpace * entityList.size());
                mMinRight = mTotalWidth - mLeftMargin - mRightMargin;
                mStartY = mTotalHeight - mBottomMargin - mPaddingBottom;
                mDrawArea = new RectF(mStartX, mPaddingTop, mTotalWidth - mPaddingRight - mRightMargin, mTotalHeight - mPaddingBottom);
                mHintArea = new RectF(mDrawArea.right - mDrawArea.right / 4, mDrawArea.top + mTopMargin / 2,
                        mDrawArea.right, mDrawArea.top + mDrawArea.height() / 4 + mTopMargin / 2);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mLineDataList == null || mLineDataList.isEmpty()) return;
        if (!mNeedEdgeEffect) return;
        if (!mEdgeEffectLeft.isFinished()) {
            canvas.save();
            canvas.rotate(-90);
            canvas.translate(-mStartY, mDrawArea.left);
            mEdgeEffectLeft.setSize((int) maxHeight, (int) maxHeight);
            if (mEdgeEffectLeft.draw(canvas)) {
                postInvalidate();
            }
            canvas.restore();
        }

        if (!mEdgeEffectRight.isFinished()) {
            canvas.save();
            canvas.rotate(90);
            canvas.translate(mTopMargin, -mDrawArea.right);
            mEdgeEffectRight.setSize((int) maxHeight, (int) maxHeight);
            if (mEdgeEffectRight.draw(canvas)) {
                postInvalidate();
            }
            canvas.restore();
        }
    }

    private void endDrag() {
        recycleVelocityTracker();
        if (mEdgeEffectLeft != null) {
            mEdgeEffectLeft.onRelease();
        }
        if (mEdgeEffectRight != null) {
            mEdgeEffectRight.onRelease();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineDataList == null || mLineDataList.isEmpty()) return;
        getArea();
        mLinePoints.clear();
        canvas.drawColor(BG_COLOR);
        //重置线
        mLinePath.reset();
        mLinePath.incReserve(mData.size());
        checkTheLeftMoving();
        //画中间的线
        drawWhiteLine(canvas);
        //画左边的Y轴
        canvas.drawLine(mStartX, mStartY, mStartX, mTopMargin / 2, mAxisPaint);
        //左边Y轴的单位
        canvas.drawText(mLeftAxisUnit, mStartX, mTopMargin / 2 - 14, mTextPaint);
        //画右边的Y轴
//        canvas.drawLine(mTotalWidth - mLeftMargin * 2, mStartY, mTotalWidth - mLeftMargin * 2, mTopMargin / 2, mAxisPaint);
        //画X轴 下面的和上面
        canvas.drawLine(mStartX, mStartY, mTotalWidth - mLeftMargin * 2, mStartY, mAxisPaint);
//        canvas.drawLine(mStartX, mTopMargin / 2, mTotalWidth - mLeftMargin * 2, mTopMargin / 2, mAxisPaint);

        //调用clipRect()方法后，只会显示被裁剪的区域
        // canvas.clipRect(mDrawArea.left, mDrawArea.top, mDrawArea.right, mDrawArea.bottom + mDrawArea.height());
        //画线形图
        drawLines(canvas);
        //画左边和右边的遮罩层
        mLeftWhiteRect.right = (int) mStartX;
        canvas.drawRect(mLeftWhiteRect, mBgPaint);
        canvas.drawRect(mRightWhiteRect, mBgPaint);

        //画左边的Y轴text
        drawLeftYAxis(canvas);

        //画线上的点
        drawCircles(canvas);
        //画X轴的text
        drawXAxisText(canvas);
        if (isDrawHint) {
            drawHint(canvas);
        }
    }

    private void drawXAxisText(Canvas canvas) {
        //这里设置 x 轴的字一条最多显示3个，大于三个就换行
        // for (List<ChartEntity> entityList : mLineDataList) {
        for (int j = 0; j < mLineDataList.size(); j++) {
            for (int i = 0; i < mLineDataList.get(j).size(); i++) {
                String text = mLineDataList.get(j).get(i).getxLabel();
                //当在可见的范围内才绘制
                if (mLinePoints.get(j).get(i).x >= mStartX - (mTextPaint.measureText(text)) / 2 && mLinePoints.get(j).get(i).x < (mTotalWidth - mLeftMargin * 2)) {
                    // LogUtils.d(this, "x轴上的距离：" + (mLinePoints.get(i).x - (mTextPaint.measureText(text)) / 2 ));
                    if (text.length() <= 4) {
                        // if (text.length() <= text.length()) {
                        canvas.drawText(text, mLinePoints.get(j).get(i).x - (mTextPaint.measureText(text)) / 2, mTotalHeight - mBottomMargin * 2 / 3, mTextPaint);
                    } else {
                        String text1 = text.substring(0, text.length());
                        String text2 = text.substring(text.length(), text.length());
                        canvas.drawText(text1, mLinePoints.get(j).get(i).x - (mTextPaint.measureText(text1)) / 2, mTotalHeight - mBottomMargin * 2 / 3, mTextPaint);
                        canvas.drawText(text2, mLinePoints.get(j).get(i).x - (mTextPaint.measureText(text2)) / 2, mTotalHeight - mBottomMargin / 3, mTextPaint);
                    }
                }
            }
        }
    }

    private void drawWhiteLine(Canvas canvas) {
        mAxisPaint.setColor(Color.parseColor("#EEEEEE"));
        float eachHeight = (maxHeight / 5f);
        for (int i = 1; i <= 5; i++) {
            float startY = mStartY - eachHeight * i;
            if (startY < mTopMargin / 2) {
                break;
            }
            // 设置横线的虚线
            if (isMiddleDottedLine) {
                mAxisPaint.setStyle(Paint.Style.STROKE);
                mAxisPaint.setAntiAlias(true);
                mAxisPaint.setStrokeWidth(2.f);
                mAxisPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
            }
            canvas.drawLine(mStartX, startY, mTotalWidth - mLeftMargin * 2, startY, mAxisPaint);
        }
        mAxisPaint.setColor(Color.BLACK);
    }

    private float percent = 1f;
    private TimeInterpolator pointInterpolator = new DecelerateInterpolator();

    public void startAnimation(int duration) {
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0f, 1);
        mAnimator.setDuration(duration);
        mAnimator.setInterpolator(pointInterpolator);
        mAnimator.addUpdateListener(animation -> {
            float percent = (float) animation.getAnimatedValue();
            mLinePaint.setPathEffect(new DashPathEffect(new float[]{pathLength, pathLength}, pathLength - pathLength * percent));
            invalidate();
        });
        mAnimator.start();
    }

    private float pathLength;

    /**
     * 画线形图
     */
    private void drawLines(Canvas canvas) {
        float distance = 0;
//        float lineStart = mStartX + mTextPaint.measureText(mData.get(0).getxLabel()) / 2 + 20;
        float lineStart = mStartX;
        // for (List<ChartEntity> entityList : mLineDataList) {
        for (int j = 0; j < mLineDataList.size(); j++) {
            List<Point> mSubPoint = new ArrayList<>();
            for (int i = 0; i < mLineDataList.get(j).size(); i++) {
                distance = mSpace * i - mLeftMoving;
                float lineHeight = mLineDataList.get(j).get(i).getyValue() * maxHeight / mMaxYDivisionValue;
                if (i == 0) {
                    mLinePath.moveTo(lineStart + distance, (mStartY - lineHeight) * percent);
                } else {
                    if (!isCurve) {
                        mLinePath.lineTo(lineStart + distance, (mStartY - lineHeight) * percent);
                    } else {
                        float lineHeightPre = mLineDataList.get(j).get(i - 1).getyValue() * maxHeight / mMaxYDivisionValue;
                        mLinePath.cubicTo(lineStart + distance - mSpace / 2, (mStartY - lineHeightPre) * percent,
                                lineStart + distance - mSpace / 2, (mStartY - lineHeight) * percent,
                                lineStart + distance, (mStartY - lineHeight) * percent);
                    }

                }
                Point point = new Point((int) (lineStart + distance), (int) (mStartY - lineHeight));
                mSubPoint.add(point);
            }
            mLinePoints.add(mSubPoint);
            PathMeasure measure = new PathMeasure(mLinePath, false);
            pathLength = measure.getLength();
            canvas.drawPath(mLinePath, mLinePaint);
        }
    }

    /**
     * 画线上的点
     */
    private void drawCircles(Canvas canvas) {
        for (int j = 0; j < mLineDataList.size(); j++) {
            for (int i = 0; i < mLineDataList.get(j).size(); i++) {
                // 设置点的颜色j
                mPointPaint.setColor(getResources().getColor(R.color.green10));
                // 画点
                if (mLinePoints.get(j).get(i).x >= mStartX
                        && mLinePoints.get(j).get(i).x
                        < (mTotalWidth - mLeftMargin * 2)) {
                    canvas.drawCircle(mLinePoints.get(j).get(i).x, mLinePoints.get(j).get(i).y * percent, RADIUS, mPointPaint);
                }
            }
        }
    }

    /**
     * 画Y轴上的text (1)当最大值大于1 的时候 将其分成5份 计算每个部分的高度  分成几份可以自己定
     * （2）当最大值大于0小于1的时候  也是将最大值分成5份
     * （3）当为0的时候使用默认的值
     *
     * @param canvas
     */
    private void drawLeftYAxis(Canvas canvas) {
        float eachHeight = (maxHeight / 5f);
        if (mMaxYValue > 1) {
            for (int i = 1; i <= 5; i++) {
                float startY = mStartY - eachHeight * i;
                if (startY < mTopMargin / 2) {
                    break;
                }
                BigDecimal maxValue = new BigDecimal(mMaxYDivisionValue);
                BigDecimal fen = new BigDecimal(0.2 * i);
                String text = null;
                //因为图表分了5条线，如果能除不进，需要显示小数点不然数据不准确
                if (mMaxYDivisionValue % 5 != 0) {
                    text = String.valueOf(maxValue.multiply(fen).floatValue());
                } else {
                    text = String.valueOf(maxValue.multiply(fen).longValue());
                }
                canvas.drawText(text, mStartX - mTextPaint.measureText(text) - 5, startY + mTextPaint.measureText("0"), mTextPaint);
            }
        } else if (mMaxYValue > 0 && mMaxYValue <= 1) {
            for (int i = 1; i <= 5; i++) {
                float startY = mStartY - eachHeight * i;
                if (startY < mTopMargin / 2) {
                    break;
                }
                float textValue = CalculateUtil.numMathMul(mMaxYDivisionValue, (float) (0.2 * i));
                String text = String.valueOf(textValue);
                canvas.drawText(text, mStartX - mTextPaint.measureText(text) - 5, startY + mTextPaint.measureText("0"), mTextPaint);
            }
        } else {
            for (int i = 1; i <= 5; i++) {
                float startY = mStartY - eachHeight * i;
                String text = String.valueOf(10 * i);
                canvas.drawText(text, mStartX - mTextPaint.measureText(text) - 5, startY + mTextPaint.measureText("0"), mTextPaint);
            }
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mMovingThisTime = (mScroller.getCurrX() - mLastPointX);
            mLeftMoving = mLeftMoving + mMovingThisTime;
            mLastPointX = mScroller.getCurrX();
            if (mNeedEdgeEffect) {
                if (!mHasAbsorbLeft && isArriveAtLeftEdge()) {
                    mHasAbsorbLeft = true;
                    mEdgeEffectLeft.onAbsorb((int) mScroller.getCurrVelocity());
                } else if (!mHasAbsorbRight && isArriveAtRightEdge()) {
                    mHasAbsorbRight = true;
                    mEdgeEffectRight.onAbsorb((int) mScroller.getCurrVelocity());
                }
            }
            postInvalidate();
            postInvalidate();
        } else {
            mHasAbsorbLeft = false;
            mHasAbsorbRight = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastPointX = event.getX();
                mScroller.abortAnimation();//终止动画
                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(event);//将用户的移动添加到跟踪器中。
                break;
            case MotionEvent.ACTION_MOVE:
                float movex = event.getX();
                mMovingThisTime = mLastPointX - movex;
                mLeftMoving = mLeftMoving + mMovingThisTime;
                mLastPointX = movex;
                invalidate();
                mVelocityTracker.addMovement(event);
                if (mNeedEdgeEffect) {
                    if (isArriveAtLeftEdge()) {
                        mEdgeEffectLeft.onPull(Math.abs(mStartX) / mDrawArea.height());
                    } else if (isArriveAtRightEdge()) {
                        mEdgeEffectRight.onPull(Math.abs(mStartX) / mDrawArea.height());
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                clickAction(event);
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                int initialVelocity = (int) mVelocityTracker.getXVelocity();
                mVelocityTracker.clear();
                if (!isArriveAtLeftEdge() && !isArriveAtRightEdge()) {
                    mScroller.fling((int) event.getX(), (int) event.getY(), -initialVelocity / 2,
                            0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
                    invalidate();
                } else {
                    endDrag();
                }
                mLastPointX = event.getX();
                break;
            default:
                return super.onTouchEvent(event);
        }
        return true;
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            isDrawHint = false;
            postInvalidate();
        }
    };

    /**
     * 绘制提示文字
     */
    private void drawHint(Canvas canvas) {
        //竖线
        for (int i = 0; i < mLinePoints.size(); i++) {
            for (int j = 0; j < mLinePoints.get(i).size(); j++) {
                canvas.drawLine(mLinePoints.get(i).get(mSelectIndex).x, mStartY, mLinePoints.get(i).get(mSelectIndex).x, mTopMargin / 2, mHintPaint);
            }
        }
        //横线
        for (int i = 0; i < mLinePoints.size(); i++) {
            canvas.drawLine(mStartX, mLinePoints.get(i).get(mSelectIndex).y, mTotalWidth - mLeftMargin * 2, mLinePoints.get(i).get(mSelectIndex).y, mHintPaint);
        }
        mHintPaint.setAlpha(90);
        canvas.drawRect(mHintArea, mHintPaint);
        // 设置提示文字颜色
        mHintPaint.setTextSize(FontSwitchUtil.dip2px(mContext, 10.f));
        mHintPaint.setColor(mContext.getResources().getColor(R.color.fontColor));
        canvas.drawText("x : " + mLineDataList.get(selectedLine).get(mSelectIndex).getxLabel(), mHintArea.centerX(), mHintArea.centerY() - 12, mHintPaint);
        canvas.drawText("y : " + mLineDataList.get(selectedLine).get(mSelectIndex).getyValue(), mHintArea.centerX(),
                mHintArea.centerY() + 12 - mHintPaint.ascent() - mHintPaint.descent(), mHintPaint);
        // 设置背景颜色
        mHintPaint.setColor(getResources().getColor(R.color.colorGrey));
        // 提示文字消失时长
        postDelayed(mRunnable, 1000 * 5);
    }

    /**
     * 点击X轴坐标或者折线节点
     *
     * @param event
     */
    private void clickAction(MotionEvent event) {
        int range = FontSwitchUtil.dip2px(getContext(), 8);
        float eventX = event.getX();
        float eventY = event.getY();
        for (int j = 0; j < mLinePoints.size(); j++) {
            for (int i = 0; i < mLinePoints.get(j).size(); i++) {
                //节点
                int x = mLinePoints.get(j).get(i).x;
                int y = mLinePoints.get(j).get(i).y;
                if (eventX >= x - range && eventX <= x + range &&
                        eventY >= y - range && eventY <= y + range) {//每个节点周围4dp都是可点击区域
                    mSelectIndex = i;
                    selectedLine = j;
                    LogUtils.d("sxs", "点击的位置：" + i);
                    isDrawHint = true;
                    removeCallbacks(mRunnable);//移除掉上次点击的runnable
                    invalidate();
                    return;
                }
            }
        }
    }

    /**
     * 检查向左滑动的距离 确保没有画出屏幕
     */
    private void checkTheLeftMoving() {
        if (mLeftMoving > (mMaxRight - mMinRight)) {
            mLeftMoving = mMaxRight - mMinRight;
        }
        if (mLeftMoving < 0) {
            mLeftMoving = 0;
        }
    }

    /**
     * 启动和关闭硬件加速   在绘制View的时候支持硬件加速,充分利用GPU的特性,使得绘制更加平滑,但是会多消耗一些内存。
     *
     * @param enabled
     */
    public void setHardwareAccelerationEnabled(boolean enabled) {

        if (android.os.Build.VERSION.SDK_INT >= 11) {


            if (enabled)
                setLayerType(View.LAYER_TYPE_HARDWARE, null);
            else
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            Log.e("error",
                    "Cannot enable/disable hardware acceleration for devices below API level 11.");
        }
    }
}
