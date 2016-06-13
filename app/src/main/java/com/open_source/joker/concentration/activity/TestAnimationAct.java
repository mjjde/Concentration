package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.view.View;

import com.open_source.joker.concentration.app.CONActivity;
import com.open_source.joker.concentration.util.PeriscopeLayout;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/5/9
 * 作者: joker
 */
public class TestAnimationAct extends CONActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PeriscopeLayout periscopeLayout = new PeriscopeLayout(this);
        setContentView(periscopeLayout);
        periscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periscopeLayout.addHeart();
            }
        });
    }

}
