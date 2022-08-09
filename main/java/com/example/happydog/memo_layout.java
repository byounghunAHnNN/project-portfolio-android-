package com.example.happydog;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class memo_layout extends AppCompatActivity {
    EditText my_memo;
    TextView time_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_layout);

        my_memo = findViewById(R.id.my_memo);
        time_memo = findViewById(R.id.time_memo);

    }
}
