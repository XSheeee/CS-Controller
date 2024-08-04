package io.github.xsheeee.cs_controller.Tools;

import java.io.DataOutputStream;
import java.io.IOException;

public class SetOOM {

    private static final String LOG_TAG = "OomAdjustment";
    private static final int OOM_ADJ_Value = -17;

    public static void setOomScoreAdj(int pid){
        MagiskHelper mhelper = new MagiskHelper();
        mhelper.runShellAndWait("su -c echo "+OOM_ADJ_Value+" > /proc/"+pid+"/oom_adj");
    }

    public static void doit() {
        // 获取当前进程的 PID
        int pid = android.os.Process.myPid();
        setOomScoreAdj(pid);
    }
}