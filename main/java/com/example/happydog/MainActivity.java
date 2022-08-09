package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    ImageView iv_1; // 상단 로고
    EditText login_id, login_pw; // 아이디, 비밀번호
    Button btn_login, btn_join; // 로그인 , 회원가입 버튼
    Retrofit retrofit;
    String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitClient(); // 레트로핏 초기화

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_join = findViewById(R.id.btn_join);
        iv_1 = findViewById(R.id.iv_1);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent2);
            }
        });
    }

    private void login() {
        String id = login_id.getText().toString(); // 사용자가 로그인시 입력한 id
        String pw = login_pw.getText().toString(); // 사용자가 로그인시 입력한 pw

        // 객체 획득 후 반환
        Service service = retrofit.create(Service.class);
        Call<ResponseCode> call = service.getMember(id, pw);

        call.enqueue(new Callback<ResponseCode>() {
            @Override
            public void onResponse(Call<ResponseCode> call, Response<ResponseCode> response) {
                Log.d("", "response --> " + response.body().getResponseCode());
                userNickName = response.body().getResponseCode().getUsernickname();

                if (id.equals("") || pw.equals("")) {
                    Toast.makeText(MainActivity.this, "아이디와 패스워드 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (id.equals(response.body().getResponseCode().getUsername())) {
                    Toast.makeText(MainActivity.this, response.body().getResponseCode().getUsernickname() + "님으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(getApplicationContext(), LoginSuccess.class);
                    // 로그인시 받은 사용자 닉네임을 저장해서 전환되는 액티비티마다 넘겨줄것 -> 추후 게시글 삭제 및 댓글 삭제시 본인만 삭제할 수 있도록
                    intent1.putExtra("userNickname", userNickName);
                    startActivity(intent1);
                }  else {
                    Toast.makeText(MainActivity.this, "아이디나 패스워드 확인 후 다시 시도하여 주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCode> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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