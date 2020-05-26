package com.example.freezeappdemo1.ui.home.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.List;

public class TimerAdapter extends ListAdapter<FreezeTasker, TimerAdapter.MyViewHolder> {

    TimerFragment timerFragment;


    protected TimerAdapter(TimerFragment timerFragment) {
        super(new DiffUtil.ItemCallback<FreezeTasker>() {
            @Override
            public boolean areItemsTheSame(@NonNull FreezeTasker oldItem, @NonNull FreezeTasker newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull FreezeTasker oldItem, @NonNull FreezeTasker newItem) {
                return oldItem.getCategoryId() == newItem.getCategoryId()
                        && oldItem.getStartTime().equals(newItem.getStartTime())
                        && oldItem.getEndTime().equals(newItem.getEndTime())
                        && oldItem.getCategoryName().equals(newItem.getCategoryName())
                        ;
            }
        });
        this.timerFragment = timerFragment;
 ;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View inflate = inflater.inflate(R.layout.cell_add_timer, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        FreezeTasker item = getItem(position);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String formatStart = sdf.format(item.getStartTime());

        String formatEnd = new SimpleDateFormat("HH:mm").format(item.getEndTime());
        holder.textViewStartTime.setText(formatStart);
        holder.textViewEndTime.setText(formatEnd);
        holder.textViewCategoryName.setText(item.getCategoryName());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FreezeTasker currentDeleteItem = getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("WARNING")
                        .setMessage("SURE TO DELETE?")
                        .setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                timerFragment.homeViewModel.deleteFreezeTasks(currentDeleteItem);
                                timerFragment.getAdapter().notifyItemRemoved(position);
                            }
                        }).create().show();
            }
        });
        if (item.isFrozen()) {
            holder.imageViewVisible.setImageResource(R.drawable.ic_visibility_off_black_24dp);
        } else {
            holder.imageViewVisible.setImageResource(R.drawable.ic_visibility_black_24dp);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", getItem(position).getId());
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_freezeTimerEditFragment, bundle);
            }
        });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStartTime, textViewEndTime, textViewCategoryName;
        ImageButton imageButton;
        ImageView imageViewVisible;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTimeCell);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTimeCell);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryNameCell);
            imageButton = itemView.findViewById(R.id.imageButtonDeleteTimer);
            imageViewVisible = itemView.findViewById(R.id.imageViewVisable);
        }
    }

}