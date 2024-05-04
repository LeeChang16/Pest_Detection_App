package com.example.pestdetectionapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface apiset {

    @GET("RestApi.php")
    Call<List<mydata>> getData();


    @GET("get_detection.php")
    Call<List<modelClass>> getUserData(@Query("userid") String userid);
}
