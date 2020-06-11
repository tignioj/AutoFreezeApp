package com.tignioj.freezeapp.config;

public class MyConfig {

    //SHP文件Start=======================================================
    public static final String PERSONAL_SHP_CONFIG_DB = "personal_shp_config_db";

    //key
    public static final String PERSONAL_SHP_CONFIG_KEY_IS_NIGHT_MODE = "personal_shp_config_key_is_night_mode";
    public static final String PERSONAL_SHP_CONFIG_KEY_EDITABLE_START_TIME = "editable_start_time";
    public static final String PERSONAL_SHP_CONFIG_KEY_EDITABLE_END_TIME = "editable_end_time";
    public static final String PERSONAL_SHP_CONFIG_KEY_EDITABLE_ENABLE = "editable_enable";
    public static final String PERSONAL_SHP_CONFIG_KEY_HIDE_ICON = "hide_icon";
    public static final String PERSONAL_SHP_CONFIG_KEY_ENABLE_ALL_TASKS = "enable_all_tasks";

    //Shp文件End=============================================================


    //调试Start=============================================
    public static final String MY_TAG = "myTag";
    public static final String BROADCAST_RECEIVER = "broadcast_receiver";
    public static final String TAG_ERR = "tag_err";
    public static final String LOG_TAG_TIMER_FRAGMENT = "timer_fragment";
    public static final String LOG_TAG_FREEZE_SERVICE = "freeze_service";
    public static final String HOME_FRAGMENT_TAG = "home_fragment";

    public static final String LOG_TAG_MyDeviceAdminReceiver = "mydeviceAdminReceiver";
    public static final String LOG_TAG_MyBootReceiver = "MyBootReceiver";

    //调试End=============================================

    //其它配置===========
    //冷静时间，禁用App本身后过多久启用,默认5分钟
    public static final long CALM_TIME = 5 * 60 * 1000;
//    public static final long CALM_TIME = 15 * 1000;

}
