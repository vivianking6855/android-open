package com.wenxi.learn.blockmonitor;

import android.util.Log;
import android.view.Choreographer;

/**
 * ChoreographerMonitor will monitor FrameCallback of Choreographer
 */
public final class ChoreographerMonitor {
    private static boolean isStop = false;

    /**
     * start frame callback monitor
     */
    public static void start() {
        // init log man
        LogMan.getInstance().init();
        // set frame callback
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
//            long lastFrameTimeNanos = 0;
//            long currentFrameTimeNanos = 0;

            @Override
            public void doFrame(long frameTimeNanos) {
                if (isStop) {
                    Log.d(Const.BLOCK_TAG, "ChoreographerMonitor stop");
                    return;
                }
//                if (lastFrameTimeNanos == 0) {
//                    lastFrameTimeNanos = frameTimeNanos;
//                }
//                currentFrameTimeNanos = frameTimeNanos;
//                    long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos-lastFrameTimeNanos, TimeUnit.NANOSECONDS);
//                    if (diffMs > 16.6f) {
//                        long droppedCount = (long)diffMs / 16.6;
//                    }
                // if already has log message, remove it.
                // if not remove it at TIME_BLOCK(default 1s), block dump message will show
                LogMan.getInstance().removeMonitor();
                LogMan.getInstance().startMonitor();

                // set again to receive next frame callback
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    /**
     * stop frame callback monitor
     */
    public static void stop() {
        isStop = true;
        LogMan.getInstance().destroy();
    }
}

