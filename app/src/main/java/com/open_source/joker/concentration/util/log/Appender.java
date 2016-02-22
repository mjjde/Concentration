package com.open_source.joker.concentration.util.log;

/**
 * 文件名：com.open_source.joker.concentration.util.log
 * 描述：用于记录error和运行信息的日志
 * 时间：16/2/22
 * 作者: joker
 */
public abstract class Appender {

    public abstract void i(int priority, String TAG, String message);

    public abstract void e(int priority, String TAG, String message);

    public void open() {
    }

    public void close() {
    }
}
