package com.tignioj.freezeapp.ui.home.frozen.category;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.FreezeApp;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.Inform;

public class FrozenAppByCategoryAdapter extends ListAdapter<FreezeApp, FrozenAppByCategoryAdapter.MyViewHolder> {
    FrozenAppByCategoryFragment frozenAppByCategoryFragment;


    protected FrozenAppByCategoryAdapter(HomeViewModel homeViewModel, FrozenAppByCategoryFragment frozenAppByCategoryFragment) {

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
        this.frozenAppByCategoryFragment = frozenAppByCategoryFragment;
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
                if (!DeviceMethod.getInstance(homeViewModel.getApplication()).isAdmin()) {
                    Inform.showError("你还没有激活设备呢");
                    return;
                }
                holder.progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final FreezeApp item = getItem(holder.getAdapterPosition());
                        FragmentActivity activity = frozenAppByCategoryFragment.getActivity();
                        final boolean b = !item.isFrozen();
                        DeviceMethod.getInstance(homeViewModel.getApplication()).freeze(item.getPackageName(), b);
                        item.setFrozen(b);
                        homeViewModel.updateFreezeApp(item);
                        homeViewModel.updateAllMemoryData();
                        if (activity != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (b) {
                                        holder.imageButton.setImageResource(R.drawable.ic_lock_open_black_24dp);
                                    } else {
                                        holder.imageButton.setImageResource(R.drawable.ic_lock_black_24dp);
                                    }
                                    holder.progressBar.setVisibility(View.INVISIBLE);
                                    notifyItemChanged(holder.getAdapterPosition());
                                }
                            });
                        } else {
                            Log.e(MyConfig.TAG_ERR, "activity null" + activity);
                        }
                    }
                }).start();
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
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
            textViewPackageName = itemView.findViewById(R.id.textViewPackageName);
            imageView = itemView.findViewById(R.id.imageViewAppIcon);
//            checkBox = itemView.findViewById(R.id.checkBoxAppsCategory);
            imageButton = itemView.findViewById(R.id.imageButton);
            progressBar = itemView.findViewById(R.id.progressBarAppsByCategoryCell);
        }
    }
}
