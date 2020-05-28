package com.tignioj.freezeapp.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.DeviceMethod;

public class MyReceiver extends BroadcastReceiver {
    public static boolean isLockScreen;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(MyConfig.MY_TAG, intent.getAction() + ":" + isLockScreen);

        if (intent.getAction() == null) {
            return;
        }

        HomeViewModel homeViewModel = new HomeViewModel((Application) context.getApplicationContext());

        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
            case Intent.ACTION_USER_PRESENT:
                if (isLockScreen) {
                    Log.d(MyConfig.BROADCAST_RECEIVER, "屏幕开启了");
                    DeviceMethod.getInstance(context).lockNow();
                }
                break;
//            case Intent.ACTION_PACKAGE_REMOVED:
            case Intent.ACTION_PACKAGE_FULLY_REMOVED:
                Log.d(MyConfig.MY_TAG, "安装包移除");
                String dataString = intent.getDataString();
                if (dataString != null) {
                    String[] split = dataString.split(":");
                    homeViewModel.deleteFreezeAppByPackageName(split[1]);
                    homeViewModel.updateAllMemoryData();
                }
                break;
            case Intent.ACTION_PACKAGE_ADDED:
                Log.d(MyConfig.MY_TAG, "安装了一个app");
                homeViewModel.updateAllMemoryData();
        }
    }
}
