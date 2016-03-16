package com.open_source.joker.concentration.location.model;

import android.text.TextUtils;

import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location.model
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class WifiModel {
    private String mSsid;

    /** mac address */
    private String mBssid;

    /** signal strength, dBm */
    private int mLevel;

    public WifiModel(String ssid, String bssid, int level) {
        mSsid = ssid.replace("|", "-").replace(",", "_");
        mBssid = bssid;
        mLevel = level;
    }

    public String getSsid() {
        return mSsid;
    }

    public String getBssid() {
        return mBssid;
    }

    public int getLevel() {
        return mLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof WifiModel) {
            WifiModel wifiModel = (WifiModel) obj;
            if (mBssid.equals(wifiModel.getBssid())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return mLevel;
    }

    @Override
    public String toString() {
        return String.format("{ssid:%s,mac:%s,dBm:%d}", mSsid, mBssid, mLevel);
    }

    public String toDPString() {
        if (TextUtils.isEmpty(mSsid)) {
            return "";
        }

        return String.format("%s,%s,%d", mSsid, mBssid, mLevel);
    }

    /**
     * wifi = SID,MAC,dBm|SID,MAC,dBm|...
     * 第一个表示当前连接着的wifi，没有则为空
     */
    public static String listToString(List<WifiModel> wifiModelList) {
        String ret = "";

        if (wifiModelList == null) {
            return ret;
        }

        for (int i = 0; i < wifiModelList.size(); i++) {
            WifiModel wifiModel = wifiModelList.get(i);
            if (wifiModel == null) {
                continue;
            }

            if (i == 0) {
                // 第一个不需要加“|”
                ret = wifiModel.toDPString();
            } else {
                ret = String.format("%s|%s", ret, wifiModel.toDPString());
            }
        }

        return ret;
    }
}
