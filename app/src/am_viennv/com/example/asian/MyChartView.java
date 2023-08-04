package com.example.asian;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyChartView extends View {
    private List<SellExpense> mData;
    private int mSalesColor;
    private int mExpensesColor;
    private float mInitialDistance;
    private float mScaleFactor = 1.0f;
    private Paint mExpensesPaint;
    private Paint mSalesPaint;
    private Paint mPaintSite;
    private Paint mPaint;
    private float mLastTouchY;
    private float mOffsetY = -50;
    private float mLastTouchX;
    private float mOffsetX = 0;

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
        mExpensesPaint = new Paint();
        mSalesPaint = new Paint();
        mPaintSite = new Paint();
        mPaint = new Paint();
        invalidate();
    }

    public void setSalesColor(int salesColor) {
        this.mSalesColor = salesColor;
        invalidate();
    }

    public void setExpensesColor(int expensesColor) {
        this.mExpensesColor = expensesColor;
        invalidate();
    }

    private float getDistance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount = event.getPointerCount();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (pointerCount == 2) {
                    mInitialDistance = getDistance(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (pointerCount == 1 && mScaleFactor != 1f) {
                    float deltaY = event.getY() - mLastTouchY;
                    mLastTouchY = event.getY();
                    mOffsetY += deltaY;
                    invalidate();
                }
                if (mData.size() > 6) {
                    float deltaX = event.getX() - mLastTouchX;
                    mLastTouchX = event.getX();
                    mOffsetX += deltaX * 1.2f;
                    invalidate();
                }
                if (pointerCount == 2) {
                    float newDistance = getDistance(event);
                    float scale = newDistance / mInitialDistance;
                    mScaleFactor *= scale;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                mLastTouchX = 0;
                mLastTouchY = 0;
                break;
        }
        return true;
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, mOffsetY);
        mPaint.setColor(BLACK);
        float height = getHeight();
        int countLine = 10;
        float maxValue = getMaxValue();

        mExpensesPaint.setColor(mExpensesColor);
        mSalesPaint.setColor(mSalesColor);
        mPaintSite.setColor(WHITE);
        mPaint.setTextSize(16);
        canvas.save();
        if (mScaleFactor < 1) mScaleFactor = 1;
        if (mScaleFactor == 1) {
            mOffsetY = -50;
        }
        canvas.scale(1.0f, mScaleFactor, getWidth() / 2f, getHeight() / 2f);
        drawGrid(canvas, height, countLine);
        if (mData.size() > 6) {
            if (mOffsetX >= 0) {
                mOffsetX = 0;
            } else {
                if (mOffsetX <= -(mData.size() - 6) * 150 * mScaleFactor && mScaleFactor == 1) {
                    mOffsetX = -(mData.size() - 6) * 150;
                }
            }
        }
        float scale = calculateScale(height, maxValue);
        drawChartValue(height, maxValue, canvas, scale);
        canvas.drawRect(2f, 0, 140, (height + 50) * mScaleFactor, mPaintSite);
        drawTextLabelValue(countLine, maxValue, height, canvas);
        canvas.restore();
    }

    private float calculateScale(float height, float maxValue) {
        return height / maxValue;
    }

    private void drawGrid(Canvas canvas, float height, int countLine) {
        float withPadding = 140;
        canvas.drawLine(withPadding + 20, height + 20, withPadding + 20, height / (countLine * (int) mScaleFactor), mPaint);
        if (mScaleFactor > 4f) {
            mScaleFactor = 5f;
        }
        for (int i = 1; i <= countLine * ((int) mScaleFactor); i++) {
            canvas.drawLine(withPadding, i * height / (countLine * (int) mScaleFactor), (180 * mData.size()) * ((int) mScaleFactor), i * height / (countLine * (int) mScaleFactor), mPaint);
        }
    }

    private void drawChartValue(float height, float maxValue, Canvas canvas, float scale) {
        float withCol = 50;
        float initValueSales = 180 + mOffsetX;
        float initValueEx = initValueSales + withCol;
        float spaceBetween = 50;
        if (mScaleFactor >= 4f) {
            mPaint.setTextSize(16);
        }
        List<String> lstLabelMonth = getLstLabel();
        for (int i = 0; i < mData.size(); i++) {
            float leftSales = initValueSales + withCol * i;
            canvas.drawRect(leftSales, (maxValue - mData.get(i).getmSales()) * scale / mScaleFactor, leftSales + withCol, height, mSalesPaint);
            initValueSales += spaceBetween + withCol;

            canvas.drawText(lstLabelMonth.get(i), (leftSales + withCol / 2), height + spaceBetween, mPaint);
            float leftEx = initValueEx + withCol * i;
            canvas.drawRect(leftEx, (maxValue - mData.get(i).getmExpenses()) * scale / mScaleFactor, leftEx + withCol, height, mExpensesPaint);
            initValueEx += spaceBetween + withCol;
        }
    }

    private void drawTextLabelValue(int countLine, float maxValue, float height, Canvas canvas) {
        if (mScaleFactor == 4f) {
            mPaint.setTextSize(16);
        }
        for (int i = 1; i <= countLine * ((int) mScaleFactor); i++) {
            float value = (maxValue - i * maxValue / ((countLine) * ((int) mScaleFactor)));
            String formattedValue = formatString(value);
            float y = i * height / (countLine * ((int) mScaleFactor));
            canvas.drawText(formattedValue, 30, y, mPaint);
        }
    }

    private String formatString(float inp) {
        return String.format(Locale.getDefault(), "$%,d", (int) inp);
    }

    private List<String> getLstLabel() {
        if (mScaleFactor == 4f) {
            mPaint.setTextSize(16);
        }
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
}
