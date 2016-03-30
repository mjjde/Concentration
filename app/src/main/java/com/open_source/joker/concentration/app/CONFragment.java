package com.open_source.joker.concentration.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.open_source.joker.concentration.util.CrashReportHelper;
import com.open_source.joker.concentration.util.DialogUtils;
import com.open_source.joker.concentration.util.OtherUtils;
import com.open_source.joker.concentration.widget.BeautifulProgressDialog;

/**
 * 文件名：com.open_source.joker.concentration.app
 * 描述：
 * 时间：16/2/17
 * 作者: joker
 */
public class CONFragment extends Fragment {

    private static final String TAG = CONFragment.class.getSimpleName();
    protected CONActivity conActivity;

    @Override
    public void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof CONActivity)) {
            throw new IllegalArgumentException("DSFragment must attach to DSActivity!");
        }
        conActivity = (CONActivity) activity;
    }

    public boolean isActivityFinish() {
        return conActivity == null || conActivity.isFinishing();
    }

    @Override
    public void startActivity(Intent intent) {
        if (isAdded()) {
            super.startActivity(intent);
        } else {
            Log.e(TAG,
                    "startActivity java.lang.IllegalStateException: Fragment xxx not attached to Activity ");
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (isAdded()) {
            if (intent == null) {
                return;
            }
            String url = intent.getDataString();
            CrashReportHelper.putUrlSchema(url);
            super.startActivityForResult(intent, requestCode);
        } else {
            Log.e(TAG,
                    "startActivity java.lang.IllegalStateException: Fragment xxx not attached to Activity ");
        }
    }

    public void startActivity(String urlSchema) {
        startActivityForResult(urlSchema, -1);
    }

    public void startActivityForResult(String urlSchema, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema));
        startActivityForResult(intent, requestCode);
    }

    public void setResult(int resultCode, Intent data) {
        getActivity().setResult(resultCode, data);
    }

    public void setResult(int resultCode) {
        setResult(resultCode, null);
    }

    /**
     * ##############################   广播  ##############################
     */
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return getActivity().registerReceiver(receiver, filter);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter,
                                   String broadcastPermission, Handler scheduler) {
        return getActivity().registerReceiver(receiver, filter, broadcastPermission, scheduler);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        getActivity().unregisterReceiver(receiver);
    }

    /**
     * ##############################   View相关  ##############################
     */
    protected AlertDialog alertDialog;
    private BeautifulProgressDialog progressDialog;
    private Toast toast;

    public void showSuccessToastContentView(Context context, int toastTop, int duration, String message) {
        if (context == null || (context instanceof Activity && ((Activity) context).isFinishing())) {
            return;
        }
        OtherUtils.showSuccessToastContentView(context, toastTop, duration, message);
    }

    public void showWrongToastContentView(Context context, int toastTop, int duration, String message) {
        if (context == null || (context instanceof Activity && ((Activity) context).isFinishing())) {
            return;
        }
        OtherUtils.showWrongToastContentView(context, toastTop, duration, message);
    }

    public void showShortToast(String message) {
        if (isActivityFinish()) {
            return;
        }
        Toast.makeText(conActivity, message, Toast.LENGTH_SHORT).show();
    }

    protected void onProgressDialogCancel() {

    }

    public void showAlert(String message) {
        showAlert("提示", message, false, null, null);
    }

    public void showAlert(String title, String message, boolean hasCancelBtn, DialogInterface.OnClickListener lOk,
                          DialogInterface.OnClickListener lCancel) {
        if (isActivityFinish()) {
            return;
        }

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
        if (lOk == null) {
            lOk = listener;
        }
        if (lCancel == null) {
            lCancel = listener;
        }
        AlertDialog dlg = null;
        if (!hasCancelBtn) {
            dlg = DialogUtils.showAlert(conActivity, message, title, "确定", false, lOk);
        } else {
            dlg = DialogUtils.showAlert(conActivity, message, title, "确定", "取消", false, lOk, lCancel);
        }
        if (dlg != null) {
            dlg.show();
        }
    }

    public void showProgressDialog() {
        showProgressDialog("请稍候...");
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = DialogUtils.showProgressDialog(getActivity(), msg,
                    new DialogInterface.OnCancelListener() {

                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onProgressDialogCancel();

                        }
                    }, true);
        } else {
            progressDialog.setMessage(msg);
        }

        progressDialog.show();
    }

    public void dismissDialog() {
        if ((alertDialog != null) && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        dismissProgressDialog();

    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public interface onProgressDialogCancelListener {
        void onProgressDialogCancel();
    }

    public Dialog getDialog() {
        return alertDialog;
    }

    public void showAlertDialog(String title,
                                DialogInterface.OnClickListener onPositiveButtonClickListener) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setMessage(title).setCancelable(false).setPositiveButton("确定",
                onPositiveButtonClickListener).setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlertDialog(String message) {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("提示").setMessage(message).setCancelable(false).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void showToast(String msg) {
        if (getActivity() == null) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * ##############################   获取参数快捷方法  ##############################
     */
    public int getIntParam(String name, int defaultValue) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                String val = bundle.getString(name);
                return Integer.parseInt(val);
            } catch (Exception e) {
            }
            return bundle.getInt(name, defaultValue);
        }
        return defaultValue;
    }

    public int getIntParam(String name) {
        return getIntParam(name, 0);
    }

    public boolean getBooleanParam(String name, boolean defaultValue) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(name)) {
            String val = bundle.getString(name);
            if (val != null)
                return "true".equalsIgnoreCase(val) || "1".equals(val);
            return bundle.getBoolean(name);
        }
        return defaultValue;
    }

    public boolean getBooleanParam(String name) {
        return getBooleanParam(name, false);
    }

    public String getStringParam(String name, String defaultValue) {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(name)) {
            return bundle.getString(name);
        }
        return defaultValue;
    }

    public String getStringParam(String name) {
        return getStringParam(name, null);
    }

    public double getDoubleParam(String name, double defaultValue) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            try {
                String val = bundle.getString(name);
                return Double.parseDouble(val);
            } catch (Exception e) {
            }
            return bundle.getDouble(name, defaultValue);
        }
        return defaultValue;
    }

    public double getDoubleParam(String name) {
        return getDoubleParam(name, 0);
    }


    public SharedPreferences preferences(Context c) {
        return c.getSharedPreferences(c.getPackageName(), Context.MODE_APPEND);
    }


}
