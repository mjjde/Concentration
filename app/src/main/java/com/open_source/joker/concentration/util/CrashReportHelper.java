package com.open_source.joker.concentration.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.*;
import android.os.Process;


import com.open_source.joker.concentration.app.CONApplication;
import com.open_source.joker.concentration.util.log.FileAppender;
import com.open_source.joker.concentration.util.log.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.UUID;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述： 奔溃日志
 * 时间：16/2/22
 * 作者: joker
 */
public class CrashReportHelper {

    static final int crashReportCount = 20;
    private static LinkedList<String> listSchema = new LinkedList<String>();
    private static String TAG = CrashReportHelper.class.getSimpleName();
    private static Thread.UncaughtExceptionHandler unknownCrashHandler;


    private static Thread.UncaughtExceptionHandler defaultHandler;
    public static int versionCode = 0;
    public static String versionName = null;
    public static File reportFile;

    // 记录URL Schema 跳转历史，打印到Crash Log中，方便定位错误位置
    public static long lastOutOfMemoryMills;
    public static boolean debug = true;

    static {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        unknownCrashHandler = new CrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(unknownCrashHandler);
    }

    public static void init(Context mContext) {
        if (reportFile != null)
            return;
        PackageInfo pi = DeviceUtils.getPackageInfo(mContext);
        versionCode = Environment.versionCode();
        versionName = Environment.versionName();
        if (pi.applicationInfo != null)
            debug = (pi.applicationInfo.flags & 0x2) != 0;

        reportFile = new File(mContext.getFilesDir(), "crash_report");
        File oomFile = new File(reportFile.getParent(), "out_of_memory");
        if (oomFile.exists()) {
            lastOutOfMemoryMills = oomFile.lastModified();
            oomFile.delete();
        }
    }

    public static void installSafeLooper() {
        SafeLooper.install();
        if (!Environment.isDebug()) {
            // 不抛出去
            defaultHandler = null;
        }
        SafeLooper.setUncaughtExceptionHandler(unknownCrashHandler);
    }

    /**
     * 向栈中添加一条新的URL Schema。
     *
     * @param urlSchema
     */
    public static void putUrlSchema(String urlSchema) {
        if (urlSchema == null || urlSchema.length() == 0) {
            return;
        }
        Log.d(TAG, "urlSchema: " + urlSchema);
        if (listSchema.size() > 10) {
            listSchema.removeLast();
        }
        listSchema.addFirst(urlSchema);
    }

    private static boolean hasOutOfMemoryError(Throwable ex) {
        if (ex == null)
            return false;
        Throwable next = ex.getCause();
        for (int i = 0; i < 0xF; i++) {
            if (ex instanceof OutOfMemoryError)
                return true;
            if (next == null || next == ex)
                return false;
            ex = next;
            next = ex.getCause();
        }
        return false;
    }

    public static boolean isAvailable() {
        return reportFile != null && reportFile.exists();
    }

    public static String getReport() {
        if (reportFile == null)
            return null;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(reportFile);
            if (fis.available() > 64 * 1000)
                return null;
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            return new String(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getReportBak() {
        if (reportFile == null)
            return null;
        File bak = new File(reportFile.getParent(), reportFile.getName() + ".bak");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(bak);
            if (fis.available() > 64 * 1000)
                return null;
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            return new String(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean deleteReport() {
        if (reportFile == null)
            return false;
        File bak = new File(reportFile.getParent(), reportFile.getName() + ".bak");
        bak.delete();
        return reportFile.renameTo(bak);
    }

    public static void sendAndDelete() {
        if (!isAvailable())
            return;
        final String report = getReport();
        deleteReport();
        Log.i(TAG, report);
        if (report == null || !report.startsWith("====") || report.contains("debug=true"))
            return;
        new Thread("Send Crash Report") {
            @Override
            public void run() {
                try {
                    // http request
                } catch (Exception e) {
                    Log.e(TAG, "Failed to send crash report " + e);
                }
            }
        }.start();
    }

    /**
     * 每24小时只上报N条crash
     **/
    static public boolean isReportOn() {

        if (crashReportCount == 0) {
            return true;
        }

        CONApplication context = CONApplication.instance();
        long mmOneDay = 24 * 3600 * 1000;
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        long curTime = System.currentTimeMillis();
        long baseReportTime = sp.getLong("baseReportTime", 0);
        int reportCount = sp.getInt("reportCount", 0);
        SharedPreferences.Editor edit = sp.edit();

        if (curTime - baseReportTime > 0 && curTime - baseReportTime < mmOneDay) {
            if (reportCount < crashReportCount) {
                edit.putInt("reportCount", ++reportCount);
                edit.apply();
                return true;
            } else {
                return false;
            }
        } else {
            edit.putLong("baseReportTime", curTime);
            edit.putInt("reportCount", 1);
            edit.apply();
            return true;
        }
    }

    static class CrashHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            boolean oom = false;
            try {
                if (!isReportOn()) {
                    return;
                }
                //flush log writer
                FileAppender.getInstance().flush();

                if (reportFile == null)
                    return;
                if (oom = hasOutOfMemoryError(ex)) {
                    File oomFile = new File(reportFile.getParent(), "out_of_memory");
                    oomFile.delete();
                    oomFile.createNewFile();
                }
                PrintWriter w = new PrintWriter(reportFile, "utf-8");

                w.print("===============================");
                w.print(UUID.randomUUID().toString());
                w.println("============================");

                if (debug)
                    w.println("debug=true");



                w.print("addtime=");


                w.print("user-agent=");



                w.print("deviceid=");


                w.print("dpid=");


                w.print("sessionid=");


                w.print("cityid=");


                w.print("token=");


                w.print("network=");



                w.print("os-version=");
                w.println(Build.VERSION.RELEASE);

                w.print("os-build=");
                w.println(Build.ID);

                w.print("device-brand=");
                w.println(Build.BRAND);

                w.print("device-model=");
                w.println(Build.MODEL);

                w.print("device-fingerprint=");
                w.println(Build.FINGERPRINT);

                w.print("thread=");
                w.println(thread.getName());

                w.print("buildNumber=");

                w.println();
                ex.printStackTrace(w);
                w.println();
                w.println();

                w.println("Url Schema history:");
                for (String url : listSchema) {
                    w.println(url);
                }

                w.close();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                if (oom) {
                    android.os.Process.killProcess(Process.myPid());
                } else if (defaultHandler != null) {
                    defaultHandler.uncaughtException(thread, ex);
                }
            }
        }
    }

}
