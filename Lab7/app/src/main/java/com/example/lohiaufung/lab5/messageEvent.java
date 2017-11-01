package com.example.lohiaufung.lab5;

/**
 * Created by LoHiaufung on 2017/10/27.
 */

public class messageEvent {
    private String msg;

    public messageEvent(String msg) {//事件传递参数
        this.msg = msg;
    }

    public String getMsg() {//取出事件参数
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
