package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;
import com.open_source.joker.concentration.location.geo.GeoListener;
import com.open_source.joker.concentration.location.geo.GeoServiceImpl;

import java.util.Map;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class LocationActivity extends CONActivity implements GeoListener {
    GeoServiceImpl loc;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        tv = (TextView)findViewById(R.id.result);
        loc = GeoServiceImpl.getInstance(this);
        loc.addListener(this);
        loc.requestGeoParams();
    }

    @Override
    public void onRequestGeoParamsFinish(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append("key:"+key+"  value:"+params.get(key)+"\n");
        }
        tv.setText(sb.toString());
    }
}
