package com.example.freezeappdemo1.ui.home.timer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.freezeappdemo1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTimerFragment extends Fragment {

    private static final String START_TIME_PICKER_TAG = "start_time_picker_tag";
    private static final String END_TIME_PICKER_TAG = "end_time_picker_tag";
    private EditText editTextEndTime;
    private EditText editTextStartTime;

    public AddTimerFragment() {
        // Required empty public constructor
    }


    Button buttonBack, buttonSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        buttonSave = inflate.findViewById(R.id.button_addTime_save);
        return inflate;
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        AddTimerFragment fragment;
        Calendar c;
        Date time;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        public TimePickerFragment(AddTimerFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            if (this.getTag().equals(START_TIME_PICKER_TAG)) {
//                time = this.fragment.timeLine.getStartTime();
            } else {
//                time = this.fragment.timeLine.getEndTime();
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
//                    fragment.timeLine.setStartTime(c.getTime());
                    break;
                case END_TIME_PICKER_TAG:
                    fragment.editTextEndTime.setText(sdf.format(c.getTime()));
//                    fragment.timeLine.setEndTime(c.getTime());
                    break;
            }
        }

    }
}
