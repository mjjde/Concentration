package com.open_source.joker.concentration.location.model;

/**
 * 文件名：com.open_source.joker.concentration.location.model
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class CellWcdmaModel extends  CellModel{
    public CellWcdmaModel(int mcc, int mnc, int cid, int lac, int asu) {
        super(TYPE_WCDMA, mcc, mnc, cid, lac, asu);
    }
}
