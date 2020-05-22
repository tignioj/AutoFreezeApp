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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText2);

        //执行的命令
        editText.setText("com.example.myapp");
    }


    /**
     * 冻结App
     * @param view
     */
    public void freeze(View view) {
        String packageName = editText.getText().toString();
        DeviceMethod.getInstance(getApplicationContext()).freeze(packageName, true);
    }

    /**
     * 解冻App
     * @param view
     */
    public void  unfreeze(View view) {
        String packageName = editText.getText().toString();
        DeviceMethod.getInstance(getApplicationContext()).freeze(packageName, false);
    }
}
