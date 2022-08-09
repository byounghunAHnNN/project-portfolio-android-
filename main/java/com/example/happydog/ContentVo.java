package com.example.happydog;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContentVo implements Serializable {
    @SerializedName("board")
    private List<BoardVo> board = null;

    public List<BoardVo> getBoard() {
        return board;
    }

    public void setBoard(List<BoardVo> board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "ContentVo{" +
                "board=" + board +
                '}';
    }
}
