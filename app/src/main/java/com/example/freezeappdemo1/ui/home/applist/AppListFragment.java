package com.example.freezeappdemo1.ui.home.applist;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.viewmodel.HomeViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment {

    public AppListFragment() {
        // Required empty public constructor
    }

    TextView textViewSearch;
    ListView listViewAppList;
    private AppListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MutableLiveData<List<AppInfo>> unfreezeApps = homeViewModel.getMutableLiveDataUnFreezeAppListLive();

        adapter = new AppListAdapter(this, unfreezeApps.getValue());

        listViewAppList.setAdapter(adapter);

        unfreezeApps.observe(requireActivity(), new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(List<AppInfo> appInfos) {
                Log.d("myTag", "applist update");
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

    HomeViewModel homeViewModel;

    /**
     * 冻结App
     *
     * @param view
     */
    public void freeze(View view) {
        List<AppInfo> unFreezeAppList = homeViewModel.getMutableLiveDataUnFreezeAppListLive().getValue();
        for (AppInfo a : unFreezeAppList) {
            if (a.isSelected()) {
                Log.d("myTag", a.getAppName());
                DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), true);
            }
        }
        homeViewModel.updateAll();
    }

}
