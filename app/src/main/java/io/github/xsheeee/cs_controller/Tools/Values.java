package io.github.xsheeee.cs_controller.Tools;

import android.annotation.SuppressLint;

import com.topjohnwu.superuser.Shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Values {
    public static boolean isFirst;
    public static final String CSConfigPath = "/storage/emulated/0/Android/MW_CpuSpeedController/config.txt";
    public static final String CSCPath = "/sdcard/Android/CSController/";
    public static final String bPath = "/sdcard/Android/CSController/balance.txt";
    public static final String poPath = "/sdcard/Android/CSController/powersave.txt";
    public static final String pePath = "/sdcard/Android/CSController/performance.txt";
    public static final String faPath = "/sdcard/Android/CSController/fast.txt";
    public static final String spPath = "/sdcard/Android/CSController/super_powersave.txt";
    public static final String csmodulePath = "/data/adb/modules/MW_CpuSpeedController/module.prop";
    public static final String csProcess = "/data/adb/modules/MW_CpuSpeedController/system/bin/MW_CpuSpeedController";
    public static final String csLog = "/storage/emulated/0/Android/MW_CpuSpeedController/log.txt";
    public static final String csSettings = "/storage/emulated/0/Android/MW_CpuSpeedController/config.ini";
    public static final String balanceName = "balance";
    public static final String powersaveName = "powersave";
    public static final String performanceName = "performance";
    public static final String fastName = "fast";
    public static final String superPowersaveName = "super_powersave";
    public static String powersaveConfigPath = "/path/to/powersave/config";
    public static String balanceConfigPath = "/path/to/balance/config";
    public static String performanceConfigPath = "/path/to/performance/config";
    public static String fastConfigPath = "/path/to/fast/config";
    public static String superPowersaveConfigPath = "/path/to/superPowersave/config";

    // 这里定义模式对应的包名列表
    public static List<String> powersaveList = new ArrayList<>();
    public static List<String> balanceList = new ArrayList<>();
    public static List<String> performanceList = new ArrayList<>();
    public static List<String> fastList = new ArrayList<>();
    public static List<String> superPowersaveList = new ArrayList<>();
    public static List<String> poList = new ArrayList<>();
    public static List<String> baList = new ArrayList<>();
    public static List<String> peList = new ArrayList<>();
    public static List<String> faList = new ArrayList<>();
    public static List<String> spList = new ArrayList<>();
    private static List<String> filePaths = Arrays.asList(
            poPath,
            bPath,
            pePath,
            faPath,
            spPath
    );
    public static List<List<String>> lists = Arrays.asList(
            poList,
            baList,
            peList,
            faList,
            spList
    );

    public static void updateLists() {
        for (int i = 0; i < filePaths.size(); i++) {
            Shell.Result result = Shell.cmd("su -c cat " + filePaths.get(i)).exec();
            if (result.isSuccess()) {

                // 清除旧的数据
                lists.get(i).clear();

                // 添加每一行到列表中
                for (String line : result.getOut()) {
                    if (!line.trim().isEmpty()) { // 跳过空行
                        lists.get(i).add(line.trim());
                    }
                }
            }
        }
    }

    public static void toUpdateLists() {
        for (int i = 0; i < lists.size(); i++) { // 使用 '<' 而不是 '<='
            StringBuilder dataBuilder = new StringBuilder(); // 使用 StringBuilder 提高效率

            for (String p : lists.get(i)) {
                if (dataBuilder.length() > 0) {
                    dataBuilder.append('\n'); // 使用正确的换行符
                }
                dataBuilder.append(p);
            }

            String data = dataBuilder.toString();
            Shell.cmd("echo \"" + data + "\" > " + filePaths.get(i)).exec(); // 使用双引号包围 data
        }
    }
}
