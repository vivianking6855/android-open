package com.wenxi.learn.blockmonitor.customized;

/**
 * Created by vivian on 2018/3/21.
 */

public class Config implements IConfig{
    private static final long TIME_BLOCK = 1000L;

    @Override
    public long getTimeBlock() {
        return TIME_BLOCK;
    }
}
