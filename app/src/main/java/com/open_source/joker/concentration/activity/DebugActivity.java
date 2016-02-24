package com.open_source.joker.concentration.activity;

import android.os.Bundle;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/2/24
 * 作者: joker
 */
public class DebugActivity extends CONActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
    }
}
