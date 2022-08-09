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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Modify_Content extends AppCompatActivity {
    EditText modify_title, modify_content;
    TextView modify_writer, delete_image;
    Button btn_modify_finish;
    Retrofit retrofit;
    Intent intent;
    Integer id;
    String title, content_text, userNickname, writer, ServerImg, bitmapEncode, realpath, AndroidImage, imagelink;
    int GET_GALLERY_IMAGE = 200;
    Bitmap bitmap;
    Uri uri;
    File file;
    ImageView modify_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_content);

        modify_title = findViewById(R.id.modify_title);
        modify_content = findViewById(R.id.modify_content);
        btn_modify_finish = findViewById(R.id.btn_modify_finish);
        modify_writer = findViewById(R.id.modify_writer);
        modify_image = findViewById(R.id.modify_image);
        delete_image = findViewById(R.id.delete_image);

        RetrofitClient();

        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        content_text = intent.getStringExtra("content_text");
        userNickname = intent.getStringExtra("userNickname");
        writer = intent.getStringExtra("writer");
        ServerImg = intent.getStringExtra("content_image");
        imagelink = intent.getStringExtra("imagelink");

        modify_title.setText(title);
        modify_content.setText(content_text);
        modify_writer.setText(writer);

        Glide.with(this)
                .load(ServerImg)
                .into(modify_image);

        AndroidImage = imagelink;

        modify_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);

                activityResultLauncher.launch(intent);
            }
        });

        delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidImage = "";
                modify_image.setVisibility(View.INVISIBLE);

            }
        });

        btn_modify_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = modify_title.getText().toString();
                content_text = modify_content.getText().toString();

                Service service = retrofit.create(Service.class);
                Call<ContentVo> call = service.AndroidModify(id, title, content_text, AndroidImage);
                Log.d("", "이미지 :: " + id + title + content_text + AndroidImage);
                call.enqueue(new Callback<ContentVo>() {
                    @Override
                    public void onResponse(Call<ContentVo> call, Response<ContentVo> response) {
                        Intent intent = new Intent(Modify_Content.this, Community.class);
                        intent.putExtra("userNickname", userNickname);
                        startActivity(intent);
                        Toast.makeText(Modify_Content.this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ContentVo> call, Throwable t) {

                    }
                });
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        uri = result.getData().getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            realpath = getRealPathFromURI(uri);
                            file = new File(realpath);

                            // 비트맵 인코딩
                            ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytearray);

                            byte[] bytes = bytearray.toByteArray();
                            bitmapEncode = Base64.encodeToString(bytes, Base64.NO_WRAP);

                            Log.d("", "이미지 주소 bitmapencode :: " + bitmapEncode);

                            int num = realpath.lastIndexOf("/");
                            String pathReplace = realpath.substring(num).replace("/", ",");
                            AndroidImage = bitmapEncode + pathReplace;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (result.equals("")) {
                        AndroidImage = " ";
                    }
                }
            }
    );


    // 이미지 파일의 실제 경로 구하는 메서드
    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.46:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }

}