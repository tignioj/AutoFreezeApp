package com.example.freezeappdemo1.ui.home.applist;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.config.MyConfig;
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.util.List;

public class ChooseCategoryDialog extends DialogFragment {

    Spinner spinnerChooseCategory;
    Button buttonConfirm, buttonCancel, buttonAdd;
    EditText editTextAdd;
    HomeViewModel homeViewModel;
    List<FreezeApp> readyToFreezeApp;

    public ChooseCategoryDialog(HomeViewModel homeViewModel, List<FreezeApp> freezeAppList) {
        this.homeViewModel = homeViewModel;
        this.readyToFreezeApp = freezeAppList;
    }


    //待插入的数据
    AppsCategory categoryReadyToAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_choose_category, container, false);
        spinnerChooseCategory = view.findViewById(R.id.spinner);
        editTextAdd = view.findViewById(R.id.et_choose_category_new_category);
        buttonConfirm = view.findViewById(R.id.btn_choose_category_confirm);
        buttonCancel = view.findViewById(R.id.btn_choose_category_cancel);
        buttonAdd = view.findViewById(R.id.btn_choose_category_add_new);


        List<AppsCategory> value = homeViewModel.getAppsCategorys();
        LiveData<List<AppsCategory>> appsCategoryLive = homeViewModel.getListLiveDataAppsCategoryForSpinner();


        final ArrayAdapter<AppsCategory> adapter = new ArrayAdapter<AppsCategory>(
                requireContext(),
                R.layout.cell_spinner_on_tv,
                value
        );

        appsCategoryLive.observe(getViewLifecycleOwner(), new Observer<List<AppsCategory>>() {
            @Override
            public void onChanged(List<AppsCategory> appsCategories) {

                adapter.clear();
                adapter.addAll(appsCategories);

                if (categoryReadyToAdd != null) {
                    AppsCategory categoryAdded = homeViewModel.getCategoryByCategoryName(categoryReadyToAdd.toString());
                    int position = adapter.getPosition(categoryAdded);
                    spinnerChooseCategory.setSelection(position);
                }

                adapter.notifyDataSetChanged();
            }
        });

        spinnerChooseCategory.setAdapter(adapter);


        editTextAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    buttonAdd.setEnabled(true);
                } else {
                    buttonAdd.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextAdd.getText() != null) {
                    categoryReadyToAdd = new AppsCategory();
                    categoryReadyToAdd.setCategoryName(editTextAdd.getText().toString());
                    homeViewModel.insertAppsCategory(categoryReadyToAdd);
                    Toast.makeText(homeViewModel.getApplication(), "Add category success!", Toast.LENGTH_SHORT).show();
                }
                editTextAdd.setText("");
            }
        });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireDialog().dismiss();
                int i = 0;
                AppsCategory selectedItem = (AppsCategory) spinnerChooseCategory.getSelectedItem();
                long id = selectedItem.getId();
                for (FreezeApp a : readyToFreezeApp) {
                    Log.d("myTag", a.getAppName());
                    a.setCategoryId(id);
                    DeviceMethod.getInstance(requireContext()).freeze(a.getPackageName(), true);
                    homeViewModel.insertFreezeApp(a);
                    i++;
                }
                if (i > 0) {
                    Toast.makeText(requireContext(), "freeze " + i + " apps", Toast.LENGTH_SHORT).show();
                    homeViewModel.updateAll();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireDialog().dismiss();
            }
        });

        return view;
    }

    public View getSpinner() {
        return spinnerChooseCategory;
    }
}
