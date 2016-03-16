package com.open_source.joker.concentration.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.constant.ToolBarStyle;
import com.open_source.joker.concentration.model.SimpleMsg;
import com.open_source.joker.concentration.util.CrashReportHelper;
import com.open_source.joker.concentration.util.ToolBarHelper;
import com.open_source.joker.concentration.widget.BeautifulProgressDialog;

/**
 * 文件名：com.open_source.joker.concentration.app
 * 描述：
 * 时间：16/2/17
 * 作者: joker
 */
public class CONActivity extends AppCompatActivity {

    protected static final int DLG_PROGRESS = 0xFA05;
    protected static final int DLG_MESSAGE = 0xFA06;
    protected static final int DLG_SIMPLE = 0xFA07;
    private static SharedPreferences prefs;

    private ToolBarHelper mToolBarHelper;
    private Toolbar toolbar;

    private Toast toast;

    private FragmentManager mFragmentManager;
    private int mCurrentFragment = -1;

    protected String dlgProgressTitle;
    protected SimpleMsg dlgMessage;
    protected Dialog managedDialog;
    protected int managedDialogId = 0;

    protected boolean isDestroyed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CONApplication.instance().activityOnCreate(this);
        prefs = preferences(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONApplication.instance().activityOnResume(this);
    }

    @Override
    protected void onPause() {
        CONApplication.instance().activityOnPause(this);

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CONApplication.instance().activityOnDestory(this);
        isDestroyed = true;
    }


    public static SharedPreferences preferences(Context c) {
        return c.getSharedPreferences(c.getPackageName(), MODE_PRIVATE);
    }

