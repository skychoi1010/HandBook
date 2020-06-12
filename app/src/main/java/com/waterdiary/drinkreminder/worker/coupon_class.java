package com.waterdiary.drinkreminder.worker;
import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class coupon_class{
    public String cost;
    public String isUsed;
    public String path;
    public coupon_class(){}
    public coupon_class(String cost,String isUsed, String path){
        this.cost = cost;
        this.isUsed=isUsed;
        this.path =path;
    }
}