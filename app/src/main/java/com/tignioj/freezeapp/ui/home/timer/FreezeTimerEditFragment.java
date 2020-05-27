package com.tignioj.freezeapp.ui.home.timer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.utils.MyDateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FreezeTimerEditFragment extends Fragment {
    private static final String START_TIME_PICKER_TAG = "start_time_picker_tag";
    private static final String END_TIME_PICKER_TAG = "end_time_picker_tag";
    private EditText editTextEndTime;
    private EditText editTextStartTime;
    private RadioGroup radioGroupUnFreezeOrUnfreeze;
    private RadioButton radioButtonFreeze, radioButtonUnFreeze;
    private CheckBox checkBoxEditTimerIsLockScreen;


    HomeViewModel homeViewModel;


    public FreezeTimerEditFragment() {
    }

    FreezeTasker freezeTaskerFromDb;

    Button buttonBack, buttonSave;
    Spinner spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        Bundle arguments = getArguments();
        long id = arguments.getLong("id");
        freezeTaskerFromDb = homeViewModel.getFreezeTaskerById(id);


        View inflate = inflater.inflate(R.layout.fragment_edit_timer, container, false);
        buttonBack = inflate.findViewById(R.id.button_addTimer_Back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        editTextStartTime = inflate.findViewById(R.id.editText_addTime_startTime);
        editTextStartTime.setText(MyDateUtils.format(freezeTaskerFromDb.getStartTime()));

        editTextEndTime = inflate.findViewById(R.id.editText_addTime_endTime);
        editTextEndTime.setText(MyDateUtils.format(freezeTaskerFromDb.getEndTime()));


        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FreezeTimerEditFragment.TimePickerFragment(FreezeTimerEditFragment.this).show(requireActivity().getSupportFragmentManager(),
                        START_TIME_PICKER_TAG);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FreezeTimerEditFragment.TimePickerFragment(FreezeTimerEditFragment.this).show(requireActivity().getSupportFragmentManager(),
                        END_TIME_PICKER_TAG);
            }
        });

        radioGroupUnFreezeOrUnfreeze = inflate.findViewById(R.id.radioButtonUnFreezeOrUnfreeze);
        radioButtonFreeze = inflate.findViewById(R.id.radioButtonFreeze);
        radioButtonUnFreeze = inflate.findViewById(R.id.radioButtonUnfreeze);

        if (freezeTaskerFromDb.isFrozen()) {
            radioGroupUnFreezeOrUnfreeze.check(R.id.radioButtonFreeze);
        } else {
            radioGroupUnFreezeOrUnfreeze.check(R.id.radioButtonUnfreeze);
        }


        checkBoxEditTimerIsLockScreen = inflate.findViewById(R.id.checkBoxEditTimerIsLockScreen);
        checkBoxEditTimerIsLockScreen.setChecked(freezeTaskerFromDb.isLockScreen());

        List<AppsCategory> appsCategorys = homeViewModel.getAppsCategorys();
        AppsCategory[] appsCategories = appsCategorys.toArray(new AppsCategory[0]);

        spinner = inflate.findViewById(R.id.addTimerTaskSpinner);
        ArrayAdapter<AppsCategory> adapter = new ArrayAdapter<AppsCategory>(
                requireContext(),
                R.layout.cell_spinner_on_tv,
                appsCategories
        );

        int itemPosition = getPosition(appsCategories);

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(itemPosition);

        buttonSave = inflate.findViewById(R.id.button_addTime_save);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppsCategory selectedItem = (AppsCategory) spinner.getSelectedItem();
                freezeTaskerFromDb.setCategoryId(selectedItem.getId());
                freezeTaskerFromDb.setCategoryName(selectedItem.getCategoryName());
                if (radioGroupUnFreezeOrUnfreeze.getCheckedRadioButtonId() == R.id.radioButtonFreeze) {
                    freezeTaskerFromDb.setFrozen(true);
                } else {
                    freezeTaskerFromDb.setFrozen(false);
                }
                freezeTaskerFromDb.setLockScreen(checkBoxEditTimerIsLockScreen.isChecked());
//                homeViewModel.insertFreezeTasks(freezeTaskerFromDb);
                homeViewModel.updateFreezeTasks(freezeTaskerFromDb);
                Toast.makeText(getContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).navigateUp();
            }
        });

        return inflate;
    }

    private int getPosition(AppsCategory[] appsCategories) {
        int position = 0;
        for (int i = 0; i < appsCategories.length; i++) {
            AppsCategory a = appsCategories[i];
            if (a.getId() == freezeTaskerFromDb.getCategoryId()) {
                position = i;
            }
        }
        return position;
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        FreezeTimerEditFragment fragment;
        Calendar c;
        Date time;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        public TimePickerFragment(FreezeTimerEditFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (this.getTag().equals(START_TIME_PICKER_TAG)) {
                time = this.fragment.freezeTaskerFromDb.getStartTime();
            } else {
                time = this.fragment.freezeTaskerFromDb.getEndTime();
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
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            switch (this.getTag()) {
                case START_TIME_PICKER_TAG:
                    fragment.editTextStartTime.setText(sdf.format(c.getTime()));
                    fragment.freezeTaskerFromDb.setStartTime(c.getTime());
                    break;
                case END_TIME_PICKER_TAG:
                    fragment.editTextEndTime.setText(sdf.format(c.getTime()));
                    fragment.freezeTaskerFromDb.setEndTime(c.getTime());
                    break;
            }
        }
    } 
}
