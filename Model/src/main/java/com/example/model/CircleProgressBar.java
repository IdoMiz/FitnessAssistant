package com.example.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {

    private Paint paint;
    private RectF rectF;
    private float progress;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        rectF = new RectF();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY);

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw background circle
        paint.setColor(0xFFCCCCCC);
        canvas.drawArc(rectF, -90, 360, true, paint);

        // Draw progress arc
        paint.setColor(0xFF5CB85C); // Green color for progress
        canvas.drawArc(rectF, -90, 360 * progress, true, paint);
    }

    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }
}
