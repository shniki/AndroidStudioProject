package com.example.androidstudioproject.entities;

import android.graphics.Bitmap;

public class User {
    //login info
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    //personal info
    private String bio;
    private int age;
    private int gender; // 0 - male, 1 - female
    private int sexualPreferences; // 0 - male, 1 - female, 2 - both
    //private List<String> moreInfo;

    //helpful
    private Boolean isDeleted; // default: false
    private Bitmap profilePicture; // binary photo
    // anything else?

    public User(){
        this.isDeleted = false;
    }

    public User(String userName, String firstName, String lastName, String email, String phoneNumber, String password, String bio, int age, int gender, int sexualPreferences, Bitmap profilePicture) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.bio = bio;
        this.age = age;
        this.gender = gender;
        this.sexualPreferences = sexualPreferences;
        this.isDeleted = false;
        this.profilePicture = profilePicture;
    }

    //getters
    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    //setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setSexualPreferences(int sexualPreferences) {
        this.sexualPreferences = sexualPreferences;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}