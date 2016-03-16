package com.open_source.joker.concentration.location.model;

public class CoordNetModel extends CoordModel {

    public CoordNetModel(double lat, double lng, int acc, long elapse) {
        super(SOURCE_NETWORK, lat, lng, acc, elapse);
    }

}
