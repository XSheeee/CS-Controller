package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        //当前默认启动MainActivity
        Intent intent=new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);

    }
}