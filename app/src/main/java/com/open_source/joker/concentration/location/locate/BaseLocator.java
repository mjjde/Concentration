package com.open_source.joker.concentration.location.locate;

import android.content.Context;
import android.text.TextUtils;

import com.open_source.joker.concentration.location.model.CoordModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：com.open_source.joker.concentration.location.locate
 * 描述：
 * 时间：16/3/1
 * 作者: joker
 */
public abstract class BaseLocator implements Locator {

    protected final Context mContext;

    /**
     * 定位结果
     */
    protected final List<CoordModel> mResultList = new ArrayList<CoordModel>();

    protected long mElapse;

    protected String mErrorMsg = "";

    private LocateListener mListener;

    public BaseLocator(Context context) {
        mContext = context;
    }

    @Override
    public void locate(LocateListener listener) {
        mResultList.clear();
        mErrorMsg = "";
        mListener = listener;
        startLocate();
    }


    private void startLocate() {
        // 记录真正定位前的时间
        mElapse = System.currentTimeMillis();
        onStartLocate();
    }

    protected void notifyLocateFinish() {
        calculateElapse();
        if (!TextUtils.isEmpty(mErrorMsg)) {

        }

        if (hasListener()) {
            mListener.onLocateFinish(mResultList);
        }
    }

    private void calculateElapse() {
        mElapse = System.currentTimeMillis() - mElapse;
    }

    private boolean hasListener() {
        return (mListener != null);
    }


    protected abstract void onStartLocate();



}
