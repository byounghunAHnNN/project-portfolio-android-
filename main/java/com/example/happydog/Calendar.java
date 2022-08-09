package com.example.happydog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Calendar extends AppCompatActivity {

    MaterialCalendarView materialCalendarView;
    Button write_memo;
    TextView view_memo;
    Intent intent;
    String userNickname, memo, getdate;
    List<CalendarContent> contentList;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        RetrofitClient();

        intent = getIntent();
        userNickname = intent.getStringExtra("userNickname");
        memo = intent.getStringExtra("memo");

        materialCalendarView = findViewById(R.id.calendarView);
        write_memo = findViewById(R.id.write_memo);
        view_memo = findViewById(R.id.view_memo);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(java.util.Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2000, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator(),
                new OneDayDecorator());

        String[] result = {"2017,03,18", "2017,04,18", "2017,05,18", "2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                String.valueOf(Year);
                String.valueOf(Month);
                String.valueOf(Day);

                getdate = String.valueOf(Year) + "-" + String.valueOf(Month) + "-" + String.valueOf(Day);

                Log.d("", "날짜 확인 ::: " + getdate.substring(2, 9));

                String day = getdate.substring(2, 9);

                contentList = new ArrayList<>();

                Service service = retrofit.create(Service.class);
                Call<MemoVo> call = service.getDayMemo("민기", day);

                Log.d("", "메모 데이 :: " + day);

                call.enqueue(new Callback<MemoVo>() {
                    @Override
                    public void onResponse(Call<MemoVo> call, Response<MemoVo> response) {
                        Log.d("", "메모 Response :: " + response.body());
                        MemoVo memoVo = response.body();
                        contentList = memoVo.getCalendar();




                        Log.d("", "메모 Response :: " + response.body().getCalendar());


                        //view_memo.setText(response.body().getCalendarContent().getMemo_content());ㅉ
                    }

                    @Override
                    public void onFailure(Call<MemoVo> call, Throwable t) {

                    }
                });

                write_memo.setVisibility(View.VISIBLE);
                materialCalendarView.clearSelection();

//                if (Year ==  && Month && Day) {
//
//                }

            }
        });

        write_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Calendar.this, Calendar_memo.class);
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("date", getdate.substring(2, 9));
                startActivity(intent);
            }
        });

    }


    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ArrayList<CalendarDay> dates = new ArrayList<>();

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }
        }
    }

    public void RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.24:8080/") // 가져올 데이터가 담긴 서버 주소
                .addConverterFactory(GsonConverterFactory.create()) // 통신 완료 후 어떤 컨버터로 데이터를 파싱할것인지
                .build();
    }
}