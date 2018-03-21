package com.wenxi.learn.blockmonitor;

/**
 * ChoreographerMonitor will monitor FrameCallback of Choreographer
 */

public final class ChoreographerMonitor {
    public static void start() {
//            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
//                long lastFrameTimeNanos = 0;
//                long currentFrameTimeNanos = 0;
//
//                @Override
//                public void doFrame(long frameTimeNanos) {
//                    if(lastFrameTimeNanos == 0){
//                        lastFrameTimeNanos == frameTimeNanos;
//                    }
//                    currentFrameTimeNanos = frameTimeNanos;
//                    long diffMs = TimeUnit.MILLISECONDS.convert(currentFrameTimeNanos-lastFrameTimeNanos, TimeUnit.NANOSECONDS);
//                    if (diffMs > 16.6f) {
//                        long droppedCount = (int)diffMs / 16.6;
//                    }
//                    if (LogMonitor.getInstance().isMonitor()) {
//                        LogMonitor.getInstance().removeMonitor();
//                    }
//                    LogMonitor.getInstance().startMonitor();
//                    Choreographer.getInstance().postFrameCallback(this);
//                }
//            });
//        }
    }
}
