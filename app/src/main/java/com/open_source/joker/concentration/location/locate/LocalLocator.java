package com.open_source.joker.concentration.location.locate;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.open_source.joker.concentration.location.model.CoordModel;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public abstract class LocalLocator extends BaseLocator implements LocationListener {

    private LocationManager locationManager;
    private LocationProvider provider;
    private Handler mMonitorHandler;
    private Location mCurrentBestLocation;

    private static final int HALF_MINUTES = 30 * 1000;
    private final int TIMEOUT = 1000;
    private final int MESSAGE_TIMEOUT = 10;

    public LocalLocator(Context context) {
        super(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            provider = locationManager.getProvider(getProviderName());
        } catch (Exception e) {
            provider = null;
        }

        mMonitorHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.what;
                switch (code) {
                    case MESSAGE_TIMEOUT:
                        finishLocate();
                        break;
                    default:
                        break;
                }
            }
        };

    }

    @Override
    protected void onStartLocate() {
        if (!hasProvider()) {
            mErrorMsg = getProviderName() + " provider is null or disabled";
            notifyLocateFinish();
            return;
        }

        doLocalLocate();
    }

    public void doLocalLocate() {
        mMonitorHandler.sendEmptyMessageDelayed(MESSAGE_TIMEOUT, TIMEOUT);
        try {
            mCurrentBestLocation = locationManager.getLastKnownLocation(getProviderName());
            locationManager.requestLocationUpdates(getProviderName(), 0, 0, this);
        } catch (SecurityException e) {
            finishLocate();
        }

    }

    private void finishLocate() {
        onLocateFinish();

        if (mCurrentBestLocation != null) {
            // 定位成功
            CoordModel coord = locationToCoord(mCurrentBestLocation);
            mResultList.add(coord);
        } else {
            // 定位失败
            mErrorMsg = getProviderName() + " locate timeout";
        }

        notifyLocateFinish();
    }

    private void onLocateFinish() {
        mMonitorHandler.removeMessages(MESSAGE_TIMEOUT);

        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (isBetterLocation(location, mCurrentBestLocation)) {
            // 使用新的位置信息
            mCurrentBestLocation = location;
        }

        finishLocate();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * 决定新的位置信息是否比当前的修正位置信息更可靠.
     *
     * @param location            新的位置信息
     * @param currentBestLocation 当前的修正位置信息
     */
    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > HALF_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -HALF_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than half minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than half minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate) {
            return true;
        }
        return false;
    }

    private boolean hasProvider() {
        if (provider == null)
            return false;

        try {
            if (!locationManager.isProviderEnabled(getProviderName()))
                return false;
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    protected abstract String getProviderName();

    protected abstract CoordModel locationToCoord(Location location);
}
