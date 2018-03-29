package com.learn.blockmonitor.dumplog;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * File Manager
 */

class FileMan {

    // single thread pool
    static final Executor THREAD_POOL_EXECUTOR;
    static {
        THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();
    }

}
