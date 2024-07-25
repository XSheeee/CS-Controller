package io.github.xsheeee.cs_controller.Tools;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class Tools {
    private static Context context;

    public Tools(Context context) {
        this.context = context;
    }

    public void showToast(String str) {
        Toast makeText = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }
}
