package com.example.freezeappdemo1;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final int REQUEST_CODE_WRITE_SETTINGS = 0x10;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText2);
        editText.setText("pm disable-user --user 0 com.example.myapp");

        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    freeze(v);
                    return true;
                }
                return false;
            }
        });
    }

    boolean canWrite;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(getApplicationContext())) {
                    Log.d("myTag", "你获取了权限");
                    canWrite = true;
                } else {
                    Log.d("myTag", "你拒绝了权限");
                    canWrite = false;
                }
            }
        }
    }

    /**
     * 点击按钮执行
     * @param view
     */
    public void freeze(View view) {
        /**
         * 检测系统修改权限
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(MainActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            } else {
                // 如果有权限做些什么
                canWrite = true;
            }
        }
        Process process = null;
        try {
            String command = editText.getText().toString();
            if (canWrite) {
                process = Runtime.getRuntime().exec(command);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d("myTag", line);
                    sb.append(line + "\n");
                }
                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                editText.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "没有权限", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
