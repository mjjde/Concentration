package com.open_source.joker.concentration.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.open_source.joker.concentration.IBinderPool;
import com.open_source.joker.concentration.service.BinderPoolService;

import java.util.concurrent.CountDownLatch;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：
 * 时间：16/2/29
 * 作者: joker
 */
public class ServiceManager {
    private final static String TAG = ServiceManager.class.getSimpleName();
    private IBinderPool mBinderPool;
    private CountDownLatch countDownLatch;
    private static Context mContext;
    private static ServiceManager serviceManager;

    IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mBinderPool = null;
            connectService();
        }
    };


    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private ServiceManager(Context context) {
        mContext = context.getApplicationContext();
        connectService();
    }

    public static ServiceManager instance() {
        if (serviceManager == null)
            synchronized (ServiceManager.class) {
                if (serviceManager == null)
                    serviceManager = new ServiceManager(mContext);
            }
        return serviceManager;
    }

    public void connectService() {
        countDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public IBinder getService(String name) {
        IBinder binder = null;
        try {
            binder = mBinderPool.queryBinder(name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

}
