package io.github.xsheeee.cs_controller.Tools;

import com.topjohnwu.superuser.*;
import com.topjohnwu.superuser.nio.FileSystemManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    public static final String log_path = "/sdcard/Android/CSController/log.txt";
    public static void writeLog(String type, String logMessage) {
        // 尝试写入日志
        Shell.cmd("echo '" + "["+type+"]"+" "+ logMessage + "' > " + log_path).submit();
    }

}
