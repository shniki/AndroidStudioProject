package com.example.androidstudioproject.activities.main;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/{
    AuthenticationViewModel mAuth = new AuthenticationViewModel(this.getActivity().getApplication());
    Switch edtDarkMode;
    EditText edtChangePassword;
    Switch edtChangeLanguage;
    Button btnSignOut;
    Button btnChangeDetails;

    public static SettingsFragment newInstance(/*Data data, int position*/) {
        SettingsFragment frag = new SettingsFragment();

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtDarkMode = (Switch) view.findViewById(R.id.fragSettings_DarkModeSwitch); //get input line (edit text) by id
        edtChangePassword = view.findViewById(R.id.fragSettings_ChangePassword); //get input line (edit text) by id
        edtChangeLanguage = (Switch) view.findViewById(R.id.fragSettings_ChangeLanguage); //get input line (edit text) by id
        btnSignOut = (Button) view.findViewById(R.id.fragSettings_signOut);
        btnChangeDetails = (Button) view.findViewById(R.id.fragSettings_changeDetails);

        edtDarkMode.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        edtChangeLanguage.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            String language;
            if (isChecked)
                language = getString(R.string.spanish);
            else language = getString(R.string.english);
            ((MainActivity)this.getActivity()).setLanguage(language);
        });

        edtChangePassword.setOnClickListener(v -> {
            if(edtChangePassword == null)
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }
            String text = edtChangePassword.getText().toString();
            if(text.length() == 0)
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }
            mAuth.changePassword(text);
        });

        btnSignOut.setOnClickListener(v -> {
            mAuth.signOut();
            ((MainActivity)this.getActivity()).gotoLoginActivity();
        });

        btnChangeDetails.setOnClickListener(v -> {
            ((MainActivity)getActivity()).replaceFragments(EditDetailsFragment.class);
        });
    }
}
