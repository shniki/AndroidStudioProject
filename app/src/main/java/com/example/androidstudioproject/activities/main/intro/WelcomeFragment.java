package com.example.androidstudioproject.activities.main.intro;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.activities.main.EditDetailsFragment;
import com.example.androidstudioproject.activities.main.FeedFragment;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.activities.main.SettingsFragment;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.material.snackbar.Snackbar;

public class WelcomeFragment extends Fragment{
    Button btnGotoUpdateUser;
    Button btnSkip;
    ImageView btnNext;

    public static WelcomeFragment newInstance() {
        WelcomeFragment frag = new WelcomeFragment();

        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnGotoUpdateUser = view.findViewById(R.id.updateUser);
        btnSkip = view.findViewById(R.id.skipBtn);
        btnNext = view.findViewById(R.id.nextBtn);

        btnGotoUpdateUser.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(EditDetailsFragment.class);
        });

        btnSkip.setOnClickListener(v -> {
            this.getFragmentManager().popBackStack();
            ((MainActivity) getActivity()).replaceFragments(FeedFragment.class);
        });

        btnNext.setOnClickListener(v -> {
            ((MainActivity) getActivity()).replaceFragments(WelcomeSettingsFragment.class);
        });
    }
}
