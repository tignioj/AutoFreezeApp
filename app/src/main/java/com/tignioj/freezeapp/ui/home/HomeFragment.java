package com.tignioj.freezeapp.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tignioj.freezeapp.R;
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
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireContext(), requireActivity().getSupportFragmentManager());
        //当切换到另一个Fragment后返回，部分Fragment的数据不显示，解决办法是替换getSupportFragmentManager为getChildFragmentManager
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(requireContext(), getChildFragmentManager());
        View inflate = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager viewPager = inflate.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = inflate.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        return inflate;
    }
}
