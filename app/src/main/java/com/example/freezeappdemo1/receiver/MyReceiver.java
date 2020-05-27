package com.example.freezeappdemo1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.utils.DeviceMethod;

public class MyReceiver extends BroadcastReceiver {
    public static boolean isLockScreen;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MyConfig.MY_TAG, intent.getAction()  + ":" +  isLockScreen);
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            if (isLockScreen) {
                Log.d(MyConfig.BROADCAST_RECEIVER, "lock screen");
                DeviceMethod.getInstance(context).lockNow();
            }
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(MyConfig.MY_TAG, "屏幕关闭了");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d(MyConfig.MY_TAG, "屏幕开启了111");
            if (isLockScreen) {
                Log.d(MyConfig.BROADCAST_RECEIVER, "lock screen");
                DeviceMethod.getInstance(context).lockNow();
            }
        }
    }
}
