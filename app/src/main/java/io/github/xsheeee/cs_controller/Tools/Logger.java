package io.github.xsheeee.cs_controller.Tools;

import com.topjohnwu.superuser.*;
import com.topjohnwu.superuser.nio.FileSystemManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    private static final String log_path = "/sdcard/Android/CSController/log.txt";
    private static final String DIRECTORY_PATH = "/sdcard/Android/CSController";
    private static final String LOG_FORMAT = "[%tF %<tT] (%s) : %s";
    public static void initLog() {
        if (!checkDirectoryAndFileWithLibSu()) {
            Shell.cmd("su -c mkdir " + DIRECTORY_PATH).exec();
            Shell.cmd("su -c touch " + log_path).exec();
        }
    }
    public static void writeLog(String type, String logMessage) {
        // 尝试写入日志
        Shell.cmd("echo '" + "["+type+"]"+" "+ logMessage + "' > " + log_path).submit();
    }
    public static boolean checkDirectoryAndFileWithLibSu() {

        // 检查目录是否存在
        boolean directoryExists = Shell.cmd("ls " + DIRECTORY_PATH).exec().getCode() == 0;

        // 检查文件是否存在
        boolean fileExists = Shell.cmd("ls " + log_path).exec().getCode() == 0;

        // 返回结果
        return directoryExists && fileExists;
    }

}
