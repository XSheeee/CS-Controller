package io.github.xsheeee.cs_controller.Tools;

import java.io.DataOutputStream;
import java.io.IOException;

public class SetOOM {

    private static final String LOG_TAG = "OomAdjustment";
    private static final int OOM_ADJ_Value = -17;

    public static void setOomScoreAdj(int pid) {
        try {
            // 执行 su 命令并调整 oom_score_adj
            DataOutputStream os = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
            os.writeBytes("echo " + OOM_ADJ_Value + " > /proc/" + pid + "/oom_score_adj\n");
            os.writeBytes("exit\n");
            os.flush();

            // 关闭输出流
            os.close();
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    public static void doit() {
        // 获取当前进程的 PID
        int pid = android.os.Process.myPid();
        setOomScoreAdj(pid);
    }
}