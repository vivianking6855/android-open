package com.learn.blockmonitor.data.task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * File Manager
 */

public class FileTask {

    // single thread pool
    public static final Executor THREAD_POOL_EXECUTOR;
    static {
        THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();
    }

}
