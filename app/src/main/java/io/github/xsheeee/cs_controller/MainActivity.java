package io.github.xsheeee.cs_controller;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;

import io.github.xsheeee.cs_controller.Tools.SetOOM;
import io.github.xsheeee.cs_controller.Tools.Tools;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetOOM.doit();

        com.google.android.material.button.MaterialButton powersaveButton = findViewById(R.id.powersave);
        com.google.android.material.button.MaterialButton balanceButton = findViewById(R.id.balance);
        com.google.android.material.button.MaterialButton performanceButton = findViewById(R.id.performance);
        com.google.android.material.button.MaterialButton fastButton = findViewById(R.id.fast);
        com.google.android.material.button.MaterialButton superPowersaveButton = findViewById(R.id.super_powersave);
        com.google.android.material.button.MaterialButton appListConfigButton = findViewById(R.id.go_app_list);
        Tools tools = new Tools(getApplicationContext());
        powersaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.changeMode(1);
            }
        });
        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.changeMode(2);
            }
        });
        performanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.changeMode(3);
            }
        });
        fastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.changeMode(4);
            }
        });
        superPowersaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tools.changeMode(5);
            }
        });
        appListConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppListActivity.class);
                startActivity(intent);
            }
        });

    }
}



