package com.tignioj.freezeapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;


import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.receiver.PackageReceiver;
import com.tignioj.freezeapp.receiver.ScreenReceiver;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.MyDateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class FreezeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isServiceEnd;


    List<FreezeTasker> freezeTaskers;
    private HomeViewModel homeViewModel;
    PackageReceiver packageReceiver;
    ScreenReceiver screenReceiver;


    public class ServiceThread extends Thread {
        HashMap<Long, Boolean> screenSchedulingMap;


        ServiceThread() {
            //初始化数据
            screenSchedulingMap = new HashMap<>();
        }

        @Override
        public void run() {
            while (!isServiceEnd) {
                Log.d(MyConfig.MY_TAG, new Date().toString() + ":循环查看任务, " + (freezeTaskers == null ? null : freezeTaskers.size()));
                if (freezeTaskers != null) {
                    if (freezeTaskers.size() > 0) {
                        loopTasks();
                    }
                }
                //五秒刷新一次
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private void loopTasks() {
            for (FreezeTasker freezeTasker : freezeTaskers) {
                //只处理这时间段的App
                if (MyDateUtils.betweenStartTimeAndEndTime(freezeTasker.getStartTime(), freezeTasker.getEndTime())) {
                    processLockScreen(freezeTasker);

                    processFreezeApp(freezeTasker);
                } else {
                    processUnLockScreen(freezeTasker);
                }
            }
        }

        private void processUnLockScreen(FreezeTasker freezeTasker) {
            //如果不是该时间段的，查看是不是之前有冻结的记录
            //如果有, 说明之前灭屏了，则把广播的设为false
            Boolean aBoolean = screenSchedulingMap.get(freezeTasker.getId());
            if (aBoolean != null && aBoolean) {
                Log.d(MyConfig.MY_TAG, "out of data: unlock screen");
                screenSchedulingMap.remove(freezeTasker.getId());
                ScreenReceiver.isLockScreen = false;
            }
        }


        private void processFreezeApp(final FreezeTasker freezeTasker) {
            //如果规定冻结App
            List<FreezeApp> freezeAppsByCategory = homeViewModel.getAppsByCategory(freezeTasker.getCategoryId());

            if (freezeTasker.isFrozen()) {
                Log.d(MyConfig.MY_TAG, "lock apps");
                freezeApps(freezeAppsByCategory, homeViewModel);
            } else {
                Log.d(MyConfig.MY_TAG, "unlock apps");
                unfreezeApps(freezeAppsByCategory, homeViewModel);
            }
        }

        private void processLockScreen(FreezeTasker freezeTasker) {
            //如果规定锁屏
            if (freezeTasker.isLockScreen()) {
                //如果屏幕还没锁
                if (!ScreenReceiver.isLockScreen) {
                    Log.d(MyConfig.MY_TAG, "lock screen");
                    screenSchedulingMap.put(freezeTasker.getId(), true);
                    ScreenReceiver.isLockScreen = true;
                    DeviceMethod.getInstance(getApplicationContext()).lockNow();
                }
            } else {
                //如果规定不锁屏
                Log.d(MyConfig.MY_TAG, "unlock screen");
                screenSchedulingMap.put(freezeTasker.getId(), false);
            }
        }


        private void unfreezeApps(List<FreezeApp> appsByCategory, HomeViewModel homeViewModel) {
            for (FreezeApp freezeApp : appsByCategory) {
                if (freezeApp.isFrozen()) {
                    DeviceMethod.getInstance(getApplicationContext()).
                            freeze(freezeApp.getPackageName(), false);
                    Log.d("myTag", "解冻：" + freezeApp.getAppName());
                    freezeApp.setFrozen(false);
                    homeViewModel.updateFreezeApp(freezeApp);
                }
            }
        }

        /**
         * 冻结指定App集合
         *
         * @param appsByCategory 待冻结的App
         */
        private void freezeApps(List<FreezeApp> appsByCategory, HomeViewModel homeViewModel) {

            for (FreezeApp freezeApp : appsByCategory) {
                if (!freezeApp.isFrozen()) {
                    DeviceMethod.getInstance(getApplicationContext()).
                            freeze(freezeApp.getPackageName(), true);
                    Log.d("myTag", "冻结：" + freezeApp.getAppName());
                    freezeApp.setFrozen(true);
                    homeViewModel.updateFreezeApp(freezeApp);
                }
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        registBroadCast();

        homeViewModel = new HomeViewModel(getApplication());

        LiveData<List<FreezeTasker>> allFreezeTaskerLive = homeViewModel.getAllFreezeTaskerLive();

        allFreezeTaskerLive.observeForever(new Observer<List<FreezeTasker>>() {
            @Override
            public void onChanged(List<FreezeTasker> freezeTaskers) {
                if (FreezeService.this.freezeTaskers != null) {
                    Log.d("myTag", "数据更新 从" + FreezeService.this.freezeTaskers.size() + "到 " + freezeTaskers.size());
                    //如果删掉了一个任务，必须先设置为false，否则当遍历不到被删掉的任务时，屏幕保持锁定状态
                    ScreenReceiver.isLockScreen = false;
                }
                FreezeService.this.freezeTaskers = freezeTaskers;
            }
        });

        serviceThread = new ServiceThread();
        serviceThread.start();
    }

    ServiceThread serviceThread;

    /**
     * 动态注册广播
     */
    private void registBroadCast() {
        IntentFilter intentFilterPackage = new IntentFilter();
        intentFilterPackage.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilterPackage.addAction(Intent.ACTION_PACKAGE_FULLY_REMOVED);
        intentFilterPackage.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilterPackage.addDataScheme("package");
        packageReceiver = new PackageReceiver();
        registerReceiver(packageReceiver, intentFilterPackage);


        IntentFilter intentFilterScreen = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilterScreen.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilterScreen.addAction(Intent.ACTION_USER_PRESENT);
        screenReceiver = new ScreenReceiver();
        registerReceiver(screenReceiver, intentFilterScreen);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceEnd = true;
        Log.d(MyConfig.MY_TAG, "服务结束！");
        unregisterReceiver(screenReceiver);
        unregisterReceiver(packageReceiver);
    }
}
