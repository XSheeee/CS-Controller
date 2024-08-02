package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.github.xsheeee.cs_controller.Tools.Tools;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        //接受拒绝的两个按钮
        Button refuseButton = findViewById(R.id.info_refuse);
        Button acceptButton = findViewById(R.id.info_accept);

        //工具对象
        Tools tools = new Tools(getApplicationContext());

        //设置监听器
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此时用户接受
                //打开主Activity
                Intent intent = new Intent(InfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        refuseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此时用户拒绝
                tools.showToast("不同意将退出应用");
                finish();
            }
        });

    }

    ;

}
