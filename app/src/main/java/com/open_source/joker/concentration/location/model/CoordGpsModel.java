package com.open_source.joker.concentration.location.model;

public class CoordGpsModel extends CoordModel {

    public CoordGpsModel(double lat, double lng, int acc, long elapse) {
        super(SOURCE_GPS, lat, lng, acc, elapse);
    }

}
