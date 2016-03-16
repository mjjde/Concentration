package com.open_source.joker.concentration.location.model;

import android.text.TextUtils;

import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location.model
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class CellModel {
    public static final int TYPE_NO_SIM = -1;
    public static final int TYPE_GSM = 0;
    public static final int TYPE_CDMA = 1;
    public static final int TYPE_LTE = 2;
    public static final int TYPE_WCDMA = 3;

    /** 蜂窝类型 */
    protected int mType;

    protected int mMcc;

    protected int mMncSid;

    protected int mCidBid;

    protected int mLacNid;

    protected int mAsu;

    protected int mLat;

    protected int mLng;

    public CellModel(int type) {
        mType = type;
    }

    public CellModel(int type, int mcc, int mnc, int cid, int lac, int asu) {
        this(type, mcc, mnc, cid, lac, asu, 0, 0);
    }

    public CellModel(int type, int mcc, int sid, int bid, int nid, int lat, int lng) {
        this(type, mcc, sid, bid, nid, 0, lat, lng);
    }

    private CellModel(int type, int mcc, int mncSid, int cidBid, int lacNid, int asu, int lat,
                      int lng) {
        mType = type;
        mMcc = mcc;
        mMncSid = mncSid;
        mCidBid = cidBid;
        mLacNid = lacNid;
        mAsu = asu;
        mLat = lat;
        mLng = lng;
    }

    public int getType() {
        return mType;
    }

    public int getMcc() {
        return mMcc;
    }

    public int getMncSid() {
        return mMncSid;
    }

    public int getCidBid() {
        return mCidBid;
    }

    public int getLacNid() {
        return mLacNid;
    }

    public int getAsu() {
        return mAsu;
    }

    public int getLat() {
        return mLat;
    }

    public int getLng() {
        return mLng;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof CellModel) {
            CellModel cellModel = (CellModel) obj;
            if ((mMcc == cellModel.getMcc()) && (mMncSid == cellModel.getMncSid())
                    && (mCidBid == cellModel.getCidBid()) && (mLacNid == cellModel.getLacNid())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return mMcc + mMncSid + mCidBid + mLacNid;
    }

    @Override
    public String toString() {
        return String.format("{type:%s,mcc:%s,mnc:%s,cid:%s,lac:%s,asu:%s}", mType, mMcc, mMncSid,
                mCidBid, mLacNid, mAsu);
    }

    public String toDPString() {
        return String.format("%s,%s:%s,%s,%s", mMcc, mMncSid, mCidBid, mLacNid, mAsu);
    }

    public String toDPStringNoTypeMccMnc() {
        return String.format("%s,%s,%s", mCidBid, mLacNid, mAsu);
    }

    /**
     * cell = gsm/cdma/lte/wcdma
     * gsm = MCC,MNC:CID,LAC,ASU|CID,LAC,ASU|...
     * cdma = MCC,SID:BID,NID,LAT,LNG
     * lte = MCC,MNC:CI,TAC,ASU
     * wcdma = MCC,MNC:CID,LAC,ASU
     */
    public static String listToString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            ret = formatCell(cellModel, ret);
        }

        return ret;
    }

    public static String listToNoSimString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            if (TYPE_NO_SIM == cellModel.getType()) {
                ret = formatCell(cellModel, ret);
            }
        }

        return ret;
    }

    public static String listToGsmString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            if (TYPE_GSM == cellModel.getType()) {
                ret = formatCell(cellModel, ret);
            }
        }

        return ret;
    }

    public static String listToCdmaString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            if (TYPE_CDMA == cellModel.getType()) {
                ret = formatCell(cellModel, ret);
            }
        }

        return ret;
    }

    public static String listToLteString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            if (TYPE_LTE == cellModel.getType()) {
                ret = formatCell(cellModel, ret);
            }
        }

        return ret;
    }

    public static String listToWcdmaString(List<CellModel> cellModelList) {
        String ret = "";

        if (cellModelList == null) {
            return ret;
        }

        for (CellModel cellModel : cellModelList) {
            if (TYPE_WCDMA == cellModel.getType()) {
                ret = formatCell(cellModel, ret);
            }
        }

        return ret;
    }

    private static String formatCell(CellModel cellModel, String ret) {
        if (TextUtils.isEmpty(ret)) {
            // 第一个不需要加“|”
            ret = cellModel.toDPString();
        } else {
            ret = String.format("%s|%s", ret, cellModel.toDPStringNoTypeMccMnc());
        }

        return ret;
    }
}
