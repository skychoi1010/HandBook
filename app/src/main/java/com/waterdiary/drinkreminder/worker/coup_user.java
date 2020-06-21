package com.waterdiary.drinkreminder.worker;

public class coup_user {
    public String date;
    public String isUsed;
    public String path;
   // public String img;
    public coup_user(){}
    public coup_user(String date, String isUsed,String path){
        this.date = date;
        this.isUsed=isUsed;
        this.path =path;
        //this.img =img;
    }
}
