package com.example.androidstudioproject.activities.main;

import static com.example.androidstudioproject.MyApplication.context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.androidstudioproject.R;
import com.example.androidstudioproject.SelectListener;
import com.example.androidstudioproject.adapters.UserAdapter;
import com.example.androidstudioproject.entities.User;
import com.example.androidstudioproject.repositories.user.UsersViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class SearchFragment extends Fragment implements SelectListener {
    UsersViewModel usersViewModel;
    List<User> usersFilteredList;
    RecyclerView listUsers;
    SearchView searchView;
    public SearchFragment(UsersViewModel uvm) {
        // Required empty public constructor
        usersViewModel=uvm;
        usersFilteredList=new ArrayList<>();
    }
    public static SearchFragment newInstance(UsersViewModel uvm) {
        SearchFragment fragment = new SearchFragment(uvm);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)this.getActivity()).currentFragmentName = this.getClass().getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = view.findViewById(R.id.searchView);
         listUsers=view.findViewById(R.id.rvSearch);
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s != null) {
                    filter(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (text != null) {
                   filter(text);
                }
                return false;
            }
        });
    }
    public void filter(String text) {
        usersFilteredList.clear();
        for (int i = 0; i < usersViewModel.getAllUsers().getValue().size(); i++) {
            String userName_ = usersFilteredList.get(i).getFirstName()+"_"+usersFilteredList.get(i).getLastName();
            if (userName_.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) ) {
                usersFilteredList.add(usersViewModel.getAllUsers().getValue().get(i));
            }
        }
        UserAdapter adapter = new UserAdapter(usersFilteredList,this);
        listUsers.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(User userSelected) {

    }
}