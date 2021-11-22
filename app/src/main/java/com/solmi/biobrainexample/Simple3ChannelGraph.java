package com.solmi.biobrainexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Simple3ChannelGraph extends View {

    /**
     * Number of Data channels to be drawn.
     */
    private final int DATA_CHANNEL = 3;
    /**
     * Maximum number of X-axis data.
     */
    private float MAX_X_AXIS_COUNT = 1250f;
    /**
     * Line position value object to be drawn.
     */
    private Path[] mPathArray = null;
    /**
     * Line paint to be drawn.
     */
    private Paint mPaint = null;
    /**
     * Number of data drawn.
     */
    private int mDataCount = 0;
    /**
     * View width.
     */
    private int mViewWidth = 0;
    /**
     * Data channel view height.
     */
    private int mChannelHeight = 0;
    /**
     * Center value per Data channel.
     */
    private int[] mCenter = null;
    /**
     * Interval per X-axis data.
     */
    private float mXValueTick = 0f;
    /**
     * Interval per Y-axis data.
     */
    private  float mYValueTick = 0f;

    public Simple3ChannelGraph(Context context) {
        super(context);
        initComponent();
    }

    public Simple3ChannelGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public Simple3ChannelGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    /**
     * Function to initialize class components.
     */
    private void initComponent() {
        mPathArray = new Path[DATA_CHANNEL];
        for (int index = 0; index < DATA_CHANNEL; index++) {
            mPathArray[index] = new Path();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0xFFCC3333);
        mPaint.setStrokeWidth(3);
        setWillNotDraw(false);
        mCenter = new int[DATA_CHANNEL];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(0xFFFFFFFF);
        for (Path path :
                mPathArray) {
            canvas.drawPath(path, mPaint);
        }
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
        for (Path path :
                mPathArray) {
            path.reset();
        }

        mDataCount = 0;
        mViewWidth = getWidth();
        int viewHeight = getHeight();
        mChannelHeight = viewHeight / DATA_CHANNEL;
        int channelCenter = mChannelHeight / 2;
        mCenter[0] = channelCenter;
        for (int index = 1; index < DATA_CHANNEL; index++) {
            mCenter[index] = mCenter[index - 1] + mChannelHeight;
        }

        mXValueTick = mViewWidth / MAX_X_AXIS_COUNT;
        mYValueTick = channelCenter / 3f;
        invalidate();
    }

    /**
     * Function to add data to be drawn.
     * @param valueArray Data array to be drawn.
     */
    public void putValueArray(float[] valueArray) {
        float x = getX(mDataCount);
        for (int index = 0; index < DATA_CHANNEL; index++) {
            Path path = mPathArray[index];
            if (mDataCount == 0) {
                path.reset();
                path.moveTo(x, getY(index, valueArray[index]));
            } else {
                path.lineTo(x, getY(index, valueArray[index]));
            }
        }

        mDataCount++;
        if (mDataCount > MAX_X_AXIS_COUNT) {
            mDataCount = 0;
        }

        invalidate();
    }

    /**
     * Function to get X-axis position value to be drawn.
     * @param xValue X-axis value.
     * @return X-axis position value to be drawn.
     */
    private float getX(int xValue) {
        float pos = (mXValueTick) * xValue;
        return pos;
    }

    /**
     * Function to get Y-axis position value to be drawn.
     * @param channel Data channel to be drawn.
     * @param yValue  Y-axis value
     * @return Y-axis position value to be drawn
     */
    private float getY(int channel, float yValue) {
        float pos = mCenter[channel] - (mYValueTick * yValue);
        return pos;
    }
}
