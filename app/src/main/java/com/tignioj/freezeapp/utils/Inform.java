package com.tignioj.freezeapp.utils;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tignioj.freezeapp.MainActivity;

public class Inform {
    public static final int ERROR = -1;

    private static MainActivity mainActivity;


    public static void setMainActivity(MainActivity mainActivity) {
        Inform.mainActivity = mainActivity;
    }

    public static void showError(String msg) {
        toast(msg);
    }


    public static void toast(final String msg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void alert(final String title, final String msg, final String okText, final String cancelText, final Callback callback) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder= new AlertDialog.Builder(mainActivity);
                builder.setTitle(title).setMessage(msg);
                builder.setNegativeButton(okText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.ok();
                    }
                }).setPositiveButton(cancelText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.cancel();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callback.dismiss();

                    }
                }).create().show();
            }
        });
    }

    public static void alert(final String title, final String message, final Callback callback) {
    }

    public interface Callback {
        void ok();
        void cancel();
        void dismiss();
    }
}
