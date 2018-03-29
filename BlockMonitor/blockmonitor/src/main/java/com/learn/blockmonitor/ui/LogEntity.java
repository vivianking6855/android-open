package com.learn.blockmonitor.ui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Venjee_Shen on 2018/3/28.
 *
 */

public class LogEntity {

    final static int TYPE_STACK = 1;
    final static int TYPE_CPU = 2;
    final static int TYPE_DEVICE = 3;

    private String content;

    @LogType
    private int type;

    @IntDef({TYPE_STACK,TYPE_CPU,TYPE_DEVICE})
    @Retention(RetentionPolicy.SOURCE)
    @interface LogType {
    }

    private LogEntity(){

    }

    static LogEntity obtain(){
        return new LogEntity();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @LogType
    public int getType() {
        return type;
    }

    void setType(@LogType int type) {
        this.type = type;
    }
}
