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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Tools {
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private static Context context;

    public Tools(Context context) {
        this.context = context;
    }

    public void showToast(String str) {
        Toast makeText = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }

    public boolean getSU(){
        MagiskHelper mhelper = new MagiskHelper();
        Shell.Result result = mhelper.runShellAndWaitWithResult("su");
        if(!result.isSuccess()){
            return false;
        }
        return true;
    }
}

