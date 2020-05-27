package com.tignioj.freezeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.service.MyBroadCastService;
import com.tignioj.freezeapp.utils.DeviceMethod;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeviceMethod.getInstance(getApplicationContext()).onRemoveActivate();


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
