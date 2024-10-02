package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.xsheeee.cs_controller.Tools.AppInfo;
import io.github.xsheeee.cs_controller.Tools.Logger;
import io.github.xsheeee.cs_controller.Tools.Tools;

public class AppListActivity extends AppCompatActivity {
    private ListView listView;
    private AppListAdapter adapter;
    private PackageManager pm;
    private List<AppInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        // 初始化ListView
        listView = findViewById(R.id.list_view);

        // 获取所有应用信息
        data = getAllAppInfos();
        adapter = new AppListAdapter();
        listView.setAdapter(adapter);

        // 工具类实例化
        Tools tools = new Tools(getApplicationContext());

        // 获取backButton并设置点击事件
        ImageView back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 列表项点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = data.get(position);
                String packageName = appInfo.getPackageName();
                String appName = appInfo.getAppName();
                // 跳转到 AppConfigActivity 并传递数据
                Intent intent = new Intent(AppListActivity.this, AppConfigActivity.class);
                intent.putExtra("aName", appName);
                intent.putExtra("pName", packageName);
                startActivity(intent);
            }
        });
    }

    // 获取所有已安装的应用信息
    protected List<AppInfo> getAllAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // 创建主界面的 Intent
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 获取应用列表
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);

        // 遍历应用信息
        for (ResolveInfo ri : resolveInfos) {
            String packageName = ri.activityInfo.packageName;
            Drawable icon = ri.loadIcon(packageManager);
            String appName = ri.loadLabel(packageManager).toString();
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            list.add(appInfo);
        }

        return list;
    }

    // 自定义适配器类
    class AppListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Logger.writeLog("Info", "getView() load layout");
                convertView = View.inflate(AppListActivity.this, R.layout.app_info_layout, null);
            }

            // 获取应用信息并更新视图
            AppInfo appInfo = data.get(position);
            ImageView imageView = convertView.findViewById(R.id.app_icon);
            TextView textView = convertView.findViewById(R.id.app_name);
            TextView tv = convertView.findViewById(R.id.pck_name);

            // 设置应用信息
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());
            tv.setText(appInfo.getPackageName());

            return convertView;
        }
    }
}
