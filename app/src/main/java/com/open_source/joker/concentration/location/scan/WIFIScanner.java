package com.open_source.joker.concentration.location.scan;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.open_source.joker.concentration.location.model.WifiModel;

import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class WifiScanner extends BaseSacnner<WifiModel> {

    private final WifiManager mWifiManager;

    public WifiScanner(Context context) {
        super(context);
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    protected void onStartScan() {
        if (!mWifiManager.isWifiEnabled()) {
            // wifi没有打开
            mErrorMsg = "Wifi is not enabled";
            return;
        }

        doWifiScan();
    }

    private void doWifiScan() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        WifiModel curWifiModel = null;
        if (wifiInfo != null && !TextUtils.isEmpty(wifiInfo.getSSID())) {
            curWifiModel = new WifiModel(wifiInfo.getSSID(), wifiInfo.getBSSID(),
                    wifiInfo.getRssi());
        } else {
            curWifiModel = new WifiModel("", "", 0);
        }
        mResultList.add(curWifiModel);
        List<ScanResult> wifiScanners = mWifiManager.getScanResults();
        if (wifiScanners != null) {
            for (ScanResult scanResult : wifiScanners) {
                WifiModel wifiModel = new WifiModel(scanResult.SSID, scanResult.BSSID,
                        scanResult.level);
                if (wifiModel.equals(curWifiModel)) {
                    // 不重复加入已经连上的wifi
                    continue;
                }
                mResultList.add(wifiModel);
            }
        }

    }
}
