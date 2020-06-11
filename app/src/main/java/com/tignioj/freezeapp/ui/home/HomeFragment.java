package com.tignioj.freezeapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tignioj.freezeapp.R;
import com.google.android.material.tabs.TabLayout;
import com.tignioj.freezeapp.config.MyConfig;


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

    /**
     * 防止返回到LockStateFragment, 而是直接退出
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    requireActivity().finish();
                    return true;
                }
                return false;
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
