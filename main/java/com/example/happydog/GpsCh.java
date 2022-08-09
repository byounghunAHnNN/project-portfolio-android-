package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GpsCh extends AppCompatActivity {
    ImageButton btn_owner, btn_slave, btn_friend;
    RecyclerView rv_friend;
    Intent intent;
    public static String friendNickname;
    String userNickname;
    List<UserVo> userlist;
    Retrofit retrofit;
    gpsUserList gpsuserlist;
    FriendAdapter friendadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_ch);

        btn_friend = findViewById(R.id.btn_friend);
        btn_owner = findViewById(R.id.btn_owner);
        btn_slave = findViewById(R.id.btn_slave);
        rv_friend = findViewById(R.id.rv_friend);

        RetrofitClient();

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname"); // 로그인한 유저 닉네임

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        rv_friend.setLayoutManager(layoutManager);
        rv_friend.addItemDecoration(new DividerItemDecoration(this, 1));

        userlist = new ArrayList<>();

        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭하면 db에 저장된 유저정보를 불러오고 -> 유저 닉네임만 띄움
                Service service = retrofit.create(Service.class);
                Call<gpsUserList> call = service.getGpsList("");

                call.enqueue(new Callback<gpsUserList>() {
                    @Override
                    public void onResponse(Call<gpsUserList> call, Response<gpsUserList> response) {
                        gpsuserlist = response.body();
                        userlist = gpsuserlist.getUservo();
                        friendadapter = new FriendAdapter(userlist);
                        rv_friend.setAdapter(friendadapter);
                        friendadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<gpsUserList> call, Throwable t) {

                    }
                });
            }
        });

        // 견주가 위치 확인하는 지도로 넘어감
        btn_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GpsCh.this, MasterGps.class);
                intent.putExtra("friendNickname", friendNickname);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);
            }
        });

        // 산책대행러 지도로 넘어감
        btn_slave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(GpsCh.this, Gps.class);
                intent.putExtra("friendNickname", friendNickname);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);

            }
        });


    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.61.7:8080/Dog14/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}