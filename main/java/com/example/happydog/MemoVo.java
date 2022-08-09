package com.example.happydog;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MemoVo implements Serializable {
    @SerializedName("calendar")
    private List<CalendarContent> calendar = null;

    public List<CalendarContent> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<CalendarContent> calendar) {
        this.calendar = calendar;
    }

    @Override
    public String toString() {
        return "MemoVo{" +
                "calendar=" + calendar +
                '}';
    }
}
