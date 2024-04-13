package com.example.pestdetectionapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiset {

    @GET("RestApi.php")
    Call<List<mydata>> getData();


    @GET("RestApi.php")
    Call<List<modelClass>> getUserData();
}
