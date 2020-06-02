package com.tignioj.freezeapp.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.tignioj.freezeapp.MyDeviceAdminReceiver;
import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.ui.setting.SettingFragment;

import java.util.List;

/**
 * 来源：https://www.jianshu.com/p/8934d47aed3b
 * 感谢作者 Javen205 提供封装
 * 侵删
 */
public class DeviceMethod {
    private static DeviceMethod mDeviceMethod;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Context mContext;


    private DeviceMethod(Context context) {
        mContext = context;
        //获取设备管理服务
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(context, MyDeviceAdminReceiver.class);
    }

    public static DeviceMethod getInstance(Context context) {
        if (mDeviceMethod == null) {
            synchronized (DeviceMethod.class) {
                if (mDeviceMethod == null) {
                    mDeviceMethod = new DeviceMethod(context);
                }
            }
        }
        return mDeviceMethod;
    }

    // 激活程序
    public void onActivate() {
        Toast.makeText(mContext, "激活", Toast.LENGTH_SHORT).show();
        //判断是否激活  如果没有就启动激活设备
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(
                    DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                    componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "提示文字");
            mContext.startActivity(intent);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 移除程序 如果不移除程序 APP无法被卸载
     */
    public void onRemoveActivate() {
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    /**
     * 设置解锁方式 不需要激活就可以运行
     */
    public void startLockMethod() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        mContext.startActivity(intent);
    }

    /**
     * 设置解锁方式
     */
    public void setLockMethod() {
        // PASSWORD_QUALITY_ALPHABETIC
        // 用户输入的密码必须要有字母（或者其他字符）。
        // PASSWORD_QUALITY_ALPHANUMERIC
        // 用户输入的密码必须要有字母和数字。
        // PASSWORD_QUALITY_NUMERIC
        // 用户输入的密码必须要有数字
        // PASSWORD_QUALITY_SOMETHING
        // 由设计人员决定的。
        // PASSWORD_QUALITY_UNSPECIFIED
        // 对密码没有要求。
        if (devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            devicePolicyManager.setPasswordQuality(componentName,
                    DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
            mContext.startActivity(intent);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 立刻锁屏
     */
    public void lockNow() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        } else {
            Inform.showError("请先激活设备");
        }
    }


    /**
     * 设置多长时间后锁屏
     *
     * @param time
     */
    public void LockByTime(long time) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, time);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 恢复出厂设置
     */
    public void WipeData() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 设置密码锁
     *
     * @param password
     */
    public void setPassword(String password) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(password,
                    DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 冻结App
     *
     * @param packageName
     * @param isFreeze
     */
    public void freeze(String packageName, boolean isFreeze) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d(MyConfig.MY_TAG, "freeze:" + isFreeze + " " + packageName);
                devicePolicyManager.setApplicationHidden(componentName, packageName, isFreeze);
            }
        } else {
            Inform.showError("请先激活设备");
        }
    }

    /**
     * 检测App是否隐藏
     *
     * @param packageName
     * @return
     */
    public boolean isHidden(String packageName) {
        boolean b = false;
        if (devicePolicyManager.isAdminActive(componentName)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    b = devicePolicyManager.isApplicationHidden(componentName, packageName);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Inform.showError("请先激活设备");
                }
            }
        } else {
            Inform.showError("请先激活设备");
        }
        return b;
    }


    public boolean isAdmin() {
        return devicePolicyManager.isAdminActive(componentName);
    }

    public DevicePolicyManager getDevicePolicyManager() {
        return devicePolicyManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void clearDeviceOwnerApp(String packageName) {
        if (isAdmin()) {
            devicePolicyManager.clearDeviceOwnerApp(packageName);
        }
    }


//    PackageInstaller mPackageInstaller;

//    /**
//     * @param packageName 应用包名
//     */
//    private void uninstallPackage(String packageName) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.REQUEST_DELETE_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
//                // 检查权限状态
//                if (ActivityCompat.shouldShowRequestPermissionRationale(Inform.getMainActivity(), Manifest.permission.REQUEST_DELETE_PACKAGES)) {
//                    //  用户彻底拒绝授予权限，一般会提示用户进入设置权限界面
//                } else {
//                    //  用户未彻底拒绝授予权限
//                    ActivityCompat.requestPermissions(Inform.getMainActivity(), new String[]{Manifest.permission.REQUEST_DELETE_PACKAGES}, 1);
//                }
//            }
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            //使用PackageInstaller卸载
//            mPackageInstaller = mContext.getPackageManager().getPackageInstaller();
//            Intent intent = new Intent(mContext, mContext.getClass());
//            PendingIntent sender = PendingIntent.getActivity(mContext, 0, intent, 0);
//            mPackageInstaller.uninstall(packageName, sender.getIntentSender());
//        } else {
//            //使用原始方式卸载
//            Uri packageUri = Uri.parse("package:" + packageName);
//            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
//            Inform.getMainActivity().startActivityForResult(uninstallIntent, SettingFragment.DEACTIVATE);
//        }
//    }

    public void deActivate(final SettingFragment settingFragment) {
        Inform.alert(R.string.warning, R.string.confirm_deactive_tips, R.string.yes, R.string.no, new Inform.Callback() {
            @Override
            public void ok() {
                final List<String> freezeApps = settingFragment.getHomeViewModel().getFonzenAppListPackageName();
                settingFragment.getProgressBar().setVisibility(View.VISIBLE);
                settingFragment.getProgressSettingText().setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final FragmentActivity activity = settingFragment.getActivity();
                        final int i = settingFragment.getHomeViewModel().unFreezeAllApp();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    settingFragment.getProgressSettingText().setVisibility(View.INVISIBLE);
                                    settingFragment.getProgressBar().setVisibility(View.INVISIBLE);
                                    Toast.makeText(settingFragment.getContext(), activity.getString(R.string.unfreeze_text) + i + activity.getString(R.string.application_text), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            clearDeviceOwnerApp(componentName.getPackageName());
                        } else {
                            onRemoveActivate();
                        }
                    }
                }).start();
            }

            @Override
            public void cancel() {
            }

            @Override
            public void dismiss() {
            }
        });


    }

}