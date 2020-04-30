package com.waterdiary.drinkreminder.model;

public class Menu
{
    private String menuName;
    private boolean isSelected=false;

    public Menu(String menuName,boolean isSelected)
    {
        this.isSelected = isSelected;
        this.menuName = menuName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void isSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}