package com.example.happydog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MasterGps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView tv_timer, tv_distance, tv_fee;
    ImageButton btn_mypet_location;
    Retrofit retrofit;
    String userNickname, friendNickname;
    ArrayList<LatLng> coordList = new ArrayList<LatLng>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_gps);

        RetrofitClient();

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        friendNickname = intent.getStringExtra("friendNickname");

        tv_timer = findViewById(R.id.tv_timer);
        tv_distance = findViewById(R.id.tv_distance);
        tv_fee = findViewById(R.id.tv_fee);
        btn_mypet_location = findViewById(R.id.btn_mypet_location);

        // fragment 안에 mapfragment 사용 할 때
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Map);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);
        /////

        Log.d("", "닉네임" + userNickname + friendNickname);


        btn_mypet_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Service service = retrofit.create(Service.class);
                Call<getGps> call = service.getlocation(userNickname, friendNickname);
                call.enqueue(new Callback<getGps>() {
                    @Override
                    public void onResponse(Call<getGps> call, Response<getGps> response) {

                        double latitude = response.body().getGpsVo().get(1).getLatitude();
                        double longitude = response.body().getGpsVo().get(1).getLongitude();

                        Log.d("", "확인 :: " + latitude + "//" + longitude);

                        LatLng position = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.runsiba));

                        mMap.addMarker(markerOptions.position(position).title("시작"));

                        Log.d("", "확인 list :: " + coordList);

                        for (int i = 0; i < response.body().getGpsVo().size(); i++) {
                            Log.d("", "확인 list :: " + coordList);
                            coordList.add(new LatLng(response.body().getGpsVo().get(i).getLatitude(), response.body().getGpsVo().get(i).getLongitude()));

                            Log.d("", "확인 list :: " + coordList);

                        }
                        PolylineOptions polylineOptions = new PolylineOptions();

                        polylineOptions.addAll(coordList);
                        polylineOptions
                                .width(15)
                                .color(Color.YELLOW);

                        mMap.addPolyline(polylineOptions);

                        coordList.clear();
                    }

                    @Override
                    public void onFailure(Call<getGps> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        Service service = retrofit.create(Service.class);
        Call<getGps> call = service.getlocation(userNickname, friendNickname);
        call.enqueue(new Callback<getGps>() {
            @Override
            public void onResponse(Call<getGps> call, Response<getGps> response) {
                Log.d("", "확인 :: " + response.body().getGpsVo());

                LatLng DEFAULT_LOCATION = new LatLng(response.body().getGpsVo().get(0).getLatitude(), response.body().getGpsVo().get(0).getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(DEFAULT_LOCATION);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.runsiba));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
                mMap.moveCamera(cameraUpdate);

                Log.d("", "확인 list :: " + coordList);

                for (int i = 0; i < response.body().getGpsVo().size(); i++) {
                    Log.d("", "확인 list :: " + coordList);
                    coordList.add(new LatLng(response.body().getGpsVo().get(i).getLatitude(), response.body().getGpsVo().get(i).getLongitude()));

                    String time1 = String.valueOf(response.body().getGpsVo().get(i).getTimer());
                    String distance1 = String.valueOf(response.body().getGpsVo().get(i).getDistance());
                    String fee1 = String.valueOf(response.body().getGpsVo().get(i).getFee());

                    tv_timer.setText("산책 시간 : " + time1 + " 초");
                    tv_distance.setText("산책 거리 : " + distance1 + " m");
                    tv_fee.setText("산책 요금:" + fee1 + " 원");

                    Log.d("", "확인 list :: " + coordList);

                }
                PolylineOptions polylineOptions = new PolylineOptions();

                polylineOptions.addAll(coordList);
                polylineOptions
                        .width(15)
                        .color(Color.YELLOW);

                mMap.addPolyline(polylineOptions);

                coordList.clear();
            }

            @Override
            public void onFailure(Call<getGps> call, Throwable t) {

            }
        });
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.61.7:8080/Dog14/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}