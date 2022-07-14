package com.example.androidstudioproject.activities.main.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.main.EditDetailsFragment;
import com.example.androidstudioproject.activities.main.FeedFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.activities.main.SettingsFragment;

public class WelcomeSettingsFragment extends Fragment{
    Button btnGotoSettings;
    Button btnSkip;
    ImageView btnNext;
    ImageView btnPrev;

    public static WelcomeSettingsFragment newInstance() {
        WelcomeSettingsFragment frag = new WelcomeSettingsFragment();

        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnGotoSettings = view.findViewById(R.id.gotoSettings);
        btnSkip = view.findViewById(R.id.skipBtnSettings);
        btnNext = view.findViewById(R.id.fragSettings_next);
        btnPrev = view.findViewById(R.id.fragSettings_prev);

        btnGotoSettings.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(SettingsFragment.class);
        });

        btnSkip.setOnClickListener(v -> {
            this.getFragmentManager().popBackStack();
            this.getFragmentManager().popBackStack();
            ((MainActivity) getActivity()).replaceFragments(FeedFragment.class);
        });

        btnNext.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(GuideFragment.class);
        });

        btnPrev.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(WelcomeFragment.class);
        });
    }
}
