package com.example.madcamp_project2java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_NO_LOCALIZED_COLLATORS;

public class CalendarFrag extends Fragment {
    private View view;
    private String BASE_URL = "http://192.249.18.124:443";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public String str=null;
    public CalendarView calendarView;
    public Button cha_Btn,del_Btn,save_Btn;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;
    Integer year;
    Integer month;
    Integer day;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        view = inflater.inflate(R.layout.frag_calendar,container,false);
        calendarView=view.findViewById(R.id.calendarView);
        diaryTextView=view.findViewById(R.id.diaryTextView);
        save_Btn=view.findViewById(R.id.save_Btn);
        del_Btn=view.findViewById(R.id.del_Btn);
        cha_Btn=view.findViewById(R.id.cha_Btn);
        textView2=view.findViewById(R.id.textView2);
        textView3=view.findViewById(R.id.textView3);
        contextEditText=view.findViewById(R.id.contextEditText);

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        //Intent intent=getIntent();
        //String name=intent.getStringExtra("userName");
        String name="kimjh5182";
        //final String userID=intent.getStringExtra("userID");
        final String userID="kimjh5182";
        textView3.setText(name+"님의 달력 일기장");

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int cyear, int cmonth, int cday) {
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",cyear,cmonth+1,cday));
                year=cyear;
                month=cmonth;
                day=cday;
                contextEditText.setText("");
                checkDay(year,month,day,userID);
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDiary(year,month,day);
                str=contextEditText.getText().toString();
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);
                del_Btn.setVisibility(View.VISIBLE);
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }
    public void  checkDay(int cYear,int cMonth,int cDay,String userID){

        try{
            HashMap<String, Integer> map = new HashMap<>();
            map.put("group",1); //1 나중에 group number로 수정
            map.put("year", cYear);
            map.put("month",cMonth);
            map.put("day",cDay);
            Call<CalendarResult> call = retrofitInterface.executeCalendar(map);


            call.enqueue(new Callback<CalendarResult>() {
                private Context mContext;
                private WorkAdapter mWorkAdapter;
                @Override
                public void onResponse(Call<CalendarResult> call, Response<CalendarResult> response) {
                    if (response.code() == 200) {
                        CalendarResult result = response.body();
                        str=result.getCalendartext();
                        textView2.setText(str);
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), "Wrong Credentials",
                                Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<CalendarResult> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str);

            save_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str);

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText());
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(cYear,cMonth,cDay);
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void removeDiary(Integer year,Integer month,Integer day){
        try{
            String content="";
            /*
            fos.write((content).getBytes());
            fos.close();
*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(Integer year,Integer month,Integer day){
        try{
            /*
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
             */
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}