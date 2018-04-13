package com.learn.blockmonitor.data.api;

import android.util.Log;
import android.view.Choreographer;

import com.learn.blockmonitor.data.util.Const;

import java.util.concurrent.TimeUnit;

/**
 * FrameMonitor will monitor FrameCallback of Choreographer
 */
public final class FrameMonitor {
    private static boolean isStop = false;

    /**
     * start frame callback monitor
     */
    public static void start() {
        // start log man
        LogMan.getInstance().start();
        // set frame callback
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            long lastFrameTimeNanos = 0;
            long currentFrameTimeNanos = 0;

            @Override
            public void doFrame(long frameTimeNanos) {
                if (isStop) {
                    Log.d(Const.BLOCK_TAG, "FrameMonitor stop");
                    return;
                }

                // computer time diff between two frame
                if (lastFrameTimeNanos == 0) {
                    lastFrameTimeNanos = frameTimeNanos;
                }
                currentFrameTimeNanos = frameTimeNanos;
                recordTimeDiff(lastFrameTimeNanos, currentFrameTimeNanos);
                lastFrameTimeNanos = currentFrameTimeNanos;

                // if already has log message, remove it.
                // if not remove it at TIME_BLOCK(default 1s), block dump message will show
                LogMan.getInstance().removeMonitor();
                LogMan.getInstance().startMonitor();

                // set again to receive next frame callback
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    private static void recordTimeDiff(long last, long current) {
        long diffMs = TimeUnit.MILLISECONDS.convert(current - last, TimeUnit.NANOSECONDS);
      //  Log.w(Const.BLOCK_TAG,"diffMs="+diffMs);
        if(LogMan.getInstance().getIsDrawing() && diffMs != 0){
            LogMan.getInstance().getMonitorLogUtils().addRefreshFrameDurationCache(diffMs);
        }

        if (diffMs > 16.6f) {
            double droppedCount = diffMs / 16.6;
            LogMan.getInstance().getMonitorLogUtils().setCost(last, current, droppedCount);
        }
    }

    /**
     * stop frame callback monitor
     */
    public static void stop() {
        isStop = true;
        LogMan.getInstance().stop();
    }
}

