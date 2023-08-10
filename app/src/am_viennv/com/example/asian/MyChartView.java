package com.example.asian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class MyChartView extends View {

    private List<SellExpense> mData;
    private float mScale;
    private int mSalesColor;
    private int mExpensesColor;
    private final int mCount = 10;
    private final int mAxis = 170;
    private float mMaxValue = 0;
    private final Paint mPaintAxis = new Paint();
    private float mOffsetX = 0;
    private final float mColumnWidth = 58;
    private final float mColumnSpacing = 20;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mScrollY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mWidth;
    private float mHeight;

    public MyChartView(Context context) {
        super(context);
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<SellExpense> data) {
        this.mData = data;
        mMaxValue = getMaxValue();
        mScaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mData.size() > 6) {
                    float deltaX = event.getX() - mLastTouchX;
                    mLastTouchX = event.getX();
                    mOffsetX += deltaX * 2f;
                    float maxOffsetX;
                    if (mScaleFactor <= 1) {
                        maxOffsetX = -(mData.size() - 6) * (mColumnWidth * 2 + mColumnSpacing * 2);
                    } else {
                        maxOffsetX = -(mData.size() - 6) * (mColumnWidth * 6 + mColumnSpacing * 6);
                    }
                    mOffsetX = Math.min(0, Math.max(maxOffsetX, mOffsetX));
                }
                if (mScaleFactor > 1.0f) {
                    float deltaY = event.getY() - mLastTouchY;
                    mLastTouchY = event.getY();
                    if (mScrollY + deltaY >= 0) {
                        float maxScrollY = mMaxValue * mScale - 100;
                        mScrollY = Math.min(maxScrollY, mScrollY + deltaY);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mLastTouchX = 0;
                mLastTouchY = 0;
                invalidate();
                break;
        }
        return true;
    }

    public void setSalesColor(int salesColor) {
        this.mSalesColor = salesColor;
        invalidate();
    }

    public void setExpensesColor(int expensesColor) {
        this.mExpensesColor = expensesColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();
        if (mScaleFactor < 1) {
            mScrollY = 0;
        }
        mScale = (mHeight - 20) / getMaxValue();
        drawMainAxis(canvas);
        canvas.save();
        canvas.scale(1.0f, mScaleFactor, mWidth / 2, mHeight - 20);
        drawGrid(canvas, mScale, mCount / mScaleFactor);
        canvas.restore();

        canvas.save();
        canvas.scale(1.0f, mScaleFactor, mWidth / 2, mHeight - 20);
        drawDataColumns(canvas, mScale);
        canvas.restore();

        drawLabelColumn(canvas);

        drawRectangle(canvas);
        canvas.save();
        canvas.scale(1.0f, mScaleFactor, mWidth / 2, mHeight - 20);
        drawTextValue(canvas, getMaxValue());
        canvas.restore();
    }

    private void drawLabelColumn(Canvas canvas) {
        mPaintAxis.setColor(Color.BLACK);
        int initStartX = mAxis + (int) mOffsetX + 20;
        List<String> lstCalender = getLstLabel();

        for (int i = 1; i <= lstCalender.size(); i++) {
            canvas.drawText(lstCalender.get(i - 1), initStartX, mHeight, mPaintAxis);
            initStartX += mColumnWidth * 2 + mColumnSpacing * 2;
        }
    }

    private void drawTextValue(Canvas canvas, float maxValue) {
        canvas.translate(0f, mScrollY);
        int count;
        if (mScaleFactor > 1) {
            count = mCount * ((int) mScaleFactor);
        } else {
            count = mCount;
        }
        mPaintAxis.setTextSize(16);
        float heightLine = (mMaxValue * mScale) / count;
        for (int i = 1; i <= count; i++) {
            float value = (maxValue - i * maxValue / count);
            float y = i * heightLine;
            canvas.drawText("$" + value, 30, y, mPaintAxis);
        }
    }

    private void drawRectangle(Canvas canvas) {
        mPaintAxis.setColor(Color.WHITE);
        canvas.drawRect(4, mHeight, mAxis - 40, 0, mPaintAxis);
        mPaintAxis.setColor(Color.BLACK);
    }

    private List<String> getLstLabel() {
        List<String> labels = new ArrayList<>();
        for (SellExpense sellExpense : mData) {
            int monthNumber = sellExpense.getmMonth();
            Month[] months = Month.values();
            if (monthNumber >= 1 && monthNumber <= months.length) {
                String monthName = months[monthNumber - 1].name();
                labels.add(monthName);
            } else {
                labels.add("Invalid Month");
            }
        }
        return labels;
    }

    private void drawGrid(Canvas canvas, float mScale, float count) {
        canvas.translate(0f, mScrollY);
        int countLine = (int) count;
        float heightLine;
        if (mScaleFactor > 1) {
            countLine = mCount * (int) mScaleFactor;
            heightLine = (mMaxValue * mScale) / (countLine);
        } else {
            heightLine = (mMaxValue * mScale) / mCount;
        }
        mPaintAxis.setTextSize(18);
        for (int i = 0; i < countLine; i++) {
            canvas.drawLine(mAxis - 40, mHeight - 20 - heightLine * i, mWidth, mHeight - 20 - heightLine * i, mPaintAxis);
        }
    }

    private void drawDataColumns(Canvas canvas, float mScale) {
        canvas.translate(0f, mScrollY);
        Paint salesPaint = new Paint();
        salesPaint.setColor(mSalesColor);
        Paint expensesPaint = new Paint();
        expensesPaint.setColor(mExpensesColor);
        int initEndY = (int) mHeight - 20;
        int initStartX = mAxis + (int) mOffsetX;
        for (int i = 0; i < mData.size(); i++) {
            float salesHeight = (mMaxValue - mData.get(i).getmSales()) * mScale;
            float expensesHeight = (mMaxValue - mData.get(i).getmExpenses()) * mScale;
            canvas.drawRect(initStartX, salesHeight, initStartX + mColumnWidth, initEndY, salesPaint);
            canvas.drawRect(initStartX + mColumnWidth, expensesHeight, initStartX + mColumnWidth * 2, initEndY, expensesPaint);
            initStartX += mColumnWidth * 2 + mColumnSpacing * 2;
        }
    }

    private void drawMainAxis(Canvas canvas) {
        canvas.drawLine(mAxis - 20, mHeight, mAxis - 20, 0, mPaintAxis);
        canvas.save();
    }

    private float getMaxValue() {
        float max = 0;
        for (SellExpense sellExpense : mData) {
            float expensesValue = sellExpense.getmExpenses();
            float salesValue = sellExpense.getmSales();
            if (expensesValue > salesValue && expensesValue > max) {
                max = expensesValue;
            } else if (salesValue > expensesValue && salesValue > max) {
                max = salesValue;
            }
        }
        return max;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            if (scaleFactor > 1.0f || mScaleFactor > 1.0f) {
                mScaleFactor *= scaleFactor;
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
                invalidate();
            }
            return true;
        }
    }
}
