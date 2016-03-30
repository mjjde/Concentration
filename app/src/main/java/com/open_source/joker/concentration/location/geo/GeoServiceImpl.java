package com.open_source.joker.concentration.location.geo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.open_source.joker.concentration.location.locate.BMapLocator;
import com.open_source.joker.concentration.location.locate.GpsLocalLocator;
import com.open_source.joker.concentration.location.locate.LocateListener;
import com.open_source.joker.concentration.location.locate.NetworkLocateLocator;
import com.open_source.joker.concentration.location.model.CellModel;
import com.open_source.joker.concentration.location.model.CoordModel;
import com.open_source.joker.concentration.location.model.WifiModel;
import com.open_source.joker.concentration.location.scan.CellScanner;
import com.open_source.joker.concentration.location.scan.WifiScanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件名：com.open_source.joker.concentration.location.geo
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class GeoServiceImpl implements GeoService, LocateListener {

    private static GeoServiceImpl sInstance;

    private Context mContext;

    GpsLocalLocator mGpsLocator;
    NetworkLocateLocator mNetworkLocator;

    private final List<CellModel> mCellList = new ArrayList<CellModel>();
    private final List<WifiModel> mWifiList = new ArrayList<WifiModel>();
    private final List<CoordModel> mCoordList = new ArrayList<CoordModel>();

    private final List<GeoListener> mListenerList = new ArrayList<GeoListener>();

    private final Map<String, String> mGeoParams = new HashMap<String, String>();

    private static final int CELL_SCAN_COUNT = 3;
    private static final int CELL_SCAN_TIMEOUT = 400;
    private static final int WIFI_SCAN_TIMEOUT = 500;
    private static final int ASSIST_LOCATE_TIMEOUT = 1000;

    private final static int MSG_SCAN_FINISH = 1001;
    private final static int MSG_REQUEST_GEO_PARAMS_FINISH = 1002;

    AtomicInteger mTaskCounter;
    LocTimeRecorder mLocTimeRecorder;

    private volatile boolean mIsLocating = false;

    Handler mMainHandler;

    public static synchronized GeoServiceImpl getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new GeoServiceImpl(context);
        }

        return sInstance;
    }

    private GeoServiceImpl(Context context) {
        mContext = context;

        mGpsLocator = new GpsLocalLocator(context);
        mNetworkLocator = new NetworkLocateLocator(context);

        mMainHandler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_SCAN_FINISH:
                        onScanEnd();
                        break;

                    case MSG_REQUEST_GEO_PARAMS_FINISH:
                        notifyListeners();
                        break;

                    default:
                        break;
                }
            }
        };

        mTaskCounter = new AtomicInteger(0);
        mLocTimeRecorder = new LocTimeRecorder();
    }

    private void onScanEnd() {
        locate();
        assistLocate();
    }

    private void locate() {
        mLocTimeRecorder.initLocalLocElapse();
        mGpsLocator.locate(this);
        mNetworkLocator.locate(this);
    }

    private void assistLocate() {
        new Thread() {
            @Override
            public void run() {
                ExecutorService executorService = Executors.newSingleThreadExecutor();

                FutureTask<List<CoordModel>> bMapLocTask = new FutureTask<List<CoordModel>>(
                        new BMapLocator(mContext, mCellList, mWifiList));
                executorService.submit(bMapLocTask);

                try {
                    List<CoordModel> coordModelList = bMapLocTask.get(ASSIST_LOCATE_TIMEOUT,
                            TimeUnit.MILLISECONDS);
                    if (coordModelList != null) {
                        synchronized (mCoordList) {
                            mCoordList.addAll(coordModelList);
                        }
                    }
                } catch (InterruptedException e) {

                } catch (ExecutionException e) {

                } catch (TimeoutException e) {

                }

                executorService.shutdown();
                mTaskCounter.decrementAndGet();
                checkLocateEnd();
            }
        }.start();
    }


    @Override
    public boolean addListener(GeoListener listener) {
        return mListenerList.add(listener);
    }

    @Override
    public boolean removeListener(GeoListener listener) {
        return mListenerList.remove(listener);
    }

    @Override
    public void requestGeoParams() {
        if (mIsLocating)
            return;
        mIsLocating = true;
        onRequestGeoParams();
    }

    @Override
    public void onLocateFinish(List<CoordModel> resultList) {
        synchronized (mCoordList) {
            mCoordList.addAll(resultList);
        }
        mTaskCounter.decrementAndGet();
        checkLocateEnd();
    }

    private void onRequestGeoParams() {
        clearData();
        scan();
    }

    private void clearData() {
        synchronized (mCellList) {
            mCellList.clear();
        }

        synchronized (mWifiList) {
            mWifiList.clear();
        }

        synchronized (mCoordList) {
            mCoordList.clear();
        }

        mLocTimeRecorder.resetAll();

        mTaskCounter.set(5);
    }

    private void scan() {
        new Thread() {

            @Override
            public void run() {
                cellScan();
            }

        }.start();

        new Thread() {

            @Override
            public void run() {
                wifiScan();
            }

        }.start();
    }

    private void cellScan() {
        mLocTimeRecorder.initCellScanElapse();
        ExecutorService executorService = Executors.newFixedThreadPool(CELL_SCAN_COUNT);
        for (int i = 1; i <= CELL_SCAN_COUNT; i++) {
            FutureTask<List<CellModel>> cellScanTask = new FutureTask<List<CellModel>>(
                    new CellScanner(mContext));
            executorService.submit(cellScanTask);

            List<CellModel> cellList = null;
            try {
                cellList = cellScanTask.get(CELL_SCAN_TIMEOUT, TimeUnit.MILLISECONDS);
                if (cellList == null) {
                    continue;
                }

                if (cellList.isEmpty()) {
                    continue;
                }

                synchronized (mCellList) {
                    mCellList.addAll(cellList);
                    break;
                }
            } catch (InterruptedException e) {

            } catch (ExecutionException e) {

            } catch (TimeoutException e) {

            }
        }
        executorService.shutdown();

        mTaskCounter.decrementAndGet();
        mLocTimeRecorder.calCellScanElapse();
        checkScanEnd();
    }

    private void wifiScan() {
        mLocTimeRecorder.initWifiScanElapse();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        FutureTask<List<WifiModel>> wifiScanTask = new FutureTask<List<WifiModel>>(new WifiScanner(
                mContext));
        executorService.submit(wifiScanTask);

        try {
            List<WifiModel> wifiList = wifiScanTask.get(WIFI_SCAN_TIMEOUT, TimeUnit.MILLISECONDS);
            if (wifiList != null) {
                synchronized (mWifiList) {
                    mWifiList.addAll(wifiList);
                }
            }
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        } catch (TimeoutException e) {

        }

        executorService.shutdown();
        mTaskCounter.decrementAndGet();
        mLocTimeRecorder.calWifiScanElapse();
        checkScanEnd();
    }


    private void onLocateEnd() {
        synchronized (mGeoParams) {
            mGeoParams.clear();

            synchronized (mCellList) {
                mGeoParams.put(GeoKey.K_NO_SIM_INFO, CellModel.listToNoSimString(mCellList));
                mGeoParams.put(GeoKey.K_GSM_INFO, CellModel.listToGsmString(mCellList));
                mGeoParams.put(GeoKey.K_CDMA_INFO, CellModel.listToCdmaString(mCellList));
                mGeoParams.put(GeoKey.K_LTE_INFO, CellModel.listToLteString(mCellList));
                mGeoParams.put(GeoKey.K_WCDMA_INFO, CellModel.listToWcdmaString(mCellList));
            }

            synchronized (mWifiList) {
                mGeoParams.put(GeoKey.K_WIFI_INFO, WifiModel.listToString(mWifiList));
            }

            synchronized (mCoordList) {
                mGeoParams.put(GeoKey.K_COORD_GPS, CoordModel.listToGpsString(mCoordList));
                mGeoParams.put(GeoKey.K_COORD_NETWORK, CoordModel.listToNetworkString(mCoordList));
                mGeoParams.put(GeoKey.K_COORD_BMAP, CoordModel.listToBMapString(mCoordList));
            }

            mGeoParams.put(GeoKey.K_CELL_SCAN_ELAPSE,
                    String.valueOf(mLocTimeRecorder.getCellScanElapse()));
            mGeoParams.put(GeoKey.K_WIFI_SCAN_ELAPSE,
                    String.valueOf(mLocTimeRecorder.getWifiScanElapse()));
            mGeoParams.put(GeoKey.K_LOCAL_LOC_ELAPSE,
                    String.valueOf(mLocTimeRecorder.getLocalLocElapse()));
        }

        mIsLocating = false;
        mMainHandler.sendEmptyMessage(MSG_REQUEST_GEO_PARAMS_FINISH);
    }

    private void notifyListeners() {
        synchronized (mGeoParams) {
            for (GeoListener listener : mListenerList) {
                listener.onRequestGeoParamsFinish(mGeoParams);
            }
        }
    }

    private void checkScanEnd() {
        if (mTaskCounter.get() == 3) {
            mMainHandler.sendEmptyMessage(MSG_SCAN_FINISH);
        }
    }

    private void checkLocateEnd() {
        if (mTaskCounter.get() == 0) {
            mLocTimeRecorder.calLocalLocElapse();
            onLocateEnd();
        }
    }
}
