package com.example.asian;

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
    private List<Float> barHeights;
    private List<Paint> barPaints;
    private float barWidth = 100;
    private float maxBarHeight = 300;
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

        //
        barHeights = new ArrayList<>();
        barHeights.add(100f); // Giá trị mặc định cho các cột

        barPaints = new ArrayList<>();
        barPaints.add(createBarPaint(Color.BLUE)); // Màu sắc mặc định cho các cột

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    private Paint createBarPaint(int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        int width = getWidth();
        int height = getHeight();
        String title = "Sales and Expenses";

        //title
        canvas.drawText(title, 33 * width / 100, height * 12 / 100, framePaint);

        //duong thang doc
        canvas.drawLine(160, height * 90 / 100 + 10, 160, height * 20 / 100, textPaint);
        canvas.drawLine(width * 2 / 100, height * 98 / 100, width * 2 / 100, height * 2 / 100, framePaint);
        canvas.drawLine(width * 98 / 100, height * 98 / 100, width * 98 / 100, height * 2 / 100,framePaint);

        canvas.save();

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);
        //duong thang ngang
        canvas.drawLine(width - 150, height * 10 / 100, 150, height * 10 / 100, textPaint);
        canvas.drawLine(width - 150, height * 80 / 100, 200, height * 80 / 100, textPaint);
        canvas.drawLine(width - 150, height * 70 / 100, 200, height * 70 / 100, textPaint);
        canvas.drawLine(width - 150, height * 60 / 100, 200, height * 60 / 100, textPaint);
        canvas.drawLine(width - 150, height * 50 / 100, 200, height * 50 / 100, textPaint);
        canvas.drawLine(width - 150, height * 40 / 100, 200, height * 40 / 100, textPaint);
        canvas.drawLine(width - 150, height * 30 / 100, 200, height * 30 / 100, textPaint);
        canvas.drawLine(width - 150, height * 20 / 100, 200, height * 20 / 100, textPaint);

        canvas.drawLine(width * 2 / 100, height * 98 / 100, width * 98 / 100, height * 98 / 100, framePaint);
        canvas.drawLine(width * 2 / 100, height * 2 / 100, width * 98 / 100, height * 2 / 100, framePaint);
        canvas.save();

        //gia tri bên trai

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);
        drawValue(width, height, canvas);
        canvas.restore();
        /*try {
            canvas.rotate(180, (width * 87f / 100) + textPaint.measureText("$0") / 2,height * 10f / 100);
            canvas.drawText("$0", width * 87f / 100, height * 10f / 100 + 7, textPaint);
            canvas.restore();
            canvas.save();
            canvas.rotate(180, (width * 87f / 100) + textPaint.measureText("$20,000") / 2,height * 20f / 100);
            canvas.drawText("$20,000", width * 87f / 100, height * 20f / 100 + 7, textPaint);
            canvas.restore();

            canvas.drawText("$40,000", width * 87f / 100, height * 30f / 100 + 7, textPaint);
            canvas.drawText("$60,000", width * 87f / 100, height * 40f / 100 + 7, textPaint);
            canvas.drawText("$80,000", width * 87f / 100, height * 50f / 100 + 7, textPaint);
            canvas.drawText("$100,000", width * 87f / 100, height * 60f / 100 + 7, textPaint);
            canvas.drawText("$120,000", width * 87f / 100, height * 70f / 100 + 7, textPaint);
            canvas.drawText("$140,000", width * 87f / 100, height * 80f / 100 + 7, textPaint);


        } catch (Exception e)  {
            Log.d("ddd", e.toString());
        }*/

        canvas.rotate(180, getWidth() / 2f, getHeight() / 2f);
        //chu thich
        canvas.drawRect(width - 180, (float) height * 45 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 45 / 100) + (width * 2 / 100)), barPaint1);
        canvas.drawText("Sales", width - 140, (float) (height * 45 / 100 + (width * 2 / 100)), textPaint);
        canvas.drawRect(width - 180, (float) height * 55 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 55 / 100) + (width * 2 / 100)), barPaint2);
        canvas.drawText("Expenses", width - 140, (float) (height * 55 / 100 + (width * 2 / 100)), textPaint);

        int dataSize = dataPoints.size();

        int bar1Height;
        int bar2Height;
        float startX = 190f;
        int columnWidth = 7 * width / 200;
        float startY, endX, endY;

        for (int i = 0; i < dataSize; i++) {

            Data dataPoint = dataPoints.get(i);
            bar1Height = dataPoint.getValue1() / (200000 / height);
            bar2Height = dataPoint.getValue2() / (200000 / height);

            startY = ((height - 10f * height / 100) - (bar1Height * scaleFactor));
            endX = startX + columnWidth * scaleFactor;
            endY = getHeight();

            //canvas.drawRect(startX, startY, endX, endY, barPaints.get(i));



            // Vẽ cột 1
            canvas.drawRect(startX, startY, endX, (float) height * 90 / 100, barPaint1);
            /* String value1 = Integer.toString(dataPoint.getValue1());
            canvas.drawText(value1, startX + (columnWidth / 2) - (textPaint.measureText(value1) / 2), height - bar1Height - 10, textPaint);*/
            startY = ((height - 10f * height / 100) - (bar2Height * scaleFactor));
            // Vẽ cột 2
            canvas.drawRect(startX + columnWidth * scaleFactor, startY, startX + 2 * columnWidth * scaleFactor, (float) height * 90 / 100, barPaint2);
            /* String value2 = Integer.toString(dataPoint.getValue2());
            canvas.drawText(value2, startX + columnWidth + (columnWidth / 2) - (textPaint.measureText(value2) / 2), height - bar2Height - 10, textPaint);*/

            startX += 2 * columnWidth * scaleFactor + 40;
            canvas.drawText(dataPoint.getName(), startX - columnWidth * scaleFactor - 40 - (textPaint.measureText(dataPoint.getName())/2),(float) height * 96 / 100, textPaint);

            //chia cach cac Object
            canvas.drawLine(startX - 20, (float) (height * 90.0 / 100), startX - 20, (float) height * 92 / 100, textPaint);


        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Khi xảy ra sự kiện phóng to hoặc thu nhỏ, cập nhật giá trị scaleFactor
            scaleFactor *= detector.getScaleFactor();
            // Giới hạn giá trị scaleFactor trong khoảng từ 0.1 đến 5
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
            invalidate(); // Yêu cầu vẽ lại Custom View
            return true;
        }
    }

    public void addBar(Data height) {
        dataPoints.add(height);
        invalidate(); // Yêu cầu vẽ lại Custom View
    }

    private void drawValue(int width, int height, Canvas canvas) {

        String text = "$";
        for (int i = 0; i <= 7; i++) {
                //canvas.rotate(180, (width * 87f / 100) + textPaint.measureText(text + (i * 20) + ",000") / 2, height * (i + 1) * 10f / 100);
                canvas.drawText(text + (int) ((7-i) * 20 / scaleFactor) + ",000",
                        width * 3f / 100,
                             height * (i+2) / 10, textPaint);

                /*canvas.drawLine(width - 150,
                        height * scaleFactor * 10 / 100  - (height * scaleFactor * 10f / 100 - height * 10f / 100),
                        200,
                        height * scaleFactor * 10 / 100 - (height * scaleFactor * 10f / 100 - height * 10f / 100),
                        textPaint);*/

        }
    }
}