    public void initMannager() {
        mFragmentManager = getSupportFragmentManager();
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
        if (toolbar != null)
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

    public  void hideLeftView(){
        if (toolbar != null)
            mToolBarHelper.hideLeftView();
    }

    protected void onLeftTitleButtonClicked() {
        onBackPressed();
    }


    public void switchFragment(Fragment toFragment, int id) {
        switchFragment(toFragment, id, null);
    }

    /**
     * 切换fragment
     *
     * @param toFragment
     * @param id
     * @param bundle
     */
    public void switchFragment(Fragment toFragment, int id, Bundle bundle) {
        if (mCurrentFragment == id) {
            if (bundle != null && !bundle.isEmpty()) {
                getCurrFragment().getArguments().putAll(bundle);
            }

            return;
        }
        Fragment fromFragment = mFragmentManager.findFragmentByTag(String
                .valueOf(mCurrentFragment));

        mCurrentFragment = id;

        if (toFragment == null) {
            toFragment = getFragment(id);
            if (bundle != null && !bundle.isEmpty()) {
                toFragment.setArguments(bundle);
            }
        } else if (bundle != null && !bundle.isEmpty()) {
            toFragment.getArguments().putAll(bundle);
        }

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (fromFragment != null && !fromFragment.isHidden()) {
            ft.hide(fromFragment);
        }

        if (!toFragment.isAdded()) {
            ft.add(R.id.content, toFragment, String.valueOf(id));
        } else {
            ft.show(toFragment);
        }

        ft.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    protected Fragment getCurrFragment() {
        return getFragment(mCurrentFragment);
    }

    protected Fragment getFragment(int markId) {
        Fragment fragment = mFragmentManager.findFragmentByTag(String
                .valueOf(markId));
        return fragment;
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        String url = intent.getDataString();
        CrashReportHelper.putUrlSchema(url);
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivity(String urlSchema) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)));
    }

    public void startActivityForResult(String urlSchema, int requestCode) {
        startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(urlSchema)), requestCode);
    }

    public int getIntParam(String name, int defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Integer.parseInt(val);
            }
        } catch (Exception e) {
        }

        return i.getIntExtra(name, defaultValue);
    }

    public int getIntParam(String name) {
        return getIntParam(name, 0);
    }

    public String getStringParam(String name) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                if (val != null)
                    return val;
            }
        } catch (Exception e) {
        }

        return i.getStringExtra(name);
    }

    public double getDoubleParam(String name, double defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Double.parseDouble(val);
            }
        } catch (Exception e) {
        }

        return i.getDoubleExtra(name, defaultValue);
    }

    public double getDoubleParam(String name) {
        return getDoubleParam(name, 0);
    }


    public boolean getBooleanParam(String name, boolean defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                if (!TextUtils.isEmpty(val))
                    return Boolean.parseBoolean(val);
            }
        } catch (Exception e) {
        }
        return i.getBooleanExtra(name, defaultValue);
    }

    public boolean getBooleanParam(String name) {
        return getBooleanParam(name, false);
    }

    public long getLongParam(String name) {
        return getLongParam(name, 0L);
    }

    public long getLongParam(String name, long defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Long.parseLong(val);
            }
        } catch (Exception e) {
        }

        return i.getLongExtra(name, defaultValue);
    }

    public byte getByteParam(String name) {
        return getByteParam(name, (byte) 0);
    }

    public byte getByteParam(String name, byte defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Byte.parseByte(val);
            }
        } catch (Exception e) {
        }

        return i.getByteExtra(name, defaultValue);
    }

    public float getFloatParam(String name) {
        return getFloatParam(name, 0f);
    }

    public float getFloatParam(String name, float defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Float.parseFloat(val);
            }
        } catch (Exception e) {
        }

        return i.getFloatExtra(name, defaultValue);
    }

    public short getShortParam(String name) {
        return getShortParam(name, (short) 0);
    }

    public short getShortParam(String name, short defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return Short.parseShort(val);
            }
        } catch (Exception e) {
        }

        return i.getShortExtra(name, defaultValue);
    }

    public char getCharParam(String name) {
        return getCharParam(name, (char) 0);

    }

    public char getCharParam(String name, char defaultValue) {
        Intent i = getIntent();
        try {
            Uri uri = i.getData();
            if (uri != null) {
                String val = uri.getQueryParameter(name);
                return val.charAt(0);
            }
        } catch (Exception e) {
        }

        return i.getCharExtra(name, defaultValue);
    }



    public void showProgressDialog(String title) {
        if (isDestroyed) {
            return;
        }
        dismissDialog();
        dlgProgressTitle = title;
        ProgressDialog dlg = new ProgressDialog(this);
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (managedDialogId == DLG_PROGRESS) {
                    managedDialogId = 0;
                }
                dlgProgressTitle = null;
                onProgressDialogCancel();
            }
        });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        });
        dlg.setMessage(dlgProgressTitle == null ? "载入中..." : dlgProgressTitle);

        managedDialogId = DLG_PROGRESS;
        managedDialog = dlg;
        dlg.show();
    }


    /**
     * 显示Progress Dialog.
     *
     * @param title
     * @param cancelListener
     */
    public void showProgressDialog(String title,
                                   final DialogInterface.OnCancelListener cancelListener) {
        if (isDestroyed) {
            return;
        }
        dismissDialog();

        dlgProgressTitle = title;

        BeautifulProgressDialog dlg = new BeautifulProgressDialog(this);
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (cancelListener != null) {
                    cancelListener.onCancel(dialog);
                }
                if (managedDialogId == DLG_PROGRESS) {
                    managedDialogId = 0;
                }
                dlgProgressTitle = null;
                managedDialog = null;
                onProgressDialogCancel();
            }
        });
        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                return false;
            }
        });
        dlg.setMessage(title == null ? "载入中..." : title);

        managedDialogId = DLG_PROGRESS;
        managedDialog = dlg;
        dlg.show();
    }

    public void showMessageDialog(SimpleMsg message, DialogInterface.OnClickListener listener) {
        if (isDestroyed) {
            return;
        }
        dismissDialog();
        if (message != null) {
            dlgMessage = message;
        } else {
            dlgMessage = new SimpleMsg("错误", "操作出错", 0);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getMessageIconId(dlgMessage.getIcon()));
        builder.setMessage(dlgMessage.getContent());
        builder.setPositiveButton(R.string.ok, listener);
        AlertDialog dlg = builder.create();
        dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onMessageConfirm();
                if (managedDialogId == DLG_MESSAGE) {
                    managedDialogId = 0;
                }
                dlgMessage = null;
            }
        });

        managedDialogId = DLG_MESSAGE;
        managedDialog = dlg;
        dlg.show();
    }

    public void showSimpleAlertDialog(String title, String message, String positive,
                                      DialogInterface.OnClickListener onPositiveClicked, String negative,
                                      DialogInterface.OnClickListener onNegativeClicked) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(positive, onPositiveClicked).setNegativeButton(
                negative, onNegativeClicked);

        AlertDialog dlg = builder.create();
        managedDialog = dlg;
        managedDialogId = DLG_SIMPLE;
        dlg.show();
    }

    public void showAlertDialog(String title, String message) {
        showAlertDialog(title, message, "确定");
    }

    public void showAlertDialog(String title, String message, String buttonTitle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(buttonTitle,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dlg = builder.create();
        dlg.show();
    }

    public void dismissDialog() {
        if (isDestroyed) {
            return;
        }
        if (managedDialogId != 0) {
            if ((managedDialog != null) && managedDialog.isShowing()) {
                managedDialog.dismiss();
            }
            dlgProgressTitle = null;
            managedDialogId = 0;
            managedDialog = null;
        }
    }

    protected int getMessageIconId(int id) {
        switch (id) {
            case 1:
                return android.R.drawable.ic_dialog_info;
            default:
                return android.R.drawable.ic_dialog_alert;
        }
    }

    public void onProgressDialogCancel() {
        // TO OVERRIDE
    }

    public void onMessageConfirm() {
        // TO OVERRIDE
    }
}
