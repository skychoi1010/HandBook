package com.waterdiary.drinkreminder;

public class handbook_newsclass {
    private String content;
    private String date;
    private String link;
    private String read;
    private String title;

    public handbook_newsclass(String content, String date, String link, String read, String title) {
        this.content = content;
        this.date = date;
        this.link = link;
        this.read = read;
        this.title = title;
    }
    public String getcontent(){
        return content;
    }
    public void setcontent(String content){
        this.content=content;
    }
    public String getdate(){
        return date;
    }
    public void setdate(String date){
        this.date=date;
    }
    public String getlink(){
        return link;
    }
    public void setlink(String link){
        this.link=link;
    }
    public String getread(){
        return read;
    }
    public void setread(String read){
        this.read=read;
    }
    public String gettitle(){
        return title;
    }
    public void settitle(String title){
        this.title=title;
    }
}

