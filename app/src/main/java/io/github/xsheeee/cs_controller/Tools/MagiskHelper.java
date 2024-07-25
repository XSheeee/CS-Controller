package io.github.xsheeee.cs_controller.Tools;

import android.content.Context;

import com.topjohnwu.superuser.Shell;

public class MagiskHelper {

    //该函数执行一个需要等待并且有返回结果的命令
    public Shell.Result runShellAndWaitWithResult(String cmd) {
        Shell.Result result = Shell.cmd(cmd).exec();
        return result;
    }

    //该函数执行一个需要等待的不带结果的命令
    public Shell.Result runShellAndWait(String cmd) {
        Shell.Result result = Shell.cmd(cmd).exec();
        return result;
    }

    //该函数执行一个不等待的不带结果的命令
    public void runShell(String cmd) {
        Shell.cmd(cmd).submit();
    }

}