package com.example.androidstudioproject.entities;
import android.graphics.Bitmap;
//import androidx.room.PrimaryKey;


//@Entity
public class User {
    //    @PrimaryKey
//    @NonNull
    //login info
    private String email;
    //private String userName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    //private String password;

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

    public User(String email, String firstName, String lastName, String phoneNumber, String password, String bio, int age, int gender, int sexualPreferences, Bitmap profilePicture) {
        //this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
//        this.password = password;
        this.bio = bio;
        this.age = age;
        this.gender = gender;
        this.sexualPreferences = sexualPreferences;
        this.isDeleted = false;
        this.profilePicture = profilePicture;
    }

    //getters
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

    public void setProfilePicture(Bitmap profilePicture) { this.profilePicture = profilePicture; }
}