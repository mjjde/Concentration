package com.open_source.joker.concentration.location.model;

/**
 * 文件名：com.open_source.joker.concentration.location.model
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class CellLteModel extends CellModel {

    public CellLteModel(int mcc, int mnc, int ci, int tac, int asu) {
        super(TYPE_LTE, mcc, mnc, ci, tac, asu);
    }

    @Override
    public String toString() {
        return String.format("{type:%s,mcc:%s,mnc:%s,ci:%s,tac:%s,asu:%s}", mType, mMcc, mMncSid,
                mCidBid, mLacNid, mAsu);
    }
}
