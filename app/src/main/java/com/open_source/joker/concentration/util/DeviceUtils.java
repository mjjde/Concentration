package com.open_source.joker.concentration.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：手机参数
 * 时间：16/2/22
 * 作者: joker
 */
public class DeviceUtils {
    /**
     * 获取包相关信息
     *
     * @param c
     * @return
     */
    public static PackageInfo getPackageInfo(Context c) {
        try {
            return c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
