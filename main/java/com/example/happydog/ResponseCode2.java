package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class ResponseCode2 {
    @SerializedName("responseCode")
    private String responseCode2;

    public String getResponseCode2() {
        return responseCode2;
    }

    public void setResponseCode2(String responseCode2) {
        this.responseCode2 = responseCode2;
    }

    @Override
    public String toString() {
        return "ResponseCode2{" +
                "responseCode2='" + responseCode2 + '\'' +
                '}';
    }
}
