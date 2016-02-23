package com.open_source.joker.concentration.app;

import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.open_source.joker.concentration.constant.ConfigHelper;
import com.open_source.joker.concentration.util.CrashReportHelper;
import com.open_source.joker.concentration.util.Environment;
import com.open_source.joker.concentration.util.log.Log;

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

    @Override
    public void onCreate() {
        super.onCreate();
        if ((getApplicationInfo().flags & 0x2) != 0) {
            Log.LEVEL = Log.VERBOSE;
        } else {
            Log.LEVEL = Integer.MAX_VALUE;
        }

        checkCrashReport();
    }

    private void checkCrashReport() {
        CrashReportHelper.init(this);
        if (!ConfigHelper.disableSafeLooper) {
            CrashReportHelper.installSafeLooper();
        }
        boolean crashed = CrashReportHelper.isAvailable();
        if (crashed) {
            CrashReportHelper.sendAndDelete();
        }
        if (CrashReportHelper.lastOutOfMemoryMills + 10000 > System.currentTimeMillis()) {
            if (Environment.isDebug())
                Toast.makeText(this, "内存不足", Toast.LENGTH_LONG).show();
        }
        CrashReportHelper.lastOutOfMemoryMills = 0;
    }
}
