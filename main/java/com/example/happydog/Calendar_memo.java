package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Calendar_memo extends AppCompatActivity {
    RecyclerView memo_rv;
    Button upload_memo;
    EditText et_memo;
    Intent intent;
    Retrofit retrofit;
    String memo, userNickname, date;
    MemoVo memovo;
    List<CalendarContent> calendarcontent;
    private MemoAdapter memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_memo);

        memo_rv = findViewById(R.id.memo_rv);
        upload_memo = findViewById(R.id.upload_memo);
        et_memo = findViewById(R.id.et_memo);

        memo_rv.setLayoutManager(new LinearLayoutManager(this, memo_rv.VERTICAL, false));

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        date = intent.getStringExtra("date");

        RetrofitClient();

        calendarcontent = new ArrayList<>();

        Service service = retrofit.create(Service.class);
        Call<MemoVo> call = service.allMemo("단비짱");

        call.enqueue(new Callback<MemoVo>() {
            @Override
            public void onResponse(Call<MemoVo> call, Response<MemoVo> response) {
                memovo = response.body();
                calendarcontent = memovo.getCalendar();
                memoAdapter = new MemoAdapter(calendarcontent);

                memo_rv.setAdapter(memoAdapter);

                Log.d("", "메모 :: " + memovo);
            }

            @Override
            public void onFailure(Call<MemoVo> call, Throwable t) {

            }
        });

        upload_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long now = System.currentTimeMillis();
//                Date date = new Date(now);
//                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
//                getTime = simpleDate.format(date).substring(2, 10);
                memo = et_memo.getText().toString();
                Log.d("", "메모 :: " + userNickname + memo + date);

                Service service = retrofit.create(Service.class);
                Call<Memo> call = service.sendMemo(memo, date, "민기");
                call.enqueue(new Callback<Memo>() {
                    @Override
                    public void onResponse(Call<Memo> call, Response<Memo> response) {
                        Log.d("" , "dddddd :: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<Memo> call, Throwable t) {

                    }
                });

                memo_rv.setAdapter(memoAdapter);
                memoAdapter.notifyDataSetChanged();
            }
        });

    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.24:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}