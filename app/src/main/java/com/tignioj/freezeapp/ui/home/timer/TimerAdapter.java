package com.tignioj.freezeapp.ui.home.timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.MyDateUtils;

import java.text.SimpleDateFormat;

public class TimerAdapter extends ListAdapter<FreezeTasker, TimerAdapter.MyViewHolder> {

    private TimerFragment timerFragment;


    TimerAdapter(TimerFragment timerFragment) {
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
                        && oldItem.isLockScreen() == newItem.isLockScreen()
                        && oldItem.getDescription().equals(newItem.getDescription())
                        && oldItem.isCurrent() == newItem.isCurrent()
                        ;
            }

        });
        this.timerFragment = timerFragment;
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
                builder.setTitle(R.string.warning)
                        .setMessage(R.string.dialog_confirm_deletion_text)
                        .setNegativeButton(R.string.no,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).setPositiveButton(R.string.yes,
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



        holder.imageViewLockPhone.setVisibility(item.isLockScreen() ? View.VISIBLE : View.INVISIBLE);

        holder.textViewDescription.setText(item.getDescription());

        //高亮当前
        if (item.isCurrent()) {
            holder.itemView.setBackgroundColor(Color.rgb(0x79, 0x86, 0xCB));
        } else {
            holder.itemView.setBackground(null);
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStartTime, textViewEndTime, textViewCategoryName;
        ImageButton imageButton;
        ImageView imageViewVisible, imageViewLockPhone;
        TextView textViewDescription;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStartTime = itemView.findViewById(R.id.textViewStartTimeCell);
            textViewDescription = itemView.findViewById(R.id.textviewDescriptionCell);
            textViewEndTime = itemView.findViewById(R.id.textViewEndTimeCell);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryNameCell);
            imageButton = itemView.findViewById(R.id.imageButtonDeleteTimer);
            imageViewVisible = itemView.findViewById(R.id.imageViewVisable);
            imageViewLockPhone = itemView.findViewById(R.id.imageViewLockPhone);
        }
    }

}