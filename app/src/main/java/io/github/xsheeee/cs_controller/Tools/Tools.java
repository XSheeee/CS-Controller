package io.github.xsheeee.cs_controller.Tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.topjohnwu.superuser.Shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Tools {
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private static Context context;
    MagiskHelper mhelper = new MagiskHelper();

    public Tools(Context context) {
        this.context = context;
    }

    public void showToast(String str) {
        Toast makeText = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }

    public boolean getSU() {
        Shell.Result result = mhelper.runShellAndWaitWithResult("su");
        if (!result.isSuccess()) {
            return false;
        }
        return true;
    }

    private static final String CS_CONFIG_PATH = Values.CSConfigPath; // 假设这个路径已经在Values类中定义

    public void changeMode(int mode) {
        switch (mode) {
            case 1:
                writeToFile(CS_CONFIG_PATH, Values.powersaveName);
                break;
            case 2:
                writeToFile(CS_CONFIG_PATH, Values.balanceName);
                break;
            case 3:
                writeToFile(CS_CONFIG_PATH, Values.performanceName);
                break;
            case 4:
                writeToFile(CS_CONFIG_PATH, Values.fastName);
                break;
            case 5:
                writeToFile(CS_CONFIG_PATH, Values.superPowersaveName);
                break;
            default:
                // 不做任何操作
                break;
        }
    }

    private void writeToFile(String filePath, String content) {
        try {
            // 清空文件
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            writer.write(""); // 清空文件

            // 写入新的内容
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

