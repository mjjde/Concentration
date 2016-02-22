package com.open_source.joker.concentration.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


import com.open_source.joker.concentration.app.CONApplication;
import com.open_source.joker.concentration.util.log.Log;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：
 * 时间：16/2/22
 * 作者: joker
 */
public class Environment {

    private static PackageInfo packageInfo;

    private static PackageInfo pkgInfo() {
        if (packageInfo == null) {
            try {
                Context c = CONApplication._instance();
                packageInfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
        }

        return packageInfo;
    }

    /**
     * versionName
     *
     * @return
     */
    public static String versionName() {
        return pkgInfo().versionName;
    }

    /**
     * versionCode
     *
     * @return
     */
    public static int versionCode() {
        return pkgInfo().versionCode;
    }

    public static boolean isDebug() {
        return Log.LEVEL < Integer.MAX_VALUE;
    }
}
