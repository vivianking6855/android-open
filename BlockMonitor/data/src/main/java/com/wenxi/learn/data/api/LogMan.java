package com.wenxi.learn.data.api;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.open.utislib.device.CPUSample;
import com.open.utislib.file.FileUtils;
import com.open.utislib.file.PathUtils;
import com.open.utislib.time.TimeUtils;
import com.wenxi.learn.data.config.Config;
import com.wenxi.learn.data.config.IConfig;
import com.wenxi.learn.data.listener.IMonitorListener;
import com.wenxi.learn.data.task.FileTask;
import com.wenxi.learn.data.util.Const;
import com.wenxi.learn.data.util.LogFormat;

import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
    private ArrayList<StringBuilder> stackTraceBuilder = new ArrayList<>();

    // log bean, all log information here
    private LogFormat logFormat;

    // log already start or not
    private boolean isRunning;

    // default time format  "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat(TimeUtils.DEFAULT_PATTERN, Locale.getDefault());

    // block monitor listener
    private IMonitorListener mListener;

    private boolean isDrawing;

    // config for dump information
    private IConfig mConfig;
    private Reference<Context> mContextRef;

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
     *
     * @param context Context
     */
    public void init(Context context, IMonitorListener listener) {
        mListener = listener;
        mContextRef = new WeakReference<>(context);
        if (logFormat == null) {
            logFormat = new LogFormat(context);
        }
        if (mConfig == null) {
            mConfig = new Config();
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
        // capture log when start
        writeStickyLog2File();
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
            isRunning = false;
        } catch (Exception e) {
            Log.w(Const.BLOCK_TAG, "LogMan stop ex", e);
        }
        clearCache();
        logFormat.destroy();
        logFormat = null;
    }

    /**
     * post runnable delay TIME_BLOCK or user customized time block
     */
    public void startMonitor() {
        // post to start capture system trace, not delay at the first time
        mHandler.post(stackTraceRunnable);
        // post log runnable to record block
        mHandler.postDelayed(dealWithBlockRunnable, mConfig.getBlockThreshold());
    }

    /**
     * remove runnable from Handler message queue
     */
    public void removeMonitor() {
        mHandler.post(clearStackCacheRunnable);
        mHandler.removeCallbacks(stackTraceRunnable);
        mHandler.removeCallbacks(dealWithBlockRunnable);
    }

    // log runnable to record block
    private Runnable dealWithBlockRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(Const.BLOCK_TAG, "LogMan get block! dump them!");
            // deal stack trace
            collectStackTrace(true);
            logFormat.setStackEntries(stackTraceBuilder);
            // debug
            //     dumpStackTrace2LogCat();
            dumpStackTrace2File();
            if (mListener != null) {
                mListener.onBlocked(stackTraceBuilder.get(0).toString());
            }
            clearCache();
        }
    };

    // post to start capture system trace
    // no delay at the first time, only delay for next time
    private Runnable stackTraceRunnable = new Runnable() {
        @Override
        public void run() {
            // collect system trace
            collectStackTrace(false);
            if (mLogThread.isAlive()) {
                // post to start capture system trace, delay for next time
                mHandler.postDelayed(this, STACKTRACE_DURATION);
            }
        }
    };

    private Runnable clearStackCacheRunnable = new Runnable() {
        @Override
        public void run() {
            clearCache();
        }
    };

    private void clearCache() {
        if (stackTraceBuilder == null) {
            return;
        }
        stackTraceBuilder.clear();
        stackTraceBuilder = null;
    }

    /**
     * deal device sticky info, such as cpu count
     */
    private void writeStickyLog2File() {
        FileTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                FileUtils.writeFileFromString(getLogPath(),
                        logFormat.getHeaderString(), false);
            }
        });
    }

    /**
     * dump stack to target file
     */
    private void dumpStackTrace2File() {
        final String traceLog = logFormat.getStackString();
        final String cpulog = logFormat.getCPUStat(CPUSample.getInstance().sample());
        FileTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                FileUtils.writeFileFromString(getLogPath(),
                        cpulog + traceLog, true);
            }
        });
    }

    /**
     * deal system stack trace
     */
    private void collectStackTrace(boolean blocked) {
        if (stackTraceBuilder == null) {
            stackTraceBuilder = new ArrayList<>();
        }

        if (blocked) {
            StringBuilder blockBegin = new StringBuilder();
            blockBegin.append("================block begin:")
                    .append(TIME_FORMATTER.format(System.currentTimeMillis())).append("\n");
            stackTraceBuilder.add(0, blockBegin);
        }

        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        for (StackTraceElement s : stackTrace) {
            builder.append(s.toString()).append("\n");
        }
        stackTraceBuilder.add(builder);
        if (blocked) {
            StringBuilder blockEnd = new StringBuilder();
            blockEnd.append("================block end:")
                    .append(TIME_FORMATTER.format(System.currentTimeMillis())).append("\n");
            stackTraceBuilder.add(blockEnd);
        }
    }

    public LogFormat getMonitorLogUtils() {
        return logFormat;
    }

    public File getLogPath() {
        return PathUtils.getDiskCacheDir(mContextRef.get(),
                mConfig.getLogPath() + File.separator + Const.LOG_FILE_NAME);
    }

    public void setIsDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    public boolean getIsDrawing() {
        return isDrawing;
    }

    /**
     * set config, such as time block
     *
     * @param config customised IConfig
     */
    public void setConfig(IConfig config) {
        mConfig = config;
    }

    /**
     * get config, such as time block
     *
     * @return IConfig
     */
    public IConfig getConfig() {
        return mConfig;
    }
}
