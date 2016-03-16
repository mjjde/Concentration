package com.open_source.joker.concentration.location.model;

/**
 * 文件名：com.open_source.joker.concentration.location.model
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class CellCdmaModel extends CellModel {


    public CellCdmaModel(int mcc, int sid, int bid, int nid, int lat, int lng) {
        super(TYPE_CDMA, mcc, sid, bid, nid, lat, lng);
    }

    @Override
    public String toString() {
        return String.format("{type:%s,mcc:%s,sid:%s,bid:%s,nid:%s,lat:%s,lng:%s}", mType, mMcc,
                mMncSid, mCidBid, mLacNid, mLat, mLng);
    }

    @Override
    public String toDPString() {
        return String.format("%s,%s:%s,%s,%s,%s", mMcc, mMncSid, mCidBid, mLacNid, mLat, mLng);
    }

    @Override
    public String toDPStringNoTypeMccMnc() {
        return String.format("%s,%s,%s,%s", mCidBid, mLacNid, mLat, mLng);
    }

}
