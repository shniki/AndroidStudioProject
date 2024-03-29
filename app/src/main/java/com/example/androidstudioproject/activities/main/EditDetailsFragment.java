package com.example.androidstudioproject.activities.main;

import android.graphics.Bitmap;
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
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.login.LoginActivity;
import com.example.androidstudioproject.activities.login.SignUpFragment;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.authentication.AuthenticationViewModel;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.material.snackbar.Snackbar;

public class EditDetailsFragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/{
    EditText edtPhoneNumber;
    EditText edtAge;
    EditText edtFullName;
    EditText edtBio;
    Button edtProfilePicture;
    Button btnSave;
    Spinner edtSexualPreference;
    Spinner edtGender;

    Bitmap image;
    UsersViewModel usersViewModel;


    public static EditDetailsFragment newInstance() {
        EditDetailsFragment frag = new EditDetailsFragment();

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersViewModel = new UsersViewModel(this.getActivity().getApplication());

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragment = this;

        User loggedInUser = usersViewModel.getUserByEmail(((MainActivity)this.getActivity()).currEmail);

        int preference = loggedInUser.getSexualPreferences();
        String prefString;
        if(preference == 0)
            prefString = getString(R.string.male);
        else if(preference == 1)
            prefString = getString(R.string.female);
        else prefString = getString(R.string.both);
        edtSexualPreference.setSelection(preference+1);//.setPrompt(prefString);

        int gender = loggedInUser.getGender();
        String genderString;
        if(gender == 0)
            genderString = getString(R.string.male);
        else
            genderString = getString(R.string.female);
        edtGender.setSelection(gender+1);

        String fullName = loggedInUser.getFirstName() + getString(R.string.spaceChar) + loggedInUser.getLastName();
        edtFullName.setText(fullName);
        edtPhoneNumber.setText(loggedInUser.getPhoneNumber());
        edtAge.setText(String.valueOf(loggedInUser.getAge()));
        edtBio.setText(loggedInUser.getBio());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_edit_details, container, false);
    }

    public void setImage(Bitmap bitmap){
        image=bitmap;
//        edtProfilePicture.setImageBitmap(image);
//        Snackbar.make(view, R.string.underage, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User loggedInUser = usersViewModel.getUserByEmail(((MainActivity)this.getActivity()).currEmail);
        edtFullName = view.findViewById(R.id.fragEditDetails_fullName); //get input line (edit text) by id
        edtPhoneNumber = view.findViewById(R.id.fragEditDetails_phoneNumber); //get input line (edit text) by id
        edtAge = view.findViewById(R.id.fragEditDetails_age); //get input line (edit text) by id
        edtBio = view.findViewById(R.id.fragEditDetails_bio); //get input line (edit text) by id
        edtProfilePicture = view.findViewById(R.id.fragEditDetails_profilePicture);
        btnSave = view.findViewById(R.id.fragEditDetails_save);

        edtSexualPreference = view.findViewById(R.id.fragEditDetails_sexualPreference);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> prefAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sexual_preference_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        prefAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        edtSexualPreference.setAdapter(prefAdapter);

        edtGender = view.findViewById(R.id.fragEditDetails_gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        edtGender.setAdapter(genderAdapter);

        edtProfilePicture.setOnClickListener(v -> {
            ((MainActivity)this.getActivity()).pickImageFromGallery();
        });

        btnSave.setOnClickListener(v -> {

            String strAge = edtAge.getText().toString();
            String strSexualPreference = edtSexualPreference.getSelectedItem().toString();
            String strGender = edtGender.getSelectedItem().toString();
            String strFullName = edtFullName.getText().toString();
            String strPhoneNumber = edtPhoneNumber.getText().toString();
            String strBio = edtBio.getText().toString();

            if (TextUtils.isEmpty(strFullName)||TextUtils.isEmpty(strPhoneNumber)) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            if(strBio.length() > 140)
            {
                Snackbar.make(view, R.string.bio_too_long, Snackbar.LENGTH_LONG).show();
                return;
            }

            if(!Patterns.PHONE.matcher(strPhoneNumber).matches()) {
                Snackbar.make(view, R.string.wrong_mobile, Snackbar.LENGTH_LONG).show();
                return;
            }

            if(!strFullName.contains(getString(R.string.spaceChar))){
                Snackbar.make(view, R.string.no_full_name, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (Integer.parseInt(strAge) < 18) {
                Snackbar.make(view, R.string.underage, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (Integer.parseInt(strAge) > 100) {
                Snackbar.make(view, R.string.overage, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (strSexualPreference.equals(getString(R.string.sexualPreference))) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (strGender.equals(getString(R.string.gender))) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            loggedInUser.setAge(Integer.parseInt(strAge));
            loggedInUser.setBio(strBio);
            int toUpdate;
            if(strSexualPreference.equals(getString(R.string.male)))
                toUpdate = 0;
            else if(strSexualPreference.equals(getString(R.string.female)))
                toUpdate = 1;
            else toUpdate = 2;
            loggedInUser.setSexualPreferences(toUpdate);

            if(strGender.equals(getString(R.string.male)))
                toUpdate = 0;
            else
                toUpdate = 1;
            loggedInUser.setGender(toUpdate);
            loggedInUser.setFirstName(strFullName.split(getString(R.string.spaceChar))[0]);
            loggedInUser.setLastName(strFullName.split(getString(R.string.spaceChar))[1]);
            loggedInUser.setPhoneNumber(strPhoneNumber);
            // set profile picture if exists
            if(image!=null)
                ((MainActivity)this.getActivity()).getStorageViewModel().addImageAndUpdateUser(this,image,loggedInUser);
            else{
                usersViewModel.update(loggedInUser);
                Snackbar.make(view, R.string.updated_successfully, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
