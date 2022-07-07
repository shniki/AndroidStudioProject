package com.example.androidstudioproject.activities.main;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;

public class SettingsFragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/{

    Switch edtDarkMode;
    EditText edtChangePassword;
    Switch edtChangeLanguage;

    public static SettingsFragment newInstance(/*Data data, int position*/) {
        SettingsFragment frag = new SettingsFragment();

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtDarkMode = (Switch) view.findViewById(R.id.fragSettings_DarkModeSwitch); //get input line (edit text) by id
        Spinner colorPicker = (Spinner) view.findViewById(R.id.fragSettings_ColorPicker);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.color_entries, R.layout.gender_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorPicker.setAdapter(adapter);
        edtChangePassword = view.findViewById(R.id.fragSettings_ChangePassword); //get input line (edit text) by id
        edtChangeLanguage = (Switch) view.findViewById(R.id.fragSettings_ChangeLanguage); //get input line (edit text) by id

        edtDarkMode.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            if (isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        edtChangeLanguage.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            String language;
            if (isChecked)
                language = "es";
            else language = "en";
            ((MainActivity)this.getActivity()).setLanguage(language);
        });

        edtChangePassword.setOnClickListener(v -> {
            // todo: update password in firebase auth
        });

        //todo: implement color picker

    }
}
