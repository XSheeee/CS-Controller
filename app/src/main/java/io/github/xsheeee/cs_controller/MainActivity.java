package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE = 1;
    private static boolean permissionStatus = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    // 检查应用是否已获得管理外部存储的权限，如果没有，则请求权限
    private void checkAndGetThePermissionOfFile() {
        // 检查权限状态
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // 请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE);
        }
    }

    // 处理权限请求的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被授予
                permissionStatus=true;
            } else {
                finish();
            }
        }
    }
}