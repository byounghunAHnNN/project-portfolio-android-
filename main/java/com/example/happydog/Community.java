package com.example.happydog;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Community extends AppCompatActivity {
    // 게시글 목록 주르륵
    RecyclerView rv;
    SwipeRefreshLayout sl;
    BoardAdapter boardAdapter;

    FloatingActionButton new_write;
    Retrofit retrofit;
    ContentVo contentvo;
    List<BoardVo> boardlist;
    String userNickname;
    Intent intent;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        context = this;
        new_write = findViewById(R.id.new_write);
        rv = findViewById(R.id.rv);
        sl = findViewById(R.id.sl);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        rv.setLayoutManager(layoutManager);
        rv.addItemDecoration(new DividerItemDecoration(context, 1));

        boardlist = new ArrayList<>();

        // 로그인 성공화면에서 넘긴 닉네임을 받음
        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");

        RetrofitClient();

        Service service = retrofit.create(Service.class);
        Call<ContentVo> call = service.getBoard(userNickname);

        call.enqueue(new Callback<ContentVo>() {
            @Override
            public void onResponse(Call<ContentVo> call, Response<ContentVo> response) {
                contentvo = response.body();
                Log.d("", "response :: " + contentvo);
                boardlist = contentvo.getBoard();
                boardAdapter = new BoardAdapter(boardlist);
                intent.putExtra("userNickname", userNickname);
                rv.setAdapter(boardAdapter);
                rv.smoothScrollToPosition(boardAdapter.getItemCount());
                //boardAdapter.notifyDataSetChanged();

                sl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        boardAdapter.notifyItemChanged(0);
                        change();
                        Toast.makeText(Community.this, "새로고침 완료", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<ContentVo> call, Throwable t) {

            }
        });

        // 버튼을 누르면 새 글 작성 페이지로 이동
        new_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Community.this, Write.class);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);
            }
        });
    }

    public void change() {
        Intent intent = new Intent();
        intent.putExtra("userNickname", userNickname);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        rv.setLayoutManager(layoutManager);
        boardlist = new ArrayList<>();

        Service service = retrofit.create(Service.class);
        Call<ContentVo> call = service.getBoard(userNickname);

        call.enqueue(new Callback<ContentVo>() {
            @Override
            public void onResponse(Call<ContentVo> call, Response<ContentVo> response) {

                contentvo = response.body();
                Log.d("", "response :: " + contentvo);
                boardlist = contentvo.getBoard();

                boardAdapter = new BoardAdapter(boardlist);
                rv.setAdapter(boardAdapter);
                boardAdapter.notifyDataSetChanged();
                sl.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ContentVo> call, Throwable t) {

            }
        });
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}
