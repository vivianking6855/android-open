package com.wenxi.learn.blockmonitor;

import android.content.Context;

/**
 * BlockMonitor is singleton mode
 * call install method to initial it
 */
public class BlockMonitor {
    private volatile static BlockMonitor instance = null;

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
        return getInstance();
    }

    /**
     * Uninstall, release all resource, delete all related log files
     */
    public static void uninstall() {

    }

    /**
     * start monitor frame
     */
    public void start() {

    }

    /**
     * stop monitor
     */
    public void stop() {

    }

}
