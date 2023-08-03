package com.example.asian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MyChartView extends View {
    private Paint barPaint1;
    private Paint barPaint2;
    private Paint textPaint;

    private Paint framePaint;
    private List<Data> dataPoints;

    public MyChartView(Context context) {
        super(context);
        init();
    }

    public MyChartView(Context context, AttributeSet attrs) {
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
    }

    public void setData(List<Data> data) {
        dataPoints.clear();
        dataPoints.addAll(data);
        invalidate();
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

        //duong thang ngang
        canvas.drawLine(150, height * 90 / 100, width - 200, height * 90 / 100, textPaint);
        canvas.drawLine(150, height * 80 / 100, width - 200, height * 80 / 100, textPaint);
        canvas.drawLine(150, height * 70 / 100, width - 200, height * 70 / 100, textPaint);
        canvas.drawLine(150, height * 60 / 100, width - 200, height * 60 / 100, textPaint);
        canvas.drawLine(150, height * 50 / 100, width - 200, height * 50 / 100, textPaint);
        canvas.drawLine(150, height * 40 / 100, width - 200, height * 40 / 100, textPaint);
        canvas.drawLine(150, height * 30 / 100, width - 200, height * 30 / 100, textPaint);
        canvas.drawLine(150, height * 20 / 100, width - 200, height * 20 / 100, textPaint);

        canvas.drawLine(width * 2 / 100, height * 98 / 100, width * 98 / 100, height * 98 / 100, framePaint);
        canvas.drawLine(width * 2 / 100, height * 2 / 100, width * 98 / 100, height * 2 / 100, framePaint);

        //gia tri bên trai
        canvas.drawText("$0", (float) width * 3 / 100, (float) height * 90 / 100 + 5, textPaint);
        canvas.drawText("$20,000", (float) width * 3 / 100, (float) height * 80 / 100 + 5, textPaint);
        canvas.drawText("$40,000", (float) width * 3 / 100, (float) height * 70 / 100 + 5, textPaint);
        canvas.drawText("$60,000", (float) width * 3 / 100, (float) height * 60 / 100 + 5, textPaint);
        canvas.drawText("$80,000", (float) width * 3 / 100, height * 50 / 100 + 5, textPaint);
        canvas.drawText("$100,000", (float) width * 3 / 100, height * 40 / 100 + 5, textPaint);
        canvas.drawText("$120,000", (float) width * 3 / 100, height * 30 / 100 + 5, textPaint);
        canvas.drawText("$140,000", (float) width * 3 / 100, (float) height * 20 / 100 + 5, textPaint);

        //chu thich
        canvas.drawRect(width - 180, (float) height * 45 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 45 / 100) + (width * 2 / 100)), barPaint1);
        canvas.drawText("Sales", width - 140, (float) (height * 45 / 100 + (width * 2 / 100)), textPaint);
        canvas.drawRect(width - 180, (float) height * 55 / 100, (float) (width - 180 + (width * 2 / 100)), (float) ((height * 55 / 100) + (width * 2 / 100)), barPaint2);
        canvas.drawText("Expenses", width - 140, (float) (height * 55 / 100 + (width * 2 / 100)), textPaint);

        int dataSize = dataPoints.size();

        int bar1Height;
        int bar2Height;
        int startX = 190;
        int columnWidth = 7 * width / 200;

        for (int i = 0; i < dataSize; i++) {
            Data dataPoint = dataPoints.get(i);
            bar1Height = (height * 10 / 100) + dataPoint.getValue1() / (200000 / height);
            bar2Height = (height * 10 / 100) + dataPoint.getValue2() / (200000 / height);

            // Vẽ cột 1
            canvas.drawRect(startX, height - bar1Height, startX + columnWidth, (float) height * 90 / 100, barPaint1);
            /* String value1 = Integer.toString(dataPoint.getValue1());
            canvas.drawText(value1, startX + (columnWidth / 2) - (textPaint.measureText(value1) / 2), height - bar1Height - 10, textPaint);*/

            // Vẽ cột 2
            canvas.drawRect(startX + columnWidth, height - bar2Height, startX + 2 * columnWidth, (float) height * 90 / 100, barPaint2);
            /* String value2 = Integer.toString(dataPoint.getValue2());
            canvas.drawText(value2, startX + columnWidth + (columnWidth / 2) - (textPaint.measureText(value2) / 2), height - bar2Height - 10, textPaint);*/

            startX += 2 * columnWidth + 40;
            canvas.drawText(dataPoint.getName(), startX - columnWidth - 40 - (textPaint.measureText(dataPoint.getName())/2),(float) height * 96 / 100, textPaint);

            //chia cach cac Object
            canvas.drawLine(startX - 20, (float) (height * 90.0 / 100), startX - 20, (float) height * 92 / 100, textPaint);

            int x = height * 90 / 100;
            Log.d("ddd", String.valueOf(x));
        }
    }
}
