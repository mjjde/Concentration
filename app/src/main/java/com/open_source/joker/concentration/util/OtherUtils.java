package com.open_source.joker.concentration.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.open_source.joker.concentration.R;

/**
 * 文件名：com.open_source.joker.concentration.util
 * 描述：
 * 时间：16/3/17
 * 作者: joker
 */
public class OtherUtils {
    public static void copy2ClipBoard(final Context context, String content) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(content);
    }

    private static void showToastContentView(Context context, View toastContentView, int toastTop,
                                             int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        if (toastTop == 0)
            toast.setGravity(Gravity.CENTER, 0, 0);
        else
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, toastTop);
        toast.setView(toastContentView);
        toast.show();
    }

    public static void showSuccessToastContentView(Context context, int toastTop, int duration,
                                                   String message) {
        View toastContentView = LayoutInflater.from(context).inflate(
                R.layout.toast_content_view_success, null);
        if (toastContentView != null) {
            TextView messageTextView = (TextView) toastContentView.findViewById(R.id.message_text_view);
            messageTextView.setText(message);

            showToastContentView(context, toastContentView, toastTop, duration);
        }
    }

    public static void showWrongToastContentView(Context context, int toastTop, int duration,
                                                 String message) {
        View toastContentView = LayoutInflater.from(context).inflate(
                R.layout.toast_content_view_wrong, null);
        if (toastContentView != null) {
            TextView messageTextView = (TextView) toastContentView.findViewById(R.id.message_text_view);
            messageTextView.setText(message);

            showToastContentView(context, toastContentView, toastTop, duration);
        }
    }

    public static boolean checkPhoneNo(@Nullable String phoneNo){
//        return !(TextUtils.isEmpty(phoneNo) || !phoneNo.startsWith("1") || !phoneNo.matches("^[0-9]*$") || phoneNo.length() != 11);
        return !TextUtils.isEmpty(phoneNo);
    }

    @Deprecated
    public static String setPhoneNo(@Nullable String phoneNo){
        if (phoneNo != null && phoneNo.length() == 11) {
            StringBuffer strb = new StringBuffer(phoneNo);
            return strb.substring(0, 3) + "****" + strb.substring(7);
        }
        return "";
    }

    /**
     * 各种长度的国际手机号加密
     * @param number
     * @return
     */
    public static String encryptPhoneNumber(String number) {
        if (number == null || number.length() == 0)
            return "--";
        if (number.length() > 4) {
            int length = number.length();
            String result = "";
            int encryptLength = length - 4;
            if (encryptLength > 4) {
                encryptLength = 4;
            }
            int prefixLength = length - 4 - encryptLength;
            if (prefixLength > 0) {
                result += number.substring(0, prefixLength);
            }
            for (int i = 0; i < encryptLength; i++) {
                result += "*";
            }
            return result + number.substring(length - 4);
        }
        return number;
    }


}
