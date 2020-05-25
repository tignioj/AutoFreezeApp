package com.example.freezeappdemo1.backend.viewmodel.repo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.utils.ShpUtils;

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

    private HomeRepository(Context context) {
        this.context = context;
        mutableLiveDataUnFreezeAppList = new MutableLiveData<>();
        mutableLiveDataUnFreezeAppList.setValue(getUnFreezeAppList());

        mutableLiveDataFrozenAppList = new MutableLiveData<>();
        mutableLiveDataFrozenAppList.setValue(getFrozenAppList());

        mutableLiveDataAllAppList = new MutableLiveData<>();
        mutableLiveDataAllAppList.setValue(getAllAppList());
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataAllAppList() {
        return mutableLiveDataAllAppList;
    }

    /**
     * 获取所有的非系统安装包
     *
     * @return
     */
    public List<AppInfo> getFrozenAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;
            //非隐藏的跳过
            if (!DeviceMethod.getInstance(context).isHidden(ai.packageName)) {
                continue;
            }

            AppInfo a = new AppInfo();
            a.setHidden(true);
            a.setAppName((String) pm.getApplicationLabel(ai));
            a.setPackageName(p.packageName);
            a.setIcon(ai.loadIcon(pm));

            a.setIconLayout(ai.icon);
            appInfos.add(a);
        }
        return appInfos;
    }


    /**
     * 获取所有的非系统安装包
     *
     * @return
     */
    public List<AppInfo> getUnFreezeAppList() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);

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


    public MutableLiveData<List<AppInfo>> getMutableLiveDataUnFreezeAppListLive() {
        return mutableLiveDataUnFreezeAppList;
    }

    public MutableLiveData<List<AppInfo>> getMutableLiveDataFrozenAppList() {
        return mutableLiveDataFrozenAppList;
    }


    public void updateFrozenApps() {
        mutableLiveDataFrozenAppList.setValue(getFrozenAppList());
    }

    public void updateUnFreezeApps() {
        mutableLiveDataUnFreezeAppList.setValue(getUnFreezeAppList());
    }

    public void updateAll() {
        updateFrozenApps();
        updateUnFreezeApps();
        updateAllApps();
    }

    private void updateAllApps() {
        mutableLiveDataAllAppList.setValue(getAllAppList());
    }


    public List<AppInfo> getAllAppList() {
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
     * 不加载图标，速度更快
     * @return
     */
    public List<AppInfo> getFrozenAppListNoIcon() {
        List<AppInfo> appInfos = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);

        for (PackageInfo p : installedPackages) {
            ApplicationInfo ai = p.applicationInfo;
            //非隐藏的跳过
            if (!DeviceMethod.getInstance(context).isHidden(ai.packageName)) {
                continue;
            }

            AppInfo a = new AppInfo();
            a.setHidden(true);
            a.setAppName((String) pm.getApplicationLabel(ai));
            a.setPackageName(p.packageName);
//            a.setIcon(ai.loadIcon(pm));
//            a.setIconLayout(ai.icon);

            appInfos.add(a);
        }
        return appInfos;
    }
}
