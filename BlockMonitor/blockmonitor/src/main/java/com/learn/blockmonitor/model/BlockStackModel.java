package com.learn.blockmonitor.model;

/**
 * BlockStack model,include several LogModel: device, cpu, stack
 */
public class BlockStackModel {
    private LogModel device;
    private LogModel cpu;
    private LogModel stack;

    private BlockStackModel() {
        stack = LogModel.obtain();
        stack.setType(LogModel.TYPE_STACK);
    }

    /**
     * Obtain block stack log entity.
     *
     * @return the block stack log entity
     */
    public static BlockStackModel obtain() {
        return new BlockStackModel();
    }

    /**
     * Gets device.
     *
     * @return the device
     */
    public LogModel getDevice() {
        return device;
    }

    /**
     * Gets cpu.
     *
     * @return the cpu
     */
    public LogModel getCpu() {
        return cpu;
    }

    /**
     * Sets device.
     *
     * @param device the device
     */
    public void setDevice(LogModel device) {
        this.device = device;
    }

    /**
     * Sets cpu.
     *
     * @param cpu the cpu
     */
    public void setCpu(LogModel cpu) {
        this.cpu = cpu;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        stack.setContent(content);
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return stack.getContent();
    }
}
