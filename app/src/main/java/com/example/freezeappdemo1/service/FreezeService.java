package com.example.freezeappdemo1.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static com.example.freezeappdemo1.config.MyConfig.SHP_FREEZE_APP_LIST_FOR_TIMER;

public class FreezeService extends Service {
    SharedPreferences shp;
    private MyBinder myBinder;

    public MyBinder getMyBinder() { return myBinder; }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        myBinder = new MyBinder();
        return myBinder;
    }

    public class MyBinder extends Binder {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //定时冻结
        shp = getSharedPreferences(SHP_FREEZE_APP_LIST_FOR_TIMER, Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
