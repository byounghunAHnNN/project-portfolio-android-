package com.example.happydog;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class BoardVo {

    private Integer id;
    private String title;
    private String content_image;
    private String content_text;
    private Integer count;
    @SerializedName("user")
    private UserVo uservo;
    private List<ReplysVo> replies = null;
    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent_image() {
        return content_image;
    }

    public void setContent_image(String content_image) {
        this.content_image = content_image;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserVo getUservo() {
        return uservo;
    }

    public void setUservo(UserVo uservo) {
        this.uservo = uservo;
    }

    public List<ReplysVo> getReplys() {
        return replies;
    }

    public void setReplys(List<ReplysVo> replies) {
        this.replies = replies;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    @Override
    public String toString() {
        return "BoardVo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content_image='" + content_image + '\'' +
                ", content_text=" + content_text +
                ", count=" + count +
                ", uservo=" + uservo +
                ", replys=" + replies +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}

