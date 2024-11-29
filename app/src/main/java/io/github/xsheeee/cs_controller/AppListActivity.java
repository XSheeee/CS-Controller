package io.github.xsheeee.cs_controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import io.github.xsheeee.cs_controller.Tools.AppInfo;

public class AppListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<AppInfo> data = new ArrayList<>();
    private List<AppInfo> filteredData = new ArrayList<>();
    private FrameLayout loadingView;
    private PackageManager packageManager;
    private AppListAdapter adapter;
    private ExecutorService executorService;
    private LruCache<String, WeakReference<Drawable>> iconCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        // 初始化组件
        recyclerView = findViewById(R.id.recycler_view);
        loadingView = findViewById(R.id.loading_view);
        packageManager = getPackageManager();
        executorService = Executors.newFixedThreadPool(4);

        // 初始化图标缓存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        iconCache = new LruCache<>(cacheSize);

        // 设置 Toolbar
        Toolbar toolbar = findViewById(R.id.backButton);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppListAdapter();
        recyclerView.setAdapter(adapter);

        // 加载应用信息
        loadAppInfos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_text));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        return true;
    }

    private void filter(String text) {
        String lowerCaseText = text.toLowerCase();
        List<AppInfo> newFilteredData = data.stream()
            .filter(appInfo -> appInfo.getAppName().toLowerCase().contains(lowerCaseText) || 
                              appInfo.getPackageName().toLowerCase().contains(lowerCaseText))
            .collect(Collectors.toList());

        updateAdapterData(newFilteredData);
    }

    private void updateAdapterData(List<AppInfo> newFilteredData) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(filteredData, newFilteredData));
        filteredData.clear();
        filteredData.addAll(newFilteredData);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private void loadAppInfos() {
        loadingView.setVisibility(View.VISIBLE);
        executorService.execute(() -> {
            List<AppInfo> loadedData = getAllAppInfos();
            runOnUiThread(() -> {
                updateAppData(loadedData);
                loadingView.setVisibility(View.GONE);
            });
        });
    }

    private void updateAppData(List<AppInfo> loadedData) {
        data.clear();
        data.addAll(loadedData);
        filteredData.clear();
        filteredData.addAll(data);
        adapter.notifyDataSetChanged();
        loadIcons();
    }

    protected List<AppInfo> getAllAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent, 0);

        for (ResolveInfo ri : resolveInfos) {
            String packageName = ri.activityInfo.packageName;
            String appName = ri.loadLabel(packageManager).toString();
            list.add(new AppInfo(null, appName, packageName));
        }
        return list;
    }

    private void loadIcons() {
        recyclerView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View view = recyclerView.getChildAt(i);
                int position = recyclerView.getChildAdapterPosition(view);
                if (position != RecyclerView.NO_POSITION) {
                    AppInfo appInfo = filteredData.get(position);
                    if (appInfo.getIcon() == null) {
                        loadIconAsync(appInfo);
                    }
                }
            }
        });

        // Immediately load existing icons
        for (AppInfo appInfo : filteredData) {
            WeakReference<Drawable> cachedIconRef = iconCache.get(appInfo.getPackageName());
            if (cachedIconRef != null) {
                Drawable cachedIcon = cachedIconRef.get();
                if (cachedIcon != null) {
                    appInfo.setIcon(cachedIcon);
                }
            }
        }
    }

    private void loadIconAsync(AppInfo appInfo) {
        WeakReference<Drawable> cachedIconRef = iconCache.get(appInfo.getPackageName());
        if (cachedIconRef != null) {
            Drawable cachedIcon = cachedIconRef.get();
            if (cachedIcon != null) {
                appInfo.setIcon(cachedIcon);
                runOnUiThread(() -> adapter.notifyItemChanged(filteredData.indexOf(appInfo), 0));
                return;
            }
        }

        executorService.execute(() -> {
            try {
                Drawable icon = packageManager.getApplicationIcon(appInfo.getPackageName());
                appInfo.setIcon(icon);
                iconCache.put(appInfo.getPackageName(), new WeakReference<>(icon));
                runOnUiThread(() -> adapter.notifyItemChanged(filteredData.indexOf(appInfo), 0));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                appInfo.setIcon(null);
            }
        });
    }

    class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_info_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AppInfo appInfo = filteredData.get(position);
            holder.imageView.setImageDrawable(appInfo.getIcon());
            holder.textView.setText(appInfo.getAppName());
            holder.packageNameView.setText(appInfo.getPackageName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(AppListActivity.this, AppConfigActivity.class);
                intent.putExtra("aName", appInfo.getAppName());
                intent.putExtra("pName", appInfo.getPackageName());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return filteredData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView packageNameView;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.app_icon);
                textView = itemView.findViewById(R.id.app_name);
                packageNameView = itemView.findViewById(R.id.pck_name); // 初始化 packageNameView
            }
        }
    }

    static class DiffCallback extends DiffUtil.Callback {
        private final List<AppInfo> oldList;
        private final List<AppInfo> newList;

        DiffCallback(List<AppInfo> oldList, List<AppInfo> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getPackageName().equals(newList.get(newItemPosition).getPackageName());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            AppInfo oldItem = oldList.get(oldItemPosition);
            AppInfo newItem = newList.get(newItemPosition);
            return oldItem.getAppName().equals(newItem.getAppName()) &&
                    oldItem.getIcon() == newItem.getIcon();
        }
    }
}