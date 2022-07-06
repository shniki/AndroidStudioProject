package com.example.androidstudioproject.activities.main;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.R;

public class MainActivity extends AppCompatActivity {
    private AppDB roomDB;
    //private FirebaseAuth AuthUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "main\n");

       // TextView tv = findViewById(R.id.helloworld);
       /* tv.setOnClickListener(view-> {
                    Intent switchActivityIntent = new Intent(this, SettingsActivity.class);
                    startActivity(switchActivityIntent);
                }); */

        //initialize
        //AuthUI = FirebaseAuth.getInstance();

        // Write a message to the database
        //choose dataset
        //FirebaseDatabase database = FirebaseDatabase.getInstance("https://androidstudioproject-b82dc-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference myRef = database.getReference("message");//myRef = key

        //database.ge

//        myRef.setValue("Hello, World!"); //value

        //if(FirebaseAuth.getInstance().getCurrentUser()==null){
            //...
        //}

        // Read from the database
        //addListenerForSingleValueEvent = only for one value
 /*       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = AuthUI.getCurrentUser();
        //updateUI(currentUser);
    }
}