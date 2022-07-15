package com.example.androidstudioproject.activities.main;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
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
    Button btnConfirmChange;

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
        edtChangeLanguage.setText(R.string.language_change_title);
        btnSignOut.setText(R.string.signOut);
        btnChangeDetails.setText(R.string.changeUserDetails);
        btnConfirmChange.setText(R.string.confirmChange);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth=((MainActivity)getActivity()).getAuthenticationViewModel();
        SharedPreferences settings = ((MainActivity)this.getActivity()).getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", mAuth.getCurrentEmail());
        editor.commit();
        edtDarkMode = (SwitchCompat) view.findViewById(R.id.fragSettings_DarkModeSwitch); //get input line (edit text) by id
        edtChangePassword = view.findViewById(R.id.fragSettings_ChangePassword); //get input line (edit text) by id
        edtChangeLanguage = (SwitchCompat) view.findViewById(R.id.fragSettings_ChangeLanguage); //get input line (edit text) by id
        btnSignOut = (Button) view.findViewById(R.id.fragSettings_signOut);
        btnChangeDetails = (Button) view.findViewById(R.id.fragSettings_changeDetails);
        btnConfirmChange = (Button) view.findViewById(R.id.fragSettings_confirmChange);

        edtDarkMode.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if (isChecked) {
                editor.putBoolean("isDarkMode", true);
                editor.putBoolean("returnToSettings", true);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else {
                editor.putBoolean("isDarkMode", false);
                editor.putBoolean("returnToSettings", true);
                editor.commit();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            };
        });

        edtChangeLanguage.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            String language;
            if (isChecked) {
                language = getString(R.string.romanian);
            }
            else language = getString(R.string.english);
            editor.putBoolean("isRomanian", language.equals(getString(R.string.romanian)));
            editor.commit();
            ((MainActivity)this.getActivity()).setLanguage(language);
            changeLanguageInFragment();
        });

        edtDarkMode.setChecked(settings.getBoolean("isDarkMode", false) || ((MainActivity)this.getActivity()).isNightModeOn());
        edtChangeLanguage.setChecked(settings.getBoolean("isRomanian", false) || ((MainActivity)this.getActivity()).getCurrentLanguage().equals(getString(R.string.romanian)));

        btnConfirmChange.setOnClickListener(v -> {
            String text = edtChangePassword.getText().toString();
            if(TextUtils.isEmpty(text))
            {
                Snackbar.make(view, R.string.empty_password, Snackbar.LENGTH_LONG).show();
                return;
            }
            if(text.length() < 8)
            {
                Snackbar.make(view, R.string.short_password, Snackbar.LENGTH_LONG).show();
                return;
            }
            mAuth.changePassword(this, text);
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
