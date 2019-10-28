package com.example.messenger;

public class DatabaseMessageModel {
    int id;
    int id1;
    int id2;
    String msg;
    String image;

    public DatabaseMessageModel(int id, int id1, int id2, String msg, String image) {
        this.id = id;
        this.id1 = id1;
        this.id2 = id2;
        this.msg = msg;
        this.image = image;
    }

    public DatabaseMessageModel(int id1, int id2, String msg, String image) {
        this.id1 = id1;
        this.id2 = id2;
        this.msg = msg;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
