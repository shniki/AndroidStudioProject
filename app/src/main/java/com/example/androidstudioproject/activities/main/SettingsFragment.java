package com.example.androidstudioproject.activities.main;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends Fragment {
    AuthenticationViewModel mAuth;
    SwitchCompat edtDarkMode;
    EditText edtChangePassword;
    SwitchCompat edtChangeLanguage;
    Button btnSignOut;
    Button btnChangeDetails;

    public SettingsFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public static SettingsFragment newInstance(/*Data data, int position*/) {
        SettingsFragment frag = new SettingsFragment();

        return frag;
    }

    public void changeLanguageInFragment(){
        edtDarkMode.setText(R.string.change_theme_title);
        edtChangePassword.setText(R.string.password_change_title);
        edtChangeLanguage.setText(R.string.language_change_title);
        btnSignOut.setText(R.string.signOut);
        btnChangeDetails.setText(R.string.changeUserDetails);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtDarkMode = (SwitchCompat) view.findViewById(R.id.fragSettings_DarkModeSwitch); //get input line (edit text) by id
        edtChangePassword = view.findViewById(R.id.fragSettings_ChangePassword); //get input line (edit text) by id
        edtChangeLanguage = (SwitchCompat) view.findViewById(R.id.fragSettings_ChangeLanguage); //get input line (edit text) by id
        btnSignOut = (Button) view.findViewById(R.id.fragSettings_signOut);
        btnChangeDetails = (Button) view.findViewById(R.id.fragSettings_changeDetails);

        mAuth=((MainActivity)getActivity()).getAuthenticationViewModel();

        edtDarkMode.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        edtChangeLanguage.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            String language;
            if (isChecked)
                language = getString(R.string.romanian);
            else language = getString(R.string.english);
            ((MainActivity)this.getActivity()).setLanguage(language);
            changeLanguageInFragment();
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
