package com.wenxi.learn.blockmonitor.dumplog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.customized.IConfig;
import com.wenxi.learn.blockmonitor.util.Const;

/**
 * Log Manager
 */

public class LogMan {
    // singleton instance
    private volatile static LogMan instance = null;

    // handler thread to get
    private HandlerThread mLogThread;
    private Handler mHandler;

    private LogBean mLogBean;

    private LogMan() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LogMan getInstance() {
        if (instance == null) {
            synchronized (LogMan.class) {
                if (instance == null) {
                    instance = new LogMan();
                }
            }
        }
        return instance;
    }

    /**
     * init data
     */
    public void init() {
        if (mLogBean == null) {
            mLogBean = new LogBean();
        }
    }

    /**
     * start HandlerThread and init mHander
     */
    public void start() {
        if (mLogThread != null) {
            return;
        }
        mLogThread = new HandlerThread("dumplog");
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper());
    }

    /**
     * destroy log man, release res and stop HandlerThread
     */
    public void stop() {
        try {
            if (mLogThread != null) {
                removeMonitor();
                mLogThread.quit();
            }
        } catch (Exception e) {
            Log.w(Const.BLOCK_TAG, "LogMan stop ex", e);
        }
    }

    // dump log
    private Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            dealTrace();
        }
    };

    /**
     * post runnable delay TIME_BLOCK or user customized time block
     */
    public void startMonitor() {
        IConfig config = BlockMonitor.getInstance().getConfig();
        mHandler.postDelayed(mLogRunnable, config.getBlockThreshold());
    }

    /**
     * remove runnable from Handler message queue
     */
    public void removeMonitor() {
        mHandler.removeCallbacks(mLogRunnable);
    }

    /**
     * deal all message and save
     */
    private void dealTrace() {
        dealDeviceStickyInfo();
        dealDynamicTrace();
    }

    /**
     * deal all dynamic message and save
     */
    private void dealDynamicTrace() {
        dealDeviceDynamicInfo();
        dealStackTrace();
    }

    /**
     * deal device sticky info, such as cpu count
     */
    private void dealDeviceStickyInfo() {
        int cpu_count = Runtime.getRuntime().availableProcessors();
    }

    /**
     * deal device dynamic info, such cpu usage, memory
     */
    private void dealDeviceDynamicInfo() {
    }

    /**
     * deal system stack trace
     */
    private void dealStackTrace() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            sb.append(s.toString() + "\n");
        }
        Log.w(Const.BLOCK_TAG, sb.toString());
    }

    private LogBean getLogBean() {
        return mLogBean;
    }

}
