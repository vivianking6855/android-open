package com.learn.blockmonitor.data.entity;

/**
 * The type Log entity.
 */
public class LogEntity {
    // all logs for user
    private String model = "";
    private String apiLevel = "";
    private String imei = "";
    private int cpuCore = -1;
    private String freeMemory;
    private String totalMemory;

    private long timeCost; // time diff between two frames
    private double droppedCount; // drop frame count
    private String timeStart;
    private String timeEnd;
    private boolean cpuBusy;

    /**
     * Gets model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Gets api level.
     *
     * @return the api level
     */
    public String getApiLevel() {
        return apiLevel;
    }

    /**
     * Sets api level.
     *
     * @param apiLevel the api level
     */
    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    /**
     * Gets imei.
     *
     * @return the imei
     */
    public String getImei() {
        return imei;
    }

    /**
     * Sets imei.
     *
     * @param imei the imei
     */
    public void setImei(String imei) {
        this.imei = imei;
    }

    /**
     * Gets cpu core.
     *
     * @return the cpu core
     */
    public int getCpuCore() {
        return cpuCore;
    }

    /**
     * Sets cpu core.
     *
     * @param cpuCore the cpu core
     */
    public void setCpuCore(int cpuCore) {
        this.cpuCore = cpuCore;
    }

    /**
     * Gets free memory.
     *
     * @return the free memory
     */
    public String getFreeMemory() {
        return freeMemory;
    }

    /**
     * Sets free memory.
     *
     * @param freeMemory the free memory
     */
    public void setFreeMemory(String freeMemory) {
        this.freeMemory = freeMemory;
    }

    /**
     * Gets total memory.
     *
     * @return the total memory
     */
    public String getTotalMemory() {
        return totalMemory;
    }

    /**
     * Sets total memory.
     *
     * @param totalMemory the total memory
     */
    public void setTotalMemory(String totalMemory) {
        this.totalMemory = totalMemory;
    }

    /**
     * Gets time cost.
     *
     * @return the time cost
     */
    public long getTimeCost() {
        return timeCost;
    }

    /**
     * Sets time cost.
     *
     * @param timeCost the time cost
     */
    public void setTimeCost(long timeCost) {
        this.timeCost = timeCost;
    }

    /**
     * Gets dropped count.
     *
     * @return the dropped count
     */
    public double getDroppedCount() {
        return droppedCount;
    }

    /**
     * Sets dropped count.
     *
     * @param droppedCount the dropped count
     */
    public void setDroppedCount(double droppedCount) {
        this.droppedCount = droppedCount;
    }

    /**
     * Gets time start.
     *
     * @return the time start
     */
    public String getTimeStart() {
        return timeStart;
    }

    /**
     * Sets time start.
     *
     * @param timeStart the time start
     */
    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * Gets time end.
     *
     * @return the time end
     */
    public String getTimeEnd() {
        return timeEnd;
    }

    /**
     * Sets time end.
     *
     * @param timeEnd the time end
     */
    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    /**
     * Is cpu busy boolean.
     *
     * @return the boolean
     */
    public boolean isCpuBusy() {
        return cpuBusy;
    }

    /**
     * Sets cpu busy.
     *
     * @param cpuBusy the cpu busy
     */
    public void setCpuBusy(boolean cpuBusy) {
        this.cpuBusy = cpuBusy;
    }
}
