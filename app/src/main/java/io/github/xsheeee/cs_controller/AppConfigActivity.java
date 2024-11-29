package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.widget.Toolbar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import io.github.xsheeee.cs_controller.Tools.AppInfo;
import io.github.xsheeee.cs_controller.Tools.Values;

public class AppConfigActivity extends AppCompatActivity {
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private TextView appNameTextView, packageNameTextView, versionTextView, versionCodeTextView;
    private ImageView appIconImageView;
    private AppInfo appInfo;
    private int defaultPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_config);

        Toolbar toolbar = findViewById(R.id.backButton2);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化视图
        appNameTextView = findViewById(R.id.appNameTextView);
        packageNameTextView = findViewById(R.id.packageNameTextView);
        versionTextView = findViewById(R.id.versionTextView);
        versionCodeTextView = findViewById(R.id.versionCodeTextView);
        appIconImageView = findViewById(R.id.iconImageView);  // 获取图标显示的ImageView

        appInfo = new AppInfo();

        // 获取传入的包名
        Intent intent = getIntent();
        String pName = intent.getStringExtra("pName");
        fetchAppInfo(pName);

        toolbar.setSubtitle(appInfo.getAppName());

        // 设置spinner（选择框）
        spinner = findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.modes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Values.updateLists();
        findDefaultPosition(pName);
        setDefaultSpinnerItem(defaultPosition);

        // 设置spinner的选择监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == defaultPosition) return;
                removeAndAddToNewList(pName, position);
                Values.toUpdateLists();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // 获取应用信息
    private void fetchAppInfo(String packageName) {
        PackageManager pm = getPackageManager();

        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);

            Drawable icon = pm.getApplicationIcon(applicationInfo); // 获取应用图标
            String appName = pm.getApplicationLabel(applicationInfo).toString(); // 获取应用名称
            String versionName = packageInfo.versionName;  // 获取应用版本名
            int versionCode = (int)packageInfo.getLongVersionCode();  // 获取应用版本代号

            appInfo.setIcon(icon);
            appInfo.setAppName(appName);
            appInfo.setPackageName(packageName);

            // 更新UI
            appNameTextView.setText(appInfo.getAppName());
            packageNameTextView.setText(appInfo.getPackageName());
            versionTextView.setText("版本: " + versionName);
            versionCodeTextView.setText("版本代号: " + versionCode);

            // 设置图标
            appIconImageView.setImageDrawable(icon);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void findDefaultPosition(String pName) {
        for (int i = 0; i < Values.lists.size(); i++) {
            if (Values.lists.get(i).contains(pName)) {
                defaultPosition = i;
                break;
            }
        }
        if (defaultPosition == -1) {
            defaultPosition = 1;
        }
    }

    private void setDefaultSpinnerItem(int defaultItem) {
        spinner.setSelection(defaultItem);
    }

    private void removeAndAddToNewList(String pName, int newPosition) {
        for (int i = 0; i < Values.lists.size(); i++) {
            if (Values.lists.get(i).contains(pName)) {
                Values.lists.get(i).remove(pName);
                break;
            }
        }
        Values.lists.get(newPosition).add(pName);
    }
}