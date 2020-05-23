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

    MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppList;
    MutableLiveData<List<AppInfo>> mutableLiveDataFrozenAppList;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        mutableLiveDataUnFreezeAppList = new MutableLiveData<>();
        mutableLiveDataUnFreezeAppList.setValue(getUnFreezeAppList());

        mutableLiveDataFrozenAppList = new MutableLiveData<>();
        mutableLiveDataFrozenAppList.setValue(getFrozenAppList());
    }


    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLive() {
        return mutableLiveDataUnFreezeAppList;
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataFrozenAppList() {
        return mutableLiveDataFrozenAppList;
    }

    /**
     * 获取所有的非系统安装包
     *
     * @return
     */
    private  List<AppInfo> getFrozenAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;
            if (!DeviceMethod.getInstance(context).isHidden(ai.packageName)) {
                continue;
            }

            AppInfo a = new AppInfo();
            a.setAppName((String) pm.getApplicationLabel(ai));
            a.setPackageName(p.packageName);
            a.setIcon(ai.loadIcon(pm));

            a.setHidden(DeviceMethod.getInstance(context).isHidden(ai.packageName));
            appInfos.add(a);
        }
        return appInfos;
    }


    /**
     * 获取所有的非系统安装包
     *
     * @return
     */
    private List<AppInfo> getUnFreezeAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;
            AppInfo a = new AppInfo();
            boolean b = (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if (b) {
                continue;
            }
            a.setAppName((String) pm.getApplicationLabel(ai));
            a.setPackageName(p.packageName);
            a.setIcon(ai.loadIcon(pm));

            a.setHidden(DeviceMethod.getInstance(context).isHidden(ai.packageName));
            appInfos.add(a);
        }
        return appInfos;
    }

    public void updateFrozenApps() {
        mutableLiveDataFrozenAppList.setValue(getFrozenAppList());
    }

    public void updateUnFreezeApps() {
        mutableLiveDataUnFreezeAppList.setValue(getUnFreezeAppList());
    }

    public void  updateAll() {
        updateFrozenApps();
        updateUnFreezeApps();
    }
}
