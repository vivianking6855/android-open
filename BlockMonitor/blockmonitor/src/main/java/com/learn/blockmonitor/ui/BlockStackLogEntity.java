package com.learn.blockmonitor.ui;

/**
 * Created by Venjee_Shen on 2018/3/27.
 *
 */

public class BlockStackLogEntity {


    private LogEntity device;
    private LogEntity cpu;
    private LogEntity stack;

    private BlockStackLogEntity(){
        stack = LogEntity.obtain();
        stack.setType(LogEntity.TYPE_STACK);
    }

    static BlockStackLogEntity obtain(){
        return new BlockStackLogEntity();
    }

    LogEntity getDevice() {
        return device;
    }

    LogEntity getCpu() {
        return cpu;
    }

    void setDevice(LogEntity device) {
        this.device = device;
    }

    void setCpu(LogEntity cpu) {
        this.cpu = cpu;
    }

    public void setContent(String content){
        stack.setContent(content);
    }

    public String getContent(){
        return stack.getContent();
    }
}
