package com.tignioj.freezeapp.ui.home;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tignioj.freezeapp.R;
import com.tignioj.freezeapp.ui.home.applist.AppListFragment;
import com.tignioj.freezeapp.ui.home.frozen.FrozenFragment;
import com.tignioj.freezeapp.ui.home.timer.TimerFragment;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    private ArrayList<Fragment> fragments;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        fragments = new ArrayList<>();
        fragments.add(new AppListFragment());
        fragments.add(new FrozenFragment());
        fragments.add(new TimerFragment());
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}