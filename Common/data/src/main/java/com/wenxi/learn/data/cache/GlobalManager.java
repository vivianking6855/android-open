package com.wenxi.learn.data.cache;

/**
 * Created by vivian on 2017/11/13.
 * global variable manager,
 * light data cache and share
 * if you need large data cache,user LruCache or DiskCache
 */
public enum GlobalManager {
    INSTANCE;

    public int mDataCount;

    public void init(){

    }

    public void release(){

    }
}
