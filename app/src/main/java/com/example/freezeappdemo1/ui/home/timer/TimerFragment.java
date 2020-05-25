package com.example.freezeappdemo1.ui.home.timer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.freezeappdemo1.R;
import com.example.freezeappdemo1.backend.entitys.FreezeTasker;
import com.example.freezeappdemo1.backend.viewmodel.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {

    public TimerFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    TimerAdapter timerAdapter;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_timer, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        floatingActionButton= inflate.findViewById(R.id.floatingActionButtonAddTimer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_addTimerFragment);
            }
        });


        recyclerView = inflate.findViewById(R.id.rcv_timer);
        timerAdapter = new TimerAdapter(this);
        recyclerView.setAdapter(timerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        LiveData<List<FreezeTasker>> allFreezeTaskerLive = homeViewModel.getAllFreezeTaskerLive();
        allFreezeTaskerLive.observe(getViewLifecycleOwner(), new Observer<List<FreezeTasker>>() {
            @Override
            public void onChanged(List<FreezeTasker> freezeTaskers) {
                timerAdapter.submitList(freezeTaskers);
            }
        });


        return inflate;
    }

    public TimerAdapter getAdapter() {
        return timerAdapter;
    }
}
