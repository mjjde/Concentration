package com.open_source.joker.concentration.app;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.constant.ToolBarStyle;
import com.open_source.joker.concentration.util.ToolBarHelper;

/**
 * 文件名：com.open_source.joker.concentration.app
 * 描述：
 * 时间：16/2/17
 * 作者: joker
 */
public class CONActivity extends AppCompatActivity {

    private ToolBarHelper mToolBarHelper;
    private Toolbar toolbar;
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mToolBarHelper = new ToolBarHelper(this, layoutResID);
        toolbar = mToolBarHelper.getToolBar();
        toolbar.setContentInsetsRelative(0, 0);
        setContentView(mToolBarHelper.getContentView());
        setSupportActionBar(toolbar);
        onCreateCustomToolBar(ToolBarStyle.TITLE_TYPE_STANDARD);
        setLeftViewClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftTitleButtonClicked();
            }
        });
        setTitle(getTitle());
    }

    public void onCreateCustomToolBar(int flag) {
        int menuId = 0;
        switch (flag) {
            case ToolBarStyle.TITLE_TYPE_STANDARD:
                menuId = R.layout.standard_title_bar;
                break;
        }
        getLayoutInflater().inflate(menuId, toolbar);
        mToolBarHelper.onFinishInflate();
    }

    public void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (toolbar != null)
            mToolBarHelper.setTitle(title);
    }


    public void setSubtitle(CharSequence subTitle) {
        if (toolbar != null)
            mToolBarHelper.setSubTitle(subTitle);
    }

    public void setDoubleLineTitle(CharSequence title, CharSequence subTitle) {
        setTitle(title);
        setSubtitle(subTitle);
    }

    public void setLeftViewClick(View.OnClickListener l) {
        if (toolbar != null)
            mToolBarHelper.setLeftClick(l);
    }

    protected void onLeftTitleButtonClicked() {
        onBackPressed();
    }


}
