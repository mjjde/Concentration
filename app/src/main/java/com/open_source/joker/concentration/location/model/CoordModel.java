package com.open_source.joker.concentration.location.model;

import android.text.TextUtils;

import java.util.List;

public class CoordModel {
    public static final int SOURCE_GPS = 0;
    public static final int SOURCE_NETWORK = 1;
    public static final int SOURCE_AMAP = 2;
    public static final int SOURCE_BMAP = 3;

    /** 定位源 */
    protected int mSource;

    protected double mLat;

    protected double mLng;

    protected int mAcc;

    protected long mElapse;

    public CoordModel(int source, double lat, double lng, int acc, long elapse) {
        mSource = source;
        mLat = lat;
        mLng = lng;
        mAcc = acc;
        mElapse = elapse;
    }

    public int getSource() {
        return mSource;
    }

    public double getLat() {
        return mLat;
    }

    public double getLng() {
        return mLng;
    }

    public int getAcc() {
        return mAcc;
    }

    public long getElapse() {
        return mElapse;
    }

    @Override
    public String toString() {
        return String.format("{source:%s,Lat:%s,Lng:%s,Accuracy:%s,Elapse:%s}", mSource, mLat,
                mLng, mAcc, mElapse);
    }

    public String toDPString() {
        return String.format("%s,%s@%s,%s", mLat, mLng, mAcc, mElapse);
    }

    public static String listToString(List<CoordModel> coordModelList) {
        String ret = "";

        if (coordModelList == null) {
            return ret;
        }

        for (CoordModel coordModel : coordModelList) {
            ret = formatCoord(coordModel, ret);
        }

        return ret;
    }

    public static String listToGpsString(List<CoordModel> coordModelList) {
        String ret = "";

        if (coordModelList == null) {
            return ret;
        }

        for (CoordModel coordModel : coordModelList) {
            if (SOURCE_GPS == coordModel.getSource()) {
                ret = formatCoord(coordModel, ret);
            }
        }

        return ret;
    }

    public static String listToNetworkString(List<CoordModel> coordModelList) {
        String ret = "";

        if (coordModelList == null) {
            return ret;
        }

        for (CoordModel coordModel : coordModelList) {
            if (SOURCE_NETWORK == coordModel.getSource()) {
                ret = formatCoord(coordModel, ret);
            }
        }

        return ret;
    }

    public static String listToAMapString(List<CoordModel> coordModelList) {
        String ret = "";

        if (coordModelList == null) {
            return ret;
        }

        for (CoordModel coordModel : coordModelList) {
            if (SOURCE_AMAP == coordModel.getSource()) {
                ret = formatCoord(coordModel, ret);
            }
        }

        return ret;
    }

    public static String listToBMapString(List<CoordModel> coordModelList) {
        String ret = "";

        if (coordModelList == null) {
            return ret;
        }

        for (CoordModel coordModel : coordModelList) {
            if (SOURCE_BMAP == coordModel.getSource()) {
                ret = formatCoord(coordModel, ret);
            }
        }

        return ret;
    }

    private static String formatCoord(CoordModel coordModel, String ret) {
        if (TextUtils.isEmpty(ret)) {
            // 第一个不需要加“|”
            ret = coordModel.toDPString();
        } else {
            ret = String.format("%s|%s", ret, coordModel.toDPString());
        }

        return ret;
    }

}
