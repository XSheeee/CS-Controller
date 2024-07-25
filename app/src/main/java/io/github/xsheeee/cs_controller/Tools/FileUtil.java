package io.github.xsheeee.cs_controller.Tools;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    private static final String TAG = "FileUtil";

    public static void createAndWriteToFile(Context context, String textToWrite) {
        // 指定文件路径
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File appSpecificDirectory = new File(externalStorageDirectory, "Android/CSController");

        // 检查并创建目录
        if (!appSpecificDirectory.exists()) {
            if (!appSpecificDirectory.mkdirs()) {
                Log.e(TAG, "Failed to create directory.");
                return;
            }
        }

        // 创建文件
        File file = new File(appSpecificDirectory, "info.txt");

        try {
            // 如果文件不存在，则创建新文件
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    Log.e(TAG, "Failed to create file.");
                    return;
                }
            }

            // 写入文件
            FileWriter writer = new FileWriter(file);
            writer.write(textToWrite);
            writer.close();

            Log.i(TAG, "File written successfully.");
        } catch (IOException e) {
            Log.e(TAG, "Error writing to file.", e);
        }
    }
}