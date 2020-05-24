package com.example.freezeappdemo1.ui.home.applist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.viewmodel.HomeViewModel;

import java.util.List;

public class AppListAdapter extends ArrayAdapter<AppInfo> {

    List<AppInfo> appInfos;
    HomeViewModel homeViewModel;
    private int resourceLayout;
    private Context context;

    public AppListAdapter(@NonNull Context context, int resource, List<AppInfo> appInfos, HomeViewModel homeViewModel) {
        super(context, resource);
        this.resourceLayout = resource;
        this.context = context;
        this.appInfos = appInfos;
        this.homeViewModel = homeViewModel;
    }

    public void updateInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            view = View.inflate(context, resourceLayout, null);
        } else {
            view = convertView;
        }
        AppInfo item = getItem(position);

        CheckBox checkBox = view.findViewById(R.id.checkbox_cell);
        TextView appName = view.findViewById(R.id.tv_appname);
        TextView packageName = view.findViewById(R.id.tv_packagename);
        ImageView imageView = view.findViewById(R.id.imageView);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem(position).setSelected(isChecked);
                homeViewModel.getMutableLiveDataUnFreezeAppListLive().getValue().get(position).setSelected(isChecked);
            }
        });
        checkBox.setChecked(item.isSelected());
        appName.setText(item.getAppName());
        packageName.setText(item.getPackageName());
        imageView.setImageDrawable(item.getIcon());
        return view;
    }
}
