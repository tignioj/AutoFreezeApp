package com.tignioj.freezeapp.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.telecom.StatusHints;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tignioj.freezeapp.InitApplication;
import com.tignioj.freezeapp.MainActivity;
import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.uientity.SettingEntity;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.Inform;

class SettingAdapter extends ListAdapter<SettingEntity, SettingAdapter.SettingViewHolder> {
    private Context context;
    SettingFragment settingFragment;
    HomeViewModel homeViewModel;

    protected SettingAdapter(Context context, SettingFragment settingFragment, HomeViewModel homeViewModel) {
        super(new DiffUtil.ItemCallback<SettingEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull SettingEntity oldItem, @NonNull SettingEntity newItem) {
                return oldItem.getText().equals(newItem.getText());
            }

            @Override
            public boolean areContentsTheSame(@NonNull SettingEntity oldItem, @NonNull SettingEntity newItem) {
                return oldItem.getText().equals(newItem.getText());
            }
        });
        this.context = context;
        this.settingFragment = settingFragment;
        this.homeViewModel = homeViewModel;


    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_rcv_fragment_setting, parent, false);
        SettingViewHolder holder = new SettingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        final SettingEntity item = getItem(position);
        holder.textView.setText(item.getText());
        holder.imageView.setImageDrawable(item.getDrawable());
        holder.itemView.setOnClickListener(new MyClickListener(holder, item));

    }

    public class MyClickListener implements View.OnClickListener {
        SettingViewHolder holder;
        SettingEntity item;


        public MyClickListener(SettingViewHolder holder, SettingEntity item) {
            this.holder = holder;
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            switch (item.getAction()) {
                case SettingFragment.DEACTIVATE:

                    DeviceMethod.getInstance(context).deActivate(settingFragment);
                    break;
                case SettingFragment.CHANGE_DAY_NIGHT_MODE:
                    InitApplication instance = InitApplication.getInstance();
                    Intent intent;
                    if (instance.isNightModeEnabled()) {
                        InitApplication.getInstance().setIsNightModeEnabled(false);
                        intent = settingFragment.getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    } else {
                        InitApplication.getInstance().setIsNightModeEnabled(true);
                        intent = settingFragment.getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    }
                    settingFragment.getActivity().finish();
                    settingFragment.getActivity().startActivity(intent);
                    break;
                case SettingFragment.OTHER_APPS:
                    Inform.alert(R.string.tips_text, R.string.open_ousite_url_text, new Inform.Callback() {
                        @Override
                        public void ok() {
                            Uri parse = Uri.parse(context.getString(R.string.githubURL));
                            Intent intent = new Intent(Intent.ACTION_VIEW, parse);
                            context.startActivity(intent);
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void dismiss() {

                        }
                    });
            }
        }
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public SettingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cell_rcv_fragment_setting_imgview);
            textView = itemView.findViewById(R.id.cell_rcv_fragment_setting_textview);
        }
    }

}
