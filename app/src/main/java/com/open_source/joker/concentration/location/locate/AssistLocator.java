package com.open_source.joker.concentration.location.locate;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.open_source.joker.concentration.location.model.CellModel;
import com.open_source.joker.concentration.location.model.CoordModel;
import com.open_source.joker.concentration.location.model.WifiModel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public abstract class AssistLocator extends BaseLocator implements Callable<List<CoordModel>> {

    protected final List<CellModel> mCellModelList = new ArrayList<CellModel>();
    protected final List<WifiModel> mWifiModelList = new ArrayList<WifiModel>();

    protected ConnectivityManager mConnectManager;
    protected TelephonyManager mTelephonyManager;

    public AssistLocator(Context context, List<CellModel> cellModelList,
                         List<WifiModel> wifiModelList) {
        super(context);

        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mConnectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cellModelList != null) {
            mCellModelList.addAll(cellModelList);
        }

        if (wifiModelList != null) {
            mWifiModelList.addAll(wifiModelList);
        }
    }

    @Override
    public List<CoordModel> call() throws Exception {
        locate(null);
        return mResultList;
    }

    @Override
    protected void onStartLocate() {
        NetworkInfo networkInfo = mConnectManager.getActiveNetworkInfo();
        if ((networkInfo == null) || !networkInfo.isConnected()) {
            // 没有网络连接情况下不进行辅助定位
            mErrorMsg = "Network is not available";
            return;
        }

        doAssistLocate();
    }

    private void doAssistLocate() {
        try {
            URL url = new URL(getHttpPostUrl());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(new Proxy(java.net.Proxy.Type.HTTP, getProxy()));
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            OutputStream os = urlConnection.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.print(getHttpData(formatRequest()));
            pw.flush();
            pw.close();
            sendRequest(urlConnection);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected SocketAddress getProxy() {
        if (mConnectManager == null) {
            return null;
        }

        try {
            NetworkInfo activeNetInfo = mConnectManager.getActiveNetworkInfo();
            if (activeNetInfo == null) {
                return null;
            }
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return null;
            }
            if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = activeNetInfo.getExtraInfo();
                if (extraInfo == null) {
                    return null;
                }
                extraInfo = extraInfo.toLowerCase();
                if (extraInfo.contains("cmnet")) {
                    return null;
                }
                if (extraInfo.contains("cmwap")) {
                    return new InetSocketAddress("10.0.0.172", 80);
                }
                if (extraInfo.contains("3gnet")) {
                    return null;
                }
                if (extraInfo.contains("3gwap")) {
                    return new InetSocketAddress("10.0.0.172", 80);
                }
                if (extraInfo.contains("uninet")) {
                    return null;
                }
                if (extraInfo.contains("uniwap")) {
                    return new InetSocketAddress("10.0.0.172", 80);
                }
                if (extraInfo.contains("ctnet")) {
                    return null;
                }
                if (extraInfo.contains("ctwap")) {
                    return new InetSocketAddress("10.0.0.200", 80);
                }
                if (extraInfo.contains("#777")) {
                    Cursor c = mContext.getContentResolver().query(
                            Uri.parse("content://telephony/carriers/preferapn"),
                            new String[]{"proxy", "port"}, null, null, null);
                    int port = 0;
                    String host = "";
                    if (c.moveToFirst()) {
                        try {
                            port = Integer.parseInt(c.getString(1));
                            host = c.getString(0);
                        } catch (NumberFormatException e) {

                        } finally {
                            c.close();
                        }
                    } else
                        c.close();

                    if (host.length() > 3) {
                        return new InetSocketAddress(host, port > 0 ? port : 80);
                    }
                    return null;
                }
            }
        } catch (Exception e) {

        }

        return null;
    }


    protected abstract String getHttpPostUrl();

    protected abstract String formatRequest();

    protected abstract byte[] getHttpData(String request);

    protected abstract void sendRequest(HttpURLConnection urlConnection);
}

