package com.wenxi.learn.data.cache;


import android.content.Context;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;
import com.open.utislib.base.AppUtils;
import com.open.utislib.file.FileUtils;
import com.open.utislib.file.PathUtils;

import java.io.IOException;

/**
 * Created by vivian on 2017/11/13.
 * cache manager
 */
enum DiskCacheManager {
    INSTANCE;

    private final String TAG = DiskCacheManager.class.getSimpleName();

    // disk cache
    private DiskLruCache mDiskLruCache;
    // disk cache directory
    private static final String DISK_CACHE_PATH = "diskcache";
    // disk cache max size
    private static final int DISK_MAX_SIZE = 10 * 1024 * 1024;

    public void init(Context context) {

    }

    public void release() {
        closeDiskLruCache();
    }

    /**
     * Gets disk lru cache.
     *
     * @param context the context
     * @return the disk lru cache
     */
    public DiskLruCache getDiskLruCache(Context context) {
        try {
            if (mDiskLruCache == null) {
                // create disk cache path
                FileUtils.createOrExistsDir(PathUtils.getDiskCacheDir(context.getApplicationContext(), DISK_CACHE_PATH));
                // open DiskLruCache
                mDiskLruCache = DiskLruCache.open(PathUtils.getDiskCacheDir(context.getApplicationContext(), DISK_CACHE_PATH),
                        AppUtils.getAppVersionCode(context), 1, DISK_MAX_SIZE);
                Log.d(TAG, "mDiskLruCache path " + mDiskLruCache.getDirectory().getPath());
            }
        } catch (IOException e) {
            Log.w(TAG, "getDiskCache ex ", e);
        }

        return mDiskLruCache;
    }

    /**
     * Flush disk lru cache.
     * <p>
     * 这个方法用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。
     * 这个方法非常重要，因为DiskLruCache能够正常工作的前提就是要依赖于journal文件中的内容。
     * 并不是每次写入缓存都要调用一次flush()方法的，频繁地调用并不会带来任何好处，
     * 只会额外增加同步journal文件的时间。
     * 比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了
     * </p>
     */
    public void flushDiskLruCache() {
        try {
            if (mDiskLruCache != null) {
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            Log.w(TAG, "flushDiskLruCache ex ", e);
        }
    }

    /**
     * 这个方法用于将所有的缓存数据全部删除
     * 其实只需要调用一下DiskLruCache的delete()方法就可以实现了。
     * 会删除包括日志文件在内的所有文件
     */
    public void clearDiskLruCache() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close disk lru cache.
     * 这个方法用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。
     * 关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法，
     * 通常只应该在Activity的onDestroy()方法中去调用close()方法。
     */
    public void closeDiskLruCache() {
        try {
            if (mDiskLruCache != null) {
                mDiskLruCache.close();
            }
        } catch (IOException e) {
            Log.w(TAG, "flushDiskLruCache ex ", e);
        }
    }

}
