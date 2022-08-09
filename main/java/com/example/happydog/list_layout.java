package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class list_layout extends AppCompatActivity {

    TextView Board_No, Board_Title, Board_Writer, Board_Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layout);

        Board_No = findViewById(R.id.Board_No);
        Board_Title = findViewById(R.id.Board_Title);
        Board_Writer = findViewById(R.id.Board_Writer);
        Board_Day = findViewById(R.id.Board_Day);
    }
}