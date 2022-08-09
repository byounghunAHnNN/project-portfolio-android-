package com.example.happydog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.CustomViewHolder> {
    private Context c;
    String userNickname;
    private List<CalendarContent> list;

    public MemoAdapter(List<CalendarContent> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_memo_layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.CustomViewHolder holder, int position) {
        holder.my_memo.setText(list.get(position).getMemo_content());
        holder.time_memo.setText(list.get(position).getMemo_date());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        EditText my_memo;
        TextView time_memo;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.my_memo = itemView.findViewById(R.id.my_memo);
            this.time_memo = itemView.findViewById(R.id.time_memo);
        }
    }
}
