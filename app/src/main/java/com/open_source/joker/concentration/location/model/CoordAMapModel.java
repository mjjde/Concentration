package com.open_source.joker.concentration.location.model;

public class CoordAMapModel extends CoordModel {

    public CoordAMapModel(double lat, double lng, int acc, long elapse) {
        super(SOURCE_AMAP, lat, lng, acc, elapse);
    }

}
