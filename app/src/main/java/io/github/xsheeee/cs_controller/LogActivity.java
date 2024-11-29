package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import io.github.xsheeee.cs_controller.Tools.Tools;

public class LogActivity extends AppCompatActivity {

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        
        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.backButton4);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish()); // 返回上一个活动


        // 初始化 Tools
        tools = new Tools(this);
        String logData = tools.readLogFile(); // 获取日志内容

        TextView logTextView = findViewById(R.id.log_text_view);
        logTextView.setText(logData != null ? logData : "无法读取日志文件");
    }
}