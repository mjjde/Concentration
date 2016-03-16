package com.open_source.joker.concentration.location.scan;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.open_source.joker.concentration.location.model.CellCdmaModel;
import com.open_source.joker.concentration.location.model.CellGsmModel;
import com.open_source.joker.concentration.location.model.CellLteModel;
import com.open_source.joker.concentration.location.model.CellModel;
import com.open_source.joker.concentration.location.model.CellWcdmaModel;

import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class CellScanner extends BaseSacnner<CellModel> {

    private static String TAG = CellScanner.class.getSimpleName();

    private final TelephonyManager telephonyManager;

    public CellScanner(Context context) {
        super(context);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    @Override
    protected void onStartScan() {
        if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_READY) {
            mErrorMsg = "Sim state is not ready";
            CellModel cellModel = new CellModel(CellModel.TYPE_NO_SIM);
            mResultList.add(cellModel);
            return;
        }

        doCellScan();

    }

    private void doCellScan() {
        if (Build.VERSION.SDK_INT >= 17)
            doCellScanAboveApi17();
        doCellScanBelowApi17();
        if (mResultList.size() == 0)
            mErrorMsg = "No cell scan result";
    }


    @TargetApi(18)
    private void doCellScanAboveApi17() {
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();
        if (cellInfos == null || cellInfos.isEmpty())
            return;
        for (CellInfo info : cellInfos) {
            CellModel cellModel = null;

            if (info instanceof CellInfoGsm) {
                CellInfoGsm gsmCellInfo = (CellInfoGsm) info;
                CellIdentityGsm cellIdentityGsm = gsmCellInfo.getCellIdentity();

                cellModel = new CellGsmModel(cellIdentityGsm.getMcc(), cellIdentityGsm.getMnc(),
                        cellIdentityGsm.getCid(), cellIdentityGsm.getLac(), 0);
            } else if (info instanceof CellInfoCdma) {
                CellInfoCdma cdmaCellInfo = (CellInfoCdma) info;
                CellIdentityCdma cellIdentityCdma = cdmaCellInfo.getCellIdentity();

                int mcc = getMccFromOperator();
                cellModel = new CellCdmaModel(mcc, cellIdentityCdma.getSystemId(),
                        cellIdentityCdma.getBasestationId(), cellIdentityCdma.getNetworkId(),
                        cellIdentityCdma.getLatitude(), cellIdentityCdma.getLongitude());
            } else if (info instanceof CellInfoLte) {
                CellInfoLte lteInfo = (CellInfoLte) info;
                CellIdentityLte lteCell = lteInfo.getCellIdentity();

                cellModel = new CellLteModel(lteCell.getMcc(), lteCell.getMnc(), lteCell.getCi(),
                        lteCell.getTac(), 0);
            } else if (Build.VERSION.SDK_INT >= 18) {
                if (info instanceof CellInfoWcdma) {
                    CellInfoWcdma wcdmaInfo = (CellInfoWcdma) info;
                    CellIdentityWcdma wcdmaCell = wcdmaInfo.getCellIdentity();

                    cellModel = new CellWcdmaModel(wcdmaCell.getMcc(), wcdmaCell.getMnc(),
                            wcdmaCell.getCid(), wcdmaCell.getLac(), 0);
                }
            }

            if (cellModel != null) {
                int mcc = cellModel.getMcc();
                int mnc = cellModel.getMncSid();
                int cid = cellModel.getCidBid();
                int lac = cellModel.getLacNid();

                boolean isValid = isDataValid(mcc) && isDataValid(mnc) && isDataValid(cid)
                        && isDataValid(lac) && (lac != 0);
                if (isValid) {
                    mResultList.add(cellModel);
                } else {
                    mErrorMsg = "invalid value";
                }
            }
        }
    }

    private void doCellScanBelowApi17() {
        CellLocation cellLocation = telephonyManager.getCellLocation();
        if (cellLocation == null) {
            mErrorMsg = "cellLocation is null";
            return;
        }
        if (cellLocation instanceof GsmCellLocation) {
            doGsmScanBelowApi17((GsmCellLocation) cellLocation);
        } else if (cellLocation instanceof CdmaCellLocation) {
            doCdmaScanBelowApi17((CdmaCellLocation) cellLocation);
        }
    }

    private void doGsmScanBelowApi17(GsmCellLocation cellLocation) {
        int mcc = getMccFromOperator();
        int mnc = getMncFromOperator();
        int cid = cellLocation.getCid();
        int lac = cellLocation.getLac();
        int asu = 0;

        boolean isValid = isDataValid(mcc) && isDataValid(mnc) && isDataValid(cid)
                && isDataValid(lac) && (lac != 0);
        if (!isValid) {
            mErrorMsg = "gsm invalid value";
            return;
        }

        CellModel cellModel = new CellGsmModel(mcc, mnc, cid, lac, asu);
        mResultList.add(cellModel);

        List<NeighboringCellInfo> neighboringCellInfos = telephonyManager.getNeighboringCellInfo();
        if (neighboringCellInfos != null && !neighboringCellInfos.isEmpty()) {
            for (NeighboringCellInfo cellInfo : neighboringCellInfos) {
                cid = cellInfo.getCid();
                lac = cellInfo.getLac();
                asu = cellInfo.getRssi();
                if ((cid != NeighboringCellInfo.UNKNOWN_CID)
                        && (lac != NeighboringCellInfo.UNKNOWN_CID)) {
                    cellModel = new CellGsmModel(mcc, mnc, cid, lac, asu);
                    mResultList.add(cellModel);
                }
            }
        }
    }

    private void doCdmaScanBelowApi17(CdmaCellLocation cellLocation) {
        int mcc = getMccFromOperator();
        int sid = cellLocation.getSystemId();
        int bid = cellLocation.getBaseStationId();
        int nid = cellLocation.getNetworkId();
        int lat = cellLocation.getBaseStationLatitude();
        int lng = cellLocation.getBaseStationLongitude();

        boolean isValid = isDataValid(mcc) && isDataValid(sid) && isDataValid(bid)
                && isDataValid(nid);
        if (!isValid) {
            mErrorMsg = "cdma invalid value";
            return;
        }

        CellModel cellModel = new CellCdmaModel(mcc, sid, bid, nid, lat, lng);
        mResultList.add(cellModel);
    }

    private int getMccFromOperator() {
        String operator = getOperator();

        if (isOperatorValid(operator)) {
            // mcc
            return Integer.parseInt(operator.substring(0, 3));
        }

        return -1;
    }

    private int getMncFromOperator() {
        String operator = getOperator();

        if (isOperatorValid(operator)) {
            // mnc
            return Integer.parseInt(operator.substring(3, operator.length()));
        }

        return -1;
    }

    private String getOperator() {
        String operator = telephonyManager.getNetworkOperator();
        if (!isOperatorValid(operator)) {
            operator = telephonyManager.getSimOperator();
        }
        return operator;
    }

    private boolean isOperatorValid(String operator) {
        boolean ret = false;

        if (operator != null) {
            int length = operator.length();
            // 5位或6位
            if ((length == 5) || (length == 6)) {
                ret = true;
            }
        }

        return ret;
    }

    private boolean isDataValid(int data) {
        if (data == Integer.MAX_VALUE) {
            return false;
        }

        return data >= 0;

    }
}
