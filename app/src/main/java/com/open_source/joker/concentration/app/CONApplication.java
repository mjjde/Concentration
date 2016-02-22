package com.open_source.joker.concentration.app;

import android.app.Application;

/**
 * 文件名：com.open_source.joker.concentration.app
 * 描述：
 * 时间：16/2/17
 * 作者: joker
 */
public class CONApplication extends MultiDexApplication {

    private static CONApplication instance;

    public CONApplication() {
        instance = this;
    }

    public static CONApplication _instance() {
        return instance;
    }

    public static CONApplication instance() {
        if (instance == null) {
            throw new IllegalStateException("Application has not been created");
        }

        return instance;
    }
}
