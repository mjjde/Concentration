package com.open_source.joker.concentration.util;

import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：调用SafeLooper.install()，主消息循环会被接管，所有的消息会运行在一个嵌套的子消息循环中<br>
 * 一旦崩溃，getUncaughtExceptionHandler()将会接收到崩溃的消息，但是主消息循环会继续执行。<br>
 * 由于同步机制，post一个无限循环的Runnable,主线程的MessageQueue.next(),不会被调用，才有机会被接管。
 * 时间：16/2/22
 * 作者: joker
 */
public class SafeLooper implements Runnable {

    private static final Object EXIT = new Object();
    private static Handler handler = new Handler(Looper.getMainLooper());
    private boolean isInstall;
    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static void install() {
        handler.removeMessages(0, EXIT);
        handler.post(new SafeLooper());
    }

    public static void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler h) {
        uncaughtExceptionHandler = h;
    }

    @Override
    public void run() {
        if (isInstall)
            return;
        if (Looper.myLooper() != Looper.getMainLooper())
            return;

        Method next = null;
        Field target = null;
        Method recycleUnchecked = null;

        try {
            Method m = MessageQueue.class.getDeclaredMethod("next");
            m.setAccessible(true);
            next = m;
            Field f = Message.class.getDeclaredField("target");
            f.setAccessible(true);
            target = f;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Method method = Message.class.getDeclaredMethod("recycleUnchecked");
                method.setAccessible(true);
                recycleUnchecked = method;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        isInstall = true;
        MessageQueue queue = Looper.myQueue();
        Binder.clearCallingIdentity();

        while (true) {
            try {
                Message msg = (Message) next.invoke(queue);
                if (msg == null || msg.obj == EXIT)
                    break;
                Handler h = (Handler) target.get(msg);
                h.dispatchMessage(msg);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (recycleUnchecked != null) {
                        recycleUnchecked.invoke(msg);
                    }
                } else {
                    msg.recycle();
                }
            } catch (Exception e) {
                if (Environment.isDebug()) {
                    Thread.UncaughtExceptionHandler h = Thread.getDefaultUncaughtExceptionHandler();
                    if (h != null) {
                        h.uncaughtException(Thread.currentThread(), e);
                    }
                    break;
                } else {
                    Thread.UncaughtExceptionHandler h = uncaughtExceptionHandler;
                    Throwable ex = e;
                    if (e instanceof InvocationTargetException) {
                        ex = ((InvocationTargetException) e).getCause();
                        if (ex == null) {
                            ex = e;
                        }
                    }
                    e.printStackTrace(System.err);
                    if (h != null) {
                        h.uncaughtException(Thread.currentThread(), ex);
                    }
                    new Handler().post(this);
                    break;
                }
            }
        }
    }
}
