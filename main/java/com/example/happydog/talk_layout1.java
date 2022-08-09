package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class talk_layout1 extends AppCompatActivity {
TextView talk_sender_name, talk_sender_msg, talk_sender_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_layout1);

        talk_sender_name = findViewById(R.id.talk_sender_name);
        talk_sender_msg = findViewById(R.id.talk_sender_msg);
        talk_sender_time = findViewById(R.id.talk_sender_time);
    }
}