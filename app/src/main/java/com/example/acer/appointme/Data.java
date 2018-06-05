package com.example.acer.appointme;

/**
 * Charana Mayakaduwa
 * 2016139
 * w1626663
 */

public class Data {

    private String date;
    private String time;
    private String title;
    private String details;

    public Data(String date, String time, String title, String details) {
        this.date = date;
        this.time = time;
        this.title = title;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }


}
