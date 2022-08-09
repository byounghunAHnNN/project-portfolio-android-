package com.example.happydog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginSuccess extends AppCompatActivity {
    ViewFlipper vf;
    ImageButton gps, cal, community, doginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        gps = findViewById(R.id.gps);
        cal = findViewById(R.id.cal);
        community = findViewById(R.id.community);
        doginfo = findViewById(R.id.doginfo);
        vf = findViewById(R.id.vf);

        // 로그인 성공시 로그인시 입력한 id 넘겨받아서 String 타입의 login_user_id에 저장
        Intent intent = getIntent();
        String userNickname = intent.getStringExtra("userNickname");
        Log.d("", "로그인성공시_response " + userNickname);


        ///////////////////// 이미지 자동 슬라이드
//        for (int i = 0; i < 2; i++) {
//            ImageView img = new ImageView(this);
//            img.setImageResource(R.drawable.cute1 + i);
//            vf.addView(img);
//        }
//
//        Animation showIn = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
//        vf.setInAnimation(showIn);
//        vf.setOutAnimation(this, android.R.anim.slide_out_right);
//        vf.setFlipInterval(2000);
//        vf.startFlipping();
        /////////////////////////////////

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSuccess.this, GpsCh.class);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);
            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSuccess.this, Calendar.class);
                intent.putExtra("userNickname", userNickname);
                startActivity(intent);
            }
        });

        community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginSuccess.this, Community.class);
                intent1.putExtra("userNickname", userNickname);
                startActivity(intent1);
            }
        });

        doginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginSuccess.this, Modify_Myinfo.class);
                intent1.putExtra("userNickname", userNickname);
                startActivity(intent1);
            }
        });
    }
}