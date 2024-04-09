package com.example.pestdetectionapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface apiset {

    @GET("RestApi.php")
    Call<List<mydata>> getData();
}
