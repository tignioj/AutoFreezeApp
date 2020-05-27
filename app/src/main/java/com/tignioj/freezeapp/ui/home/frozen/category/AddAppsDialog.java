package com.tignioj.freezeapp.ui.home.frozen.category;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.entity.AppInfo;
import com.tignioj.freezeapp.ui.home.applist.AppListAdapter;

import java.util.List;

public class AddAppsDialog extends DialogFragment {
    ListView listView;
    Button buttonConfirm, buttonCancle;

    HomeViewModel homeViewModel;
    long categoryId;
    FrozenAppByCategoryFragment frozenAppByCategoryFragment;
    EditText editTextSearch;

    public AddAppsDialog(FrozenAppByCategoryFragment frozenAppByCategoryFragment) {
        this.frozenAppByCategoryFragment = frozenAppByCategoryFragment;
        this.homeViewModel = frozenAppByCategoryFragment.homeViewModel;
        this.categoryId = frozenAppByCategoryFragment.categoryId;
    }

    MutableLiveData<List<AppInfo>> mutableLiveDataUnFreezeAppListLive;
    AppListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_apps_to_category_dialog, container, false);
        listView = view.findViewById(R.id.lv_add_apps_to_category);

        buttonConfirm = view.findViewById(R.id.btn_add_apps_to_category_confirm);
        buttonCancle = view.findViewById(R.id.btn_add_apps_to_category_cancel);

        editTextSearch = view.findViewById(R.id.et_add_adds_to_category_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mutableLiveDataUnFreezeAppListLive = homeViewModel.getMutableLiveDataUnFreezeAppListLiveNotInCategoryWithPattern(frozenAppByCategoryFragment.freezeAppListLive.getValue(),s.toString().trim());
                mutableLiveDataUnFreezeAppListLive.removeObservers(getViewLifecycleOwner());
                mutableLiveDataUnFreezeAppListLive.observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
                    @Override
                    public void onChanged(List<AppInfo> appInfos) {
                        adapter.updateInfos(appInfos);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mutableLiveDataUnFreezeAppListLive = homeViewModel.getMutableLiveDataUnFreezeAppListLiveNotInCategory(frozenAppByCategoryFragment.freezeAppListLive.getValue());
        adapter = new AppListAdapter(
                requireContext(),
                R.layout.cell_listview, mutableLiveDataUnFreezeAppListLive.getValue(), homeViewModel,
                getActivity()

        );
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
