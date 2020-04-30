package com.waterdiary.drinkreminder.model;

public class History
{
    private String id;
    private String containerValue;
    private String containerMeasure;
    private String containerValueOZ;
    private String drinkDate;
    private String drinkTime;
    private String total_ml;

    public String getTotalML() {
        return total_ml;
    }

    public void setTotalML(String total_ml) {
        this.total_ml = total_ml;
    }

    public String getDrinkDate() {
        return drinkDate;
    }

    public void setDrinkDate(String drinkDate) {
        this.drinkDate = drinkDate;
    }

    public String getDrinkTime() {
        return drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getContainerValue() {
        return containerValue;
    }

    public void setContainerValue(String containerValue) {
        this.containerValue = containerValue;
    }

    public String getContainerMeasure() {
        return containerMeasure;
    }

    public void setContainerMeasure(String containerMeasure) {
        this.containerMeasure = containerMeasure;
    }

    public String getContainerValueOZ() {
        return containerValueOZ;
    }

    public void setContainerValueOZ(String containerValueOZ) {
        this.containerValueOZ = containerValueOZ;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}