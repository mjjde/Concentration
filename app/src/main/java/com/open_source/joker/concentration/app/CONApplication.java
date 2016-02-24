package com.open_source.joker.concentration.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.open_source.joker.concentration.constant.ConfigHelper;
import com.open_source.joker.concentration.service.DebugWindowService;
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

    private static int liveCounter;
    private static int activeCounter;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if ((--liveCounter) == 0) {
                    onApplicationStop();
                }
            }
            if (msg.what == 2) {
                // 1. skip a event loop in case onPause or onDestory takes too
                // long
                // 2.
                // 增加100ms延迟来处理:startActivityForResult->startActivityForResult->startActivityForResult->
                // finish->onActivityResult,finish->onActivityResult,finish
                // 导致onApplicationPause的问题，导致重新定位的问题。
                sendEmptyMessageDelayed(3, 100);
            }
            if (msg.what == 3) {
                if ((--activeCounter) == 0) {
                    onApplicationPause();
                }
            }
        }
    };

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

    public void activityOnCreate(Activity a) {
        if (liveCounter++ == 0) {
            onApplicationStart();
        }
    }

    public void activityOnDestory(Activity a) {
        handler.sendEmptyMessage(1);
    }

    public void activityOnResume(Activity a) {
        if (activeCounter++ == 0) {
            onApplicationResume();
        }
    }

    public void activityOnPause(Activity a) {
        handler.sendEmptyMessage(2);
    }


    public void onApplicationStart() {
        if (Environment.isDebug()) {
            startService(new Intent(this, DebugWindowService.class));
        }
    }

    public void onApplicationPause() {
        Log.i("application", "onApplicationPause");
    }

    public void onApplicationStop() {
        Log.i("application", "onApplicationStop");
    }

    public void onApplicationResume() {
        Log.i("application", "onApplicationResume");
    }
}
