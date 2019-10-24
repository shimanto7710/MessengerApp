package com.example.messenger.retrofit;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("status")
    boolean statusString; //variable name is statusString but it'll map with "status"
    @SerializedName("message")
    String messageString; //variable name is messageString but it'll map with "message"

    public boolean isSuccess(){
        return statusString;
    }

    public String getMessage() {
        return messageString;
    }
}