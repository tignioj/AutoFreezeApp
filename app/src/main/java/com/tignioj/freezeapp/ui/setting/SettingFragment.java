package com.tignioj.freezeapp.ui.setting;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.uientity.SettingEntity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {
    public static final int OTHER_APPS = 0x3;
    public int currentNightMode;
    RecyclerView recyclerView;
    private ProgressBar progressBar;

    public SettingFragment() {
        // Required empty public constructor

    }


    public static final int DEACTIVATE = 0x1;
    public static final int CHANGE_DAY_NIGHT_MODE = 0x2;


    private Drawable getDrawable(int id) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Drawable drawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                drawable = getResources().getDrawable(id, activity.getTheme());
            } else {
                drawable = getResources().getDrawable(id);
            }
            return drawable;
        } else {
            return getResources().getDrawable(R.drawable.ic_launcher_background);
        }
    }

    private HomeViewModel homeViewModel;
    TextView progressSettingText;

    public TextView getProgressSettingText() {
        return progressSettingText;
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_setting, container, false);
        progressBar = inflate.findViewById(R.id.progressBarSetting);
        progressBar.setVisibility(View.INVISIBLE);
        progressSettingText = inflate.findViewById(R.id.progressSettingText);
        progressSettingText.setText(R.string.fragment_frozen_unfreezing_center_text);
        progressSettingText.setVisibility(View.INVISIBLE);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        recyclerView = inflate.findViewById(R.id.rcv_fragment_setting);

        SettingAdapter settingAdapter = new SettingAdapter(requireContext(), this, homeViewModel);

        SettingEntity deActivate = new SettingEntity(getDrawable(R.drawable.ic_baseline_mobile_off_24),
                getString(R.string.setting_item_deactive_text), DEACTIVATE);
        SettingEntity nightMode = new SettingEntity(getDrawable(R.drawable.ic_baseline_brightness_medium_24),
                getString(R.string.setting_toggle_night_mode_text), CHANGE_DAY_NIGHT_MODE);

        SettingEntity otherApps = new SettingEntity(getDrawable(R.drawable.ic_baseline_apps_24),
                getString(R.string.develpoe_other_apps), OTHER_APPS
        );


        ArrayList<SettingEntity> settingEntities = new ArrayList<>();
        settingEntities.add(deActivate);
        settingEntities.add(nightMode);
        settingEntities.add(otherApps);

        settingAdapter.submitList(settingEntities);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(settingAdapter);


        return inflate;
    }
}
