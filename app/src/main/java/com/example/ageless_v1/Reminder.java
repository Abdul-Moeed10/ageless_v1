package com.example.ageless_v1;

import java.util.List;

public class Reminder {
    private String name;
    private String time;
    private List<String> days;
    private boolean selected;


    public Reminder(String name, String time, List<String> days) {
        this.name = name;
        this.time = time;
        this.days = days;
        this.selected = false;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}



