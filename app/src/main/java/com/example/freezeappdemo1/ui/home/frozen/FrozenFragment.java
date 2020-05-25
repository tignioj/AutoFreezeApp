package com.example.freezeappdemo1.ui.home.frozen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrozenFragment extends Fragment {

    public FrozenFragment() {
        // Required empty public constructor
    }


    private RecyclerView recyclerViewAppListFrozen;
    private EditText editTextSearch;
    private FrozenAdapter adapter;
    HomeViewModel homeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LiveData<List<AppsCategory>> appsCategoryLive = homeViewModel.getAppsCategoryLive();

        adapter = new FrozenAdapter(requireContext(), homeViewModel);

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);
        recyclerViewAppListFrozen.setLayoutManager(layoutManager);


        recyclerViewAppListFrozen.setAdapter(adapter);
        appsCategoryLive.observe(getViewLifecycleOwner(), new Observer<List<AppsCategory>>() {
            @Override
            public void onChanged(List<AppsCategory> appInfos) {
                Log.d(MyConfig.MY_TAG, "frozen list update");
                adapter.submitList(appInfos);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_frozen, container, false);

        editTextSearch = inflate.findViewById(R.id.et_search_frozen);

        recyclerViewAppListFrozen = inflate.findViewById(R.id.rcv_applist_frozen);



        inflate.findViewById(R.id.buttonAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog(homeViewModel);
                addCategoryDialog.show(requireActivity().getSupportFragmentManager(), "add category");
            }
        });

        return inflate;
    }

    private void timing(View v) {
//        Intent service = new Intent(requireActivity(), FreezeService.class);
//        startActivity(service);
    }

    private void unfreeze(View v) {
//        List<AppInfo> appInfos = homeViewModel.getMutableLiveDataFrozenAppList().getValue();
//        if (appInfos == null) {
//            Inform.error("Error,please try it later", requireContext());
//            return;
//        }
//
//        int i = 0;
//        for (AppInfo a : appInfos) {
//            if (a.isSelected()) {
//                i++;
//                Log.d("myTag", a.getAppName());
//                DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), false);
//            }
//        }
//        if (i > 0) {
//            Toast.makeText(requireContext(), "unFreeze " + i + " apps", Toast.LENGTH_SHORT).show();
//            homeViewModel.updateAll();
//            ShpUtils.updateAppFrozenList(requireContext(), homeViewModel);
//        }
    }


}
