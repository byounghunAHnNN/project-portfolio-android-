package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class ResponseCode {

    private UserVo responseCode;

    public UserVo getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(UserVo responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "responseCode=" + responseCode +
                '}';
    }
}
