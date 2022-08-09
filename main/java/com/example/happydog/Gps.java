package com.example.happydog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Gps extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    TextView tv_timer, tv_distance, tv_fee;
    Button btn_timer_start, btn_timer_finish, btn_timer_reset;
    Retrofit retrofit;
    private Marker currentMarker = null;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 5000; //10초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 5000; //5초
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final int PRIORITY_HIGH_ACCURACY = 100;
    boolean needRequest = false;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION}; //외부 저장소
    LatLng currentPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    int i = 0;
    private boolean state = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Boolean isRunning = true;
    private Thread timeThread = null;
    ArrayList<Location> myPath = new ArrayList<Location>();
    private View mLayout;
    private GoogleMap mMap;

    String lat, longi, userNickname, friendNickname;
    Intent intent;
    double setlat, setlng;
    double distance, timer, fee;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        tv_timer = findViewById(R.id.tv_timer);
        tv_distance = findViewById(R.id.tv_distance);
        tv_fee = findViewById(R.id.tv_fee);
        btn_timer_start = findViewById(R.id.btn_timer_start);
        btn_timer_finish = findViewById(R.id.btn_timer_finish);
        btn_timer_reset = findViewById(R.id.btn_timer_reset);

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        friendNickname = intent.getStringExtra("friendNickname");

        RetrofitClient();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Gps.this);

        ///// 타이머 실행
        btn_timer_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    btn_timer_start.setText("산책중");

                    Timer timer = new Timer();

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            sendLatLng(setlat, setlng);
                            getDistance();
                            sendDistimer(totalDistance, totaltime, Mtotaltime);
                        }
                    };
                    timer.schedule(timerTask,0,5000);
                }

                timeThread = new Thread(new timeThread());
                timeThread.start();

                if (checkPermission()) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, polylocation, Looper.myLooper());


                    //btn_timer_start.setText("산책 다시 시작");
                }
            }
        });

        // 타이머 일시 중지
        btn_timer_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = !isRunning;
                if (isRunning) {
                    btn_timer_finish.setText("일시정지");
                    tv_distance.setText("산책거리 : " + totalDistance + " m");
                    tv_fee.setText("산책요금 : " + Mtotaltime + "원");
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                fusedLocationProviderClient.removeLocationUpdates(polylocation);

                btn_timer_start.setText("다시 산책");

                btn_timer_finish.setText("산책 멈추기");

            }
        });

        // 타이머 리셋
        btn_timer_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_timer.setText("00:00:00:00");

                mMap.clear();
                if (myPath != null) {
                    myPath.clear();
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mLayout = findViewById(R.id.layout_gps);

        locationRequest = LocationRequest.create()
                .setPriority(PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS)
                .setSmallestDisplacement(5);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.Map);
        assert mapFragment != null;
        mapFragment.getMapAsync((OnMapReadyCallback) this);


    }

    private void sendLatLng(Double a, Double b) {
        String latitude = a.toString();
        String longtiude = b.toString();

        lat = latitude;
        longi = longtiude;
    }

    private void sendDistimer(double distance, double timer, double fee) {

        Service service = retrofit.create(Service.class);
        Call<ResponseCode2> call = service.sendGps(distance, timer, fee, lat, longi, friendNickname, userNickname);
        call.enqueue(new Callback<ResponseCode2>() {
            @Override
            public void onResponse(Call<ResponseCode2> call, Response<ResponseCode2> response) {
                Log.d("", "디비에 저장됐는지 확인 ::: " + response.body());
            }

            @Override
            public void onFailure(Call<ResponseCode2> call, Throwable t) {
            }
        });
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();
        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates(); // 3. 위치 업데이트 시작
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Gps.this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> {
                    // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(Gps.this, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE);
                }).show();
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Gps.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }

    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                //3줄이 실시간으로 서버에 위도경도 보내는거.
                setlat = location.getLatitude();
                setlng = location.getLongitude();

                myPath.add(location);
                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());
                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + location.getLatitude()
                        + " 경도:" + location.getLongitude();
                Log.d("", "onLocationResult : " + markerSnippet);
                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);
            }
        }
    };

    LocationCallback polylocation = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(locationList.size() - 1);
                // 3줄이 실시간으로 서버에 위도경도 보내는거.

                myPath.add(location);
                updateMap();

            }
        }
    };

    private void startLocationUpdates() {
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            if (checkPermission())

                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "onStart");

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap != null)
                mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    public String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.runsiba));
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        currentMarker = mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
    }

    public void setDefaultLocation() {
        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

        if (currentMarker != null) currentMarker.remove();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);

        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.runsiba));
        currentMarker = mMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);
    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           String[] permissions,
                                           int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            if (check_result) {
                // 퍼미션을 허용했다면 위치 업데이트를 시작합니다.
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> finish()).show();
                } else {
                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", view -> finish()).show();
                }
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Gps.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, id) -> {
            Intent callGPSSettingIntent
                    = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
        });
        builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        needRequest = false;
                        return;
                    }
                }
                break;
        }
    }

    double totaltime = 0;
    double Mtotaltime = totaltime * 0.03;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 1000 = 1초
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60;
            int hour = (msg.arg1 / 100) / 3600;
            totaltime = msg.arg1;
            Mtotaltime = msg.arg1 * 0.03;

            String result = String.format("%02d:%02d:%02d:%02d", hour, min, sec, mSec);

            tv_timer.setText(result);
        }
    };

    public void onClick(View view) {
    }


    public class timeThread implements Runnable {
        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(10); // 스레드가 돌아가는 도중에 오류가 일어나지 않으면 10millis에 한번 스레드 발생


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 핸들러에 데이터를 전달해줄 공간
                Message msg = new Message();
                msg.arg1 = i++; // 10millis에 한번씩 더해짐 -> 스톱워치에 보여지는 시간
                handler.sendMessage(msg);
            }
        }
    }

    double totalDistance = 0;

    public void getDistance() {
        Location lastLoc;
        Location curLoc;
        if (myPath.size() > 1) {
            for (int i = 1; i < myPath.size(); i++) {
                lastLoc = myPath.get(i - 1);
                curLoc = myPath.get(i);

                float distanceMeters = lastLoc.distanceTo(curLoc);
                totalDistance = totalDistance + distanceMeters;

            }
        }
    }

    //stackflow aagitoEx source
    private LatLng polyLatLng;
    Polyline polyline;

    public void updateMap() {
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < myPath.size(); i++) {
            location = myPath.get(i);
            polyLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            polylineOptions.add(polyLatLng);

        }
        polylineOptions.width(10f)
                .color(Color.YELLOW)
                .geodesic(true);
        if (mMap != null) {
            polyline = mMap.addPolyline(polylineOptions);
        }
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://3.39.61.7:8080/Dog14/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}