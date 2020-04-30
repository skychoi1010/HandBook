package com.waterdiary.drinkreminder.model;

public class SoundModel
{
    private int id;
    private String name;
    private boolean isSelected=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void isSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}