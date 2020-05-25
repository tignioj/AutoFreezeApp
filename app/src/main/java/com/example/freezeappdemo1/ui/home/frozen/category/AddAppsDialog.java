package com.example.freezeappdemo1.ui.home.frozen.category;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.ui.home.applist.AppListAdapter;

import java.util.List;

public class AddAppsDialog extends DialogFragment {
    ListView listView;
    Button buttonConfirm, buttonCancle;

    HomeViewModel homeViewModel;
    long categoryId;
    FrozenAppByCategoryFragment frozenAppByCategoryFragment;

    public AddAppsDialog(FrozenAppByCategoryFragment frozenAppByCategoryFragment) {
        this.frozenAppByCategoryFragment = frozenAppByCategoryFragment;
        this.homeViewModel = frozenAppByCategoryFragment.homeViewModel;
        this.categoryId = frozenAppByCategoryFragment.categoryId;
    }

    MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppListLive;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_apps_to_category_dialog, container, false);
        listView = view.findViewById(R.id.lv_add_apps_to_category);

        buttonConfirm = view.findViewById(R.id.btn_add_apps_to_category_confirm);
        buttonCancle = view.findViewById(R.id.btn_add_apps_to_category_cancel);

        mutableLiveDataUnFreezeAppListLive = homeViewModel.getMutableLiveDataAllAppList();
        final AppListAdapter adapter = new AppListAdapter(requireContext(), R.layout.cell_listview, mutableLiveDataUnFreezeAppListLive.getValue(), homeViewModel);
        listView.setAdapter(adapter);

        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AppInfo> value = mutableLiveDataUnFreezeAppListLive.getValue();
                for (AppInfo a : value) {
                    if (a.isSelected()) {
                        Log.d(MyConfig.MY_TAG, a.getAppName());
                        FreezeApp freezeApp = new FreezeApp();
                        freezeApp.setFrozen(false);
                        freezeApp.setCategoryId(categoryId);
                        freezeApp.setIcon(a.getIconLayout());
                        freezeApp.setAppName(a.getAppName());
                        freezeApp.setPackageName(a.getPackageName());
                        homeViewModel.insertFreezeApp(freezeApp);
                        dismiss();
                    }
                }
                homeViewModel.unSelectFreezeAppAll();
            }
        });

        return view;
    }

}
