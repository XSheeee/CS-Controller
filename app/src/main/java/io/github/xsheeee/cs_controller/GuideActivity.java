package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.topjohnwu.superuser.Shell;

import java.io.IOException;

import io.github.xsheeee.cs_controller.Tools.MagiskHelper;
import io.github.xsheeee.cs_controller.Tools.Tools;

public class GuideActivity extends AppCompatActivity {
    public static boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Tools tools = new Tools(getApplicationContext());
        try {
            Process process = Runtime.getRuntime().exec("su");
            try {
                int exitCode = process.waitFor(); // 阻塞，等待命令执行结束
                if (exitCode == 0) {
                    //此时获取root权限成功
                    MagiskHelper mhelper = new MagiskHelper();
                    Shell.Result result = mhelper.runShellAndWaitWithResult("cat /sdcard/Android/CSContrller/infoSt");
                    if (result.isSuccess()) {
                        String content = result.getOut().isEmpty() ? null : result.getOut().get(0);
                        boolean isContentOne = "1".equals(content.trim());
                        if (isContentOne) {
                            isFirst = false;
                            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Intent intent = new Intent(GuideActivity.this, InfoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(GuideActivity.this, InfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (InterruptedException e) {
                Intent intent = new Intent(GuideActivity.this, InfoActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (IOException e) {
            Intent intent = new Intent(GuideActivity.this, InfoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}