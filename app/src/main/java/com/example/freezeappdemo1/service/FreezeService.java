package com.example.freezeappdemo1.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.receiver.MyReceiver;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.utils.MyDateUtils;

import java.util.HashMap;
import java.util.List;

public class FreezeService extends Service {
    private MyBinder myBinder;

    public MyBinder getMyBinder() {
        return myBinder;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myBinder = new MyBinder();
        return myBinder;
    }


    public static class MyBinder extends Binder {
    }

    List<FreezeTasker> freezeTaskers;


    /**
     * 当任务在指定时间段为锁屏，则将应用的 id, true 存入, 同时锁定屏幕
     * 当任务在非指定时间段，则从该map中获取，是否为true，有的话则解锁屏幕
     */
    HashMap<Long, Boolean> screenSchedulingMap;

    @Override
    public void onCreate() {
        super.onCreate();
        screenSchedulingMap = new HashMap<>();
        //定时冻结
        final HomeViewModel homeViewModel = new HomeViewModel(getApplication());

        LiveData<List<FreezeTasker>> allFreezeTaskerLive = homeViewModel.getAllFreezeTaskerLive();
        allFreezeTaskerLive.observeForever(new Observer<List<FreezeTasker>>() {
            @Override
            public void onChanged(final List<FreezeTasker> freezeTaskers) {
                if (FreezeService.this.freezeTaskers != null) {
                    Log.d("myTag", "数据更新 从" + FreezeService.this.freezeTaskers.size() +
                            "到 " + freezeTaskers.size());
                }
                FreezeService.this.freezeTaskers = freezeTaskers;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            //五秒刷新一次
                            //根据FreezeTask查询到Category
                            for (FreezeTasker freezeTasker : freezeTaskers) {
                                //只处理这时间段的App
                                List<FreezeApp> appsByCategory = homeViewModel.getAppsByCategory(freezeTasker.getCategoryId());
                                if (MyDateUtils.betweenStartTimeAndEndTime(freezeTasker.getStartTime(), freezeTasker.getEndTime())) {
                                    //处理锁屏
                                    lockScreen(freezeTasker);

                                    //处理冻结应用
                                    lockApps(freezeTasker, appsByCategory);
                                } else {
                                    //如果不是该时间段的，查看是不是之前有冻结的记录
                                    //如果有, 说明之前灭屏了，则把广播的设为false
                                    Boolean aBoolean = screenSchedulingMap.get(freezeTasker.getId());
                                    if (aBoolean != null && aBoolean.booleanValue()) {
                                        Log.d(MyConfig.MY_TAG, "out of data: unlock screen");
                                        screenSchedulingMap.put(freezeTasker.getId(), false);
                                        MyReceiver.isLockScreen = false;
                                    }
                                }
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    /**
                     * 冻结应用
                     * @param freezeTasker
                     * @param appsByCategory
                     */
                    private void lockApps(FreezeTasker freezeTasker, List<FreezeApp> appsByCategory) {
                        Log.d(MyConfig.MY_TAG, "lock apps");
                        if (freezeTasker.isFrozen()) {
                            freezeApps(appsByCategory, homeViewModel);
                        } else {
                            //否则解冻
                            unfreezeApps(appsByCategory, homeViewModel);
                        }
                    }

                    /**
                     * 锁屏
                     * @param freezeTasker
                     */
                    private void lockScreen(FreezeTasker freezeTasker) {
                        //处理锁屏
                        if (freezeTasker.isLockScreen()) {
                            Log.d(MyConfig.MY_TAG, "lock screen");
                            screenSchedulingMap.put(freezeTasker.getId(), true);
                            MyReceiver.isLockScreen = true;
                            DeviceMethod.getInstance(getApplicationContext()).lockNow();
                        } else {
                            Log.d(MyConfig.MY_TAG, "unlock screen");
                            screenSchedulingMap.put(freezeTasker.getId(), false);
                        }
                    }
                }).start();
            }
        });


    }

    /**
     * 解冻App集合
     * @param appsByCategory
     * @param homeViewModel
     */
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
     * @param appsByCategory
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
