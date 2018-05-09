package com.wenxi.learn.data.task;

import android.os.AsyncTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The File task.
 * has one SingleThreadExecutor
 */
public enum FileTask {
    INSTANCE;

    // single thread pool
    static final Executor SINGLE_THREAD_POOL;
    static {
        SINGLE_THREAD_POOL = Executors.newSingleThreadExecutor();
    }

}
