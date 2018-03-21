package com.wenxi.learn.blockmonitor.customized;

/**
 * all config in BlockMonitor, such path, block threshold, etc.
 */
public interface IConfig {
    /**
     * block time threshold
     *
     * @return the block threshold
     */
    long getBlockThreshold();

    /**
     * log file path
     *
     * @return the log path
     */
    String getLogPath();

    /**
     * appendix info will be add at the end of file
     *
     * @return the appendix
     */
    String getAppendix();
}
