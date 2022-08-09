package com.example.happydog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.CustomViewHolder> {
    private Context c;
    List<ReplysVo> replylist;
    Retrofit retrofit;
    String userNickname;
    String writer;
    Integer ReplyId, boardId;

    public ReplyAdapter(List<ReplysVo> replylist) {
        this.replylist = replylist;
    }

    @NonNull
    @Override
    public ReplyAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reply_layout, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        userNickname = ((Community) Community.context).userNickname;
        boardId = ((View_Content) View_Content.context1).id;

        return holder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        String date = replylist.get(position).getCreateDate().substring(2, 10);
        holder.reply_writer.setText(replylist.get(position).getUservo().getUsernickname());
        holder.reply_content.setText(replylist.get(position).getContent());
        holder.reply_time.setText(date);

        ReplyId = replylist.get(position).getId();
        writer = replylist.get(position).getUservo().getUsernickname();

        if (userNickname.equals(writer)) {
            holder.btn_reply_delete.setVisibility(View.VISIBLE);
        }

        holder.btn_reply_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitClient();
                Service service = retrofit.create(Service.class);
                Call<Board> call = service.ReplyDelete(ReplyId, writer, boardId);

                call.enqueue(new Callback<Board>() {
                    @Override
                    public void onResponse(Call<Board> call, Response<Board> response) {
                        Toast.makeText(v.getContext(), "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        replylist.remove(replylist.get(position));
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<Board> call, Throwable t) {

                    }
                });
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext())
                .setTitle("채팅 시도")
                        .setMessage("메시지를 보내시겠습니까?")
                        .setPositiveButton("전송", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String replyWriter = replylist.get(position).getUservo().getUsernickname();
                                Intent intent = new Intent(v.getContext(), Talk.class);
                                intent.putExtra("replyWriter", replyWriter);

                                v.getContext().startActivity(intent);

                            }
                        })
              .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                  }
              });
                AlertDialog ad = builder.create();
                ad.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != replylist ? replylist.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView reply_writer;
        TextView reply_content;
        TextView reply_time;
        ImageButton btn_reply_delete;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.reply_writer = itemView.findViewById(R.id.reply_writer);
            this.reply_content = itemView.findViewById(R.id.reply_content);
            this.reply_time = itemView.findViewById(R.id.reply_time);
            btn_reply_delete = itemView.findViewById(R.id.btn_reply_delete);
        }
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}
