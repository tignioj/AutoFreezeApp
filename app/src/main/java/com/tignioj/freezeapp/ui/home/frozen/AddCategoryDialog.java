package com.tignioj.freezeapp.ui.home.frozen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;

public class AddCategoryDialog extends DialogFragment {

    EditText editTextCategoryName;
    Button buttonConfirm, buttonCancel;
    HomeViewModel homeViewModel;

    public AddCategoryDialog(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_category_dialog, container, false);


        buttonCancel = view.findViewById(R.id.btn_add_apps_to_category_cancel);
        buttonConfirm = view.findViewById(R.id.btn_add_apps_to_category_confirm);
        buttonConfirm.setEnabled(false);


        editTextCategoryName = view.findViewById(R.id.editTextCategoryName);

        editTextCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextCategoryName.getText().toString().length() == 0) {
                    buttonConfirm.setEnabled(false);
                } else {
                    buttonConfirm.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppsCategory appsCategory = new AppsCategory();
                appsCategory.setCategoryName(editTextCategoryName.getText().toString());
                homeViewModel.insertAppsCategory(appsCategory);
                dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
