package com.tignioj.freezeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.ui.setting.SettingFragment;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.Inform;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InitApplication.getInstance().isNightModeEnabled()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);

//        DeviceMethod.getInstance(getApplicationContext()).onRemoveActivate();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Inform.setMainActivity(MainActivity.this);

        Log.d(MyConfig.MY_TAG, "reload");

//        NavigationUI.setupActionBarWithNavController(this, navController);

        //判断是否可使用
        boolean admin = DeviceMethod.getInstance(getApplicationContext()).isAdmin();
        if (!admin) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Inform.alert(R.string.please_activate_alert_title, R.string.activate_tips,
                            R.string.check_link_buton_text, R.string.exit_text,
                            new Inform.Callback() {
                                @Override
                                public void ok() {
                                    Uri parse = Uri.parse(getString(R.string.githubURL));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void cancel() {
                                    finish();
                                }

                                @Override
                                public void dismiss() {
                                    finish();
                                }
                            });
                }
            }, 0);
        } else {
            startService(new Intent(getApplicationContext(), FreezeService.class));
        }


        //侧边栏
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();

        //添加返回按钮到导航栏
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
