package com.wenxi.learn.blockmonitor.dumplog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.open.utislib.file.FileUtils;
import com.open.utislib.file.PathUtils;
import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.customized.IConfig;
import com.wenxi.learn.blockmonitor.util.Const;

import java.io.File;

/**
 * Log Manager
 */

public class LogMan {
    // singleton instance
    private volatile static LogMan instance = null;
    // handler thread to get
    private HandlerThread mLogThread;
    private Handler mHandler;
    // system trace capture frequency
    private static final int STACKTRACE_DURATION = 52; // 52ms
    private StringBuilder stackTraceBuilder = new StringBuilder();

    // log bean, all log information here
    private LogBean mLogBean;

    // log already start or not
    private boolean isRunning;

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
            mLogBean.init();
        }
    }

    /**
     * start HandlerThread and init mHander
     */
    public void start() {
        if (isRunning) {
            return;
        }
        // start log thread
        mLogThread = new HandlerThread("dumplog");
        mLogThread.start();
        mHandler = new Handler(mLogThread.getLooper());

        // set status
        isRunning = true;
    }

    /**
     * destroy log man, release res and stop HandlerThread
     */
    public void stop() {
        try {
            if (mLogThread != null) {
                removeMonitor();
                mHandler.removeCallbacks(stackTraceRunnable);
                mLogThread.quit();
            }
            isRunning = false;
        } catch (Exception e) {
            Log.w(Const.BLOCK_TAG, "LogMan stop ex", e);
        }
    }

    /**
     * post runnable delay TIME_BLOCK or user customized time block
     */
    public void startMonitor() {
        // post to start capture system trace, not delay at the first time
        mHandler.post(stackTraceRunnable);
        // post log runnable to record block
        IConfig config = BlockMonitor.getInstance().getConfig();
        mHandler.postDelayed(mLogRunnable, config.getBlockThreshold());
    }

    /**
     * remove runnable from Handler message queue
     */
    public void removeMonitor() {
        clearCache();
        mHandler.removeCallbacks(stackTraceRunnable);
        mHandler.removeCallbacks(mLogRunnable);
    }

    // log runnable to record block
    private Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            dealTrace();
            dealTrace();
            // deal stack trace
            dumpStackTrace2LogCat();
            clearCache();
        }
    };

    // post to start capture system trace
    // no delay at the first time, only delay for next time
    private Runnable stackTraceRunnable = new Runnable() {
        @Override
        public void run() {
            dealTrace();
            if (mLogThread.isAlive()) {
                // post to start capture system trace, delay for next time
                mHandler.postDelayed(this, STACKTRACE_DURATION);
            }
        }
    };

    private void clearCache() {
        //   stackTraceBuilder.delete(0,stackTraceBuilder.length());
    }

    private void dumpStackTrace2LogCat() {
        if (stackTraceBuilder != null) {
            Log.w(Const.BLOCK_TAG, stackTraceBuilder.toString());
        }
    }

    /**
     * deal all message and save
     */
    private void dealTrace() {
        dealHeaderInfo();
        dealDynamicTrace();
    }

    /**
     * deal all dynamic message and save
     */
    private void dealDynamicTrace() {
        dealStackTrace();
        dealDeviceDynamicInfo();
    }

    /**
     * deal device sticky info, such as cpu count
     */
    private void dealHeaderInfo() {
        FileUtils.writeFileFromString(mLogBean.getHeaderString(),
                getLogPath(), false);
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
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            stackTraceBuilder.append(s.toString()).append("\n");
        }
    }

    private LogBean getLogBean() {
        return mLogBean;
    }

    private String getLogPath() {
        return PathUtils.getDiskCacheDir(BlockMonitor.getInstance().getContext(),
                BlockMonitor.getInstance().getConfig().getLogPath()
                        + File.separator + Const.LOG_FILE_NAME).getPath();
    }
}
