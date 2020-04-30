package com.waterdiary.drinkreminder.model;

public class NextReminderModel implements Comparable<NextReminderModel>
{
    private long millesecond;
    private String time;

    public NextReminderModel(long millesecond, String time){
        this.millesecond = millesecond;
        this.time = time;
    }

    public long getMillesecond() {
        return millesecond;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int compareTo(NextReminderModel f) {

        if (millesecond > f.millesecond) {
            return 1;
        }
        else if (millesecond <  f.millesecond) {
            return -1;
        }
        else {
            return 0;
        }

    }

    @Override
    public String toString(){
        return this.time;
    }
}
