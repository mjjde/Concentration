package com.open_source.joker.concentration.location.locate;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public class CommonUtil {

    private static final String COMMON_DATE = "yyyy-MM-dd-HH-mm-ss";

    public static String intToIpAddress(int param) {
        return (param & 0xFF) + "." + ((param >> 8) & 0xFF) + "." + ((param >> 16) & 0xFF) + "."
                + (param >> 24);
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(COMMON_DATE);
        return format.format(new Date());
    }

    public static double format(double dout, int n) {
        double p = Math.pow(10, n);
        return Math.round(dout * p) / p;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * scale) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((pxValue / scale) + 0.5f);
    }
}
