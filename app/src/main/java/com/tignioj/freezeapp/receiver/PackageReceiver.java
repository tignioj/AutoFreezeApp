package com.tignioj.freezeapp.receiver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.DeviceMethod;

public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            return;
        }

        HomeViewModel homeViewModel = new HomeViewModel((Application) context.getApplicationContext());

        switch (intent.getAction()) {

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
