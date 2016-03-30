package com.open_source.joker.concentration.location.locate;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.open_source.joker.concentration.location.model.CoordGpsModel;
import com.open_source.joker.concentration.location.model.CoordModel;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class GpsLocalLocator extends LocalLocator {


    public GpsLocalLocator(Context context) {
        super(context);
    }

    @Override
    protected CoordModel locationToCoord(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        int acc = (int) location.getAccuracy();
        long elapse = location.getTime();

        return new CoordGpsModel(lat, lng, acc, elapse);
    }

    @Override
    protected String getProviderName() {
        return LocationManager.GPS_PROVIDER;
    }
}
