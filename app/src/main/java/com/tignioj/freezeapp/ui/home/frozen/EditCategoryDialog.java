package com.tignioj.freezeapp.ui.home.frozen;

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
import androidx.fragment.app.FragmentActivity;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.utils.DeviceMethod;

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
                builder.setTitle(R.string.warning).setMessage(R.string.dialog_confirm_deletion_text).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        frozenFragment.progressBarFrozenFragmentUnfreeezing.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

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

                                FragmentActivity activity = frozenFragment.getActivity();
                                if (activity != null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            frozenFragment.getAdapter().notifyItemRemoved(position);
                                            frozenFragment.progressBarFrozenFragmentUnfreeezing.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
