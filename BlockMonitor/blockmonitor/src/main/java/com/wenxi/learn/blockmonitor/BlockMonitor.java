package com.wenxi.learn.blockmonitor;

import android.content.Context;

import com.wenxi.learn.blockmonitor.customized.Config;
import com.wenxi.learn.blockmonitor.customized.IConfig;

/**
 * BlockMonitor is singleton mode
 * call install method to initial it
 */
public class BlockMonitor {
    // singleton instance
    private volatile static BlockMonitor instance = null;
    // is start or not
    private boolean isStart = false;
    public Context context;

    private IConfig mConfig;

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
        monitor.mConfig = new Config();
        monitor.context = context;
        return getInstance();
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
    public synchronized void setConfig(IConfig config) {
        mConfig = config;
    }

    /**
     * get config, such as time block
     */
    public synchronized IConfig getConfig() {
        return mConfig;
    }

}
