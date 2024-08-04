package io.github.xsheeee.cs_controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import io.github.xsheeee.cs_controller.Tools.AppListAdapter;
import io.github.xsheeee.cs_controller.Tools.MagiskHelper;
import io.github.xsheeee.cs_controller.Tools.Tools;
import io.github.xsheeee.cs_controller.Tools.Values;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private SearchView searchView;
    private List<ApplicationInfo> applications = new ArrayList<>();
    private AppListAdapter adapter;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        pm = getPackageManager();
        applications.addAll(getAllApplicationInfos());

        adapter = new AppListAdapter(this, applications, pm);
        listView.setAdapter(adapter);
        Tools tools = new Tools(getApplicationContext());
        // 初始化 SearchView
        initializeSearchView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ApplicationInfo appInfo = applications.get(position);
                String packageName = appInfo.packageName;
                // Handle click event here
            }
        });


    }

    private void initializeSearchView() {
        MenuHost menuHost = this;
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(Menu menu, MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);

                // 找到 SearchView 并设置监听器
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // 当 SearchView 展开时执行的操作
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // 当 SearchView 折叠时执行的操作
                        return true;
                    }
                });

                // 设置查询文本监听器
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        // 当用户提交查询时执行的操作
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        // 当查询文本改变时进行过滤
                        // 这里可以添加过滤逻辑
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                return false;
            }
        }, this, Lifecycle.State.RESUMED);
    }

    
    private List<ApplicationInfo> getAllApplicationInfos() {
        List<ApplicationInfo> list = new ArrayList<>();

        List<ApplicationInfo> allApps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : allApps) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { // Skip system apps
                continue;
            }
            list.add(appInfo);
        }
        return list;
    }

    private void filterApplications(String text) {
        List<ApplicationInfo> filteredList = new ArrayList<>();
        for (ApplicationInfo appInfo : applications) {
            if (appInfo.loadLabel(pm).toString().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(appInfo);
            }
        }
        applications.clear();
        applications.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }


}
