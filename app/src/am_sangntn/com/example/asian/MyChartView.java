package com.example.asian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyChartView extends View {
    private Paint barPaint1;
    private Paint barPaint2;
    private List<Data> dataPoints;

    private int columnSpacing = 100;

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
        barPaint1.setColor(Color.BLUE);
        barPaint1.setAntiAlias(true);

        barPaint2 = new Paint();
        barPaint2.setColor(Color.GREEN);
        barPaint2.setAntiAlias(true);

        dataPoints = new ArrayList<>();
    }

    public void setData(List<Data> data) {
        dataPoints.clear();
        dataPoints.addAll(data);
        invalidate(); // Force view to redraw
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int dataSize = dataPoints.size();
        int barWidth = width / (3 * dataSize); // Lưu ý chia đôi kích thước để cách hai cột

        int bar1Height;
        int bar2Height;
        int startX = 0;

        for (int i = 0; i < dataSize; i++) {
            bar1Height = dataPoints.get(i).getValue1() * height / 100;
            bar2Height = dataPoints.get(i).getValue2() * height / 100;

            canvas.drawRect(startX, height - bar1Height, startX + barWidth, height, barPaint1);
            canvas.drawRect(startX + barWidth, height - bar2Height, startX + 2 * barWidth, height, barPaint2);

            startX += 2 * barWidth + columnSpacing;
        }
    }
}
