package com.example.freezeappdemo1.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShpUtils {

    /**
     * 更新冻结应用列表
     * @param context
     * @param homeViewModel
     */
    public static void updateAppFrozenList(Context context, HomeViewModel homeViewModel) {
        SharedPreferences shp = context.getSharedPreferences(MyConfig.SHP_FREEZE_APP_LIST_FOR_TIMER, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();

        edit.remove(MyConfig.FREEZE_APP_LIST_FOR_TIMER);
        List<AppInfo> value = homeViewModel.getMutableLiveDataFrozenAppList().getValue();

        HashSet<String> apps = new HashSet<>();
        for (AppInfo a : value) {
            apps.add(a.getPackageName());
        }
        edit.putStringSet(MyConfig.FREEZE_APP_LIST_FOR_TIMER, apps);
        edit.apply();
    }

    public static Set<String> getAppFrozenSetFromShp(Context context) {
        SharedPreferences shp = context.getSharedPreferences(MyConfig.SHP_FREEZE_APP_LIST_FOR_TIMER, Context.MODE_PRIVATE);
        return shp.getStringSet(MyConfig.FREEZE_APP_LIST_FOR_TIMER, new HashSet<String>());
    }
}
