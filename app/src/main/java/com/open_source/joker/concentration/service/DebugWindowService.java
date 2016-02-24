package com.open_source.joker.concentration.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.open_source.joker.concentration.util.DebugWindowManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 文件名：com.open_source.joker.concentration.service
 * 描述：
 * 时间：16/2/24
 * 作者: joker
 */
public class DebugWindowService extends Service {

    private Timer timer;
    Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 开启定时器，每隔0.5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        timer = null;

        DebugWindowManager.removeSmallWindow(getApplicationContext());

    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            boolean isAppRunning = isAppRunning();

            // 当前程序正在运行, 没有悬浮窗显示，则创建悬浮窗。
          if (isAppRunning && !DebugWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DebugWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
          if (!isAppRunning && DebugWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DebugWindowManager.removeSmallWindow(getApplicationContext());
                    }
                });
            }
        }
    }


    /**
     * 判断当前程序是否正在运行
     */
    boolean isAppRunning() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        String pkgName = getPackageName();
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(pkgName)
                    && info.baseActivity.getPackageName().equals(pkgName)) {
                return true;
            }
        }
        return false;
    }



}
