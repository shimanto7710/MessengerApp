package com.example.messenger.messenger;

import android.graphics.Bitmap;

import com.example.messenger.retrofit.User;

public class MessageItem {
    int id;
    String message;
    User sender;
    private boolean belongsToCurrentUser;
    String image;

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

//    public MessageItem(String message, User sender, boolean belongsToCurrentUser) {
//        this.message = message;
//        this.sender = sender;
//        this.belongsToCurrentUser = belongsToCurrentUser;
//    }

//    public MessageItem(String message, User sender, String image, boolean belongsToCurrentUser) {
//        this.message = message;
//        this.sender = sender;
//        this.image = image;
//        this.belongsToCurrentUser = belongsToCurrentUser;
//    }


    public MessageItem(int id,String message, User sender, boolean belongsToCurrentUser, String image) {
        this.message = message;
        this.sender = sender;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.image = image;
        this.id=id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}