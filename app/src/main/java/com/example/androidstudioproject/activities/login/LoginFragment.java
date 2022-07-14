package com.example.androidstudioproject.activities.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidstudioproject.R;
import com.google.android.material.snackbar.Snackbar;

public class LoginFragment extends Fragment {

    EditText edtEmail;
    EditText edtPasswd;

    public static LoginFragment newInstance(/*Data data, int position*/) {
        LoginFragment frag = new LoginFragment();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtEmail = view.findViewById(R.id.fragLogin_userName_et); //get input line (edit text) by id
        edtPasswd = view.findViewById(R.id.fragLogin_password_et); //get input line (edit text) by id

        //fragLogin_google_btn
        View googleLoginBtn = view.findViewById(R.id.fragLogin_google_btn);
        googleLoginBtn.setOnClickListener(v->{
            ((LoginActivity) getActivity()).authenticateGoogle();
        });

        //fragLogin_newAccount_tv
        TextView newAccountBtn = view.findViewById(R.id.fragLogin_newAccount_tv);
        newAccountBtn.setOnClickListener(v -> {
            ((LoginActivity) getActivity()).replaceFragments(SignUpFragment.class);
        });


        //fragLogin_login_btn
        Button loginBtn = view.findViewById(R.id.fragLogin_login_btn);
        loginBtn.setOnClickListener(v -> {
            if(edtEmail== null || edtPasswd==null) //validate input
            {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            String strEmail = edtEmail.getText().toString();
            String strPasswd = edtPasswd.getText().toString();

            if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                Snackbar.make(view, R.string.wrong_emails, Snackbar.LENGTH_LONG).show();
                return;
            }

            if (TextUtils.isEmpty(strEmail)||TextUtils.isEmpty(strPasswd)) {
                Snackbar.make(view, R.string.empty_input, Snackbar.LENGTH_LONG).show();
                return;
            }

            //authenticate using firebase

            ((LoginActivity) getActivity()).authenticate(this,strEmail,strPasswd);
        });

    }

}