package com.tignioj.freezeapp.ui.setting;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.service.FreezeService;
import com.tignioj.freezeapp.ui.home.timer.AddTimerFragment;
import com.tignioj.freezeapp.uientity.ProgramLocker;
import com.tignioj.freezeapp.uientity.SettingEntity;
import com.tignioj.freezeapp.utils.DeviceMethod;
import com.tignioj.freezeapp.utils.Inform;
import com.tignioj.freezeapp.utils.MyDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskerEditableFragment extends Fragment {

    EditText editTextStartTime, editTextEndTime;
    Button buttonConfirm, buttonCancel;
    CheckBox checkboxEnable, checkboxHideIcon;
    boolean firstBoot = true;
    private boolean firstBootIcon = true;
    private ProgressBar progressBar;
    private TextView textViewProgress;

    private static final String START_TIME_PICKER_TAG = "start_time_picker_tag";
    private static final String END_TIME_PICKER_TAG = "end_time_picker_tag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_editable_time, container, false);


        final HomeViewModel homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        progressBar = inflate.findViewById(R.id.progressBarHideInSetting);
        checkboxEnable = inflate.findViewById(R.id.checkbox_editable_enable);

        checkboxHideIcon = inflate.findViewById(R.id.checkbox_editable_hideicon);
        editTextStartTime = inflate.findViewById(R.id.et_editable_dialog_start_time);
        editTextEndTime = inflate.findViewById(R.id.et_editable_dialog_end_time);
        buttonConfirm = inflate.findViewById(R.id.btn_editable_tasker_confirm);
        buttonCancel = inflate.findViewById(R.id.btn_editable_tasker_cancel);


        MutableLiveData<ProgramLocker> programLockerMutableLiveData = homeViewModel.getProgramLockerMutableLiveData();
        programLockerMutableLiveData.observe(getViewLifecycleOwner(), new Observer<ProgramLocker>() {
            @Override
            public void onChanged(ProgramLocker programLocker) {
                checkboxEnable.setChecked(programLocker.isEnable());
                checkboxHideIcon.setChecked(programLocker.isHideIcon());
                editTextStartTime.setText(programLocker.getStartTime());
                editTextEndTime.setText(programLocker.getEndTime());
            }
        });



        progressBar.setVisibility(View.INVISIBLE);
        textViewProgress = inflate.findViewById(R.id.textViewWaitingInSetting);
        textViewProgress.setVisibility(View.INVISIBLE);



        checkboxEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (firstBoot && isChecked) {
                    Inform.alert(R.string.warning, R.string.program_lock_warning_text);
                    firstBoot = false;
                }
            }
        });

        checkboxHideIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (firstBootIcon && isChecked) {
                    Inform.alert(R.string.warning, R.string.warning_hide_icon_tips);
                    firstBootIcon = false;
                }
            }
        });

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment(TaskerEditableFragment.this).show(requireActivity().getSupportFragmentManager(), START_TIME_PICKER_TAG);
            }
        });



        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment(TaskerEditableFragment.this).show(requireActivity().getSupportFragmentManager(), END_TIME_PICKER_TAG);
            }
        });


        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgramLocker programLocker = new ProgramLocker(editTextStartTime.getText().toString(),
                        editTextEndTime.getText().toString(),
                        checkboxEnable.isChecked(),
                        checkboxHideIcon.isChecked());
                homeViewModel.setProgramLocker(programLocker);

                if (checkboxEnable.isChecked() && !MyDateUtils.betweenStartTimeAndEndTime(
                        MyDateUtils.parse(editTextStartTime.getText().toString()),
                        MyDateUtils.parse(editTextEndTime.getText().toString())
                )) {
                    if (checkboxHideIcon.isChecked()) {
                        buttonCancel.setEnabled(false);
                        buttonConfirm.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        textViewProgress.setVisibility(View.VISIBLE);
                        FreezeService.getHideMySelfHandler().sendEmptyMessage(FreezeService.HIDE_SELF);
                    } else {
                        Navigation.findNavController(v).navigate(R.id.action_taskerEditableFragment_to_lockStateFragment);
                    }
                } else {
                    Navigation.findNavController(v).navigateUp();
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        return inflate;
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        TaskerEditableFragment fragment;
        Calendar c;
        Date time;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);

        TimePickerFragment(TaskerEditableFragment fragment) {

            this.fragment = fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            if (this.getTag().equals(START_TIME_PICKER_TAG)) {
                time = MyDateUtils.parse(fragment.editTextStartTime.getText().toString());
            } else {
                time = MyDateUtils.parse(fragment.editTextEndTime.getText().toString());
            }

            sdf.format(time == null ? new Date() : time);
            c = sdf.getCalendar();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            c.set(Calendar.YEAR, 2020);
            c.set(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            String t = sdf.format(c.getTime());
            switch (this.getTag()) {
                case START_TIME_PICKER_TAG:
                    fragment.editTextStartTime.setText(t);
                    break;
                case END_TIME_PICKER_TAG:
                    fragment.editTextEndTime.setText(t);
                    break;
            }
        }
    }
}
