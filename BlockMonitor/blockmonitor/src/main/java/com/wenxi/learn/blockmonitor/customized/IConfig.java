package com.wenxi.learn.blockmonitor.customized;

/**
 * all config in BlockMonitor, such path, block threshold, etc.
 */

public interface IConfig {
    long getBlockThreshold();

    String setLogPath();
}
