package com.learn.blockmonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.learn.blockmonitor.share.util.Const;
import com.learn.blockmonitor.view.BlockNotification;
import com.wenxi.learn.data.api.FrameMonitor;
import com.wenxi.learn.data.api.LogMan;
import com.wenxi.learn.data.config.IConfig;
import com.wenxi.learn.data.listener.IMonitorListener;

/**
 * BlockMonitor is singleton mode
 * call install method to initial it
 */
public class BlockMonitor {
    // singleton instance
    @SuppressLint("StaticFieldLeak")
    private volatile static BlockMonitor instance = null;
    // is start or not
    private boolean isStart = false;
    private Context mContext;

    private BlockMonitor() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static BlockMonitor getInstance() {
        if (instance == null) {
            synchronized (BlockMonitor.class) {
                if (instance == null) {
                    instance = new BlockMonitor();
                }
            }
        }
        return instance;
    }

    /**
     * Install block monitor.
     *
     * @param context the context
     * @return the block monitor
     */
    public static BlockMonitor install(Context context) {
        Log.d(Const.BLOCK_TAG, "BlockMonitor install");
        BlockMonitor monitor = getInstance();
        monitor.init(context);
        return monitor;
    }

    private void init(Context context) {
        // init block monitor
        mContext = context;
        // init log man, such as sticky device info
        LogMan.getInstance().init(context.getApplicationContext(), (IMonitorListener) new BlockNotification(context));
    }

    /**
     * Uninstall, release all resource, delete all related log files
     */
    public void uninstall() {
        Log.d(Const.BLOCK_TAG, "BlockMonitor uninstall");
        stop();
    }

    /**
     * start monitor frame
     */
    public void start() {
        if (!isStart) {
            isStart = true;
            Log.d(Const.BLOCK_TAG, "BlockMonitor start");
            FrameMonitor.start();
        }
    }

    /**
     * stop monitor
     */
    public void stop() {
        if (isStart) {
            isStart = false;
            Log.d(Const.BLOCK_TAG, "BlockMonitor stop");
            FrameMonitor.stop();
        }
    }

    /**
     * set config, such as time block
     *
     * @param config customised IConfig
     */
    public void setConfig(IConfig config) {
        LogMan.getInstance().setConfig(config);
    }

    /**
     * get config, such as time block
     *
     * @return IConfig
     */
    public IConfig getConfig() {
        return LogMan.getInstance().getConfig();
    }

    /**
     * get context, such as time block
     *
     * @return Context
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * get context, such as time block
     *
     * @return String
     */
    public String getLogPath() {
        return LogMan.getInstance().getLogPath(mContext).getPath();
    }

}
