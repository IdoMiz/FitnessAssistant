package com.example.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.NotNull;
public class CircleProgressBar extends View {

    // Paint object used to draw the circle and arc
    private Paint paint;

    // RectF object used to define the bounds of the circle and arc
    private RectF rectF;

    // Variable to store the current progress value (between 0 and 1)
    private float progress;

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize the Paint object
        paint = new Paint();
        paint.setAntiAlias(true); // Enable the paint to draw smoother edges
        paint.setStyle(Paint.Style.FILL); // Set the paint style to fill

        // Initialize the RectF object
        rectF = new RectF();
    }

    @Override
    public void onDraw(@NotNull Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the center point of the view
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Calculate the radius of the circle
        int radius = Math.min(centerX, centerY);

        // Set the bounds of the RectF object
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw the background circle
        paint.setColor(0xFFCCCCCC); // Set the color to light gray
        canvas.drawArc(rectF, -90, 360, true, paint); // Draw the arc with a starting angle of -90 degrees and a sweep angle of 360 degrees (full circle)

        // Draw the progress arc
        paint.setColor(0xFF5CB85C); // Set the color to green
        canvas.drawArc(rectF, -90, 360 * progress, true, paint); // Draw the arc with a starting angle of -90 degrees and a sweep angle of 360 * progress degrees
    }

    // Method to set the progress value
    public void setProgress(float progress) {
        // Clamp the progress value between 0 and 1
        this.progress = Math.max(0, Math.min(1, progress));

        // Invalidate the view to force a redraw
        invalidate();
    }
}