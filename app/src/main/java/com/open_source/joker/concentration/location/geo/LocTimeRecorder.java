package com.open_source.joker.concentration.location.geo;

/**
 * 文件名：com.open_source.joker.concentration.location.geo
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class LocTimeRecorder {


    private long mCellScanElapse;

    private long mWifiScanElapse;

    private long mLocalLocElapse;

    public synchronized void resetAll() {
        mCellScanElapse = 0;
        mWifiScanElapse = 0;
        mLocalLocElapse = 0;
    }

    public synchronized long getCellScanElapse() {
        return mCellScanElapse;
    }

    public synchronized long getWifiScanElapse() {
        return mWifiScanElapse;
    }

    public synchronized long getLocalLocElapse() {
        return mLocalLocElapse;
    }

    public synchronized void initCellScanElapse() {
        mCellScanElapse = System.currentTimeMillis();
    }

    public synchronized void initWifiScanElapse() {
        mWifiScanElapse = System.currentTimeMillis();
    }

    public synchronized void initLocalLocElapse() {
        mLocalLocElapse = System.currentTimeMillis();
    }

    public synchronized void calCellScanElapse() {
        mCellScanElapse = System.currentTimeMillis() - mCellScanElapse;
    }

    public synchronized void calWifiScanElapse() {
        mWifiScanElapse = System.currentTimeMillis() - mWifiScanElapse;
    }

    public synchronized void calLocalLocElapse() {
        mLocalLocElapse = System.currentTimeMillis() - mLocalLocElapse;
    }
}
