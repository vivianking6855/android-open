package com.open.utilslib.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by vivian on 2017/11/13.
 * app utils, such as app version, memory, etc.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public final class AppUtils {
    private static final String TAG = "AppUtils";

    /**
     * get version code
     *
     * @param context the context
     * @return version code, if fail will return -1
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "getAppVersionCode ex ", e);
        }
        return -1;
    }

    /**
     * get version name
     *
     * @param context the context
     * @return version name, fail will return empty ""
     */
    public static String getAppVersionName(Context context) {
        if (context == null) {
            return "";
        }
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "getAppVersionName ex ", e);
        }
        return "";
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * 支持4.1.2,4.1.23.4.1.rc111这种形式
     *
     * @param version1 first version
     * @param version2 second version
     * @return 正数，如果version1 大于 version2; 负数，如果version1小于version2; 0 如果相等
     * @throws Exception version can't be null
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }


}
