package com.waterdiary.drinkreminder.worker;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class handbook_hospitaldata{
    public String address;
    public String doctor;
    public String img_url;
    public String lat;
    public String lon;
    public String name;
    public handbook_hospitaldata(){}

    public handbook_hospitaldata(String address,String doctor, String img_url, String lat, String lon,String name){
        this.address = address;
        this.doctor=doctor;
        this.img_url = img_url;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }
}