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
import android.widget.Button;
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
    private AppListActivity.AppListAdapter adapter;
    private PackageManager pm;
    private android.widget.ListView lv_app;
    private List<AppInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        listView = findViewById(R.id.list_view);

        data = getAllAppInfos();
        adapter = new AppListAdapter();
        //显示列表
        listView.setAdapter(adapter);
        Tools tools = new Tools(getApplicationContext());
        TextView back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = data.get(position);
                String packageName = appInfo.getPackageName();
                String appName = appInfo.getAppName();
                // Handle click event here
                tools.showToast(appName + ":" +
                        "" + packageName);
            }
        });
    }
    protected List<AppInfo> getAllAppInfos() {

        List<AppInfo> list = new ArrayList<AppInfo>();
        // 得到应用的packgeManager
        PackageManager packageManager = getPackageManager();
        // 创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到包含应用信息的列表
        List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 得到包名
            String packageName = ri.activityInfo.packageName;
            // 得到图标
            Drawable icon = ri.loadIcon(packageManager);
            // 得到应用名称
            String appName = ri.loadLabel(packageManager).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return list;
    }

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
            // TODO Auto-generated method stub
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //1. 如果convertView是null, 加载item的布局文件
            if (convertView == null) {
                Logger.writeLog("Info", "getView() load layout");
                convertView = View.inflate(AppListActivity.this, R.layout.app_info_layout, null);
            }
            //2. 得到当前行数据对象
            AppInfo appInfo = data.get(position);
            //3. 得到当前行需要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.app_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.app_name);
            TextView tv = (TextView) convertView.findViewById(R.id.pck_name);
            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());
            tv.setText(appInfo.getPackageName());
            //返回convertView
            return convertView;
        }
    }
}