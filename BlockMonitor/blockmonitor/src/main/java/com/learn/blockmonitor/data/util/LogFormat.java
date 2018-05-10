package com.learn.blockmonitor.data.util;

import android.content.Context;
import android.os.Build;

import com.learn.blockmonitor.data.entity.LogEntity;
import com.open.utilslib.device.DeviceUtils;
import com.open.utilslib.time.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * LogInfo, all log bean here
 */
public class LogFormat {
    //private static final int MAX_CACHE_SIZE = 100;

    private LogEntity mLogEntity;

    // default time format  "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat(TimeUtils.DEFAULT_PATTERN, Locale.getDefault());
    // separator
    private static final String SEPARATOR = "\n";
    private static final String LINE = " =========================================";
    // empty IMEI
    private static final String EMPTY_IMEI = "empty_imei";
    // file line header for each type
    private static final String HEAD_START = "[start]";
    private static final String HEAD_MODEL = "[model] ";
    private static final String HEAD_API = "[api-level] ";
    private static final String HEAD_IMEI = "[imei] ";
    private static final String HEAD_CPU_CORE = "[cpu-core] ";
    private static final String HEAD_CPU_BUSY = "[cpu-busy] ";
    private static final String HEAD_CPU_STAT = "[cpu-stat] ";
    private static final String HEAD_TIME_COST = "[time] ";
    private static final String HEAD_DROP = "[drop frame count] ";
    private static final String HEAD_TIME_COST_START = "[time-start] ";
    private static final String HEAD_TIME_COST_END = "[time-end] ";
    private static final String HEAD_STACK = "[stack] ";
    private static final String HEAD_TOTAL_MEMORY = "[app total memory] ";
    private static final String HEAD_FREE_MEMORY = "[system free memory] ";

    private ArrayList<StringBuilder> stackList = new ArrayList<>();
    private StringBuilder headStr = new StringBuilder();
    private StringBuilder stackStr = new StringBuilder();
    private StringBuilder cpuStr = new StringBuilder();

    //frame refresh chart data cache
    private ConcurrentLinkedQueue<Long> refreshTimeConsumeCache = new ConcurrentLinkedQueue<>();

    /**
     * Build log bean.
     *
     * @param context context
     */
    public LogFormat(Context context) {
        mLogEntity = new LogEntity();
        mLogEntity.setCpuCore(Runtime.getRuntime().availableProcessors());
        mLogEntity.setModel(Build.MODEL);
        mLogEntity.setApiLevel(Build.VERSION.SDK_INT + " " + Build.VERSION.RELEASE);
        //mLogEntity.setTotalMemory(String.valueOf(DeviceUtils.getMaxMemory() / 1024) + "M");
        if(context != null){
            //mLogEntity.setFreeMemory(String.valueOf(DeviceUtils.getDeviceUsableMemory(context)) + "M");
            String imeipre = DeviceUtils.getIMEI(context);
            mLogEntity.setImei(imeipre == null ? EMPTY_IMEI : imeipre);
        }
    }

    /**
     * Sets stack entries.
     *
     * @param threadStackEntries the thread stack entries
     */
    public void setStackEntries(ArrayList<StringBuilder> threadStackEntries) {
        stackList = threadStackEntries;
    }

    /**
     * Sets cost.
     *
     * @param timeStart    the time start
     * @param timeEnd      the time end
     * @param droppedCount drop frame count
     */
    public void setCost(long timeStart, long timeEnd, double droppedCount) {
        mLogEntity.setTimeCost(timeEnd - timeStart);
        mLogEntity.setDroppedCount(droppedCount);
        mLogEntity.setTimeStart(TIME_FORMATTER.format(timeStart));
        mLogEntity.setTimeEnd(TIME_FORMATTER.format(timeEnd));
    }

    /**
     * Sets CPU stat.
     *
     * @param cpuStat cpu and process stat
     * @return cpu stat
     */
    public String getCPUStat(String cpuStat) {
        if (cpuStr.length() > 0) {
            cpuStr.delete(0, cpuStr.length());
        }
        return cpuStr.append(HEAD_CPU_STAT).append(cpuStat).append(SEPARATOR).
                append(HEAD_CPU_BUSY).append(mLogEntity.isCpuBusy()).append(SEPARATOR).toString();
    }

    /**
     * Gets header string. at the head of file. may not need refresh for each time
     *
     * @return the header string
     */
    public String getHeaderString() {
        if (headStr.length() > 0) {
            headStr.delete(0, headStr.length() - 1);
        }

        headStr.append(HEAD_START).append(TimeUtils.getNowTimeString())
                .append(LINE).append(SEPARATOR);
        headStr.append(HEAD_MODEL).append(mLogEntity.getModel()).append(SEPARATOR);
        headStr.append(HEAD_API).append(mLogEntity.getApiLevel()).append(SEPARATOR);
        headStr.append(HEAD_IMEI).append(mLogEntity.getImei()).append(SEPARATOR);
        headStr.append(HEAD_CPU_CORE).append(mLogEntity.getCpuCore()).append(SEPARATOR);
        headStr.append(HEAD_FREE_MEMORY).append(mLogEntity.getFreeMemory()).append(SEPARATOR);
        headStr.append(HEAD_TOTAL_MEMORY).append(mLogEntity.getTotalMemory()).append(SEPARATOR);

        return headStr.toString();
    }

    /**
     * Gets stack string.
     *
     * @return the stack string
     */
    public String getStackString() {
        if (stackStr.length() > 0) {
            stackStr.delete(0, stackStr.length());
        }

        stackStr.append(HEAD_TIME_COST).append(mLogEntity.getTimeCost()).append(SEPARATOR);
        stackStr.append(HEAD_TIME_COST_START).append(mLogEntity.getTimeStart()).append(SEPARATOR);
        stackStr.append(HEAD_TIME_COST_END).append(mLogEntity.getTimeEnd()).append(SEPARATOR);
        stackStr.append(HEAD_DROP).append(mLogEntity.getDroppedCount()).append(SEPARATOR);
        stackStr.append(HEAD_STACK).append(SEPARATOR);

        if (stackList != null && !stackList.isEmpty()) {
            for (StringBuilder sb : stackList) {
                stackStr.append(sb.toString());
                stackStr.append(SEPARATOR);
            }
        }
        return stackStr.toString();
    }

    public void addRefreshFrameDurationCache(long duration) {
      /*  if(refreshTimeConsumeCache.size() == MAX_CACHE_SIZE){
            refreshTimeConsumeCache = (LinkedList<Long>)refreshTimeConsumeCache.subList((int)(MAX_CACHE_SIZE * 0.5),MAX_CACHE_SIZE);
        }*/
        refreshTimeConsumeCache.offer(duration);
    }

    public void clearDrawCache() {
        refreshTimeConsumeCache.clear();
    }

    public ConcurrentLinkedQueue<Long> getDrawCacheData() {
        return refreshTimeConsumeCache;
    }

    public void destroy() {
        refreshTimeConsumeCache.clear();
        stackList.clear();
        headStr.delete(0, headStr.length());
        headStr = null;
        stackStr.delete(0, stackStr.length());
        stackStr = null;
        cpuStr.delete(0, cpuStr.length());
        cpuStr = null;
    }
}
