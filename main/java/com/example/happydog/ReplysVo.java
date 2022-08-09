package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class ReplysVo {

    private Integer id;
    private String content;
    @SerializedName("user")
    private UserVo uservo;
    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserVo getUservo() {
        return uservo;
    }

    public void setUservo(UserVo uservo) {
        this.uservo = uservo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Replys{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", uservo=" + uservo +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
