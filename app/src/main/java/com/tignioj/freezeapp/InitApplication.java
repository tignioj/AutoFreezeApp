package com.tignioj.freezeapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.tignioj.freezeapp.config.MyConfig;

public class InitApplication extends Application {
    private boolean isNightModeEnabled = false;

    private static InitApplication singleton = null;

    public static InitApplication getInstance() {
        if (singleton == null) {
            singleton = new InitApplication();
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isNightModeEnabled = mPrefs.getBoolean(MyConfig.PERSONAL_SHP_CONFIG_KEY_IS_NIGHT_MODE, true);
    }

    public boolean isNightModeEnabled() {
        return isNightModeEnabled;
    }

    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.isNightModeEnabled = isNightModeEnabled;

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(MyConfig.PERSONAL_SHP_CONFIG_KEY_IS_NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }
}