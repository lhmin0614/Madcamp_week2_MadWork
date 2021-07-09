package com.example.madcamp_project2java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private String BASE_URL = "http://192.249.18.165:80";
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
        doneBtn = (Button) findViewById(R.id.done);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("button", "done btn clicked");
                HashMap<String, String> map = new HashMap<>();

                map.put("id", emailEdit.getText().toString());
                map.put("pw", passwordEdit.getText().toString());
                map.put("name", nameEdit.getText().toString());

                Call<SignupResult> call = retrofitInterface.executeSignup(map);

                call.enqueue(new Callback<SignupResult>() {
                    @Override
                    public void onResponse(Call<SignupResult> call, Response<SignupResult> response) {
                        if (response.code() == 200) {
                            //login succes
                            Log.i("login", "login success");
                            Log.i("response", response.toString());
                        } else if (response.code() == 404) {
                            //login fail
                            Log.i("login", "login fail");
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