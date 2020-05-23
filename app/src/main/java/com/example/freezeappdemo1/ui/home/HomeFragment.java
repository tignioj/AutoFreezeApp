package com.example.freezeappdemo1.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.freezeappdemo1.R;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireContext(), requireActivity().getSupportFragmentManager());
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager viewPager = inflate.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = inflate.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return inflate;
    }
}
