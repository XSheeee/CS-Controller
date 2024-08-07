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

import io.github.xsheeee.cs_controller.Tools.MagiskHelper;
import io.github.xsheeee.cs_controller.Tools.SetOOM;
import io.github.xsheeee.cs_controller.Tools.Tools;
import io.github.xsheeee.cs_controller.Tools.Values;

import androidx.appcompat.widget.SearchView.OnQueryTextListener;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private AppListAdapter adapter;
    private PackageManager pm;
    private ListView lv_app;
    private List<AppInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetOOM.doit();
        listView = findViewById(R.id.list_view);

        data = getAllAppInfos();
        adapter = new AppListAdapter();
        //显示列表
        listView.setAdapter(adapter);
        Tools tools = new Tools(getApplicationContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = data.get(position);
                String packageName = appInfo.getPackageName();
                // Handle click event here
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
                Log.e("TAG", "getView() load layout");
                convertView = View.inflate(MainActivity.this, R.layout.app_info_layout, null);
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
