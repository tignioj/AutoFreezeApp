package com.example.freezeappdemo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.freezeappdemo1.service.FreezeService;
import com.example.freezeappdemo1.service.MyBroadCastService;
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //添加返回按钮到导航栏
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

        //判断是否可使用
        boolean admin = DeviceMethod.getInstance(getApplicationContext()).isAdmin();
        if (!admin) {
            Toast.makeText(getApplicationContext(), "请先激活, 先退出账号，再设置该应用为为device owner", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        startService(new Intent(getApplicationContext(), FreezeService.class));
        startService(new Intent(getApplicationContext(), MyBroadCastService.class));
    }

    /**
     * 导航栏上返回按钮可用需要重写此方法
     *
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        return navController.navigateUp();
    }

    public void debug(View view) {
//        DeviceMethod.getInstance(getApplicationContext())
    }
}
