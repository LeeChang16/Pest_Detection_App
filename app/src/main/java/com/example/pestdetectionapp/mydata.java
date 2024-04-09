package com.example.pestdetectionapp;

import android.graphics.Bitmap;

public class mydata {

    String ImagePath;
    String Id;
    String Name;
    String ScientificName;
    String PestOrder;
    String PestFamily;
    String Description;
    String Intervention;

    public mydata(){

    }

    public mydata(String ImagePath,String Id, String Name, String ScientificName, String PestOrder, String PestFamily, String Description, String Intervention){
        this.ImagePath = ImagePath;
        this.Id = Id;
        this.Name = Name;
        this.ScientificName = ScientificName;
        this.PestOrder = PestOrder;
        this.PestFamily = PestFamily;
        this.Description = Description;
        this.Intervention = Intervention;


    }

    public String getId() {
        return Id;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getName() {
        return Name;
    }

    public String getScientificName() {
        return ScientificName;
    }

    public String getPestOrder() {
        return PestOrder;
    }

    public String getPestFamily() {
        return PestFamily;
    }

    public String getDescription() {
        return Description;
    }

    public String getIntervention() {
        return Intervention;
    }
}
