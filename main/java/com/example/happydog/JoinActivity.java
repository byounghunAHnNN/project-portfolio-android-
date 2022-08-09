package com.example.happydog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity {

    EditText join_id, join_pw, user_name, et_email, et_address, et_email_code;
    Button join, btn_email, btn_address, btn_id_check, btn_name_check;
    TextView tv_email_check, tv_code_check, tv_id_check, tv_name_check;
    Retrofit retrofit;
    int nCode, email_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        RetrofitClient(); // 레트로핏 초기화

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        join = findViewById(R.id.join);
        join_id = findViewById(R.id.join_id); // 아이디
        join_pw = findViewById(R.id.join_pw); // 비밀번호
        user_name = findViewById(R.id.user_name); // 닉네임
        btn_email = findViewById(R.id.btn_email);
        et_email = findViewById(R.id.et_email);
        btn_address = findViewById(R.id.btn_address);
        et_address = findViewById(R.id.et_address);
        tv_email_check = findViewById(R.id.tv_email_check);
        et_email_code = findViewById(R.id.et_email_code);
        tv_code_check = findViewById(R.id.tv_code_check);
        tv_id_check = findViewById(R.id.tv_id_check);
        btn_id_check = findViewById(R.id.btn_id_check);
        btn_name_check = findViewById(R.id.btn_name_check);
        tv_name_check = findViewById(R.id.tv_name_check);

        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCheck();
            }
        });

        btn_name_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameCheck();
            }
        });

        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinActivity.this, SearchActivity.class);
                getSearchResult.launch(intent);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //!android.util.Patterns.EMAIL_ADDRESS.matcher(et_email.toString()).matches()
                String email = et_email.getText().toString();
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Random random = new Random();
                    nCode = random.nextInt(999999) + 100000; //6자리 랜덤 정수를 생성
                    Log.d("", "emailCode::: -> " + nCode);

                    try {
                        GMailSender gMailSender = new GMailSender("danbi97771@gmail.com", "yurapyzxxuglovql");
                        //GMailSender.sendMail(제목, 본문내용(인증코드), 받는사람);
                        gMailSender.sendMail("개르만족 이메일 인증 코드 입니다.", "안녕하세요. \n 개르만족 인증코드는 " + nCode + " 입니다. ", et_email.getText().toString());

                        tv_email_check.setText("이메일 인증 코드가 발송되었습니다.");
                        et_email.setBackgroundResource(R.drawable.green_edittext);
                        tv_email_check.setTextColor(Color.GREEN);

                    } catch (SendFailedException e) {
                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    } catch (MessagingException e) {
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    tv_email_check.setText("이메일 형식을 확인해주세요.");
                    et_email.setBackgroundResource(R.drawable.red_edittext);
                    tv_email_check.setTextColor(Color.RED);
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join();


            }
        });

    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // search Activity 로부터의 결과 값이 이곳으로 전달된다. setResult에 의해
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) ;
                    {
                        String data = result.getData().getStringExtra("data");
                        et_address.setText(data);
                    }
                }
            }
    );


    public void idCheck() {
        String username = join_id.getText().toString();
        Service service = retrofit.create(Service.class);
        Call<ResponseCode2> call = service.getUsername(username);

        call.enqueue(new Callback<ResponseCode2>() {
            @Override
            public void onResponse(Call<ResponseCode2> call, Response<ResponseCode2> response) {
                if (username.length() <= 7) {
                    tv_id_check.setText("아이디는 최소 8자여야 합니다.");
                    tv_id_check.setTextColor(Color.RED);
                    join_id.setBackgroundResource(R.drawable.red_edittext);
                } else if (username.equals(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
                    tv_id_check.setText("아이디에 한글은 사용할 수 없습니다.");
                } else if (response.body().getResponseCode2().equals("사용 가능한 ID입니다.")) {
                    tv_id_check.setText(response.body().getResponseCode2());
                    join_id.setBackgroundResource(R.drawable.green_edittext);
                    tv_id_check.setTextColor(Color.GREEN);
                } else {
                    tv_id_check.setText(response.body().getResponseCode2());
                    join_id.setBackgroundResource(R.drawable.red_edittext);
                    tv_id_check.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<ResponseCode2> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void nameCheck() {
        String usernickname = user_name.getText().toString();
        Service service = retrofit.create(Service.class);
        Call<ResponseCode2> call = service.getUsernickname(usernickname);

        call.enqueue(new Callback<ResponseCode2>() {
            @Override
            public void onResponse(Call<ResponseCode2> call, Response<ResponseCode2> response) {
                if (response.body().getResponseCode2().equals("사용 가능한 닉네임입니다.")) {
                    tv_name_check.setText(response.body().getResponseCode2());
                    user_name.setBackgroundResource(R.drawable.green_edittext);
                    tv_name_check.setTextColor(Color.GREEN);
                } else {
                    tv_name_check.setText(response.body().getResponseCode2());
                    user_name.setBackgroundResource(R.drawable.red_edittext);
                    tv_name_check.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFailure(Call<ResponseCode2> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void join() {
        String username = join_id.getText().toString();
        String pw = join_pw.getText().toString();
        String usernickname = user_name.getText().toString();
        String email = et_email.getText().toString();
        String address = et_address.getText().toString();

        // 객체 획득 후 반환
        Service service = retrofit.create(Service.class);
        Call<ResponseCode2> call = service.getJoin(username, pw, usernickname, address, email);

        call.enqueue(new Callback<ResponseCode2>() {
            @Override
            public void onResponse(Call<ResponseCode2> call, Response<ResponseCode2> response) {
                email_code = Integer.parseInt(et_email_code.getText().toString());
                // 회원가입시 칸을 다 채워야 회원가입 되도록
                if (username.equals("") || pw.equals("") || usernickname.equals("") || email.equals("") || address.equals("")) {
                    Toast.makeText(getApplicationContext(), "칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else if (nCode != email_code) { // 입력한 이메일코드와 발급된 이메일코드가 틀리면
                    Log.d("", "enter_email_code --> " + email_code);
                    tv_code_check.setText("이메일 코드를 확인해주세요.");
                    tv_code_check.setTextColor(Color.RED);
                } else {
                    Toast.makeText(JoinActivity.this, usernickname + " 님, 정상적으로 가입되었습니다.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseCode2> call, Throwable t) {
                Toast.makeText(JoinActivity.this, "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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