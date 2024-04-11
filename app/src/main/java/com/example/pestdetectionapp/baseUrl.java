package com.example.pestdetectionapp;

public class baseUrl {
    private static baseUrl instance;
    private static final String url = "http://192.168.100.11/MyAdmin/";

    public String getUrl(){
        return url;
    }


    public static synchronized baseUrl getInstance() {
        if (instance == null) {
            instance = new baseUrl();
        }
        return instance;
    }
}
