package com.example.freezeappdemo1.ui.home.frozen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.AppsCategory;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.utils.DeviceMethod;

import java.util.List;

public class EditCategoryDialog extends DialogFragment {

    HomeViewModel homeViewModel;
    AppsCategory appsCategory;
    int position;

    public EditCategoryDialog(HomeViewModel homeViewModel, AppsCategory originalAppsCategory, FrozenFragment frozenFragment, int position) {
        this.homeViewModel = homeViewModel;
        this.appsCategory = originalAppsCategory;
        this.position = position;
        this.frozenFragment = frozenFragment;
    }

    Button buttonConfirm, buttonCancel, buttonDelete;
    EditText editTextRename;
    FrozenFragment frozenFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_category_dialog, container, false);

        editTextRename = view.findViewById(R.id.editTextCategoryName);
        buttonConfirm = view.findViewById(R.id.btn_edit_category_confirm);
        buttonCancel = view.findViewById(R.id.btn_edit_category_cancel);
        buttonDelete = view.findViewById(R.id.btn_edit_category_delete);

        editTextRename.setText(appsCategory.getCategoryName());

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("WARNING").setMessage("SURE TO DELETE?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<FreezeApp> appsByCategory = homeViewModel.getAppsByCategory(appsCategory.getId());
                        for (FreezeApp freezeApp : appsByCategory) {
                            //解冻所有
                            if (freezeApp.isFrozen()) {
                                DeviceMethod.getInstance(v.getContext()).freeze(freezeApp.getPackageName(), false);
                                freezeApp.setFrozen(false);
                                homeViewModel.updateFreezeApp(freezeApp);
                            }
                        }
                        homeViewModel.deleteAppsCategory(appsCategory);
                        homeViewModel.updateAllMemoryData();
                        frozenFragment.getAdapter().notifyItemRemoved(position);
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                        .create().show();
                dismiss();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editTextRename.getText().toString();
                appsCategory.setCategoryName(string);
                homeViewModel.updateAppsCategory(appsCategory);
                frozenFragment.getAdapter().notifyItemChanged(position);
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
