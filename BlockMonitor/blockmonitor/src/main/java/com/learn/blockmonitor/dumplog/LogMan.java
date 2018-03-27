package com.learn.blockmonitor.dumplog;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import com.learn.blockmonitor.BlockMonitor;
import com.learn.blockmonitor.customized.IConfig;
import com.learn.blockmonitor.util.CPUSample;
import com.learn.blockmonitor.util.Const;
import com.open.utislib.file.FileUtils;
import com.open.utislib.file.PathUtils;
import com.open.utislib.time.TimeUtils;

import java.io.File;
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
    private LogBean mLogBean;

    // log already start or not
    private boolean isRunning;

    // default time format  "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat(TimeUtils.DEFAULT_PATTERN, Locale.getDefault());

    private NotificationUtil notificationUtil;

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
    public void init(Context context) {
        notificationUtil = new NotificationUtil(context.getApplicationContext());
        if (mLogBean == null) {
            mLogBean = LogBean.build();
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
        mHandler.post(clearStackCacheRunnable);
        mHandler.removeCallbacks(stackTraceRunnable);
        mHandler.removeCallbacks(mLogRunnable);
    }

    // log runnable to record block
    private Runnable mLogRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(Const.BLOCK_TAG, "LogMan get block! dump them!");
            // deal stack trace
            collectStackTrace();
            mLogBean.setStackEntries(stackTraceBuilder);
            // debug
            //     dumpStackTrace2LogCat();
            notificationUtil.showNotification(stackTraceBuilder.get(0).toString());
            dumpStackTrace2File();
        }
    };

    // post to start capture system trace
    // no delay at the first time, only delay for next time
    private Runnable stackTraceRunnable = new Runnable() {
        @Override
        public void run() {
            // collect system trace
            collectStackTrace();
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
        FileMan.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                FileUtils.writeFileFromString(getLogPath(BlockMonitor.getInstance().getContext()),
                        mLogBean.getHeaderString(), false);
            }
        });
    }

    /**
     * dump stack to target file
     */
    private void dumpStackTrace2File() {
        final String traceLog = mLogBean.getStackString();
        final String cpulog = mLogBean.getCPUStat(CPUSample.getInstance().sample());
        FileMan.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                FileUtils.writeFileFromString(getLogPath(BlockMonitor.getInstance().getContext()),
                        traceLog + cpulog, true);
                clearCache();
            }
        });
    }

    /**
     * deal system stack trace
     */
    private void collectStackTrace() {
        if (stackTraceBuilder == null) {
            stackTraceBuilder = new ArrayList<>();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("================block begin:")
                .append(TIME_FORMATTER.format(System.currentTimeMillis())).append("\n");
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();

        for (StackTraceElement s : stackTrace) {
            builder.append(s.toString()).append("\n");
        }
        builder.append("================block end:")
                .append(TIME_FORMATTER.format(System.currentTimeMillis())).append("\n");
        stackTraceBuilder.add(builder);
    }

    /**
     * only dump system trace to logcat
     */
    private void dumpStackTrace2LogCat() {
        Log.w(Const.BLOCK_TAG, mLogBean.getStackString());
    }

    public LogBean getLogBean() {
        return mLogBean;
    }

    public File getLogPath(Context context) {
        return PathUtils.getDiskCacheDir(context,
                BlockMonitor.getInstance().getConfig().getLogPath()
                        + File.separator + Const.LOG_FILE_NAME);
    }
}
