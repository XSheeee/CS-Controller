package io.github.xsheeee.cs_controller.Tools;

import com.topjohnwu.superuser.Shell;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SetOOM {

    private static final String LOG_TAG = "OomAdjustment";
    private static final int OOM_ADJ_Value = -17;

    public static void setOomScoreAdj(int pid){
        MagiskHelper mhelper = new MagiskHelper();
        mhelper.runShellAndWait("su -c chmod"+" /proc/"+pid+"/oom_adj"+" 777");
        mhelper.runShellAndWait("su -c chattr -i "+" /proc/"+pid+"/oom_adj");
        mhelper.runShellAndWait("su -c echo "+"'"+OOM_ADJ_Value+"'"+" > /proc/"+pid+"/oom_adj");
        mhelper.runShellAndWait("su -c chattr +i "+" /proc/"+pid+"/oom_adj");
//        return result.isSuccess();
//        File f = new File("/proc/"+pid+"/oom_adj");
//        try {
//            FileWriter fw = new FileWriter(f);
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.write(OOM_ADJ_Value);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    public static void doit() {
        // 获取当前进程的 PID
        int pid = android.os.Process.myPid();
//        return setOomScoreAdj(pid);
        setOomScoreAdj(pid);
    }
}