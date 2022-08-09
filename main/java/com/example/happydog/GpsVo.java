package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class GpsVo {

    private double distance;
    private double timer;
    private double fee;
    private double latitude;
    private double longitude;
    @SerializedName("user")
    private UserVo uservo;
    private String ownusernickname;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public UserVo getUservo() {
        return uservo;
    }

    public void setUservo(UserVo uservo) {
        this.uservo = uservo;
    }

    public String getOwnusernickname() {
        return ownusernickname;
    }

    public void setOwnusernickname(String ownusernickname) {
        this.ownusernickname = ownusernickname;
    }

    @Override
    public String toString() {
        return "GpsVo{" +
                "distance='" + distance + '\'' +
                ", timer='" + timer + '\'' +
                ", fee='" + fee + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", uservo=" + uservo +
                ", ownusernickname='" + ownusernickname + '\'' +
                '}';
    }
}
