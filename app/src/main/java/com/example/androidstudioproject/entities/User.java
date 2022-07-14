package com.example.androidstudioproject.entities;

import androidx.annotation.NonNull;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Entity;



@Entity
public class User {
    @PrimaryKey
    @NonNull
    //login info
    private String email;
    //private String userName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Boolean hasLoggedIn;
    //private String password;

    //personal info
    private String bio;
    private int age;
    private int gender; // 0 - male, 1 - female
    private int sexualPreferences; // 0 - male, 1 - female, 2 - both
    //private List<String> moreInfo;

    //helpful
    private String profilePicture; // binary photo - convert to Uri
    // anything else?

    public User(@NonNull String email, String firstName, String lastName, String phoneNumber, String bio, int age, int gender, int sexualPreferences, String profilePicture) {
        //this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
//        this.password = password;
        this.bio = bio;
        this.age = age;
        this.gender = gender;
        this.hasLoggedIn = false;
        this.sexualPreferences = sexualPreferences;
        this.profilePicture = profilePicture;
    }

    @Ignore
    public User(){
        this.hasLoggedIn = false;
    }

    //getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getHasLoggedIn() { return hasLoggedIn; }

    @NonNull
    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public int getAge() {
        return age;
    }

    public int getGender() {
        return gender;
    }

    public int getSexualPreferences() {
        return sexualPreferences;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogIn() { this.hasLoggedIn = true; }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setHasLoggedIn(Boolean hasLoggedIn) {
        this.hasLoggedIn = hasLoggedIn;
    }

    public void setSexualPreferences(int sexualPreferences) {
        this.sexualPreferences = sexualPreferences;
    }

    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}