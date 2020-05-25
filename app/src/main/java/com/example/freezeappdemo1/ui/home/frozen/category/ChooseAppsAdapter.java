package com.example.freezeappdemo1.ui.home.frozen.category;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezeappdemo1.entity.AppInfo;

public class ChooseAppsAdapter extends ListAdapter<AppInfo, ChooseAppsAdapter.MyViewHolder> {

    protected ChooseAppsAdapter() {
        super(new DiffUtil.ItemCallback<AppInfo>() {
            @Override
            public boolean areItemsTheSame(@NonNull AppInfo oldItem, @NonNull AppInfo newItem) {
                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull AppInfo oldItem, @NonNull AppInfo newItem) {
                return false;
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

