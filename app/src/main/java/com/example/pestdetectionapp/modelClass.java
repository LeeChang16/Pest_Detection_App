package com.example.pestdetectionapp;

public class modelClass {
    String  pestname;
    String confidence;
    String date;
    String time;
    String userid;
    String uploaded;
    String image;
    String location;

   public modelClass(){

   }

    public modelClass(String pestname, String confidence, String date, String time, String userid, String uploaded, String image, String location) {
        this.pestname = pestname;
        this.confidence = confidence;
        this.date = date;
        this.time = time;
        this.userid = userid;
        this.uploaded = uploaded;
        this.image = image;
        this.location = location;
    }

    public String getPestname() {
        return pestname;
    }

    public String getConfidence() {
        return confidence;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUserid() {
        return userid;
    }

    public String getUploaded() {
        return uploaded;
    }

    public String getImage() {
        return image;
    }
    public String getLocation() {
        return location;
    }
}
