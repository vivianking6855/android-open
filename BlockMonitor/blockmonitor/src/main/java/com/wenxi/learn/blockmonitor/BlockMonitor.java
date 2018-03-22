package com.wenxi.learn.blockmonitor;

import android.content.Context;

import com.wenxi.learn.blockmonitor.customized.Config;
import com.wenxi.learn.blockmonitor.customized.IConfig;
import com.wenxi.learn.blockmonitor.dumplog.LogMan;

/**
 * BlockMonitor is singleton mode
 * call install method to initial it
 */
public class BlockMonitor {
    // singleton instance
    private volatile static BlockMonitor instance = null;
    // config for dump information
    private IConfig mConfig;
    // is start or not
    private boolean isStart = false;
    public Context mContext;

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
        BlockMonitor monitor = getInstance();
        monitor.init(context);
        return monitor;
    }

    private void init(Context context) {
        // init block monitor
        mContext = context;
        if (mConfig == null) {
            mConfig = new Config();
        }
        // init log man, such as sticky device info
        LogMan.getInstance().init();
    }

    /**
     * Uninstall, release all resource, delete all related log files
     */
    public void uninstall() {
        stop();
    }

    /**
     * start monitor frame
     */
    public synchronized void start() {
        if (!isStart) {
            isStart = true;
            ChoreographerMonitor.start();
        }
    }

    /**
     * stop monitor
     */
    public synchronized void stop() {
        if (isStart) {
            isStart = false;
            ChoreographerMonitor.stop();
        }
    }

    /**
     * set config, such as time block
     */
    public void setConfig(IConfig config) {
        mConfig = config;
    }

    /**
     * get config, such as time block
     */
    public IConfig getConfig() {
        return mConfig;
    }

    /**
     * get context, such as time block
     */
    public Context getContext() {
        return mContext;
    }

}
