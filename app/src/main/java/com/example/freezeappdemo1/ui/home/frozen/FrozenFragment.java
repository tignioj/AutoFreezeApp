package com.example.freezeappdemo1.ui.home.frozen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;
import com.example.freezeappdemo1.viewmodel.HomeViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrozenFragment extends Fragment {

    public FrozenFragment() {
        // Required empty public constructor
    }


    private ListView listViewAppListFrozen;
    private EditText editTextSearch;
    private FrozenAdapter adapter;
    HomeViewModel homeViewModel;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        MutableLiveData<List<AppInfo>> mutableLiveDataFrozenAppList = homeViewModel.getMutableLiveDataFrozenAppList();

        mutableLiveDataFrozenAppList.getValue();
        adapter = new FrozenAdapter(this, mutableLiveDataFrozenAppList.getValue());
        listViewAppListFrozen.setAdapter(adapter);

        mutableLiveDataFrozenAppList.observe(getViewLifecycleOwner(), new Observer<List<AppInfo>>() {
            @Override
            public void onChanged(List<AppInfo> appInfos) {
                Log.d("myTag", "frozen list update");
                adapter.updateInfos(appInfos);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_frozen, container, false);

        editTextSearch = inflate.findViewById(R.id.et_search_frozen);
        listViewAppListFrozen = inflate.findViewById(R.id.lv_applist_frozen);

        inflate.findViewById(R.id.buttonUnfreeze).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfreeze(v);
            }
        });

        inflate.findViewById(R.id.buttonTiming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timing(v);
            }
        });


        return inflate;
    }

    private void timing(View v) {
    }

    private void unfreeze(View v) {
        List<AppInfo> appInfos = homeViewModel.getMutableLiveDataFrozenAppList().getValue();
        boolean hasUpdate = false;
        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo a = appInfos.get(i);
            if (a.isSelected()) {
                hasUpdate = true;
                Log.d("myTag", a.getAppName());
                DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), false);
            }
        }
        if (hasUpdate) {
            homeViewModel.updateAll();
        }
    }
}
