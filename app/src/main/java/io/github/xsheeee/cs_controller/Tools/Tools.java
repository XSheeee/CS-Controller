package io.github.xsheeee.cs_controller.Tools;

import android.content.Context;
import android.widget.Toast;
import com.topjohnwu.superuser.Shell;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.util.Log;

public class Tools {
    private final Context context;
    private static final String LOG_PATH = Values.csLog;
    private static final Map<Integer, String> MODE_MAP = new HashMap<>();
    private static final String TAG = "Tools";
    private static final String CS_CONFIG_PATH = Values.CSConfigPath;

    static {
        MODE_MAP.put(1, Values.powersaveName);
        MODE_MAP.put(2, Values.balanceName);
        MODE_MAP.put(3, Values.performanceName);
        MODE_MAP.put(4, Values.fastName);
        MODE_MAP.put(5, Values.superPowersaveName);
    }

    public Tools(Context context) {
        this.context = context;
    }

    public void init() {
        List<String> paths = Arrays.asList(
                CS_CONFIG_PATH, Values.CSCPath, Values.poPath,
                Values.bPath, Values.pePath, Values.faPath, Values.spPath
        );
        ensureDirectoriesAndFilesExist(paths);
        if (!checkDirectoryAndFileWithLibSu()) {
            createPathWithLibSu(LOG_PATH, true);
        }
    }

    private void ensureDirectoriesAndFilesExist(List<String> paths) {
        for (String path : paths) {
            if (!executeShellCommand("cat " + path)) {
                handlePathError(path);
            }
        }
    }

    private void handlePathError(String path) {
        if (path.equals(CS_CONFIG_PATH)) {
            showErrorToast("未安装 CS 调度");
        } else {
            createPathWithLibSu(path, path.equals(Values.CSCPath));
        }
    }

    private void createPathWithLibSu(String path, boolean isDirectory) {
        String command = isDirectory ? "su -c mkdir -p " : "su -c touch ";
        executeShellCommand(command + path);
    }

    private boolean checkDirectoryAndFileWithLibSu() {
        return executeShellCommand("ls " + LOG_PATH);
    }

    public void showErrorToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, message);
    }

    public boolean getSU() {
        return executeShellCommand("su");
    }

    public void changeMode(int mode) {
        String content = MODE_MAP.getOrDefault(mode, null);
        if (content != null) {
            writeToFile(CS_CONFIG_PATH, content);
        } else {
            showErrorToast("无效的模式");
        }
    }

    public String readFileWithShell(String filePath) {
        Shell.Result result = Shell.cmd("cat " + filePath).exec();
        if (result.isSuccess() && !result.getOut().isEmpty()) {
            return String.join("\n", result.getOut());
        } else {
            String errorMessage = "读取文件失败: " + result.getErr();
            showErrorToast(errorMessage);
            return null;
        }
    }

    public void updateConfigEntry(String filePath, String key, String newValue) {
        String content = readFileWithShell(filePath);
        if (content == null) {
            showErrorToast("无法读取配置文件");
            return;
        }

        String updatedContent = content.replaceAll("(?m)^" + key + " =.*$", key + " = " + newValue);
        if (!updatedContent.contains(key + " =")) { 
            updatedContent += "\n" + key + " = " + newValue;
        }
        
        writeToFile(filePath, updatedContent.trim());
    }

    public void writeToFile(String filePath, String content) {
        if (!executeShellCommand("echo \"" + content.replace("\"", "\\\"") + "\" > " + filePath)) {
            showErrorToast("写入失败");
        }
    }

    public String getVersionFromModuleProp() {
        String filePath = Values.csmodulePath;
        Shell.Result result = Shell.cmd("cat " + filePath + " | grep version=").exec();
        if (result.isSuccess() && !result.getOut().isEmpty()) {
            return result.getOut().get(0).replace("version=", "").trim();
        } else {
            String errorMessage = "读取module.prop失败: " + result.getErr();
            showErrorToast(errorMessage);
            return null;
        }
    }

    public boolean isProcessRunning(String processPath) {
        Shell.Result result = Shell.cmd("pgrep -f " + processPath).exec();
        return result.isSuccess() && !result.getOut().isEmpty();
    }

    public String readLogFile() {
        return readFileWithShell(LOG_PATH);
    }

    private boolean executeShellCommand(String command) {
        Shell.Result result = Shell.cmd(command).exec();
        if (!result.isSuccess()) {
            Log.e(TAG, "Shell command failed: " + command + " | Error: " + result.getErr());
        }
        return result.isSuccess();
    }
}