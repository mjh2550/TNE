package com.solmi.biobrainexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class Simple1ChannelGraph extends View {

    /**
     * Maximum number of X-axis data.
     */
    private float MAX_X_AXIS_COUNT = 1250f;
    /**
     * Line position value object to be drawn.
     */
    private Path mPath = null;
    /**
     * Line paint to be drawn.
     */
    private Paint mPaint = null;
    /**
     * Number of data drawn.
     */
    private int mDataCount = 0;
    /**
     * View Width.
     */
    private int mViewWidth = 0;
    /**
     * View height.
     */
    private int mViewHeight = 0;
    /**
     * View Center.
     */
    private int mCenter = 0;
    /**
     * Interval per X-axis data.
     */
    private float mXValueTick = 0f;
    /**
     * Interval per Y-axis data.
     */
    private float mYValueTick = 0f;

    public Simple1ChannelGraph(Context context) {
        super(context);
        initComponent();
    }

    public Simple1ChannelGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public Simple1ChannelGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    /**
     * Function to initialize class components.
     */
    private void initComponent() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFFCC3333);
        mPaint.setStrokeWidth(3);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xFFFFFFFF);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * Function to data sampling rate.
     * @param samplingRate data sampling rate.
     */
    public void setSamplingRate(float samplingRate) {
        MAX_X_AXIS_COUNT = 4 * samplingRate;
        clear();
    }

    /**
     * Function to initialize view.
     */
    public void clear() {
        mPath.reset();
        mDataCount = 0;
        mViewWidth = getWidth();
        mViewHeight = getHeight();
        mCenter = mViewHeight / 2;
        mXValueTick = mViewWidth / MAX_X_AXIS_COUNT;
        mYValueTick = mCenter / 3f;
        invalidate();
    }

    /**
     * Function to add data to be drawn.
     * @param value Data to be drawn.
     */
    public void putValue(float value) {
        if (mDataCount == 0) {
            mPath.reset();
            mPath.moveTo(getX(mDataCount), getY(value));
        } else {
            mPath.lineTo(getX(mDataCount), getY(value));
        }

        mDataCount++;
        if (mDataCount > MAX_X_AXIS_COUNT) {
            mDataCount = 0;
        }

        invalidate();
    }

    /**
     * Function to get the X-axis position value to be drawn.
     * @param xValue X-axis value.
     * @return X-axis position value to be drawn.
     */
    private float getX(int xValue) {
        float pos = mXValueTick * xValue;
        return pos;
    }

    /**
     * Function to get the Y-axis position value to be drawn.
     * @param yValue Y-axis value.
     * @return Y-axis position value to be drawn.
     */
    private float getY(float yValue) {
        float pos = mCenter - (mYValueTick * yValue);
        return pos;
    }
}
