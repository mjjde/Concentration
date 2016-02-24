package com.open_source.joker.concentration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/2/17
 * 作者: joker
 */
public class SplashActivity extends CONActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        hideToolbar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent("com.open_source.joker.concentration.activity.MainActivity"));
                finish();
            }
        }, 2000);
    }
}
