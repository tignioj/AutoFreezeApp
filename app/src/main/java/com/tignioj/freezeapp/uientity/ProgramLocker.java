package com.tignioj.freezeapp.uientity;

import androidx.lifecycle.MutableLiveData;

public class ProgramLocker {
    private String startTime;
    private String endTime;
    private boolean isEnable;
    private boolean isHideIcon;

    @Override
    public String toString() {
        return "ProgramLocker{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", isEnable=" + isEnable +
                ", isHideIcon=" + isHideIcon +
                '}';
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isHideIcon() {
        return isHideIcon;
    }

    public void setHideIcon(boolean hideIcon) {
        isHideIcon = hideIcon;
    }

    public ProgramLocker() {
    }

    public ProgramLocker(String startTime, String endTime, boolean isEnable, boolean isHideIcon) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.isEnable = isEnable;
        this.isHideIcon = isHideIcon;
    }
}
