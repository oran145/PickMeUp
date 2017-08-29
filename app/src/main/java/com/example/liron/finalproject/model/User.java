package com.example.liron.finalproject.model;

import android.graphics.Bitmap;

/**
 * Created by liron on 25-Jul-17.
 */

public class User {
    private String userID;
    private Bitmap userImage;
    private String imageFireBaseUrl;
    private String imageLocalUrl;
    private String firstName;
    private String lastName;
    private long birthday;
    private String email;
    private String password;
    private long lastUpdated;
    private int isSignedIn;//int because this is how it going to be stored in sqlite,0-not signed ,1- signed


    public User(String userID, Bitmap userImage, String imageFireBaseUrl, String firstName, String lastName, long birthday, String email, String password, long lastUpdated, int isSignedIn) {
        this.userID = userID;
        this.userImage = userImage;
        this.imageFireBaseUrl = imageFireBaseUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.lastUpdated=lastUpdated;
        this.isSignedIn=isSignedIn;
    }

    public User() {}

    public String getUserID() {
        return userID;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageFireBaseUrl() {
        return imageFireBaseUrl;
    }

    public void setImageFireBaseUrl(String imageFireBaseUrl) {
        this.imageFireBaseUrl = imageFireBaseUrl;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getIsSignedIn() {
        return isSignedIn;
    }

    public void setIsSignedIn(int isSignedIn) {
        this.isSignedIn = isSignedIn;
    }

    public boolean translateIsSignedInToBool()
    {
        return isSignedIn == 1;
    }

    /**
     * taking boolean isSignIn and translate into integer,because from firebase we get boolean
     * @param isSignIn true or flase got from firebase
     */
    public void setIntFromBoolInSignIn(Boolean isSignIn)
    {
        this.isSignedIn = isSignIn==true ? 1 : 0;
    }

    public String getImageLocalUrl() {
        return imageLocalUrl;
    }

    public void setImageLocalUrl(String imageLocalUrl) {
        this.imageLocalUrl = imageLocalUrl;
    }
}
