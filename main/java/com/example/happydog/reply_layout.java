package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class reply_layout extends AppCompatActivity {
    TextView reply_writer, reply_content, reply_time;
    ImageButton btn_reply_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_layout);

        reply_writer = findViewById(R.id.reply_writer);
        reply_content = findViewById(R.id.reply_content);
        reply_time = findViewById(R.id.reply_time);
        btn_reply_delete = findViewById(R.id.btn_reply_delete);

    }


}