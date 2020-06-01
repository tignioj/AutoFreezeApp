package com.tignioj.freezeapp.exception;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;

class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private Thread.UncaughtExceptionHandler defaultUncaught;


    public CrashHandler(Context mContext) {
        this.mContext = mContext;
        defaultUncaught = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this); // 设置为当前线程默认的异常处理器
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        e.printStackTrace();


    }
}
