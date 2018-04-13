package com.learn.blockmonitor.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.learn.blockmonitor.data.api.LogMan;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FrameRefreshChart SurfaceView
 */

public class FrameRefreshChart extends SurfaceView implements SurfaceHolder.Callback {

    private static final int DRAW_FRAME_COUNT = 100;
    private int viewWidth;
    private int viewHeight;
    private float halfViewHeight;
    private Paint wavePaint;
    private boolean stop;

    public FrameRefreshChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        wavePaint = new Paint();
        wavePaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        halfViewHeight = viewHeight * 0.5f;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        stop = false;
        //   new DrawThread(holder).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop = true;
        LogMan.getInstance().setIsDrawing(false);
        LogMan.getInstance().getMonitorLogUtils().clearDrawCache();
    }

    private class DrawThread extends Thread {
        private SurfaceHolder holder;

        DrawThread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @SuppressLint("Range")
        @Override
        public void run() {
            super.run();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (holder == null) {
                return;
            }
            LogMan.getInstance().setIsDrawing(true);
            while (!stop) {
                Canvas canvas = holder.lockCanvas();
                ConcurrentLinkedQueue<Long> data = LogMan.getInstance().getMonitorLogUtils().getDrawCacheData();
                while (data.size() < DRAW_FRAME_COUNT) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int index;
                long v = 0;
                for (index = 0; index < DRAW_FRAME_COUNT; index++) {
                    long temp = Math.abs(v - 16);
                    v = temp > v ? temp : v;
                    index++;
                    if (index > DRAW_FRAME_COUNT) {
                        break;
                    }
                }
                int hDivider = viewWidth / DRAW_FRAME_COUNT;
                // float[] pts = new float[DRAW_FRAME_COUNT * 2];
                for (int i = 0; i < DRAW_FRAME_COUNT; i++) {
                    Long pre = data.poll();
                    Long post = data.peek();
                    if (data.isEmpty() || pre == null || post == null) {
                        holder.unlockCanvasAndPost(canvas);
                        return;
                    }
                    canvas.drawLine(i * hDivider, halfViewHeight - ((pre - 16) / v) * halfViewHeight,
                            (i + 1) * hDivider, halfViewHeight - ((post - 16) / v) * halfViewHeight, wavePaint);
                }
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
