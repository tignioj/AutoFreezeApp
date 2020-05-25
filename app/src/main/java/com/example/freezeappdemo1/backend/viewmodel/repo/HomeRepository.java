package com.example.freezeappdemo1.backend.viewmodel.repo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.util.ArrayList;
import java.util.List;

public class HomeRepository {
    private static HomeRepository homeRepository;
    Context context;

    MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppList;
    MutableLiveData<List<AppInfo>> mutableLiveDataFrozenAppList;
    MutableLiveData<List<AppInfo>> mutableLiveDataAllAppList;


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

    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataAllAppList() {
        return mutableLiveDataAllAppList;
    }

    /**
     * 获取所有的非系统的，已经冻结的包
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
        mutableLiveDataAllAppList.setValue(this.allApps);

        this.frozenApps = getFrozenAppList();
        mutableLiveDataFrozenAppList.setValue(this.frozenApps);

        this.unFreezeApps = getUnFreezeAppList();
        mutableLiveDataUnFreezeAppList.setValue(this.unFreezeApps);
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
}
