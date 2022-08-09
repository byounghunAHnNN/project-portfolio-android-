package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class View_Content extends AppCompatActivity {
    // 게시글 클릭 시 보여지는 상세화면
    ReplyAdapter adapter;
    RecyclerView recyclerview;
    ScrollView content_scroll;
    SwipeRefreshLayout srl;
    TextView view_title, view_writer, view_content, view_date, btn_modify, btn_delete;
    ImageView view_image;
    EditText et_reply;
    Button btn_reply;
    List<ReplysVo> datalist;
    Integer id;
    Retrofit retrofit;
    String title, content_text, content_image, userNickname, writer, ServerImg, getServerImg;
    public static Context context1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_content);

        context1 = this;

        recyclerview = findViewById(R.id.recyclerview);
        srl = findViewById(R.id.srl);
        view_title = findViewById(R.id.view_title);
        view_writer = findViewById(R.id.view_writer);
        view_content = findViewById(R.id.view_content);
        view_image = findViewById(R.id.view_image);
        et_reply = findViewById(R.id.et_reply);
        btn_reply = findViewById(R.id.btn_reply);
        btn_modify = findViewById(R.id.btn_modify);
        btn_delete = findViewById(R.id.btn_delete);
        view_date = findViewById(R.id.view_date);
        content_scroll = findViewById(R.id.content_scroll);

        Intent intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        id = intent.getIntExtra("id", 0); // 게시글 클릭시 게시글 번호 넘겨받음
        writer = intent.getStringExtra("writer");

        // 게시글 스크롤
        view_content.setMovementMethod(new ScrollingMovementMethod());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        datalist = new ArrayList<>();

        RetrofitClient();

        Service service = retrofit.create(Service.class);
        Call<Board> call = service.getBoardDetail(id);

        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                String writer = intent.getStringExtra("writer");

                view_title.setText(response.body().getBoard().getTitle());
                view_content.setText(response.body().getBoard().getContent_text());
                view_writer.setText(response.body().getBoard().getUservo().getUsernickname());
                view_date.setText(response.body().getBoard().getCreateDate());

                getServerImg = response.body().getBoard().getContent_image();

                if (getServerImg == null) {
                    ServerImg = " ";
                } else {
                    ServerImg = getServerImg.replace("localhost", "192.168.0.46");

                    Glide.with(View_Content.context1)
                            .load(ServerImg)
                            .into(view_image);
                }

                datalist = response.body().getBoard().getReplys();
                Log.d("", "reply" + datalist);
                adapter = new ReplyAdapter(datalist);
                recyclerview.setAdapter(adapter);

                adapter.notifyDataSetChanged();

                title = response.body().getBoard().getTitle();
                content_text = response.body().getBoard().getContent_text();
                content_image = ServerImg;

                // 로그인 한 유저와 게시글 작성자가 같아야 수정, 삭제 버튼 보이게함
                if (writer.equals(userNickname) == true) {
                    btn_modify.setVisibility(View.VISIBLE);
                    btn_delete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Board> call, Throwable t) {

            }
        });

        // 댓글 새로고침
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upload_reply();
            }
        });

        // 게시글 수정
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(View_Content.this, Modify_Content.class);
                intent.putExtra("id", id);
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("title", title);
                intent.putExtra("content_text", content_text);
                intent.putExtra("writer", writer);
                intent.putExtra("content_image", ServerImg);
                intent.putExtra("imagelink", getServerImg);
                startActivity(intent);
            }
        });

        // 게시글 삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Service service = retrofit.create(Service.class);
                Call<ContentVo> call = service.AndroidDelete(id, userNickname);
                call.enqueue(new Callback<ContentVo>() {
                    @Override
                    public void onResponse(Call<ContentVo> call, Response<ContentVo> response) {
                        Intent intent = new Intent(View_Content.this, Community.class);

                        intent.putExtra("id", id);
                        intent.putExtra("userNickname", userNickname);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ContentVo> call, Throwable t) {

                    }
                });
                Toast.makeText(View_Content.this, "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 댓글 작성
        btn_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply = et_reply.getText().toString();

                Service service = retrofit.create(Service.class);
                Call<BoardVo> call = service.ReplyWrite(id, userNickname, reply, " ");

                call.enqueue(new Callback<BoardVo>() {
                    @Override
                    public void onResponse(Call<BoardVo> call, Response<BoardVo> response) {
                        et_reply.setText("");

                        datalist = response.body().getReplys();
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);
                        recyclerview.setAdapter(adapter);
                        upload_reply();
                    }

                    @Override
                    public void onFailure(Call<BoardVo> call, Throwable t) {

                    }
                });

                Toast.makeText(View_Content.this, "댓글이 작성되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }

    public void upload_reply() {
        Service service = retrofit.create(Service.class);
        Call<Board> call = service.getBoardDetail(id);

        call.enqueue(new Callback<Board>() {
            @Override
            public void onResponse(Call<Board> call, Response<Board> response) {
                datalist = response.body().getBoard().getReplys();
                Log.d("", "reply" + datalist);
                adapter = new ReplyAdapter(datalist);
                recyclerview.setAdapter(adapter);
                srl.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Board> call, Throwable t) {

            }
        });
    }

}