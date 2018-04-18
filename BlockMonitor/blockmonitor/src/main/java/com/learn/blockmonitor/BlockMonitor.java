package com.learn.blockmonitor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.learn.blockmonitor.data.api.FrameMonitor;
import com.learn.blockmonitor.data.api.LogMan;
import com.learn.blockmonitor.data.config.IConfig;
import com.learn.blockmonitor.share.util.Const;
import com.learn.blockmonitor.view.BlockNotification;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * BlockMonitor is singleton mode
 * call install method to initial it
 * default monitor file will be place sdcard/Android/data/package name/block_log/
 */
public class BlockMonitor {
    // singleton instance
    @SuppressLint("StaticFieldLeak")
    private volatile static BlockMonitor instance = null;
    // is start or not
    private boolean isStart = false;
    private Reference<Context> mContextRef;

    private BlockMonitor() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    private static BlockMonitor getInstance() {
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

    public static BlockMonitor install(Context context, IConfig config) {
        Log.d(Const.BLOCK_TAG, "BlockMonitor install with config");
        BlockMonitor monitor = BlockMonitor.install(context);
        if (config != null) {
            monitor.setConfig(config);
        }
        return monitor;
    }

    private void init(Context context) {
        // do not init again, if already init before
        if (mContextRef != null && mContextRef.get() != null) {
            return;
        }
        // init block monitor
        mContextRef = new WeakReference<>(context);
        // init log man, such as sticky device info
        LogMan.getInstance().init(mContextRef, new BlockNotification(mContextRef));
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
    private void setConfig(IConfig config) {
        LogMan.getInstance().setConfig(config);
    }

    /**
     * get config, such as time block
     *
     * @return IConfig
     */
    public static IConfig getConfig() {
        return LogMan.getInstance().getConfig();
    }

}
