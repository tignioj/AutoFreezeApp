package com.tignioj.freezeapp.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.receiver.MyReceiver;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.MyDateUtils;

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


    public class MyBinder extends Binder {

    }

    List<FreezeTasker> freezeTaskers;
    private boolean isLockScreen;


    HashMap<Long, Boolean> schedulingMap;

    @Override
    public void onCreate() {
        super.onCreate();
        schedulingMap = new HashMap<>();
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
                                    if (freezeTasker.isLockScreen()) {
                                        Log.d(MyConfig.MY_TAG, "lock screen");
                                        schedulingMap.put(freezeTasker.getId(), true);
                                        MyReceiver.isLockScreen = true;
                                        DeviceMethod.getInstance(getApplicationContext()).lockNow();
                                    } else {
                                        Log.d(MyConfig.MY_TAG, "unlock screen");
                                        schedulingMap.put(freezeTasker.getId(), false);
                                    }
                                    //如何保证时间过后，变量恢复为false

                                    Log.d(MyConfig.MY_TAG, "lock apps");
                                    if (freezeTasker.isFrozen()) {
                                        freezeApps(appsByCategory, homeViewModel);
                                    } else {
                                        //否则解冻
                                        unfreezeApps(appsByCategory, homeViewModel);
                                    }
                                } else {
                                    //如果不是该时间段的，查看是不是之前有冻结的记录
                                    //如果有, 说明之前灭屏了，则把广播的设为false
                                    Boolean aBoolean = schedulingMap.get(freezeTasker.getId());
                                    if (aBoolean != null && aBoolean.booleanValue()) {
                                        Log.d(MyConfig.MY_TAG, "out of data: unlock screen");
                                        schedulingMap.put(freezeTasker.getId(), false);
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
                }).start();
            }
        });


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
