package com.example.pestdetectionapp;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MyApi {
    @FormUrlEncoded
    @POST("AddUser.php")
    Call<modelClass> insertData(
            @Field("fname")String Fullname,
            @Field("town")String Town,
            @Field("province")String Province,
            @Field("occupation")String Occupation,
            @Field("user")String Username,
            @Field("pass")String Password

            );
}
