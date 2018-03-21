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
    private static long customizedBlock = -1L;

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

    /**
     * init, start HandlerThread and init mHander
     */
    public void init() {
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper());
    }

    /**
     * destroy log man, release res and stop HandlerThread
     */
    public void destroy() {
        try {
            removeMonitor();
            mLogThread.quit();
        } catch (Exception e) {
            Log.w(Const.BLOCK_TAG, "LogMan stop ex", e);
        }
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

    /**
     * post runnable delay TIME_BLOCK or user customized time block
     */
    public void startMonitor() {
        final long time = customizedBlock == -1 ? TIME_BLOCK : customizedBlock;
        mHandler.postDelayed(mLogRunnable, TIME_BLOCK);
    }

    /**
     * remove runnable from Handler message queue
     */
    public void removeMonitor() {
        mHandler.removeCallbacks(mLogRunnable);
    }


}
