package com.open_source.joker.concentration.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/5/9
 * 作者: joker
 */
public class FrameAnimaAct extends CONActivity {
    ImageView animationIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_anim);
        animationIV = (ImageView) findViewById(R.id.animationIV);
        findViewById(R.id.buttonA).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                animationIV.setImageResource(R.drawable.wifi_animation);
                AnimationDrawable  animationDrawable = (AnimationDrawable) animationIV.getDrawable();
                animationDrawable.start();
            }

        });
    }


}
