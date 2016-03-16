package com.open_source.joker.concentration.location.locate;

import android.content.Context;

import com.open_source.joker.concentration.location.model.CellModel;
import com.open_source.joker.concentration.location.model.CoordBMapModel;
import com.open_source.joker.concentration.location.model.CoordModel;
import com.open_source.joker.concentration.location.model.WifiModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public class BMapLocator extends AssistLocator {

    private static final String B_LOC_URL = "http://loc.map.baidu.com/sdk.php";

    public BMapLocator(Context context, List<CellModel> cellModelList, List<WifiModel> wifiModelList) {
        super(context, cellModelList, wifiModelList);
    }

    @Override
    protected String getHttpPostUrl() {
        return B_LOC_URL;
    }

    @Override
    protected String formatRequest() {
        StringBuilder builder = new StringBuilder();

        // 当前的cell
        builder.append("&cl=");
        if (mCellModelList.isEmpty()) {
            // 空cell
            builder.append("0|0|-1|-1");
        } else {
            // 实际的cell
            CellModel curCellModel = mCellModelList.get(0);
            builder.append(curCellModel.getMcc());
            builder.append("|");
            builder.append(curCellModel.getMncSid());
            builder.append("|");
            builder.append(curCellModel.getLacNid());
            builder.append("|");
            builder.append(curCellModel.getCidBid());

            // 所有的cell
            builder.append("&clt=");
            for (CellModel cellModel : mCellModelList) {
                builder.append(cellModel.getMcc());
                builder.append("|");
                builder.append(cellModel.getMncSid());
                builder.append("|");
                builder.append(cellModel.getLacNid());
                builder.append("|");
                builder.append(cellModel.getCidBid());
                builder.append("|");
                builder.append("1");
                builder.append(";");
            }
            builder.append("10");
        }

        if (!mWifiModelList.isEmpty()) {
            builder.append("&wf=");
            for (WifiModel wifiModel : mWifiModelList) {
                builder.append(wifiModel.getBssid().replaceAll(":", ""));
                builder.append(";");
                builder.append(Math.abs(wifiModel.getLevel()));
                builder.append(";");
                builder.append("|");
            }

            builder.setLength(builder.length() - 1);
        }

        builder.append("&addr=detail&coor=gcj02&os=android&prod=default");

        try {
            String deviceId = mTelephonyManager.getDeviceId();
            builder.append("&im=");
            builder.append(deviceId);
        } catch (Exception e) {

        }

        String req = builder.toString();
        req = BMapDigester.digest(req) + "|tp=2";

        return req;
    }

    @Override
    protected byte[] getHttpData(String request) {
        try {
            String data = "bloc=" + URLEncoder.encode(request, "utf-8");
            return data.getBytes();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    protected void sendRequest(HttpURLConnection urlConnection) {
        try {

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = urlConnection.getInputStream();
                // 创建字节输出流对象
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                // 定义读取的长度
                int len = 0;
                // 定义缓冲区
                byte buffer[] = new byte[1024];
                // 按照缓冲区的大小，循环读取
                while ((len = is.read(buffer)) != -1) {
                    // 根据读取的长度写入到os对象中
                    baos.write(buffer, 0, len);
                }
                // 释放资源
                is.close();
                baos.close();
                // 返回字符串
                String result = new String(baos.toByteArray());
                try {
                    JSONObject json = new JSONObject(result);

                    int statusCode = json.getJSONObject("result").getInt("error");
                    if (statusCode == 167) {
                        return;
                    }

                    double lat = json.getJSONObject("content").getJSONObject("point").getDouble("y");
                    lat = CommonUtil.format(lat, 5);
                    double lng = json.getJSONObject("content").getJSONObject("point").getDouble("x");
                    lng = CommonUtil.format(lng, 5);
                    int acc = (int) json.getJSONObject("content").getDouble("radius");

                    CoordModel coordModel = new CoordBMapModel(lat, lng, acc,
                            System.currentTimeMillis());
                    mResultList.add(coordModel);
                } catch (JSONException e) {

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
