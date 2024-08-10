package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import io.github.xsheeee.cs_controller.Tools.SetOOM;
import io.github.xsheeee.cs_controller.Tools.Tools;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.widget.AppCompatImageView;

public class MainActivity extends AppCompatActivity {

    private Tools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetOOM.doit();

        tools = new Tools(getApplicationContext());

        setupButton(R.id.powersave, 1);
        setupButton(R.id.balance, 2);
        setupButton(R.id.performance, 3);
        setupButton(R.id.fast, 4);
        setupButton(R.id.super_powersave, 5);

        AppCompatImageView logo = findViewById(R.id.main_logo);
        logo.setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });

        MaterialButton appListConfigButton = findViewById(R.id.go_app_list);
        appListConfigButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppListActivity.class);
            startActivity(intent);
        });
    }

    private void setupButton(int buttonId, int mode) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(v -> tools.changeMode(mode));
    }
}