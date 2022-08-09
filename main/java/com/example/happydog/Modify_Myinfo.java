package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ServiceConfigurationError;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Modify_Myinfo extends AppCompatActivity {
    TextView user_modify_name, btn_pet_upload;
    EditText user_modify_password, user_modify_email;
    Button btn_user_modify;
    Retrofit retrofit;
    String userpw, useremail, userNickname;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_myinfo);

        RetrofitClient(); // 레트로핏 초기화

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        Log.d("", "정보2:: " + userNickname);

        user_modify_name = findViewById(R.id.user_modify_name);
        user_modify_password = findViewById(R.id.user_modify_password);
        user_modify_email = findViewById(R.id.user_modify_email);
        btn_user_modify = findViewById(R.id.btn_user_modify);
        btn_pet_upload = findViewById(R.id.btn_pet_upload);

        user_modify_name.setText(userNickname);

        btn_user_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpw = user_modify_password.getText().toString();
                useremail = user_modify_email.getText().toString();
                Log.d("", "정보:: " + userpw + useremail);

                Service service = retrofit.create(Service.class);
                Call<ResponseCode2> call = service.Modifyinfo(userNickname, userpw, useremail);

                call.enqueue(new Callback<ResponseCode2>() {
                    @Override
                    public void onResponse(Call<ResponseCode2> call, Response<ResponseCode2> response) {
                            Log.d("", "정보수정 :: " + response.body().getResponseCode2());
                            Toast.makeText(getApplicationContext(), response.body().getResponseCode2(), Toast.LENGTH_SHORT).show();
                            intent = new Intent(Modify_Myinfo.this, LoginSuccess.class);
                            intent.putExtra("userNickname", userNickname);
                            startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseCode2> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btn_pet_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Modify_Myinfo.this, Pet_Info.class);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);
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