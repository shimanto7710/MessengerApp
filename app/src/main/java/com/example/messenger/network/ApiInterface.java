package com.example.messenger.network;

import com.example.messenger.RecyclerView.RecyclerViewModel;
import com.example.messenger.retrofit.ServerResponse;
import com.example.messenger.retrofit.User;

import java.util.List;

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

    @GET("/messenger/related_data.php")
    Call <List<RecyclerViewModel>> getFriendData(@Query("id") int id);

    @GET("/messenger/all_user.php")
    Call <List<RecyclerViewModel>> getAll(@Query("id") int id);

    @GET("/messenger/write_relation.php")
    Call <ServerResponse> makeFriend(@Query("self_id") int selfId,@Query("friend_id") int friendId);

    @GET("/messenger/insert_user.php")
    Call <ServerResponse> inserUser(@Query("name") String name,@Query("email") String email,@Query("password") String password);

    @GET("/messenger/get_self_id.php")
    Call <ServerResponse> getSelfId(@Query("email") String email);

}
