package com.example.freezeappdemo1.entity;

public class AppInfo {
    private String appName;
    private String icon;
    private boolean isFrozen;

    public AppInfo() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }

    public AppInfo(String appName, String icon, boolean isFrozen) {
        this.appName = appName;
        this.icon = icon;
        this.isFrozen = isFrozen;
    }
}
