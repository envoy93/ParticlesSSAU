package com.shashov.particles;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.shashov.particles.model.MainThread;
import com.shashov.particles.model.Space;

/**
 * Created by kirill on 29.12.2015.
 */
public class MainCanvas extends View {
    private MainThread thread;
    public  static int width;
    public static int height;

    public MainCanvas(Context cxt) {
        this(cxt, null, 0, 0);
    }

    public MainCanvas(Context cxt, AttributeSet attrs) {
        this(cxt, attrs, 0, 0);
    }

    public MainCanvas(Context cxt, AttributeSet attrs, int defStyleAttr) {
        this(cxt, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MainCanvas(Context cxt, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(cxt, attrs, defStyleAttr, defStyleRes);
        System.out.println("CREATE CANVAS");
        setMinimumHeight(500);
        setMinimumWidth(500);
        thread = new MainThread(this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    protected void onDraw(Canvas cv) {
        clear(cv);
        renderParticles(thread.getFPS(), cv);
    }

    private void renderParticles(long fps, Canvas canvas) {
        Space space = thread.getSpace();
        if (space != null) {
            if (space.notEmpty()) {
                space.render(canvas);
            }

            drawText("Частиц: " + space.getSize() + " " + ((fps != 0) ? "FPS: " + fps : ""), canvas);
        }
    }

    public void clear(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), p);
    }

    public void drawText(String s, Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setTextSize(25);
        p.setStyle(Paint.Style.FILL);
        canvas.drawText(s, 20, 20, p);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction()==MotionEvent.ACTION_UP){
            MainThread.handleTouchClick(event.getX(), event.getY());
            return true;
        }
        return true;
    }
}
