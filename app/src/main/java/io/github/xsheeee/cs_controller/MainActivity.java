package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;

import io.github.xsheeee.cs_controller.Tools.FileUtil;
import io.github.xsheeee.cs_controller.Tools.MagiskHelper;
import io.github.xsheeee.cs_controller.Tools.Tools;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tools tools = new Tools(getApplicationContext());
        if (GuideActivity.isFirst) {
            try {
                Process process = Runtime.getRuntime().exec("su");
                try {
                    int exitCode = process.waitFor(); // 阻塞，等待命令执行结束
                    if (exitCode == 0) {
                        //此时获取root权限成功
                        MagiskHelper mhelper = new MagiskHelper();
                        mhelper.runShellAndWait("mkdir /sdcard/Android/CSContrller/");
                        mhelper.runShellAndWait("touch /sdcard/Android/CSContrller/infoSt");
                        mhelper.runShell("echo 1 > /sdcard/Android/CSContrller/infoSt");
                    } else {
                        tools.showToast("获取su权限失败，退出应用");
                        finish();
                    }
                } catch (InterruptedException e) {
                    tools.showToast("获取su权限失败，退出应用");
                    finish();
                }
            } catch (IOException e) {
                tools.showToast("获取su权限失败，退出应用");
                finish();
            }
        }
    }


}