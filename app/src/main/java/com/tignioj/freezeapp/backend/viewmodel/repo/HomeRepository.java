package com.tignioj.freezeapp.backend.viewmodel.repo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.lifecycle.MutableLiveData;

import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.entity.AppInfo;
import com.tignioj.freezeapp.utils.DeviceMethod;

import java.util.ArrayList;
import java.util.List;

public class HomeRepository {
    private static HomeRepository homeRepository;
    private Context context;

    private MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppList;
    private MutableLiveData<List<AppInfo>> mutableLiveDataFrozenAppList;
    private MutableLiveData<List<AppInfo>> mutableLiveDataAllAppList;
    private MutableLiveData<Integer> selectedReadyToFreezeCount;

    public synchronized static HomeRepository getInstance(Context context) {
        if (homeRepository == null) {
            homeRepository = new HomeRepository(context);
        }
        return homeRepository;
    }

    private List<AppInfo> allApps;
    private List<AppInfo> frozenApps;
    private List<AppInfo> unFreezeApps;

    private HomeRepository(Context context) {
        this.context = context;

        mutableLiveDataAllAppList = new MutableLiveData<>();
        this.allApps = getAllAppList();
        mutableLiveDataAllAppList.setValue(this.allApps);


        mutableLiveDataUnFreezeAppList = new MutableLiveData<>();
        this.unFreezeApps = getUnFreezeAppList();
        mutableLiveDataUnFreezeAppList.setValue(this.unFreezeApps);

        mutableLiveDataFrozenAppList = new MutableLiveData<>();
        this.frozenApps = getFrozenAppList();
        mutableLiveDataFrozenAppList.setValue(this.frozenApps);

        this.selectedReadyToFreezeCount = new MutableLiveData<>();
        this.selectedReadyToFreezeCount.setValue(0);
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataAllAppList() {
        return mutableLiveDataAllAppList;
    }

    /**
     * 获取所有的非系统的，已经冻结的包
     *
     * @return
     */
    public List<AppInfo> getFrozenAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        for (AppInfo a : appInfos) {
            if (a.isHidden()) {
                appInfos.add(a);
            }
        }
        return appInfos;

    }


