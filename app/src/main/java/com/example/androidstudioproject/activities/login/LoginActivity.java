package com.example.androidstudioproject.activities.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.activities.main.MainActivity;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    UsersViewModel viewModel;

    private FirebaseAuth mAuth;

    GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new UsersViewModel(this.getApplication());
        //getActivity()

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, (GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.loginFragment, fragment)
                .commit();
    }

    public Boolean authenticate(String email, String password){
        //todo send to firebase auth email password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails
                            updateUI(null);
                        }
                    }
                });
        return (mAuth.getCurrentUser()!=null);
    }

    public void gotoMainActivity(){
        Intent switchActivityIntent = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(switchActivityIntent);
    }

    public boolean isExistsUser(String email){
        //todo use firebase auth - to check if email exists
        //return viewModel.exists(email);
        return false;
    }

    public void addUser(String email, String password, String name, String number, String gender, String age) {
        //adds to firebase database
        String[] splitted = name.split(" ", 1);

        int _age = Integer.parseInt(age);

        int _gender;
        if (gender.equals("Male")) _gender = 0;
        else _gender = 1;

        User u = new User(email, splitted[0], splitted[1], number, null, _age, _gender, 2, null);
        viewModel.add(u);

        //todo authenticate using firebase (+create new user)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails
                            updateUI(null);
                        }
                    }
                });
    }

    public void authenticateGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();

                if(!isExistsUser(account.getEmail())) {
                    //create new User
                    User u = new User(account.getEmail(),
                            account.getGivenName(),
                            account.getFamilyName(),
                            null, null, 18, 0, 2,
                            account.getPhotoUrl().toString());
                    viewModel.add(u);
                }

                //goto main activity
                gotoMainActivity();
            }
        }
    }

}