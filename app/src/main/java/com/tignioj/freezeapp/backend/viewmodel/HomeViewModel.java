package com.tignioj.freezeapp.backend.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.repo.AppsCategoryRepository;
import com.tignioj.freezeapp.backend.viewmodel.repo.FreezeAppRepository;
import com.tignioj.freezeapp.backend.viewmodel.repo.FreezeTaskerRepository;
import com.tignioj.freezeapp.backend.viewmodel.repo.HomeRepository;
import com.tignioj.freezeapp.uientity.AppInfo;
import com.tignioj.freezeapp.uientity.ProgramLocker;
import com.tignioj.freezeapp.utils.DeviceMethod;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    Context context;
    HomeRepository homeRepository;
    AppsCategoryRepository appsCategoryRepository;
    FreezeAppRepository freezeAppRepository;
    FreezeTaskerRepository freezeTaskerRepository;


    public MutableLiveData<Integer> getSelectedReadyToFreezeCount() {
        return homeRepository.getSelectedReadyToFreezeCount();
    }

    public MutableLiveData<ProgramLocker> getProgramLockerMutableLiveData() {
        return homeRepository.getProgramLockerMutableLiveData();
    }

    public void setProgramLocker(ProgramLocker programLocker) {
        homeRepository.setProgramLocker(programLocker);
    }



    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        this.homeRepository = HomeRepository.getInstance(application);
        this.appsCategoryRepository = AppsCategoryRepository.getInstance(context);
        this.freezeAppRepository = FreezeAppRepository.getInstance(context);
        this.freezeTaskerRepository = FreezeTaskerRepository.getInstance(context);
    }




    public LiveData<List<AppsCategory>> getAppsCategoryLive() {
        return appsCategoryRepository.getListLiveDataAppsCategory();
    }

    public LiveData<List<AppsCategory>> getListLiveDataAppsCategoryForSpinner() {
        return appsCategoryRepository.getListLiveDataAppsCategoryForSpinner();
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


    public MutableLiveData<List<AppInfo>> findAppsListWithPattern(String string) {
        return homeRepository.getMutableLiveDataAllAppListWithPattern(string);
    }


    public void updateAllMemoryData() {
        homeRepository.updateAll();
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

    public LiveData<List<FreezeTasker>> getAllFreezeTaskerLive() {
        return this.freezeTaskerRepository.getListLiveDataFreezeTasker();
    }

    public List<AppsCategory> getAppsCategorys() {
        return this.appsCategoryRepository.getAppsCategory();
    }

    public void insertFreezeTasks(FreezeTasker... freezeTaskers) {
        this.freezeTaskerRepository.insertFreezeTasker(freezeTaskers);

    }

    public void deleteAllFreezeTasks() {
        this.freezeTaskerRepository.deleteAllFreezeTasker();
    }

    public void deleteFreezeTasks(FreezeTasker... freezeTaskers) {
        this.freezeTaskerRepository.deleteFreezeTasker(freezeTaskers);
    }

    public List<FreezeApp> getAllFrozenApps() {
        return freezeAppRepository.getAllFrozenApps();
    }


    public AppsCategory getCategoryByCategoryName(String categoryName) {
        return appsCategoryRepository.getCategoryBaCategoryCategoryName(categoryName);
    }

    public FreezeTasker getFreezeTaskerById(long id) {
        return freezeTaskerRepository.getFrezeTaskerById(id);
    }

    public void updateFreezeTasks(FreezeTasker... freezeTaskerFromDb) {
        freezeTaskerRepository.insertFreezeTasker(freezeTaskerFromDb);
    }


    public LiveData<List<AppsCategory>> findAppCategorysLiveWithPattern(String pattern) {
        return appsCategoryRepository.findAppCategorysLiveWithPattern(pattern);
    }


    public MutableLiveData<List<AppInfo>> findUnFreezeAppsListWithPattern(String pattern) {
        return homeRepository.findUnFreezeAppsListWithPattern(pattern);
    }


    public void setSelectedReadyToFreezeCount(int i) {
        homeRepository.setSelectedReadyToFreezeCount(i);
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLiveNotInCategory(List<FreezeApp> categoryId) {
        return homeRepository.getMutableLiveDataUnFreezeAppListLiveNotInCategory(categoryId);
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLiveNotInCategoryWithPattern(List<FreezeApp> freezeAppsByCategoryId, String pattern) {
        return homeRepository.getMutableLiveDataUnFreezeAppListLiveNotInCategoryWithPattern(freezeAppsByCategoryId, pattern);
    }

    public LiveData<List<FreezeApp>> getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(long categoryId, String pattern) {
        return freezeAppRepository.getMutableLiveDataFreezeAppListLiveInCategoryWithPattern(categoryId, pattern);
    }

    public void deleteFreezeAppByPackageName(String appName) {
        FreezeApp freezeAppByPackageName = freezeAppRepository.getFreezeAppByPackageName(appName);
        if (freezeAppByPackageName != null) {
            FreezeApp[] apps = new FreezeApp[]{freezeAppByPackageName};
            freezeAppRepository.deleteFreezeApp(apps);
        }
    }

    public List<String> getFonzenAppListPackageName() {
        return homeRepository.getFrozenAppListPackageNameFromPM();
    }

    /**
     * 返回解冻数量
     * @return
     */
    public int unFreezeAllApp() {
        List<AppInfo> frozenAppList = homeRepository.getFrozenAppList();
        int i = 0;
        for (AppInfo a : frozenAppList) {
            i++;
            DeviceMethod.getInstance(context).freeze(a.getPackageName(), false);
        }
        freezeAppRepository.unFreezeAllApp();
        return i;
    }

    public void updateFreezeTasksAllEnable(boolean isChecked) {
        freezeAppRepository.updateFreezeTasksAllEnable(isChecked);
    }
}