    /**
     * 获取所有的非系统安装包
     *
     * @return
     */
    public List<AppInfo> getUnFreezeAppList() {
        ArrayList<AppInfo> appInfos = new ArrayList<>();
        for (int i = 0; i < this.allApps.size(); i++) {
            AppInfo a = allApps.get(i);
            if (a.isHidden()) {
                continue;
            }
            appInfos.add(a);
        }
        return appInfos;

//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
//
//        for (PackageInfo p : installedPackages) {
//            ApplicationInfo ai = p.applicationInfo;
//            AppInfo a = new AppInfo();
//
//            //不显示系统应用
//            boolean b = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
//            if (b) {
//                continue;
//            }
//
//            //不显示本应用
//            if (ai.packageName.equals(context.getPackageName())) {
//                continue;
//            }
//
//
//            a.setAppName((String) pm.getApplicationLabel(ai));
//            a.setPackageName(p.packageName);
//            a.setIcon(ai.loadIcon(pm));
//
//            a.setIconLayout(ai.icon);
//
//            a.setHidden(DeviceMethod.getInstance(context).isHidden(ai.packageName));
//            appInfos.add(a);
//        }
//        return appInfos;
    }


    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLive() {
        return mutableLiveDataUnFreezeAppList;
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataFrozenAppList() {
        return mutableLiveDataFrozenAppList;
    }


    public void updateAll() {
        //由于后两者依赖于AppApps，所以必须先更新AllApps
        this.allApps = getAllAppList();
        mutableLiveDataAllAppList.postValue(this.allApps);

        this.frozenApps = getFrozenAppList();
        mutableLiveDataFrozenAppList.postValue(this.frozenApps);

        this.unFreezeApps = getUnFreezeAppList();
        mutableLiveDataUnFreezeAppList.postValue(this.unFreezeApps);
    }


    private List<AppInfo> getAllAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;
            AppInfo a = new AppInfo();

            //不显示系统应用
            boolean b = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if (b) {
                continue;
            }

            //不显示本应用
            if (ai.packageName.equals(context.getPackageName())) {
                continue;
            }

            a.setAppName((String) pm.getApplicationLabel(ai));

            a.setPackageName(p.packageName);

            a.setIcon(ai.loadIcon(pm));

            a.setIconLayout(ai.icon);

            a.setHidden(DeviceMethod.getInstance(context).isHidden(ai.packageName));
            appInfos.add(a);
        }
        return appInfos;
    }

    /**
     * 根据包命获取应用列表
     *
     * @param pattern
     * @return
     */
    public MutableLiveData<List<AppInfo>> getMutableLiveDataAllAppListWithPattern(String pattern) {
        MutableLiveData<List<AppInfo>> mutableLiveDataAppsWithPattern = new MutableLiveData<>();
        mutableLiveDataAppsWithPattern.setValue(getAllAppListWithPattern(pattern));
        return mutableLiveDataAppsWithPattern;
    }

    private List<AppInfo> getAllAppListWithPattern(String pattern) {
        List<AppInfo> newApps = new ArrayList<>();
        for (AppInfo a : allApps) {
            if (a.getPackageName().toLowerCase().contains(pattern.toLowerCase())
                    || a.getAppName().toLowerCase().contains(pattern.toLowerCase())
            ) {
                newApps.add(a);
            }
        }
        return newApps;
    }

    public MutableLiveData<List<AppInfo>> findUnFreezeAppsListWithPattern(String pattern) {
        MutableLiveData<List<AppInfo>> mutableLiveDataAppsWithPattern = new MutableLiveData<>();
        mutableLiveDataAppsWithPattern.setValue(getAllUnFreezeAppListWithPattern(pattern));
        return mutableLiveDataAppsWithPattern;
    }

    private List<AppInfo> getAllUnFreezeAppListWithPattern(String pattern) {
        List<AppInfo> newApps = new ArrayList<>();
        for (AppInfo a : unFreezeApps) {
            if (a.getPackageName().toLowerCase().contains(pattern.toLowerCase())
                    || a.getAppName().toLowerCase().contains(pattern.toLowerCase())
            ) {
                newApps.add(a);
            }
        }
        return newApps;
    }

    public MutableLiveData<Integer> getSelectedReadyToFreezeCount() {
        return selectedReadyToFreezeCount;
    }

    public void setSelectedReadyToFreezeCount(int i) {
        selectedReadyToFreezeCount.setValue(i);
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLiveNotInCategory(List<FreezeApp> categoryId) {
        ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) this.unFreezeApps;
        ArrayList<AppInfo> newAppInfos = (ArrayList<AppInfo>) appInfos.clone();

        MutableLiveData<List<AppInfo>> newAppInfosLive = new MutableLiveData<>();
        for (AppInfo appInfo : appInfos) {
            for (FreezeApp freezeApp : categoryId) {
                if (appInfo.getAppName().equals(freezeApp.getAppName())
                        && appInfo.getPackageName().equals(freezeApp.getPackageName())) {
                    newAppInfos.remove(appInfo);
                }
            }
        }

        newAppInfosLive.setValue(newAppInfos);
        return newAppInfosLive;
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLiveNotInCategoryWithPattern(List<FreezeApp> freezeAppsByCategoryId, String pattern) {
        ArrayList<AppInfo> appInfos = (ArrayList<AppInfo>) this.unFreezeApps;
        ArrayList<AppInfo> newAppInfos = (ArrayList<AppInfo>) appInfos.clone();

        MutableLiveData<List<AppInfo>> newAppInfosLive = new MutableLiveData<>();
        for (AppInfo appInfo : appInfos) {
            //如果不符合匹配则移除
            if (!(appInfo.getAppName().toLowerCase().contains(pattern.toLowerCase()) || appInfo.getPackageName().toLowerCase().contains(pattern.toLowerCase()))) {
                newAppInfos.remove(appInfo);
                continue;
            }

            for (FreezeApp freezeApp : freezeAppsByCategoryId) {
                if (appInfo.getAppName().equals(freezeApp.getAppName())
                        && appInfo.getPackageName().equals(freezeApp.getPackageName())
                ) {
                    newAppInfos.remove(appInfo);
                }
            }
        }
        newAppInfosLive.setValue(newAppInfos);
        return newAppInfosLive;
    }

    public List<String> getFrozenAppListPackageName() {
        List<String> appInfos = new ArrayList<>();

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;

            //不显示系统应用
            boolean b = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if (b) {
                continue;
            }

            //不显示本应用
            if (ai.packageName.equals(context.getPackageName())) {
                continue;
            }

            if(DeviceMethod.getInstance(context).isHidden(ai.packageName)) {
                appInfos.add(ai.packageName);
            }
        }
        return appInfos;
    }
}
