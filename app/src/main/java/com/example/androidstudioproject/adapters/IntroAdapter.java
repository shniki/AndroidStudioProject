package com.example.androidstudioproject.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.androidstudioproject.R;


import com.example.androidstudioproject.activities.main.intro.IntroFragment;

public class IntroAdapter extends FragmentPagerAdapter {

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IntroFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

}