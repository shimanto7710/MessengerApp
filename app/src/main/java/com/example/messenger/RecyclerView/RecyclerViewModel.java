package com.example.messenger.RecyclerView;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

public class RecyclerViewModel {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    //    private String posting;
//    private String date;
    @SerializedName("picture")
    ImageView imageView;


    public RecyclerViewModel() {

    }

    public RecyclerViewModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public RecyclerViewModel(int id, String name, ImageView imageView) {
        this.id = id;
        this.name = name;
        this.imageView = imageView;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
