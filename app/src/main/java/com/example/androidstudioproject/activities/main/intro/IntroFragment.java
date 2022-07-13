package com.example.androidstudioproject.activities.main.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.androidstudioproject.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.androidstudioproject.activities.main.FeedFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.adapters.IntroAdapter;
import com.google.android.material.tabs.TabLayout;

public class IntroFragment extends Fragment {

    ViewPager pager;
    TabLayout dots;

    public static Fragment newInstance(int position) {
        Fragment frag;
        if(position == 0)
            frag = WelcomeFragment.newInstance();
        else if(position == 1)
            frag = WelcomeSettingsFragment.newInstance();
        else frag = GuideFragment.newInstance();
        return frag;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_main_intro, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pager = view.findViewById(R.id.pager);
        pager.setAdapter(new IntroAdapter(this.getFragmentManager()));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0)
                    ((MainActivity)getActivity()).replaceFragments(WelcomeFragment.class);
                else if(position == 1)
                    ((MainActivity)getActivity()).replaceFragments(WelcomeSettingsFragment.class);
                else ((MainActivity)getActivity()).replaceFragments(GuideFragment.class);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dots = view.findViewById(R.id.dots);
        dots.setupWithViewPager(pager, true);
    }
}
