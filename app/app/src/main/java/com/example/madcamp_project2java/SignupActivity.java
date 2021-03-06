package com.example.madcamp_project2java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    Button doneBtn;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    EditText emailEdit;
    EditText passwordEdit;
    EditText nameEdit;
    String profile;
    private String BASE_URL = "http://192.249.18.124:443";
    //private String BASE_URL = "http://172.10.18.165:80";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        nameEdit = findViewById(R.id.nameEdit);
        emailEdit.setText(intent.getStringExtra("email"));
        passwordEdit.setText(intent.getStringExtra("password"));

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        handleSignup();
    }

    private void handleSignup(){
        View view = getLayoutInflater().inflate(R.layout.activity_signup, null);

        profile = "user";
        final ArrayList<String>[] profileList = new ArrayList[]{new ArrayList<String>(Arrays.asList("duck", "nupjuk", "user", "cat", "boy", "girl"))};

        CheckBox checkBox1 = (CheckBox) findViewById(R.id.check_box1) ;
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.check_box2) ;
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.check_box3) ;
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.check_box4) ;
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.check_box5) ;
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.check_box6) ;
        final CheckBox[] currentBox = {(CheckBox) findViewById(R.id.check_box1)};
        ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>(Arrays.asList(checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6));
        for(final Integer[] i = {0}; i[0] <checkBoxList.size(); i[0]++){
            Integer finalI = i[0];
            checkBoxList.get(i[0]).setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentBox[0].setChecked(false); //?????? ????????? ?????? ?????? ??????
                    checkBoxList.get(finalI).setChecked(true) ;
                    currentBox[0] = checkBoxList.get(finalI);
                    profile = profileList[0].get(finalI);
                }
            });
        }


        doneBtn = (Button) findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("button", "done btn clicked");
                HashMap<String, String> map = new HashMap<>();

                map.put("id", emailEdit.getText().toString());
                map.put("pw", passwordEdit.getText().toString());
                map.put("name", nameEdit.getText().toString());
                map.put("profile", profile);

                Call<SignupResult> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<SignupResult>() {
                    @Override
                    public void onResponse(Call<SignupResult> call, Response<SignupResult> response) {
                        if (response.code() == 200) {
                            //login succes
                            Log.i("login", "login success");
                            Log.i("response", response.toString());
                            Intent intent = new Intent(getBaseContext(), JoinGroup.class);
                            intent.putExtra("name", nameEdit.getText().toString());
                            intent.putExtra("UserID", emailEdit.getText().toString());
                            Log.i("SignupActivityProfile", profile);
                            intent.putExtra("profile", profile);
                            startActivity(intent);
                        } else if (response.code() == 404) {
                            //login fail
                            Log.i("login", "ID already exists");
                            Toast.makeText(getApplicationContext(), "?????? ????????? ???????????? ??????????????????", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<SignupResult> call, Throwable t) {
                        Log.i("connect failed", "t.getMessage");
                    }
                });
            }
        });
    }
}