package com.wenxi.learn.blockmonitor.customized;

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
