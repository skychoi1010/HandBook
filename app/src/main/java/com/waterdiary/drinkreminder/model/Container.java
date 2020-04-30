package com.waterdiary.drinkreminder.model;

public class Container
{
    private String containerId;
    private String containerValue;
    private String containerValueOZ;
    private boolean isSelected=false;
    private boolean isOpen=false;
    private boolean isCustom=false;

    public Container()
    {

    }

    public Container(String containerId,String containerValue, boolean isSelected, boolean isOpen, boolean isCustom)
    {
        this.isSelected = isSelected;
        this.containerValue = containerValue;
        this.containerId = containerId;
        this.isOpen=isOpen;
        this.isCustom=isCustom;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void isCustom(boolean isCustom) {
        this.isCustom = isCustom;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void isSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void isOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getContainerValue() {
        return containerValue;
    }

    public void setContainerValue(String containerValue) {
        this.containerValue = containerValue;
    }

    public String getContainerValueOZ() {
        return containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ) {
        this.containerValueOZ = containerValueOZ;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}