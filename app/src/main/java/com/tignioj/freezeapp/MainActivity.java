package com.tignioj.freezeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.google.android.material.navigation.NavigationView;
import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.Inform;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DeviceMethod.getInstance(getApplicationContext()).onRemoveActivate();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Inform.setMainActivity(MainActivity.this);

//        NavigationUI.setupActionBarWithNavController(this, navController);

        //判断是否可使用
        boolean admin = DeviceMethod.getInstance(getApplicationContext()).isAdmin();
        if (!admin) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Inform.alert("请先激活", "请先激活, 先退出账号，再设置该应用为device owner, 激活方法请看https://github.com/tignioj/AutoFreezeApp",
                            "查看链接", "退出",
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
        }
        startService(new Intent(getApplicationContext(), FreezeService.class));


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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
