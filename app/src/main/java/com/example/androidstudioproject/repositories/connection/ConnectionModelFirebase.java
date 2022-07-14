package com.example.androidstudioproject.repositories.connection;

import android.util.Log;

import com.example.androidstudioproject.entities.Post;
import com.example.androidstudioproject.entities.UserConnections;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class ConnectionModelFirebase {

    private static final String TAG = "MyTag";
    public static ConnectionModelFirebase instance = new ConnectionModelFirebase();

    private ConnectionModelFirebase(){}

    private String hash(final UserConnections connection){
        String mash = connection.getUserEmail()+" "+connection.getSecondUserEmail();
        return String.valueOf(mash.hashCode());
    }

    public void addConnection(UserConnections connection){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Connections");
        ref.child(hash(connection)).setValue(connection);
    }

    public void deleteConnection(UserConnections connection){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Connections");


        String key = hash(connection);
        ref.child(key).removeValue();
    }

    public void cancellGetAllConnections() {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Connections");
        stRef.removeEventListener(eventListener);
    }

    interface GetAllConnectionsListener{
        public void onSuccess(List<UserConnections> connectionslist);
    }

    ValueEventListener eventListener;

    public void getAllConnections(final GetAllConnectionsListener listener) {
        DatabaseReference stRef = FirebaseDatabase.getInstance().getReference().child("Connections");

        Log.d(TAG, "Getting from firebase");

        eventListener = stRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserConnections> stList = new LinkedList<>();

                Log.d(TAG, "Back from firebase");

                for (DataSnapshot stSnapshot: dataSnapshot.getChildren()) {
                    UserConnections st = stSnapshot.getValue(UserConnections.class);
                    stList.add(st);
                }

                Log.d(TAG, stList.size() + " from firebase");

                listener.onSuccess(stList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
