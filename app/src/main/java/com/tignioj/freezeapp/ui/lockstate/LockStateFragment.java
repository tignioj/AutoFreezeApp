package com.tignioj.freezeapp.ui.lockstate;

import android.app.Service;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tignioj.freezeapp.MainActivity;
import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.uientity.ProgramLocker;
import com.tignioj.freezeapp.utils.Inform;
import com.tignioj.freezeapp.utils.MyDateUtils;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class LockStateFragment extends Fragment {

    TextView textViewStartTime, textViewEndTime;
    boolean enable;
    Button buttonGiveUp, buttonCalm;
    int giveUpClickTime;
    ProgressBar progressBarCalm;
    TextView textViewClam;

    public ProgressBar getProgressBarCalm() {
        return progressBarCalm;
    }

    HomeViewModel homeViewModel;
    ProgramLocker programLocker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_lock_state, container, false);
        textViewStartTime = inflate.findViewById(R.id.tv_lockstatus_start_time);
        textViewEndTime = inflate.findViewById(R.id.tv_lockstatus_end_time);
        buttonCalm = inflate.findViewById(R.id.buttonCalm);
        buttonGiveUp = inflate.findViewById(R.id.buttonGiveUp);
        textViewClam = inflate.findViewById(R.id.textViewCalm);
        progressBarCalm = inflate.findViewById(R.id.progressBarCalm);
        progressBarCalm.setVisibility(View.INVISIBLE);
        textViewClam.setVisibility(View.INVISIBLE);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        MutableLiveData<ProgramLocker> programLockerMutableLiveData = homeViewModel.getProgramLockerMutableLiveData();
        programLockerMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ProgramLocker>() {
            @Override
            public void onChanged(ProgramLocker programLocker) {
                textViewStartTime.setText(programLocker.getStartTime());
                textViewEndTime.setText(programLocker.getEndTime());
                LockStateFragment.this.enable = programLocker.isEnable();
            }
        });

        programLocker = programLockerMutableLiveData.getValue();

        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!programLocker.isEnable() || MyDateUtils.betweenStartTimeAndEndTime(MyDateUtils.parse(programLocker.getStartTime()), MyDateUtils.parse(programLocker.getEndTime()))) {
            Navigation.findNavController(requireView()).navigate(R.id.action_lockStateFragment_to_nav_home);
        } else {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (activity != null) {
                ActionBar supportActionBar = activity.getSupportActionBar();
                if (supportActionBar != null) {
                    supportActionBar.hide();
                }
            }
        }

        /**
         * 给你一次放弃的机会
         */
        buttonGiveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (giveUpClickTime >= 10) {
                    programLocker.setEnable(false);
                    homeViewModel.setProgramLocker(programLocker);
                    Inform.alert(R.string.giveup_message_alert_title, R.string.giveup_message_alert_message);
                } else {
                    Inform.alert(R.string.forbid_giveup_message_alert_title, R.string.forbid_giveup_message_alert_message, new Inform.MySimpleCallback() {
                        @Override
                        public void ok() {
                            buttonGiveUp.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void dismiss() {
                            ++giveUpClickTime;
                        }
                    });
                }
            }
        });

        buttonCalm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCalm.setEnabled(false);
                buttonGiveUp.setEnabled(false);
                Handler hideMySelfHandler = FreezeService.getHideMySelfHandler();
                progressBarCalm.setVisibility(View.VISIBLE);
                textViewClam.setVisibility(View.VISIBLE);
                hideMySelfHandler.sendEmptyMessage(FreezeService.HIDE_SELF);
                hideMySelfHandler.sendEmptyMessageDelayed(FreezeService.ENABLE_SELF, MyConfig.CALM_TIME);
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
