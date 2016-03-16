package com.open_source.joker.concentration.location.scan;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 文件名：com.open_source.joker.concentration.location
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public abstract class BaseSacnner<T> implements Callable<List<T>>, Scanner {

    private Context mContext;
    /**
     * 扫描结果
     */
    protected final List<T> mResultList = new ArrayList<T>();
    protected String mErrorMsg = "";

    protected Long mElapse;

    public BaseSacnner(Context context) {
        this.mContext = context;
    }

    @Override
    public List<T> call() {
        scan();
        return mResultList;
    }

    @Override
    public void scan() {
        mResultList.clear();
        mErrorMsg = "";

        startScan();
        calculateElapse();
    }

    private void startScan() {
        mElapse = System.currentTimeMillis();
        onStartScan();
    }

    private void calculateElapse() {
        mElapse = System.currentTimeMillis() - mElapse;

    }

    protected abstract void onStartScan();
}
