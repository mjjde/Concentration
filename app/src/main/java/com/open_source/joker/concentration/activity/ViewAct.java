package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;

/**
 *     ：com.open_source.joker.concentration.activity
 * 描述：
 * 时间：16/5/4
 * 作者: joker
 */
public class ViewAct extends CONActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        imageView = (ImageView) findViewById(R.id.imageView);
        initView();
    }

    public void initView(){
        findViewById(R.id.translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(ViewAct.this, R.anim.tran_animation);
                //imageView.startAnimation(animation);
                imageView.startAnimation(createAn());
            }
        });
    }

    private AnimationSet createAn(){
        AnimationSet set = new AnimationSet(true);
        AlphaAnimation a = new AlphaAnimation(1f,0f);
        a.setDuration(2000);
        a.setRepeatMode(Animation.REVERSE);
        a.setRepeatCount(1);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Toast.makeText(ViewAct.this,"onAnimationStart",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(ViewAct.this,"onAnimationEnd",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(ViewAct.this,"onAnimationRepeat",Toast.LENGTH_SHORT).show();
            }
        });
        set.addAnimation(a);
        return set;
    }



}
