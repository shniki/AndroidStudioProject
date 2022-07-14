package com.example.androidstudioproject.activities.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.entities.UserConnections;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class SignUpFragment extends Fragment /*implements AdapterView.OnItemSelectedListener*/{

    EditText edtEmail;
    EditText edtPasswd;
    EditText edtFullName;
    EditText edtPhoneNumber;
    EditText edtAge;

    ValueEventListener eventListener;

    public static SignUpFragment newInstance(/*Data data, int position*/) {
        SignUpFragment frag = new SignUpFragment();

//        Bundle b = new Bundle();
//        b.putSerializable("data", data);
//        b.putInt("position", position);
//        frag.setArguments(b);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        data = (Data) getArguments().getSerializable("data");
//        position = getArguments().getInt("position");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtEmail = view.findViewById(R.id.fragLogin_userName_et); //get input line (edit text) by id
        edtPasswd = view.findViewById(R.id.fragLogin_password_et); //get input line (edit text) by id
        edtFullName = view.findViewById(R.id.fragLogin_fullName); //get input line (edit text) by id
        edtPhoneNumber = view.findViewById(R.id.fragLogin_phoneNumber); //get input line (edit text) by id
        edtAge = view.findViewById(R.id.fragLogin_age); //get input line (edit text) by id


        Spinner edtGender = view.findViewById(R.id.fragLogin_gender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, R.layout.gender_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        edtGender.setAdapter(adapter);

        //fragLogin_login_btn
        TextView gotoLoginBtn = view.findViewById(R.id.fragLogin_login_btn);
        gotoLoginBtn.setOnClickListener(v -> {

            if (edtEmail == null || edtPasswd == null || edtFullName == null || edtPhoneNumber == null || edtAge == null) //validate input
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            String strEmail = edtEmail.getText().toString();
            String strPasswd = edtPasswd.getText().toString();
            String strFullName = edtFullName.getText().toString();
            String strPhoneNumber = edtPhoneNumber.getText().toString();
            String strAge = edtAge.getText().toString();
            String strGender = edtGender.getSelectedItem().toString();

            if (TextUtils.isEmpty(strGender) || TextUtils.isEmpty(strAge) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPasswd) || TextUtils.isEmpty(strFullName) || TextUtils.isEmpty(strPhoneNumber)) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                Snackbar.make(view, R.string.wrong_emails, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (!Patterns.PHONE.matcher(strPhoneNumber).matches()) {
                Snackbar.make(view, R.string.wrong_mobile, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (strPasswd.length() < 8) {
                Snackbar.make(view, R.string.short_password, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (strGender.equals(getString(R.string.gender))) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (!strFullName.contains(getString(R.string.spaceChar))) {
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

            if ((((LoginActivity) getActivity()).isExistsUser(strEmail))) {
                Snackbar.make(view, R.string.user_already_exists, Snackbar.LENGTH_LONG).show();
                return;
            }
            DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Users");
            ((LoginActivity) getActivity()).addUser(strEmail,strPasswd,strFullName,strPhoneNumber,strGender,strAge);
            eventListener = stRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ((LoginActivity) getActivity()).gotoMainActivity(); //try auth instead
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });

    }


}
