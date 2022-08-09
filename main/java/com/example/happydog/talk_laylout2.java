package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class talk_laylout2 extends AppCompatActivity {
    TextView talk_my_msg, talk_my_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_laylout2);

        talk_my_msg = findViewById(R.id.talk_my_msg);
        talk_my_time = findViewById(R.id.talk_my_time);
    }
}