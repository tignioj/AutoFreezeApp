package com.example.freezeappdemo1.ui.home.frozen;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.viewmodel.HomeViewModel;

import java.util.List;

public class FrozenAdapter extends BaseAdapter {

    private List<AppInfo> appInfos;
    private FrozenFragment frozenFragment;
    private HomeViewModel homeViewModel;

    FrozenAdapter(FrozenFragment appListFragment, List<AppInfo> appInfos) {
        this.frozenFragment = appListFragment;
        this.appInfos = appInfos;
        homeViewModel = frozenFragment.homeViewModel;
    }
    void updateInfos(List<AppInfo> appInfos) {
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = View.inflate(frozenFragment.requireContext(), R.layout.cell_listview, null);
        } else {
            view = convertView;
        }
        AppInfo item = (AppInfo) getItem(position);

        CheckBox checkBox = view.findViewById(R.id.checkbox_cell);
        TextView appName = view.findViewById(R.id.tv_appname);
        TextView packageName = view.findViewById(R.id.tv_packagename);
        ImageView imageView = view.findViewById(R.id.imageView);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((AppInfo) getItem(position)).setSelected(isChecked);
                homeViewModel.getMutableLiveDataFrozenAppList().getValue().get(position).setSelected(isChecked);

            }
        });
        checkBox.setChecked(item.isSelected());
        appName.setText(item.getAppName());
        packageName.setText(item.getPackageName());
        imageView.setImageDrawable(item.getIcon());

        return view;
    }
}
