package com.open_source.joker.concentration.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONApplication;

import java.lang.reflect.Field;

/**
 * 文件名：com.open_source.joker.concentration.widget
 * 描述：
 * 时间：16/2/24
 * 作者: joker
 */
public class DebugSmallView extends LinearLayout {

    public static int viewWidth;
    public static int viewHeight;
    int screenWidth;
    int screenHeight;

    int statusBarHeight;

    float xDownInScreen;
    float yDownInScreen;

    float xInScreen;
    float yInScreen;

    float xInView;
    float yInView;

    View layoutView;

    WindowManager windowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    public DebugSmallView(Context context) {
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.debug_small, this);
        layoutView = findViewById(R.id.small_window_layout);
        viewWidth = layoutView.getLayoutParams().width;
        viewHeight = layoutView.getLayoutParams().height;
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchBegin();
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                touchEnd();
                if (Math.abs(xDownInScreen - xInScreen) < 5
                        && Math.abs(yDownInScreen - yInScreen) < 5) {
                    openBigWindow();
                }
                break;
        }

        return true;

    }


    private void touchBegin() {
        layoutView.getBackground().setAlpha(255);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * 更新小悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        mParams.windowAnimations = 0;
        windowManager.updateViewLayout(this, mParams);
    }

    private void touchEnd() {
        layoutView.getBackground().setAlpha(80);

        int centerSceenX = screenWidth / 2;
        int x = (int) (xInScreen - xInView);
        int centerViewX = x + viewWidth / 2;
        if (centerViewX < centerSceenX) {
            x = 0;
        } else {
            x = screenWidth - viewWidth;
        }
        xInScreen = x + xInView;
        updateViewPosition();
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 打开大悬浮窗，同时关闭小悬浮窗。
     */
    public void openBigWindow() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("concentration://debug"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        CONApplication.instance().startActivity(intent);
    }

}
