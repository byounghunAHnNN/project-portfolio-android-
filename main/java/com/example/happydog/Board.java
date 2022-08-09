package com.example.happydog;

import com.google.gson.annotations.SerializedName;

public class Board {
    @SerializedName("board")
    private BoardVo board;

    public BoardVo getBoard() {
        return board;
    }

    public void setBoard(BoardVo board) {
        this.board = board;
    }

    @Override
    public String toString() {
        return "Board{" +
                "board=" + board +
                '}';
    }
}
