package com.tignioj.freezeapp.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.tignioj.freezeapp.MainActivity;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.utils.DeviceMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //如果隐藏了freezeApp，则将他复原
        PackageManager p = context.getPackageManager();
//        ComponentName componentName = new ComponentName("com.tignioj.freezeapp", "com.tignioj.freezeapp.MainActivity");
        ComponentName componentName = new ComponentName(context, MainActivity.class);

        if (!DeviceMethod.isComponentEnabled(p, componentName.getPackageName(), componentName.getClassName())) {
            Log.d(MyConfig.LOG_TAG_MyBootReceiver, componentName.getPackageName() + ":" + componentName.getClassName() + "冻结了，正在解冻中");
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putBoolean(MyConfig.PERSONAL_SHP_CONFIG_KEY_EDITABLE_ENABLE, false);
            edit.apply();
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, FreezeService.class));
        } else {
            context.startService(new Intent(context, FreezeService.class));
        }
    }
}
