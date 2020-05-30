package com.tignioj.freezeapp.ui.home.timer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.backend.entitys.FreezeTasker;
import com.tignioj.freezeapp.backend.viewmodel.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tignioj.freezeapp.config.MyConfig;
import com.tignioj.freezeapp.utils.MyDateUtils;

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
    Handler handler;
    List<FreezeTasker> freezeTaskers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_timer, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        floatingActionButton = inflate.findViewById(R.id.floatingActionButtonAddTimer);
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
                TimerFragment.this.freezeTaskers = freezeTaskers;
                timerAdapter.submitList(freezeTaskers);
                timerAdapter.notifyDataSetChanged();
            }
        });

        //高亮当前
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                List<FreezeTasker> freezeTaskers = TimerFragment.this.freezeTaskers;
                if (freezeTaskers != null) {
                    for (int i = 0; i < freezeTaskers.size(); i++) {
                        FreezeTasker f = freezeTaskers.get(i);
                        if (MyDateUtils.betweenStartTimeAndEndTime(f.getStartTime(), f.getEndTime())) {
                            Log.d(MyConfig.MY_TAG, "current:" + f);
                            if (!f.isCurrent()) {
                                f.setCurrent(true);
                                timerAdapter.notifyItemChanged(i);
                            }
                        } else {
                            if (f.isCurrent()) {
                                f.setCurrent(false);
                                timerAdapter.notifyItemChanged(i);
                            }
                        }
                    }
                }
                handler.sendEmptyMessageDelayed(0x100, 2000);
            }
        };

        handler.sendEmptyMessage(0x100 );

        return inflate;
    }


    public TimerAdapter getAdapter() {
        return timerAdapter;
    }
}
