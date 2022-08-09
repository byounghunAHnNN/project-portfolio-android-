package com.example.happydog;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class gpsUserList {
    @SerializedName("user")
    private List<UserVo> uservo = null;

    public List<UserVo> getUservo() {
        return uservo;
    }

    public void setUservo(List<UserVo> uservo) {
        this.uservo = uservo;
    }

    @Override
    public String toString() {
        return "gpsUserList{" +
                "uservo=" + uservo +
                '}';
    }
}
