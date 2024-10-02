package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import io.github.xsheeee.cs_controller.Tools.Tools;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 使用 SharedPreferences 检查用户是否已接受
        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        boolean hasAccepted = preferences.getBoolean("hasAccepted", false);
        
        if (hasAccepted) {
            // 用户已经接受，直接跳转到 MainActivity
            Intent intent = new Intent(InfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return; // 结束当前活动
        }

        setContentView(R.layout.activity_info);

        Button refuseButton = findViewById(R.id.info_refuse);
        Button acceptButton = findViewById(R.id.info_accept);

        // 工具对象
        Tools tools = new Tools(getApplicationContext());

        // 设置接受按钮的点击事件
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 用户接受，保存状态并打开主Activity
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("hasAccepted", true);
                editor.apply();
                
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 设置拒绝按钮的点击事件
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.showToast("不同意将退出应用");
                finish();
            }
        });
    }
}
