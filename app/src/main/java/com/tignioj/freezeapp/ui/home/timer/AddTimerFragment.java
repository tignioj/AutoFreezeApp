package com.tignioj.freezeapp.ui.home.timer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTimerFragment extends Fragment {

    private static final String START_TIME_PICKER_TAG = "start_time_picker_tag";
    private static final String END_TIME_PICKER_TAG = "end_time_picker_tag";
    private EditText editTextEndTime;
    private EditText editTextStartTime;
    private RadioGroup radioGroupUnFreezeOrUnfreeze;
    private RadioButton radioButtonFreeze, radioButtonUnFreeze;
    private CheckBox checkBoxAddTimerIsLockScreen;
    private EditText editTextDescription;


    private HomeViewModel homeViewModel;

    public AddTimerFragment() {
    }

    private FreezeTasker freezeTasker;

    private Button buttonBack, buttonSave;
    private Spinner spinner;

    private boolean isFirstWarinng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isFirstWarinng = true;
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        freezeTasker = new FreezeTasker();
        View inflate = inflater.inflate(R.layout.fragment_add_timer, container, false);



        buttonBack = inflate.findViewById(R.id.button_addTimer_Back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        editTextStartTime = inflate.findViewById(R.id.editText_addTime_startTime);
        editTextEndTime = inflate.findViewById(R.id.editText_addTime_endTime);

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment(AddTimerFragment.this).show(requireActivity().getSupportFragmentManager(),
                        START_TIME_PICKER_TAG);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment(AddTimerFragment.this).show(requireActivity().getSupportFragmentManager(),
                        END_TIME_PICKER_TAG);
            }
        });

        radioGroupUnFreezeOrUnfreeze = inflate.findViewById(R.id.radioButtonUnFreezeOrUnfreeze);
        radioButtonFreeze = inflate.findViewById(R.id.radioButtonFreeze);
        radioButtonUnFreeze = inflate.findViewById(R.id.radioButtonUnfreeze);

        radioGroupUnFreezeOrUnfreeze.check(R.id.radioButtonFreeze);

        checkBoxAddTimerIsLockScreen = inflate.findViewById(R.id.checkBoxAddTimerIsLockScreen);
        checkBoxAddTimerIsLockScreen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && isFirstWarinng) {
                    isFirstWarinng = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setTitle(R.string.warning)
                            .setMessage(R.string.lock_screen_check_warning_text)
                            .create().show();
                }
            }
        });

        List<AppsCategory> appsCategorys = homeViewModel.getAppsCategorys();
        AppsCategory[] appsCategories = appsCategorys.toArray(new AppsCategory[0]);

        spinner = inflate.findViewById(R.id.addTimerTaskSpinner);
        ArrayAdapter<AppsCategory> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.cell_spinner_on_tv,
                appsCategories
        );

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (spinner.getSelectedItem() == null) {
            Toast.makeText(requireContext(), R.string.prompt_to_add_categories, Toast.LENGTH_SHORT).show();
        }


        buttonSave = inflate.findViewById(R.id.button_addTime_save);
        buttonSave.setEnabled(false);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppsCategory selectedItem = (AppsCategory) spinner.getSelectedItem();
//                if (selectedItem == null) {
//                    Toast.makeText(requireContext(), R.string.prompt_to_add_categories, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                freezeTasker.setCategoryId(selectedItem.getId());
                freezeTasker.setCategoryName(selectedItem.getCategoryName());
                freezeTasker.setLockScreen(checkBoxAddTimerIsLockScreen.isChecked());

                freezeTasker.setDescription(editTextDescription.getText().toString());

                if (radioGroupUnFreezeOrUnfreeze.getCheckedRadioButtonId() == R.id.radioButtonFreeze) {
                    freezeTasker.setFrozen(true);
                } else {
                    freezeTasker.setFrozen(false);
                }
                homeViewModel.insertFreezeTasks(freezeTasker);
                Navigation.findNavController(v).navigateUp();
            }
        });
        editTextDescription = inflate.findViewById(R.id.addTimerEditTextDescription);

        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0
                        && editTextStartTime.getText().toString().length() > 0
                        && editTextEndTime.getText().toString().length() > 0
                        && spinner.getSelectedItem() != null
                ) {

                    buttonSave.setEnabled(true);
                } else {
                    buttonSave.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return inflate;
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        AddTimerFragment fragment;
        Calendar c;
        Date time;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        TimePickerFragment(AddTimerFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (this.getTag().equals(START_TIME_PICKER_TAG)) {
                time = fragment.freezeTasker.getStartTime();
            } else {
                time = fragment.freezeTasker.getEndTime();
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
            c.set(Calendar.SECOND, 0);
            switch (this.getTag()) {
                case START_TIME_PICKER_TAG:
                    fragment.editTextStartTime.setText(sdf.format(c.getTime()));
                    fragment.freezeTasker.setStartTime(c.getTime());
                    break;
                case END_TIME_PICKER_TAG:
                    fragment.editTextEndTime.setText(sdf.format(c.getTime()));
                    fragment.freezeTasker.setEndTime(c.getTime());
                    break;
            }
        }
    }
}
