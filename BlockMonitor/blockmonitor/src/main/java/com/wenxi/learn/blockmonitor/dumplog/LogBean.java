package com.wenxi.learn.blockmonitor.dumplog;

import android.content.Context;
import android.os.Build;

import com.wenxi.learn.blockmonitor.BlockMonitor;
import com.wenxi.learn.blockmonitor.util.DeviceUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * LogInfo, all log bean here
 */

public class LogBean {
    // all logs for user
    private String model = "";
    private String apiLevel = "";
    private String imei = "";
    private int cpuCore = -1;
    private String freeMemory;
    private String totalMemory;

    // time format
    private static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US);
    // separator
    private static final String SEPARATOR = "\r\n";
    // empty IMEI
    private static final String EMPTY_IMEI = "empty_imei";
    // file line header for each type
    private static final String HEAD_MODEL = "[model] ";
    private static final String HEAD_API = "[api-level] ";
    private static final String HEAD_IMEI = "[imei] ";
    private static final String HEAD_CPU_CORE = "[cpu-core] ";
    private static final String HEAD_CPU_BUSY = "[cpu-busy] ";
    private static final String HEAD_TIME_COST = "[time] ";
    private static final String HEAD_THREAD_TIME_COST = "[thread-time] ";
    private static final String HEAD_TIME_COST_START = "[time-start] ";
    private static final String HEAD_TIME_COST_END = "[time-end] ";
    private static final String HEAD_STACK = "[stack] ";
    private static final String HEAD_TOTAL_MEMORY = "[total memory] ";
    private static final String HEAD_FREE_MEMORY = "[free memory] ";

    private long timeCost;
    private long threadTimeCost;
    private String timeStart;
    private String timeEnd;
    private boolean cpuBusy;
    private ArrayList<String> stackList = new ArrayList<>();

    private static StringBuilder headStr = new StringBuilder();
    private static StringBuilder dynamicStr = new StringBuilder();
    private static StringBuilder stackStr = new StringBuilder();

    public static LogBean build() {
        LogBean bean = new LogBean();
        bean.cpuCore = Runtime.getRuntime().availableProcessors();
        bean.model = Build.MODEL;
        bean.apiLevel = Build.VERSION.SDK_INT + " " + Build.VERSION.RELEASE;
        Context context = BlockMonitor.getInstance().getContext();
        bean.freeMemory = String.valueOf(DeviceUtils.getDeviceUsableMemory(context));
        bean.totalMemory = String.valueOf(DeviceUtils.getMaxMemory());
        String imeipre = DeviceUtils.getIMEI(context);
        bean.imei = imeipre == null ? EMPTY_IMEI : imeipre;
        return bean;
    }

    public LogBean setStackEntries(ArrayList<String> threadStackEntries) {
        stackList = threadStackEntries;
        return this;
    }

    public LogBean setCost(long realTimeStart, long realTimeEnd, long threadTimeStart, long threadTimeEnd) {
        timeCost = realTimeEnd - realTimeStart;
        threadTimeCost = threadTimeEnd - threadTimeStart;
        timeStart = TIME_FORMATTER.format(realTimeStart);
        timeEnd = TIME_FORMATTER.format(realTimeEnd);
        return this;
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

        headStr.append(HEAD_MODEL).append(model).append(SEPARATOR);
        headStr.append(HEAD_API).append(apiLevel).append(SEPARATOR);
        headStr.append(HEAD_IMEI).append(imei).append(SEPARATOR);
        headStr.append(HEAD_CPU_CORE).append(cpuCore).append(SEPARATOR);
        headStr.append(HEAD_FREE_MEMORY).append(freeMemory).append(SEPARATOR);
        headStr.append(HEAD_TOTAL_MEMORY).append(totalMemory).append(SEPARATOR);

        return headStr.toString();
    }

    public String getDynamicString() {
        if (dynamicStr.length() > 0) {
            dynamicStr.delete(0, dynamicStr.length() - 1);
        }

        return dynamicStr.toString();
    }

    public String getStackString() {
        if (stackStr.length() > 0) {
            stackStr.delete(0, stackStr.length() - 1);
        }
        stackStr.append(HEAD_TIME_COST).append(timeCost).append(SEPARATOR);
        stackStr.append(HEAD_THREAD_TIME_COST).append(threadTimeCost).append(SEPARATOR);
        stackStr.append(HEAD_TIME_COST_START).append(timeStart).append(SEPARATOR);
        stackStr.append(HEAD_TIME_COST_END).append(timeEnd).append(SEPARATOR);

        if (stackList != null && !stackList.isEmpty()) {
            StringBuilder temp = new StringBuilder();
            for (String s : stackList) {
                temp.append(s);
                temp.append(SEPARATOR);
            }
            stackStr.append(HEAD_STACK).append(temp.toString()).append(SEPARATOR);
        }

        return stackStr.toString();
    }

}
