package com.example.happydog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.CustomViewHolder> {
    private Context c;
    List<BoardVo> datalist;
    Intent intent;
    String userNickname;

    public BoardAdapter(List<BoardVo> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public BoardAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 뷰홀더 생성 (레이아웃)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardAdapter.CustomViewHolder holder, @SuppressLint("RecyclerView") int position) { // 뷰홀더가 재활용될 때 실행
        String date = datalist.get(getItemCount()-1-position).getCreateDate().substring(0, 10); // 데이터베이스에 저장된 날짜 슬라이스

        holder.Board_No.setText(datalist.get(getItemCount()-1-position).getId().toString());
        holder.Board_Title.setText(datalist.get(getItemCount()-1-position).getTitle());
        holder.Board_Writer.setText(datalist.get(getItemCount()-1-position).getUservo().getUsernickname());
        holder.Board_Day.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), View_Content.class);
                userNickname = ((Community) Community.context).userNickname;
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("writer", datalist.get(getItemCount()-1-position).getUservo().getUsernickname()); // 게시글 작성자
                intent.putExtra("id", datalist.get(getItemCount()-1-position).getId()); // 게시글 번호

                v.getContext().startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() { // 아이템 갯수 조회
        return datalist.size();

    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView Board_No;
        TextView Board_Title;
        TextView Board_Writer;
        TextView Board_Day;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.Board_No = itemView.findViewById(R.id.Board_No);
            this.Board_Title = itemView.findViewById(R.id.Board_Title);
            this.Board_Writer = itemView.findViewById(R.id.Board_Writer);
            this.Board_Day = itemView.findViewById(R.id.Board_Day);
        }
    }
}
