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
        contextEditText=view.findViewById(R.id.contextEditText);

        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        //Intent intent=getIntent();
        //String name=intent.getStringExtra("userName");
        String name="kimjh5182";
        //final String userID=intent.getStringExtra("userID");
        final String userID="kimjh5182";

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
                checkDay(year,month+1,day,userID);
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str=contextEditText.getText().toString();
                saveDiary(year,month+1,day,str);
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
    public void  checkDay(Integer cYear,Integer cMonth,Integer cDay,String userID){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("group","1"); //===================================================================1 나중에 group number로 수정
            map.put("date",cYear.toString()+cMonth.toString()+cDay.toString()+'_'+"1"); //===============================================1을 나중에 group number로 수정
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
                    removeDiary(cYear,cMonth,cDay);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
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
    public void removeDiary(Integer cyear,Integer cmonth,Integer cday){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("group","1"); //==================================================================1 나중에 group number로 수정
            map.put("date",cyear.toString()+cmonth.toString()+cday.toString()+'_'+"1"); //요것도
            Call<Void> call = retrofitInterface.executeRemovecalendar(map);
            call.enqueue(new Callback<Void>() {
                private Context mContext;
                private WorkAdapter mWorkAdapter;
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Remove success!",
                                Toast.LENGTH_LONG).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), "Wrong Credentials",
                                Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(Integer cyear,Integer cmonth,Integer cday,String text){
        try{
            HashMap<String, String> map = new HashMap<>();
            map.put("group","1"); //==================================================================1 나중에 group number로 수정
            map.put("date",cyear.toString()+cmonth.toString()+cday.toString()+'_'+"1");
            map.put("text",text);
            Call<Void> call = retrofitInterface.executeSavecalendar(map);
            call.enqueue(new Callback<Void>() {
                private Context mContext;
                private WorkAdapter mWorkAdapter;
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getContext(), "Save success!",
                                Toast.LENGTH_LONG).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(getContext(), "Wrong Credentials",
                                Toast.LENGTH_LONG).show();
                    }

                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}