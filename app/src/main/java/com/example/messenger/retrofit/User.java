package com.example.messenger.retrofit;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("image")
    private Image image;

    public User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name,String email,String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name) {
        this.name = name;
    }

    public User(int userId, String name, String email, String password, Image image) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}