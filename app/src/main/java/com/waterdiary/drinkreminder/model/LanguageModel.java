package com.waterdiary.drinkreminder.model;

public class LanguageModel
{
    private String name="";
    private String code="";
    private String title="";
    boolean isSelected=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void isSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
