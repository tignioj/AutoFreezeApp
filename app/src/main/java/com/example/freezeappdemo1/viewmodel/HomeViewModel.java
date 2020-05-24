package com.example.freezeappdemo1.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    Context context;


    HomeRepository homeRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        this.homeRepository = HomeRepository.getInstance(application);
    }


    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLive() {
        return homeRepository.getMutableLiveDataUnFreezeAppListLive();
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataFrozenAppList() {
        return homeRepository.getMutableLiveDataFrozenAppList();
    }


    public void updateFrozenApps() {
        homeRepository.updateFrozenApps();
    }

    public void updateUnFreezeApps() {
        homeRepository.updateUnFreezeApps();
    }

    public void  updateAll() {
        homeRepository.updateAll();
    }
}
