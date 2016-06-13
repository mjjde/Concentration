package com.open_source.joker.concentration.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;
import com.open_source.joker.concentration.util.log.Log;

/**
 * 文件名：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/5/9
 * 作者: joker
 */
public class PropertyAct extends CONActivity {
    ImageView imageView;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        imageView = (ImageView)findViewById(R.id.imageView);
        findViewById(R.id.translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // roteView();
                multView();
            }
        });
    }

    private void roteView(){
        ObjectAnimator a = ObjectAnimator.ofFloat(imageView,"RotationY",0f,360f).setDuration(2000);
        //a.setInterpolator(new EaseCubicInterpolator(0.29f,0.74f,0.99f,0.06f));
        a.start();
    }

    private void multView(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(100,0);
        valueAnimator.setDuration(10000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                count+=1;
                Log.e("point="+animation.getAnimatedValue()+"-------"+count);
                imageView.setAlpha((Float) animation.getAnimatedValue());
                imageView.setScaleX((Float) animation.getAnimatedValue());
                imageView.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
