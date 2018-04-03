package com.learn.blockmonitor.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Log Model, include log type, and log content, etc.
 */
public class LogModel {
    /**
     * The constant TYPE_STACK.
     */
    final static int TYPE_STACK = 1;
    /**
     * The constant TYPE_CPU.
     */
    final static int TYPE_CPU = 2;
    /**
     * The constant TYPE_DEVICE.
     */
    final static int TYPE_DEVICE = 3;

    @LogType
    private int type;

    /**
     * The interface Log type.
     */
    @IntDef({TYPE_STACK,TYPE_CPU,TYPE_DEVICE})
    @Retention(RetentionPolicy.SOURCE)
    @interface LogType {
    }

    private String content;

    private LogModel(){

    }

    /**
     * Obtain log model.
     *
     * @return the log model
     */
    static LogModel obtain(){
        return new LogModel();
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    @LogType
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    void setType(@LogType int type) {
        this.type = type;
    }
}
