package com.example.freezeappdemo1;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //判断是否可使用
        boolean admin = DeviceMethod.getInstance(getApplicationContext()).isAdmin();
        if (!admin) {
            Toast.makeText(getApplicationContext(), "请先激活, 设置为device owner", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        startService(new Intent(getApplicationContext(), FreezeService.class));
    }

}
