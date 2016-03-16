package com.open_source.joker.concentration.location.model;

public class CoordBMapModel extends CoordModel {

    public CoordBMapModel(double lat, double lng, int acc, long elapse) {
        super(SOURCE_BMAP, lat, lng, acc, elapse);
    }

}
