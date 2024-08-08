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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Tools {
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private static Context context;
    private static final String DIRECTORY_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/CSController";
    private static final String LOG_PATH = DIRECTORY_PATH + "/log.txt";
    MagiskHelper mhelper = new MagiskHelper();

    public Tools(Context context) {
        this.context = context;
    }

    public void init() {
        List<String> paths = Arrays.asList(
                Values.CSConfigPath,
                Values.CSCPath,
                Values.poPath,
                Values.bPath,
                Values.pePath,
                Values.faPath,
                Values.spPath
        );

        // Check and create directories and files if necessary
        ensureDirectoryAndFilesExist(paths);

        if (!checkDirectoryAndFileWithLibSu()) {
            createDirectoryAndFileWithLibSu();
        }
    }

    private void ensureDirectoryAndFilesExist(List<String> paths) {
        for (String path : paths) {
            Shell.Result result = Shell.cmd("cat " + path).exec();
            if (!result.isSuccess()) {
                // File does not exist or cannot be read
                if(path==paths.get(0))showToast("捏麻麻的没安装CS调度");
                else if(path==paths.get(1))createFolder(path);
                else createFile(path);
            }
        }
    }

    private void createFile(String path) {
        Shell.cmd("su -c touch " + path).exec(); // Create the file
    }
    private void createFolder(String path){
        Shell.cmd("su -c mkdir -p " + path).exec(); // Ensure the directory exists
    }

    private boolean checkDirectoryAndFileWithLibSu() {
        // 检查目录是否存在
        boolean directoryExists = Shell.cmd("ls " + DIRECTORY_PATH).exec().getCode() == 0;

        // 检查文件是否存在
        boolean fileExists = Shell.cmd("ls " + LOG_PATH).exec().getCode() == 0;

        // 返回结果
        return directoryExists && fileExists;
    }

    private void createDirectoryAndFileWithLibSu() {
        Shell.cmd("su -c mkdir -p " + DIRECTORY_PATH).exec();
        Shell.cmd("su -c touch " + LOG_PATH).exec();
    }

    public void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public boolean getSU() {
        Shell.Result result = mhelper.runShellAndWaitWithResult("su");
        return result.isSuccess();
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
            // Write content to the file
            Shell.Result result = Shell.cmd("echo " + content + " > " + filePath).exec();
            if (result.isSuccess()) {
                showToast("成功写入 " + content);
            } else {
                showToast("失败");
                Logger.writeLog("Error", "失败: " + result.getOut() + " 和 " + result.getErr());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("发生错误: " + e.getMessage());
            Logger.writeLog("Error", "写入文件时发生错误: " + e.getMessage());
        }
    }
}