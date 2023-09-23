package com.davinci.etone.omc;

public class Patriot_message {
    String message;
    long frequency;
    long hour;
    long start_date;

    public Patriot_message(String message, long frequency, long hour) {
        this.message = message;
        this.frequency = frequency;
        this.hour = hour;
    }
    public Patriot_message() {
        this.message = "";
        this.frequency = 0;
        this.hour = 0;
        this.start_date = 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getFrequency() {
        return frequency;
    }

    public void setFrequency(long frequency) {
        this.frequency = frequency;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getStart_date() {
        return start_date;
    }

    public void setStart_date(long start_date) {
        this.start_date = start_date;
    }
}
