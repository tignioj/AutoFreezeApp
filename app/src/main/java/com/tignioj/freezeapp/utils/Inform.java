package com.tignioj.freezeapp.utils;

import android.content.DialogInterface;
import android.telecom.Call;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tignioj.freezeapp.MainActivity;
import com.tignioj.freezeapp.R;

public class Inform {
    public static final int ERROR = -1;

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

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

    public static void confirm(final int title, final int msg, final int okText, final int cancelText, final Callback callback) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
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

    public static void confirm(final int title, final int msg, final Callback callback) {
        confirm(title, msg, R.string.yes, R.string.no, callback);
    }

    public static void alert(final int title, final int msg) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(title).setMessage(msg);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    public static void alert(final int title, final int msg, final Callback callback) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(title).setMessage(msg);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.ok();
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        callback.dismiss();
                    }
                })
                        .create().show();
            }
        });
    }

    public abstract static class MySimpleCallback implements Callback {
        @Override
        public void cancel() {

        }

        @Override
        public void ok() {

        }

        @Override
        public void dismiss() {
        }
    }

    public interface Callback {
        void ok();

        void cancel();

        void dismiss();
    }
}
