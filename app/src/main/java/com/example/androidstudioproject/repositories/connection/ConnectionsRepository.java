package com.example.androidstudioproject.repositories.connection;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidstudioproject.AppDB;
import com.example.androidstudioproject.daos.ConnectionDao;
import com.example.androidstudioproject.entities.UserConnections;

import java.util.LinkedList;
import java.util.List;

public class ConnectionsRepository {
    private static final String TAG = "MyTag";
    private ConnectionDao dao;
    private ConnectionListData connectionListData;

    public ConnectionsRepository(Application application) {
        AppDB db = AppDB.getInstance(application);
        dao = db.connectionDao();
        connectionListData = new ConnectionListData();
    }

    public void cancellGetAllConnections() {
        ConnectionModelFirebase.instance.cancellGetAllConnections();
    }

    public UserConnections getConnectionIfExists(String firstEmail, String secondEmail) { return dao.getConnectionIfExists(firstEmail, secondEmail);}

    public void delete(UserConnections connection) { dao.delete(connection); }

    class ConnectionListData extends MutableLiveData<List<UserConnections>> {

        @Override
        protected void onActive() {
            super.onActive();

            Log.d("MyTag", "OnActive");

            new ConnectionsAsyncTask().execute(new Runnable() {
                @Override
                public void run() {

                    List<UserConnections> s = dao.index();
                    Log.d(TAG, s.size() + " connections in the database");
                    postValue(s);

                    ConnectionModelFirebase.instance.getAllConnections(new ConnectionModelFirebase.GetAllConnectionsListener() {
                        @Override
                        public void onSuccess(final List<UserConnections> connectionsList) {

                            Log.d(TAG, connectionsList.size() + " connections from firebase");

                            setValue(connectionsList);

                            new ConnectionsAsyncTask().execute(new Runnable() {
                                @Override
                                public void run() {
                                    dao.clear();
                                    dao.insertAll(connectionsList);
                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            Log.d("MyTag", "OnInActive");

            cancellGetAllConnections();
        }

        public ConnectionListData() {
            super();
            setValue(new LinkedList<UserConnections>());
        }
    }

    public LiveData<List<UserConnections>> getAllConnections() {
        return connectionListData;
    }

    public void add (final UserConnections connection) {
        new ConnectionsAsyncTask().execute(new Runnable() {
            @Override
            public void run() {
                ConnectionModelFirebase.instance.addConnection(connection);
            }
        });
    }

    private static class ConnectionsAsyncTask extends AsyncTask<Runnable, Void, Void> {

        @Override
        protected Void doInBackground(final Runnable... params) {
            params[0].run();
            return null;
        }
    }
}

