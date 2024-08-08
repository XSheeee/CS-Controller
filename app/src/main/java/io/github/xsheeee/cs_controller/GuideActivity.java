package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import io.github.xsheeee.cs_controller.Tools.Logger;
import io.github.xsheeee.cs_controller.Tools.SetOOM;
import io.github.xsheeee.cs_controller.Tools.Tools;
import io.github.xsheeee.cs_controller.Tools.Values;

public class GuideActivity extends AppCompatActivity {
    private static final String FILE_PATH = "/storage/emulated/0/Android/CSController/infoSt.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        Tools ts = new Tools(getApplicationContext());
        ts.showToast(String.valueOf(android.os.Process.myPid()));
        if(!ts.getSU())finish();
//        readFileContent();
//        ts.showToast(SetOOM.doit()?"true":"false");
        SetOOM.doit();
        ts.showToast(String.valueOf(android.os.Process.myPid()));
        ts.init();

//        ts.showToast("01");
        Logger.writeLog("Info", "fck u Google");
//        ts.showToast("02");
        //判断是否为第一次进入
        SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);


        //默认false
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Values.isFirst = !isFirstRun;

        if (!isFirstRun) {
            Intent intent;
            intent = new Intent(GuideActivity.this, InfoActivity.class);
            startActivity(intent);
            finish(); // 关闭 GuideActivity
        } else {
            Intent intent;
            intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // 关闭 GuideActivity
        }

    }


}