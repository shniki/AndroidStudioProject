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
import com.example.androidstudioproject.activities.main.FeedFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.activities.main.SettingsFragment;

public class GuideFragment extends Fragment{
    Button btnGotIt;
    ImageView btnPrev;

    public static GuideFragment newInstance() {
        GuideFragment frag = new GuideFragment();

        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnGotIt = view.findViewById(R.id.btnGotIt);
        btnPrev = view.findViewById(R.id.fragGuide_back);

        btnGotIt.setOnClickListener(v -> {
            this.getFragmentManager().popBackStack();
            this.getFragmentManager().popBackStack();
            this.getFragmentManager().popBackStack();
            ((MainActivity) getActivity()).replaceFragments(FeedFragment.class);
        });

        btnPrev.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(WelcomeSettingsFragment.class);
        });
    }
}
