package com.waterdiary.drinkreminder.model;

public class AlarmModel
{
    private String id;
    private String drinkTime;
    private String alarmId;
    private String alarmType;
    private String alarmInterval;

    private Integer isOff=0;
    private Integer sunday=0;
    private Integer monday=0;
    private Integer tuesday=0;
    private Integer wednesday=0;
    private Integer thursday=0;
    private Integer friday=0;
    private Integer saturday=0;





    public String getDrinkTime() {
        return drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmInterval() {
        return alarmInterval;
    }

    public void setAlarmInterval(String alarmInterval) {
        this.alarmInterval = alarmInterval;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }


    public Integer getIsOff() {
        return isOff;
    }

    public void setIsOff(Integer isOff) {
        this.isOff = isOff;
    }

    public Integer getSunday() {
        return sunday;
    }

    public void setSunday(Integer sunday) {
        this.sunday = sunday;
    }

    public Integer getMonday() {
        return monday;
    }

    public void setMonday(Integer monday) {
        this.monday = monday;
    }

    public Integer getTuesday() {
        return tuesday;
    }

    public void setTuesday(Integer tuesday) {
        this.tuesday = tuesday;
    }

    public Integer getWednesday() {
        return wednesday;
    }

    public void setWednesday(Integer wednesday) {
        this.wednesday = wednesday;
    }

    public Integer getThursday() {
        return thursday;
    }

    public void setThursday(Integer thursday) {
        this.thursday = thursday;
    }

    public Integer getFriday() {
        return friday;
    }

    public void setFriday(Integer friday) {
        this.friday = friday;
    }

    public Integer getSaturday() {
        return saturday;
    }

    public void setSaturday(Integer saturday) {
        this.saturday = saturday;
    }

    //=============

    private String alarmSundayId;
    private String alarmMondayId;
    private String alarmTuesdayId;
    private String alarmWednesdayId;
    private String alarmThursdayId;
    private String alarmFridayId;
    private String alarmSaturdayId;

    public String getAlarmSundayId() {
        return alarmSundayId;
    }

    public void setAlarmSundayId(String alarmSundayId) {
        this.alarmSundayId = alarmSundayId;
    }


    public String getAlarmMondayId() {
        return alarmMondayId;
    }

    public void setAlarmMondayId(String alarmMondayId) {
        this.alarmMondayId = alarmMondayId;
    }


    public String getAlarmTuesdayId() {
        return alarmTuesdayId;
    }

    public void setAlarmTuesdayId(String alarmTuesdayId) {
        this.alarmTuesdayId = alarmTuesdayId;
    }


    public String getAlarmWednesdayId() {
        return alarmWednesdayId;
    }

    public void setAlarmWednesdayId(String alarmWednesdayId) {
        this.alarmWednesdayId = alarmWednesdayId;
    }


    public String getAlarmThursdayId() {
        return alarmThursdayId;
    }

    public void setAlarmThursdayId(String alarmThursdayId) {
        this.alarmThursdayId = alarmThursdayId;
    }


    public String getAlarmFridayId() {
        return alarmFridayId;
    }

    public void setAlarmFridayId(String alarmFridayId) {
        this.alarmFridayId = alarmFridayId;
    }


    public String getAlarmSaturdayId() {
        return alarmSaturdayId;
    }

    public void setAlarmSaturdayId(String alarmSaturdayId) {
        this.alarmSaturdayId = alarmSaturdayId;
    }
}