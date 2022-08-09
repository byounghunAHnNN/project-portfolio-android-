package com.example.happydog;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class getGps {
    @SerializedName("gps")
    private List<GpsVo> gpsVo = null;

    public List<GpsVo> getGpsVo() {
        return gpsVo;
    }

    public void setGpsVo(List<GpsVo> gpsVo) {
        this.gpsVo = gpsVo;
    }

    @Override
    public String toString() {
        return "getGps{" +
                "gpsVo=" + gpsVo +
                '}';
    }
}
