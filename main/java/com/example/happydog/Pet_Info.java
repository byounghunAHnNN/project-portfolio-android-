package com.example.happydog;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Pet_Info extends AppCompatActivity {
    ImageView load_pet_img;
    TextView load_pet_name, load_pet_type, load_pet_size, load_pet_weight, load_pet_unique, btn_pet_modify;
    Intent intent;
    String userNickname, dog_image, getServerImg;
    Retrofit retrofit;
    int GET_GALLERY_IMAGE = 200;
    int dogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);

        load_pet_img = findViewById(R.id.load_pet_img);
        load_pet_name = findViewById(R.id.load_pet_name);
        load_pet_type = findViewById(R.id.load_pet_type);
        load_pet_size = findViewById(R.id.load_pet_size);
        load_pet_weight = findViewById(R.id.load_pet_weight);
        load_pet_unique = findViewById(R.id.load_pet_unique);
        btn_pet_modify = findViewById(R.id.btn_pet_modify);

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");

        RetrofitClient();

        Service service = retrofit.create(Service.class);
        Call<PetVo> call = service.DogDetail(userNickname);

        call.enqueue(new Callback<PetVo>() {
            @Override
            public void onResponse(Call<PetVo> call, Response<PetVo> response) {
                Log.d("", "정보 :: " + response.body().toString());
                dogId = response.body().getId();

                getServerImg = response.body().getDog_image();
                dog_image = getServerImg.replace("localhost", "192.168.0.46");

                Glide.with(Pet_Info.this)
                        .load(dog_image)
                        .into(load_pet_img);

                load_pet_name.setText(response.body().getDog_name());
                load_pet_type.setText(response.body().getBreed());
                load_pet_size.setText(response.body().getSize());
                load_pet_weight.setText(response.body().getWeight());
                load_pet_unique.setText(response.body().getEtc());
            }

            @Override
            public void onFailure(Call<PetVo> call, Throwable t) {

            }
        });

        btn_pet_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Pet_Info.this, DogModify.class);
                intent.putExtra("dog_id", dogId);
                intent.putExtra("pet_name", load_pet_name.getText().toString());
                intent.putExtra("pet_type", load_pet_type.getText().toString());
                intent.putExtra("pet_size", load_pet_size.getText().toString());
                intent.putExtra("pet_weight", load_pet_weight.getText().toString());
                intent.putExtra("pet_unique", load_pet_unique.getText().toString());
                intent.putExtra("pet_image", getServerImg);
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