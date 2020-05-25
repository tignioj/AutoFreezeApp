package com.example.freezeappdemo1.ui.home.applist;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.utils.Inform;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment {

    public AppListFragment() {
        // Required empty public constructor
    }

    private TextView textViewSearch;
    private ListView listViewAppList;
    private AppListAdapter adapter;
    private HomeViewModel homeViewModel;

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

        adapter = new AppListAdapter(requireContext(), R.layout.cell_listview, unfreezeApps.getValue(), homeViewModel);

        listViewAppList.setAdapter(adapter);

        unfreezeApps.observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(List<AppInfo> appInfos) {
                Log.d(MyConfig.MY_TAG, "applist update");
                adapter.updateInfos(appInfos);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_app_list, container, false);

        textViewSearch = inflate.findViewById(R.id.et_search);

        listViewAppList = inflate.findViewById(R.id.lv_applist);


        inflate.findViewById(R.id.button_freeze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freeze(v);
            }
        });


        //执行的命令
        textViewSearch.setText("com.example.myapp");
        return inflate;
    }


    /**
     * 冻结App
     *
     * @param view
     */
    public void freeze(View view) {


        List<FreezeApp> readyToFreezeApps = new ArrayList<>();
        for (AppInfo appInfo : unfreezeApps.getValue()) {
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
        chooseCategoryDialog.show(requireActivity().getSupportFragmentManager(), "choose category" );
    }
}
