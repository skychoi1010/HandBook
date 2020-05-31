package com.waterdiary.drinkreminder.worker;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class News {

    public String content;
    public String date;
    public String link;
    public String read;
    public String title;

    public News(){}
    public News(String content, String date, String link, String read, String title){
        this.content = content;
        this.date = date;
        this.link = link;
        this.read = read;
        this.title = title;
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("content", content);
//        result.put("date", date);
//        result.put("link", link);
//        result.put("read", read);
//        result.put("title", title);
//
//        return result;
//
//    }
}