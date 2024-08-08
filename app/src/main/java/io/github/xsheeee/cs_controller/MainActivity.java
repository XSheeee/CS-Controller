package io.github.xsheeee.cs_controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.github.xsheeee.cs_controller.Tools.AppInfo;

import io.github.xsheeee.cs_controller.Tools.Logger;
import io.github.xsheeee.cs_controller.Tools.MagiskHelper;
import io.github.xsheeee.cs_controller.Tools.SetOOM;
import io.github.xsheeee.cs_controller.Tools.Tools;
import io.github.xsheeee.cs_controller.Tools.Values;

import androidx.appcompat.widget.SearchView.OnQueryTextListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetOOM.doit();
        Tools tools = new Tools(getApplicationContext());
        com.google.android.material.button.MaterialButton powersaveButton = findViewById(R.id.powersave);
        com.google.android.material.button.MaterialButton balanceButton = findViewById(R.id.balance);
        com.google.android.material.button.MaterialButton performanceButton = findViewById(R.id.performance);
        com.google.android.material.button.MaterialButton fastButton = findViewById(R.id.fast);
        com.google.android.material.button.MaterialButton superPowersaveButton = findViewById(R.id.super_powersave);
        com.google.android.material.button.MaterialButton appListConfigButton = findViewById(R.id.go_app_list);

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
