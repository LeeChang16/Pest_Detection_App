package com.example.pestdetectionapp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class apicontroller {
    private static final String url="http://192.168.100.10/MyAdmin/";
    private static apicontroller clientobject;
    private static Retrofit retrofit;

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Increase write timeout
            .build();
    apicontroller(){

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static synchronized apicontroller getInstance(){
        if(clientobject==null){
            clientobject=new apicontroller();
        }
        return clientobject;
    }

    apiset getapi(){
        return retrofit.create(apiset.class);
    }
}
