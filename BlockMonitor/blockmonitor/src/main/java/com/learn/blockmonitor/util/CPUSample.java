package com.learn.blockmonitor.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * CPUSample
 * get cpu stat and process stat
 * format like this:
 * cpu:7% app:0% [user:0% system:7% ioWait:0% ]
 */
public class CPUSample {
    private static final String TAG = "CPUSample";

    // cpu file
    private static final int BUFFER_SIZE = 1024;
    private static final String CPU_FILE = "/proc/stat";
    private int sPid = 0;
    private String cpuRate;
    private String pidCpuRate;

    // last status
    private long mUserLast = 0;
    private long mSystemLast = 0;
    private long mIdleLast = 0;
    private long mIoWaitLast = 0;
    private long mTotalLast = 0;
    private long mAppCpuTimeLast = 0;

    private final static class SampleHolder {
        private static final CPUSample INSTANCE = new CPUSample();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static CPUSample getInstance() {
        return SampleHolder.INSTANCE;
    }

    /**
     * Sample string.
     *
     * @return the string
     */
    public String sample() {
        readInformation();
        return parse();
    }

    /**
     * read information from cpu and process file
     */
    private void readInformation() {
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;
        try {
            // read buffer from cpu file
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(CPU_FILE)), BUFFER_SIZE);
            cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                cpuRate = "";
            }

            // get process id
            if (sPid == 0) {
                sPid = android.os.Process.myPid();
            }

            // read process stream
            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + sPid + "/stat")), BUFFER_SIZE);
            pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                pidCpuRate = "";
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "readInformation: ", throwable);
        } finally {
            try {
                if (cpuReader != null) {
                    cpuReader.close();
                }
                if (pidReader != null) {
                    pidReader.close();
                }
            } catch (IOException exception) {
                Log.e(TAG, "readInformation: ", exception);
            }
        }
    }

    /**
     * parse cpu and process information
     */
    private String parse() {
        String result = "fail";
        // parse cpuRate
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            Log.w(TAG, "parse cpu info not enough");
            return result;
        }
        long user = Long.parseLong(cpuInfoArray[2]);
        long nice = Long.parseLong(cpuInfoArray[3]);
        long system = Long.parseLong(cpuInfoArray[4]);
        long idle = Long.parseLong(cpuInfoArray[5]);
        long ioWait = Long.parseLong(cpuInfoArray[6]);
        long total = user + nice + system + idle + ioWait
                + Long.parseLong(cpuInfoArray[7])
                + Long.parseLong(cpuInfoArray[8]);

        // parse pid cpu
        String[] pidCpuInfoList = pidCpuRate.split(" ");
        if (pidCpuInfoList.length < 17) {
            Log.w(TAG, "parse process info not enough");
            return result;
        }
        long appCpuTime = Long.parseLong(pidCpuInfoList[13])
                + Long.parseLong(pidCpuInfoList[14])
                + Long.parseLong(pidCpuInfoList[15])
                + Long.parseLong(pidCpuInfoList[16]);

        if (mTotalLast != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            long idleTime = idle - mIdleLast;
            long totalTime = total - mTotalLast;
            final long cpu = (totalTime == 0) ? 0 : (totalTime - idleTime) * 100L / totalTime;
            stringBuilder
                    .append("cpu:")
                    .append(cpu)
                    .append("% ")
                    .append("app:")
                    .append((appCpuTime - mAppCpuTimeLast) * 100L / totalTime)
                    .append("% ")
                    .append("[")
                    .append("user:").append((user - mUserLast) * 100L / totalTime)
                    .append("% ")
                    .append("system:").append((system - mSystemLast) * 100L / totalTime)
                    .append("% ")
                    .append("ioWait:").append((ioWait - mIoWaitLast) * 100L / totalTime)
                    .append("% ]");
            result = stringBuilder.toString();
        }

        // save status
        mUserLast = user;
        mSystemLast = system;
        mIdleLast = idle;
        mIoWaitLast = ioWait;
        mTotalLast = total;

        mAppCpuTimeLast = appCpuTime;

        return result;
    }

}
