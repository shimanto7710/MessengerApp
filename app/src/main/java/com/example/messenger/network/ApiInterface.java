package com.example.messenger.network;

import com.example.messenger.retrofit.ServerResponse;
import com.example.messenger.retrofit.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

//    @POST("/messenger/get_data.php")
//    Call<ServerResponse> getUserValidity(@Query("email") String email);

//    @GET("/messenger/login_check.php")
//    Call<ServerResponse> getUserValidity(@Query("email") String email);


    @POST("/messenger/login_check.php")
    Call<ServerResponse> getUserValidity(@Body User userLoginCredential);

}
