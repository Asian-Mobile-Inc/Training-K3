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

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class ScalableBarChartView extends View {

    private Paint barPaint1;
    private Paint barPaint2;
    private Paint textPaint;

    private Paint framePaint;
    private List<Data> dataPoints;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    public ScalableBarChartView(Context context) {
        super(context);
        init();
    }

    public ScalableBarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        barPaint1 = new Paint();
        barPaint1.setColor(ContextCompat.getColor(getContext(), R.color.darkBlue));
        barPaint1.setAntiAlias(true);

        barPaint2 = new Paint();
        barPaint2.setColor(ContextCompat.getColor(getContext(), R.color.darkRed));
        barPaint2.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        textPaint.setAntiAlias(true);

        framePaint = new Paint();
        framePaint.setColor(Color.BLACK);
        framePaint.setTextSize(40);
        framePaint.setAntiAlias(true);
        framePaint.setFakeBoldText(true);

        dataPoints = new ArrayList<>();

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int width = getWidth();
        int height = getHeight();
        String title = "Sales and Expenses";

        //title
        canvas.drawText(title, 33f * width / 100, height * 12f / 100, framePaint);

        //duong thang doc
        canvas.drawLine(160, height * 90f / 100 + 10, 160, height * 20f / 100, textPaint);
        canvas.drawLine(width * 2f / 100, height * 98f / 100, width * 2f / 100, height * 2f / 100, framePaint);
        canvas.drawLine(width * 98f / 100, height * 98f / 100, width * 98f / 100, height * 2f / 100, framePaint);

        canvas.save();

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);

        canvas.drawLine(width - 150, height * 10f / 100, 150, height * 10f / 100, textPaint);
        canvas.drawLine(width - 150, height * 80f / 100, 200, height * 80f / 100, textPaint);
        canvas.drawLine(width - 150, height * 70f / 100, 200, height * 70f / 100, textPaint);
        canvas.drawLine(width - 150, height * 60f / 100, 200, height * 60f / 100, textPaint);
        canvas.drawLine(width - 150, height * 50f / 100, 200, height * 50f / 100, textPaint);
        canvas.drawLine(width - 150, height * 40f / 100, 200, height * 40f / 100, textPaint);
        canvas.drawLine(width - 150, height * 30f / 100, 200, height * 30f / 100, textPaint);
        canvas.drawLine(width - 150, height * 20f / 100, 200, height * 20f / 100, textPaint);

        canvas.drawLine(width * 2f / 100, height * 98f / 100, width * 98f / 100, height * 98f / 100, framePaint);
        canvas.drawLine(width * 2f / 100, height * 2f / 100, width * 98f / 100, height * 2f / 100, framePaint);
        canvas.save();

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);
        drawValue(width, height, canvas);
        canvas.restore();

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);

        canvas.drawRect(width - 180, (float) height * 45 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 45 / 100) + (width * 2 / 100)), barPaint1);
        canvas.drawText("Sales", width - 140, (float) (height * 45 / 100 + (width * 2 / 100)), textPaint);
        canvas.drawRect(width - 180, (float) height * 55 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 55 / 100) + (width * 2 / 100)), barPaint2);
        canvas.drawText("Expenses", width - 140, (float) (height * 55 / 100 + (width * 2 / 100)), textPaint);

        int dataSize = dataPoints.size();

        int startIndex = 0;
        int columnWidth = 7 * width / 200;
        int bar1Height;
        int bar2Height;
        float startX = 190f + finalDistanceChangX + distanceChangeX;
        if (startX <= 190f) {
            startX = 190f;
            startIndex = (int) (Math.abs(finalDistanceChangX + distanceChangeX) / (2 * columnWidth * scaleFactor));
        }
        int kt = (int) ((width - 360) / (columnWidth * 2 * scaleFactor + 40));
        if (kt > dataSize) {
            kt = dataSize;
        }
        float startY, endX, endY;

        endY = width - 200;
        for (int i = 0; i <= kt; i++) {
            if (endY > startX) {
                if ((i + startIndex) < dataSize) {
                    Data dataPoint = dataPoints.get(i + startIndex);
                    bar1Height = dataPoint.getValue1() / (200000 / height);
                    bar2Height = dataPoint.getValue2() / (200000 / height);


                    startY = ((height - 10f * height / 100) - (bar1Height * scaleFactor));
                    if (startY < height * 2f / 10) {
                        startY = height * 2f / 10;
                    }
                    endX = startX + columnWidth * scaleFactor;
                    endY = getHeight();


                    // Vẽ cột 1
                    canvas.drawRect(startX, startY, endX, (float) height * 90 / 100, barPaint1);
                    startY = ((height - 10f * height / 100) - (bar2Height * scaleFactor));
                    if (startY < height * 2f / 10) {
                        startY = height * 2f / 10;
                    }
                    // Vẽ cột 2
                    canvas.drawRect(startX + columnWidth * scaleFactor, startY, startX + 2 * columnWidth * scaleFactor, (float) height * 90 / 100, barPaint2);

                    startX += 2 * columnWidth * scaleFactor + 40;
                    canvas.drawText(dataPoint.getName(), startX - columnWidth * scaleFactor - 40 - (textPaint.measureText(dataPoint.getName()) / 2), (float) height * 96 / 100, textPaint);

                    canvas.drawLine(startX - 20, (float) (height * 90.0 / 100), startX - 20, (float) height * 92 / 100, textPaint);
                }
            }
        }
    }

    float distanceChangeX, distanceChangeY, startDistanceX, startDistanceY, finalDistanceChangX;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDistanceX = event.getX();
                startDistanceY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                distanceChangeX = event.getX() - startDistanceX;
                distanceChangeY = event.getY() - startDistanceY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                finalDistanceChangX += distanceChangeX;
                break;
        }

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

    public void addBar(Data height) {
        dataPoints.add(height);
        invalidate();
    }

    private void drawValue(int width, int height, Canvas canvas) {

        String text = "$";
        for (int i = 0; i <= 7; i++) {
            canvas.drawText(text + (int) ((7 - i) * 20 / scaleFactor) + ",000", width * 3f / 100, height * (i + 2) / 10f, textPaint);

        }
    }
}

