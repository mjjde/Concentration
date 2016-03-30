package com.open_source.joker.concentration.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.open_source.joker.concentration.widget.BeautifulProgressDialog;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class DialogUtils {


    public static BeautifulProgressDialog showProgressDialog(Context context, String message,
                                                             DialogInterface.OnCancelListener listener, boolean cancelable) {
        BeautifulProgressDialog progressDialog = new BeautifulProgressDialog(context);
        progressDialog.setCancelable(cancelable);
        progressDialog.setOnCancelListener(listener);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title, final String yes,
                                        final boolean cancelable, final DialogInterface.OnClickListener lOk) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(yes, lOk);
        builder.setCancelable(cancelable);
        return builder.create();
    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title, final String yes,
                                        final String no, final boolean cancelable, @NonNull final DialogInterface.OnClickListener lOk,
                                        @NonNull final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(yes, lOk);
        builder.setNegativeButton(no, lCancel);
        builder.setCancelable(cancelable);
        return builder.create();
    }
}
