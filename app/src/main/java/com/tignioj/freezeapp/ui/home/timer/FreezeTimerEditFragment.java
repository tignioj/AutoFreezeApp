package com.tignioj.freezeapp.ui.home.timer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.AppsCategory;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.tignioj.freezeapp.config.MyConfig;
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
    private EditText editTextDescription;

    private HomeViewModel homeViewModel;


    public FreezeTimerEditFragment() {
    }

    private FreezeTasker freezeTaskerFromDb;

    private Button buttonBack, buttonSave, buttonDelete;
    private Spinner spinner;

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
        buttonDelete = inflate.findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FreezeTasker currentDeleteItem = freezeTaskerFromDb;

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
                                homeViewModel.deleteFreezeTasks(currentDeleteItem);
                                Navigation.findNavController(v).navigateUp();
//                                timerFragment.getAdapter().notifyItemRemoved(position);
                            }
                        }).create().show();
            }
        });

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
        ArrayAdapter<AppsCategory> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.cell_spinner_on_tv,
                appsCategories
        );

        int itemPosition = getPosition(appsCategories);

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(itemPosition);

        buttonSave = inflate.findViewById(R.id.button_addTime_save);
        buttonSave.setEnabled(false);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppsCategory selectedItem = (AppsCategory) spinner.getSelectedItem();

                freezeTaskerFromDb.setCategoryId(selectedItem.getId());
                freezeTaskerFromDb.setCategoryName(selectedItem.getCategoryName());
                freezeTaskerFromDb.setDescription(editTextDescription.getText().toString());

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

        editTextDescription = inflate.findViewById(R.id.editTimerEditTextDescription);
        editTextDescription.setText(freezeTaskerFromDb.getDescription());


        editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (checkForm()) {
                    buttonSave.setEnabled(true);
                } else {
                    buttonSave.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (checkForm()) {
                    buttonSave.setEnabled(true);
                } else {
                    buttonSave.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                buttonSave.setEnabled(false);
            }
        });

        return inflate;
    }

    /**
     * 表单验证
     * @return 如果符合规则则返回true
     */
    public boolean checkForm() {
        if (editTextDescription.getText().toString().length() > 0
                && editTextStartTime.getText().toString().length() > 0
                && editTextEndTime.getText().toString().length() > 0
                && spinner.getSelectedItem() != null
        ) {
            return true;
        } else {
            return false;
        }
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
            c.set(Calendar.YEAR, 2020);
            c.set(Calendar.MONTH, 1);
            c.set(Calendar.DAY_OF_MONTH,1);
            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
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
