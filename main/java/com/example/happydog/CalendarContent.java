package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class CalendarContent {
    private String memo_content;
    private String memo_date;
    @SerializedName("user")
    private UserVo uservo;

    public String getMemo_content() {
        return memo_content;
    }

    public void setMemo_content(String memo_content) {
        this.memo_content = memo_content;
    }

    public String getMemo_date() {
        return memo_date;
    }

    public void setMemo_date(String memo_date) {
        this.memo_date = memo_date;
    }

    public UserVo getUservo() {
        return uservo;
    }

    public void setUservo(UserVo uservo) {
        this.uservo = uservo;
    }

    @Override
    public String toString() {
        return "CalendarContent{" +
                "memo_content='" + memo_content + '\'' +
                ", memo_date='" + memo_date + '\'' +
                ", uservo=" + uservo +
                '}';
    }
}
