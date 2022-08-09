package com.example.happydog;

import com.google.gson.annotations.SerializedName;


public class Memo {
    @SerializedName("calendar")
    private CalendarContent calendarContent;

    public CalendarContent getCalendarContent() {
        return calendarContent;
    }

    public void setCalendarContent(CalendarContent calendarContent) {
        this.calendarContent = calendarContent;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "calendarContent=" + calendarContent +
                '}';
    }
}
