package com.example.freezeappdemo1.service;

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

import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.utils.MyDateUtils;

import java.util.List;

import static com.example.freezeappdemo1.config.MyConfig.SHP_FREEZE_APP_LIST_FOR_TIMER;

public class FreezeService extends Service {
    SharedPreferences shp;
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

    @Override

    public void onCreate() {
        super.onCreate();
        //定时冻结
        final HomeViewModel homeViewModel = new HomeViewModel(getApplication());

        LiveData<List<FreezeTasker>> allFreezeTaskerLive = homeViewModel.getAllFreezeTaskerLive();
        allFreezeTaskerLive.observeForever(new Observer<List<FreezeTasker>>() {
            @Override
            public void onChanged(List<FreezeTasker> freezeTaskers) {
                if (FreezeService.this.freezeTaskers != null) {
                    Log.d("myTag", "数据更新 从" + FreezeService.this.freezeTaskers.size() +
                            "到 " + freezeTaskers.size());
                }
                FreezeService.this.freezeTaskers = freezeTaskers;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    //五秒刷新一次
                    try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
                    //根据FreezeTask查询到Category
                    for (FreezeTasker freezeTasker : freezeTaskers) {
                        //如果是当前时间段，则冻结！
                        List<FreezeApp> appsByCategory = homeViewModel.getAppsByCategory(freezeTasker.getCategoryId());
                        if (MyDateUtils.betweenStartTimeAndEndTime(freezeTasker.getStartTime(), freezeTasker.getEndTime())) {
                            freezeApps(appsByCategory, homeViewModel);
                        } else {
                            //否则解冻
                            unfreezeApps(appsByCategory, homeViewModel);
                        }
                    }
                }

            }
        }).start();
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
