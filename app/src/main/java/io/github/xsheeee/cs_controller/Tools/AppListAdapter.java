package io.github.xsheeee.cs_controller.Tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.xsheeee.cs_controller.R;

public class AppListAdapter extends BaseAdapter {

    private Context context;
    private List<ApplicationInfo> applicationList;
    private PackageManager packageManager;

    public AppListAdapter(Context context, List<ApplicationInfo> applicationList, PackageManager packageManager) {
        this.context = context;
        this.applicationList = applicationList;
        this.packageManager = packageManager;
    }

    @Override
    public int getCount() {
        return applicationList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return applicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.app_info_layout, parent, false);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.app_icon);
            holder.name = convertView.findViewById(R.id.app_name);
            holder.packageName = convertView.findViewById(R.id.pck_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ApplicationInfo appInfo = getItem(position);
        Drawable icon = appInfo.loadIcon(packageManager);
        String appName = appInfo.loadLabel(packageManager).toString();
        String packageName = appInfo.packageName;

        holder.icon.setImageDrawable(icon);
        holder.name.setText(appName);
        holder.packageName.setText(packageName);

        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView packageName;
    }
}