package com.wenxi.learn.blockmonitor;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

/**
 * Created by vivian on 2018/3/21.
 */

public class LogMan {
    // singleton instance
    private volatile static LogMan instance = null;

    // handler thread to get
    private HandlerThread mLogThread = new HandlerThread("dumplog");
    private Handler mHandler;
    private static final long TIME_BLOCK = 1000L;

    private LogMan() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LogMan getInstance() {
        if (instance == null) {
            synchronized (BlockMonitor.class) {
                if (instance == null) {
                    instance = new LogMan();
                }
            }
        }
        return instance;
    }

    public void start() {
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper());
    }

    // dump log
    private static Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            StringBuilder sb = new StringBuilder();
            StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
            for (StackTraceElement s : stackTrace) {
                sb.append(s.toString() + "\n");
            }
            Log.w(Const.BLOCK_TAG, sb.toString());
        }
    };

    public void startMonitor() {
        mHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    public void removeMonitor() {
        mHandler.removeCallbacks(mLogRunnable);
    }

    public void stop() {
        mLogThread.quit();
    }
}
