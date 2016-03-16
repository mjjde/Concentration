package com.open_source.joker.concentration.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.open_source.joker.concentration.IBinderPool;

/**
 * 文件名：com.open_source.joker.concentration.service
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class BinderPoolService extends Service {

    private IBinder binder = new IBinderPool.Stub() {

        @Override
        public IBinder queryBinder(String name) throws RemoteException {
            IBinder mIBinder = null;
            return mIBinder;
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
