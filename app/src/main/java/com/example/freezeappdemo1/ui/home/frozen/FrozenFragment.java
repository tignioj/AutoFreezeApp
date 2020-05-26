package com.example.freezeappdemo1.ui.home.frozen;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.entity.AppInfo;
import com.example.freezeappdemo1.utils.DeviceMethod;

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
    private Button buttonUnfreezeAll;
    LiveData<List<AppsCategory>> appsCategoryLive;
    //解冻中进度条
    ProgressBar progressBarFrozenFragmentUnfreeezing;

    //解冻中文字
    TextView textViewUnfreezing;


    public FrozenAdapter getAdapter() {
        return adapter;
    }

    /**
     * 只在第一次创建时候执行
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        appsCategoryLive = homeViewModel.getAppsCategoryLive();
    }

    /**
     * 每次从其他页面返回时都会执行
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new FrozenAdapter(requireContext(), homeViewModel, this);

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

        //解冻中进度条
        progressBarFrozenFragmentUnfreeezing = inflate.findViewById(R.id.progressBarFrozenFragmentUnfreeezing);
        //解冻中文字
        textViewUnfreezing = inflate.findViewById(R.id.textViewFrozenFragmentUnfreezing);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appsCategoryLive = homeViewModel.findAppCategorysLiveWithPattern(s.toString().trim());
                appsCategoryLive.removeObservers(getViewLifecycleOwner());
                appsCategoryLive.observe(getViewLifecycleOwner(), new Observer<List<AppsCategory>>() {
                    @Override
                    public void onChanged(List<AppsCategory> appsCategories) {
                        adapter.submitList(appsCategories);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerViewAppListFrozen = inflate.findViewById(R.id.rcv_applist_frozen);


        buttonUnfreezeAll = inflate.findViewById(R.id.btn_unfreeze_all);
        buttonUnfreezeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfreezeAll(v);
            }
        });

        inflate.findViewById(R.id.buttonAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryDialog addCategoryDialog = new AddCategoryDialog(homeViewModel);
                addCategoryDialog.show(requireActivity().getSupportFragmentManager(), "add category");
            }
        });
        return inflate;
    }


    private void unfreezeAll(View v) {
        final List<FreezeApp> freezeApps = homeViewModel.getAllFrozenApps();

        progressBarFrozenFragmentUnfreeezing.setVisibility(View.VISIBLE);
        textViewUnfreezing.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (FreezeApp a : freezeApps) {
                    i++;
                    DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), false);
                    a.setFrozen(false);
                    homeViewModel.updateFreezeApp(a);
                }

                //解决漏网之物
                MutableLiveData<List<AppInfo>> mutableLiveDataFrozenAppList = homeViewModel.getMutableLiveDataFrozenAppList();
                List<AppInfo> value = mutableLiveDataFrozenAppList.getValue();
                for (AppInfo a : value) {
                    DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), false);
                }

                if (i > 0) {
                    homeViewModel.updateAllMemoryData();
                }
                final int finalI = i;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarFrozenFragmentUnfreeezing.setVisibility(View.INVISIBLE);
                        textViewUnfreezing.setVisibility(View.INVISIBLE);
                        Toast.makeText(requireContext(), "unFreeze " + finalI + " apps", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
