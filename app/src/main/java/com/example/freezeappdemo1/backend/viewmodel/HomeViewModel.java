package com.example.freezeappdemo1.backend.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.repo.AppsCategoryRepository;
import com.example.freezeappdemo1.backend.viewmodel.repo.FreezeAppRepository;
import com.example.freezeappdemo1.backend.viewmodel.repo.HomeRepository;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.utils.ShpUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    Context context;

    HomeRepository homeRepository;
    AppsCategoryRepository appsCategoryRepository;
    FreezeAppRepository freezeAppRepository;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        this.homeRepository = HomeRepository.getInstance(application);
        this.appsCategoryRepository = AppsCategoryRepository.getInstance(context);
        this.freezeAppRepository = FreezeAppRepository.getInstance(context);
    }


    public LiveData<List<AppsCategory>> getAppsCategoryLive() {
        return appsCategoryRepository.getListLiveDataAppsCategory();
    }

    //appsCategory
    public void insertAppsCategory(AppsCategory... appsCategories) {
        appsCategoryRepository.insertAppsCategory(appsCategories);
    }

    public void deleteAppsCategory(AppsCategory... appsCategories) {
        appsCategoryRepository.deleteAppsCategory(appsCategories);
    }

    public void updateAppsCategory(AppsCategory... appsCategories) {
        appsCategoryRepository.updateAppsCategory(appsCategories);
    }

    public void deleteAllAppsCategory() {
        appsCategoryRepository.deleteAllAppsCategory();
    }


    //freeze apps
    public void insertFreezeApp(FreezeApp... appsCategories) {
        freezeAppRepository.insertFreezeApp(appsCategories);
    }

    public void deleteFreezeApp(FreezeApp... appsCategories) {
        freezeAppRepository.deleteFreezeApp(appsCategories);
    }

    public void updateFreezeApp(FreezeApp... appsCategories) {
        freezeAppRepository.updateFreezeApp(appsCategories);
    }

    public void deleteAllFreezeApp() {
        freezeAppRepository.deleteAllFreezeApp();
    }


    public List<FreezeApp> getAppsByCategory(long categoryId) {
        return freezeAppRepository.getFreezeAppByCategoryId(categoryId);
    }


    public List<AppInfo> getAllAppsList() {
        return homeRepository.getAllAppList();
    }


    //========================================UI=====================================

    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLive() {
        return homeRepository.getMutableLiveDataUnFreezeAppListLive();
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataFrozenAppList() {
        return homeRepository.getMutableLiveDataFrozenAppList();
    }
    public MutableLiveData<List<AppInfo>> getMutableLiveDataAllAppList() {
        return homeRepository.getMutableLiveDataAllAppList();
    }




    public void updateFrozenApps() {
        homeRepository.updateFrozenApps();
    }

    public void updateUnFreezeApps() {
        homeRepository.updateUnFreezeApps();
    }

    public void updateAll() {
        homeRepository.updateAll();
        ShpUtils.updateAppFrozenList(getApplication(), this);
    }

    public LiveData<List<FreezeApp>> getAppsByCategoryLive(long categoryId) {
        return freezeAppRepository.getFreezeAppLiveByCategoryId(categoryId);
    }


    public void unSelectFreezeAppAll() {
        MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppListLive = homeRepository.getMutableLiveDataUnFreezeAppListLive();
        List<AppInfo> value = mutableLiveDataUnFreezeAppListLive.getValue();
        for (int i = 0; i < value.size(); i++) {
            mutableLiveDataUnFreezeAppListLive.getValue().get(i).setSelected(false);
        }
    }
}
