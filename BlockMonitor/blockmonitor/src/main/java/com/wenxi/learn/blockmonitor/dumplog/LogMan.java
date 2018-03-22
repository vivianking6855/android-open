package com.wenxi.learn.blockmonitor.dumplog;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.open.utislib.file.FileUtils;
import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.customized.IConfig;
import com.wenxi.learn.blockmonitor.util.Const;
import com.wenxi.learn.blockmonitor.util.UserFileUtils;

/**
 * Log Manager
 */

public class LogMan {
    private static final int STACKTRACE_DURATION = 52;
    // singleton instance
    private volatile static LogMan instance = null;
    // handler thread to get
    private HandlerThread mLogThread;
    private Handler mHandler;
    private StringBuilder stackTraceBuilder = new StringBuilder();


    private LogBean mLogBean;
    private LogMan() {}

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
                mHandler.removeCallbacks(stackTraceRunnable);
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
            dealTrace();
            dumpStackTrace2LogCat();
            clearCache();
        }
    };

    private Runnable stackTraceRunnable = new Runnable() {
        @Override
        public void run() {
            dealTrace();
            if(mLogThread.isAlive()) {
                mHandler.postDelayed(this, STACKTRACE_DURATION);
            }
        }
    };

    private void clearCache(){
     //   stackTraceBuilder.delete(0,stackTraceBuilder.length());
    }

    private void dumpStackTrace2LogCat(){
        if(stackTraceBuilder!=null){
            Log.w(Const.BLOCK_TAG, stackTraceBuilder.toString());
        }
    }

    private static void getDumpLog(){

    }

    /**
     * post runnable delay TIME_BLOCK or user customized time block
     */
    public void startMonitor() {
        mHandler.post(stackTraceRunnable);
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
                UserFileUtils.getLogPath(), false);
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
}
