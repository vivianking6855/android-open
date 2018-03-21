package com.wenxi.learn.blockmonitor.customized;

/**
 * default config
 */

public class Config implements IConfig {
    private static final long TIME_BLOCK = 1000L;
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
