package com.tignioj.freezeapp.ui.home.applist;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.entity.AppInfo;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment {

    public AppListFragment() {
        // Required empty public constructor
    }

    private TextView textViewSearch, textViewAppListSelectCount;
    private ListView listViewAppList;
    private AppListAdapter adapter;
    private HomeViewModel homeViewModel;
    private CheckBox checkBoxAppListSelectAll;
    private Button buttonFreeze;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }


    private MutableLiveData<List<AppInfo>> unfreezeApps;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        unfreezeApps = homeViewModel.getMutableLiveDataUnFreezeAppListLive();

        adapter = new AppListAdapter(requireContext(), R.layout.cell_listview, unfreezeApps.getValue(), homeViewModel, getActivity());


        listViewAppList.setAdapter(adapter);
        unfreezeApps.observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(List<AppInfo> appInfos) {
                Log.d(MyConfig.MY_TAG, "applist update");
                adapter.updateInfos(appInfos);
            }
        });

    }


    private MutableLiveData<Integer> selectedReadyToFreezeCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_app_list, container, false);

        buttonFreeze = inflate.findViewById(R.id.button_freeze);

        buttonFreeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeze(v);
            }
        });


        textViewSearch = inflate.findViewById(R.id.et_search);
        textViewAppListSelectCount = inflate.findViewById(R.id.textViewAppListSelectCount);
        checkBoxAppListSelectAll = inflate.findViewById(R.id.checkBoxAppListSelectAll);
        checkBoxAppListSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<AppInfo> appInfos = adapter.getAppInfos();
                        for (int i = 0; i < adapter.getCount(); i++) {
                            appInfos.get(i).setSelected(isChecked);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
        });


        selectedReadyToFreezeCount = homeViewModel.getSelectedReadyToFreezeCount();
        final CharSequence selectedText = textViewAppListSelectCount.getText();

        selectedReadyToFreezeCount.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    buttonFreeze.setEnabled(false);
                } else {
                    buttonFreeze.setEnabled(true);
                }
                String s = selectedText.toString() + integer + "/" + adapter.getCount();
                textViewAppListSelectCount.setText(s);
            }
        });


        textViewSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pattern = s.toString().trim();
                unfreezeApps = homeViewModel.findUnFreezeAppsListWithPattern(pattern);
                unfreezeApps.removeObservers(getViewLifecycleOwner());
                unfreezeApps.observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
                    @Override
                    public void onChanged(List<AppInfo> appInfos) {
                        adapter.updateInfos(appInfos);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listViewAppList = inflate.findViewById(R.id.lv_applist);



        //执行的命令
        return inflate;
    }

    /**
     * 冻结App
     *
     * @param view
     */
    public void freeze(View view) {

        List<FreezeApp> readyToFreezeApps = new ArrayList<>();
        List<AppInfo> appInfos = adapter.getAppInfos();

        for (AppInfo appInfo : appInfos) {
            if (appInfo.isSelected()) {
                FreezeApp freezeApp = new FreezeApp();
                freezeApp.setPackageName(appInfo.getPackageName());
                freezeApp.setAppName(appInfo.getAppName());
                freezeApp.setIcon(appInfo.getIconLayout());
                freezeApp.setFrozen(true);
                readyToFreezeApps.add(freezeApp);
            }
        }
        if (readyToFreezeApps.size() == 0) {
            return;
        }

        ChooseCategoryDialog chooseCategoryDialog = new ChooseCategoryDialog(homeViewModel, readyToFreezeApps);
        chooseCategoryDialog.show(requireActivity().getSupportFragmentManager(), "choose category");

    }
}
