package com.example.happydog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {
    List<UserVo> userlist;

    public FriendAdapter(List<UserVo> userlist) {
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public FriendAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_friend_layout, parent, false);
        FriendAdapter.CustomViewHolder holder = new FriendAdapter.CustomViewHolder(view);

        return holder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.CustomViewHolder holder, int position) {
        holder.friend_nickname.setText(userlist.get(position).getUsernickname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GpsCh.friendNickname = userlist.get(position).getUsernickname();
                Log.d("", "클릭한 아이템 확인 :: " +  GpsCh.friendNickname);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != userlist ? userlist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView friend_nickname;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.friend_nickname = itemView.findViewById(R.id.friend_nickname);


        }
    }
}
