package com.tignioj.freezeapp.uientity;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private String appName;
    private String packageName;
    private Drawable icon;
    private int iconLayout;
    private boolean isHidden;
    private boolean isSystemApp;

    private boolean isSelectedReadyToFreeze;

    public int getIconLayout() {
        return iconLayout;
    }

    public void setIconLayout(int iconLayout) {
        this.iconLayout = iconLayout;
    }

    public boolean isSelectedReadyToFreeze() {
        return isSelectedReadyToFreeze;
    }

    public void setSelectedReadyToFreeze(boolean selectedReadyToFreeze) {
        isSelectedReadyToFreeze = selectedReadyToFreeze;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", isSelectedReadyToFreeze=" + isSelectedReadyToFreeze +
                '}';
    }

    public AppInfo() {
    }

    public boolean isSelected() {
        return isSelectedReadyToFreeze;
    }

    public void setSelected(boolean selectedReadyToFreeze) {
        isSelectedReadyToFreeze = selectedReadyToFreeze;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public AppInfo(String appName, String packageName, Drawable icon, boolean isHidden, boolean isSystemApp, boolean isSelectedReadyToFreeze) {
        this.appName = appName;
        this.packageName = packageName;
        this.icon = icon;
        this.isHidden = isHidden;
        this.isSystemApp = isSystemApp;
        this.isSelectedReadyToFreeze = isSelectedReadyToFreeze;
    }
}
