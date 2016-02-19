package com.open_source.joker.concentration.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * 文件名：com.open_source.joker.concentration.widget
 * 描述：带滤镜
 * 时间：16/2/19
 * 作者: joker
 */
public class CustomImageButton extends ImageButton implements View.OnTouchListener {
    public CustomImageButton(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    public CustomImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public CustomImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            CustomImageButton.this.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            CustomImageButton.this.setColorFilter(null);
        }
        return false;
    }
}
