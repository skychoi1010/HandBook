package com.waterdiary.drinkreminder.model;

public class BackUpFileModel
{
    private String name="";
    private String path="";
    private long lastmodify;
    boolean isSelected=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(long lastmodify) {
        this.lastmodify = lastmodify;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void isSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
