package com.example.freezeappdemo1.ui.home.timer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.freezeappdemo1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    public TimerFragment() {
        // Required empty public constructor
    }

    ListView listView;
    FloatingActionButton floatingActionButton;
    TimerAdapter timerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_timer, container, false);
        floatingActionButton= inflate.findViewById(R.id.floatingActionButtonAddTimer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_addTimerFragment);
            }
        });
        listView = inflate.findViewById(R.id.lv_timer);
        timerAdapter = new TimerAdapter();
        listView.setAdapter(timerAdapter);
        return inflate;
    }

}
