package com.tignioj.freezeapp;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.DeviceMethod;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {


    @Override
    public void onEnabled(Context context, Intent intent) {
        // 设备管理：可用
        Toast.makeText(context, "设备管理：可用", Toast.LENGTH_SHORT).show();
        ComponentName componentName = new ComponentName(context, MainActivity.class);

        //如果隐藏了App，将它复原
        Log.d(MyConfig.LOG_TAG_MyDeviceAdminReceiver, componentName.getPackageName() + ":" + componentName.getClassName() + " enable:" + DeviceMethod.isComponentEnabled(context.getPackageManager(), componentName.getPackageName()
                , componentName.getClassName()));
        //如果App不小心隐藏了，就将他激活
        showApp(context);
    }

    private void showApp(Context context) {
        PackageManager p = context.getPackageManager();
//        ComponentName componentName = new ComponentName("com.tignioj.freezeapp", "com.tignioj.freezeapp.MainActivity");
//        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//        Log.d(MyConfig.LOG_TAG_MyDeviceAdminReceiver, "receive enable!");

        ComponentName componentName = new ComponentName(context, MainActivity.class);
        if (!DeviceMethod.isComponentEnabled(p, componentName.getPackageName(), componentName.getClassName())) {
            Log.d(MyConfig.LOG_TAG_MyBootReceiver, componentName.getPackageName() + ":" + componentName.getClassName() + "冻结了，正在解冻中");
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor edit = mPrefs.edit();
            edit.putBoolean(MyConfig.PERSONAL_SHP_CONFIG_KEY_EDITABLE_ENABLE, false);
            edit.apply();
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        }
    }

    @Override
    public void onDisabled(final Context context, Intent intent) {
        // 设备管理：不可用
        Toast.makeText(context, "设备管理：不可用", Toast.LENGTH_SHORT).show();
        //如果取消了激活就再次提示激活
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                DeviceMethod.getInstance(context.getApplicationContext()).onActivate();
//            }
//        }, 3000);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
           /* // 这里处理 不可编辑设备。这里可以造成死机状态
            Intent intent2 = new Intent(context, NoticeSetting.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            context.stopService(intent);// 是否可以停止*/
        return "这是一个可选的消息，警告有关禁止用户的请求";
    }

}
