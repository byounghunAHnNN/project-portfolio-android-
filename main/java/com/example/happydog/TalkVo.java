package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class TalkVo {
    @SerializedName("sender_name")
    private String sender_name;

    @SerializedName("sender_msg")
    private String sender_msg;

    @SerializedName("sender_time")
    private String sender_time;

    @SerializedName("my_msg")
    private String my_msg;

    @SerializedName("my_time")
    private String my_time;

    public String getSender_name() { return sender_name; }

    public String getSender_msg()  { return sender_msg;}

    public String getSender_time() { return sender_time;}

    public String getMy_msg() { return my_msg;}

    public  String getMy_time() { return my_time;}

    public void setSender_name() { this.sender_name = sender_name; }

    public void setSender_msg() {  this.sender_msg = sender_msg;}

    public void setSender_time() { this.sender_time = sender_time;}

    public void setMy_msg() { this.my_msg = my_msg; }

    public void setMy_time() { this.my_time = my_time; }

    @Override
    public String toString() {
        return "TalkVo{" +
                " , sender_name ='" + sender_name + '\'' +
                " , sender_msg' " + sender_msg + '\''  +
                " , sender_time ='" + sender_time + '\'' +
                " , my_msg' " + my_msg + '\''  +
                " , my_time ='" + my_time + '\'' +
                '}';
    }
}