package com.wenxi.learn.blockmonitor.util;

import com.open.utislib.file.PathUtils;
import com.wenxi.learn.blockmonitor.BlockMonitor;

import java.io.File;

/**
 * user file util
 */

public class UserFileUtils {
    public static String getLogPath() {
        return PathUtils.getDiskCacheDir(BlockMonitor.getContext(),
                BlockMonitor.getConfig().getLogPath() + File.separator + Const.LOG_FILE_NAME).getPath();
    }
}
