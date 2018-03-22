package com.wenxi.learn.blockmonitor.customized;

import com.open.utislib.file.FileUtils;
import com.open.utislib.file.PathUtils;
import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.util.DeviceUtils;

/**
 * default config
 */

public class Config implements IConfig {
    private static final long TIME_BLOCK = 80 ;// 80ms = 5*16.6ms
    private static final String PATH = "/block_log/";

    @Override
    public long getBlockThreshold() {
        return TIME_BLOCK;
    }

    @Override
    public String getLogPath() {
        return PATH;
    }

    @Override
    public String getAppendix() {
        return "";
    }
}
