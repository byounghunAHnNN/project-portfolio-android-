package com.example.happydog;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Write extends AppCompatActivity {
    TextView write_title, write_content;
    Button btn_upload;
    ImageButton btn_img_upload;
    Retrofit retrofit;
    Intent intent;
    List<BoardVo> boardlist;
    BoardAdapter boardAdapter;
    ImageView write_image;
    int GET_GALLERY_IMAGE = 200;
    Bitmap bitmap;
    Uri uri;
    File file;
    String userNickname, AndroidTitle, AndroidContent, bitmapEncode, realpath, AndroidImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        write_title = findViewById(R.id.write_title);
        write_content = findViewById(R.id.write_content);
        btn_upload = findViewById(R.id.btn_upload);
        write_image = findViewById(R.id.write_image);
        btn_img_upload = findViewById(R.id.btn_img_upload);

        RetrofitClient();

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        Log.d("", "???????????????_response " + userNickname);

        boardlist = new ArrayList<>();

        // ????????? ?????? ??????
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AndroidTitle = write_title.getText().toString();
                AndroidContent = write_content.getText().toString();

                if (AndroidTitle.equals("") || AndroidContent.equals("")) {
                    Toast.makeText(Write.this, "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                } else if (AndroidContent.length() <= 9) {
                    Toast.makeText(Write.this, "?????? ?????? ?????? 10??? ?????? ?????????.", Toast.LENGTH_SHORT).show();
                } else {
                    Service service = retrofit.create(Service.class);
                    Call<ContentVo> call = service.AndroidWrite(AndroidTitle, AndroidContent, AndroidImage, userNickname);

                    call.enqueue(new Callback<ContentVo>() {
                        @Override
                        public void onResponse(Call<ContentVo> call, Response<ContentVo> response) {

                            intent = new Intent(Write.this, Community.class);
                            intent.putExtra("userNickname", userNickname);
                            boardAdapter = new BoardAdapter(boardlist);
                            boardAdapter.notifyItemInserted(boardAdapter.getItemCount() - 1);

                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ContentVo> call, Throwable t) {

                        }
                    });

                    Toast.makeText(Write.this, "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ????????? ?????? ???????????? ????????? ????????? ????????? ??????
        btn_img_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);

                activityResultLauncher.launch(intent);

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

                            // ????????? ?????????
                            ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytearray);

                            byte[] bytes = bytearray.toByteArray();
                            bitmapEncode = Base64.encodeToString(bytes, Base64.NO_WRAP);

                            Log.d("", "????????? ?????? bitmapencode :: " + bitmapEncode);

                            write_image.setImageBitmap(bitmap);

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


    // ????????? ????????? ?????? ?????? ????????? ?????????
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
                .baseUrl("http://192.168.0.46:8080/") // ????????? ???????????? ?????? ?????? ??????
                .addConverterFactory(GsonConverterFactory.create()) // ?????? ?????? ??? ?????? ???????????? ???????????? ??????????????????
                .build();
    }
}