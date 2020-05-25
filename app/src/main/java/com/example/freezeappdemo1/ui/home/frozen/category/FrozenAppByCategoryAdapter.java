package com.example.freezeappdemo1.ui.home.frozen.category;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.TintableCompoundButton;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeApp;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.example.freezeappdemo1.utils.DeviceMethod;

import org.w3c.dom.Text;

import java.util.List;

public class FrozenAppByCategoryAdapter extends ListAdapter<FreezeApp, FrozenAppByCategoryAdapter.MyViewHolder> {

    protected FrozenAppByCategoryAdapter(HomeViewModel homeViewModel) {
        super(new DiffUtil.ItemCallback<FreezeApp>() {
            @Override
            public boolean areItemsTheSame(@NonNull FreezeApp oldItem, @NonNull FreezeApp newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull FreezeApp oldItem, @NonNull FreezeApp newItem) {
                return oldItem.getAppName().equals(newItem.getAppName()) && oldItem.getPackageName().equals(newItem.getPackageName())
                        && oldItem.getCategoryId() == newItem.getCategoryId()
                        && oldItem.isFrozen() == newItem.isFrozen()
                        && oldItem.isSelect() == newItem.isSelect()
                        ;
            }

        });
        this.homeViewModel = homeViewModel;
    }

    HomeViewModel homeViewModel;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View inflate = inflater.inflate(R.layout.cell_apps_by_category, parent, false);
        final MyViewHolder holder = new MyViewHolder(inflate);
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                FreezeApp item = getItem(holder.getAdapterPosition());
//                item.setSelect(isChecked);
//            }
//        });
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreezeApp item = getItem(holder.getAdapterPosition());
                if (item.isFrozen()) {
                    holder.imageButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
                    DeviceMethod.getInstance(homeViewModel.getApplication()).freeze(item.getPackageName(), false);
                    item.setFrozen(false);
                } else {
                    holder.imageButton.setImageResource(R.drawable.ic_lock_black_24dp);
                    DeviceMethod.getInstance(homeViewModel.getApplication()).freeze(item.getPackageName(), true);
                    item.setFrozen(true);
                }
                homeViewModel.updateFreezeApp(item);
                notifyItemChanged(holder.getAdapterPosition());
                homeViewModel.updateAll();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        FreezeApp item = getItem(position);
        holder.textViewPackageName.setText(item.getPackageName());
        holder.imageView.setImageDrawable(homeViewModel.getApplication().getPackageManager().getDrawable(item.getPackageName(), item.getIcon(), null));
        holder.textViewAppName.setText(item.getAppName());

//        holder.checkBox.setChecked(item.isSelect());

        if (item.isFrozen()) {
            holder.imageButton.setImageResource(R.drawable.ic_lock_black_24dp);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAppName, textViewPackageName;
        ImageView imageView;
        //        CheckBox checkBox;
        ImageButton imageButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
            textViewPackageName = itemView.findViewById(R.id.textViewPackageName);
            imageView = itemView.findViewById(R.id.imageViewAppIcon);
//            checkBox = itemView.findViewById(R.id.checkBoxAppsCategory);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
