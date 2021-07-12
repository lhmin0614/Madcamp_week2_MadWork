package com.example.madcamp_project2java;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.HashMap;

import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity_retrofit extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.124:443";
    //private String BASE_URL = "http://172.10.18.165:80";
    Context mContext;
    Button loginBtn;
    View googleLoginBtn;
    Button signupBtn;
    Integer RC_SIGN_IN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_retrofit);
        mContext = this;

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //login
        handleLoginDialog();

        //google login
        RC_SIGN_IN = 369;
        googleLoginBtn = findViewById(R.id.google_button);
        // setting login option
        // DEFAULT_SIGN_IN : user id & profile data
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // request email address
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signinIntent, RC_SIGN_IN);
            }
        });
    }
        private void handleLoginDialog() {

            View view = getLayoutInflater().inflate(R.layout.activity_login_retrofit, null);

            EditText emailEdit = findViewById(R.id.emailEdit);
            EditText passwordEdit = findViewById(R.id.passwordEdit);
            EditText nameEdit = findViewById(R.id.nameEdit);

            //계정등록
            signupBtn  = (Button) findViewById(R.id.signup_button);
            signupBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( mContext , SignupActivity.class);
                    intent.putExtra("email" , emailEdit.getText().toString());
                    intent.putExtra("password" , passwordEdit.getText().toString());
                    startActivity(intent);
                }
            });

            loginBtn = (Button) findViewById(R.id.login_button);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("button", "loginbutton Clicked");
                    HashMap<String, String> map = new HashMap<>();

                    map.put("id", emailEdit.getText().toString());
                    map.put("pw", passwordEdit.getText().toString());

                    Log.i("id", emailEdit.getText().toString());

                    Call<LoginResult> call = retrofitInterface.executeLogin(map);

                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                            if (response.code() == 200) {
                                //login success
                                String apr_id =  response.body().getApprove_id();
                                String apr_pw =  response.body().getApprove_pw();
                                String name = response.body().getName();
                                Log.i("response detail", apr_id);
                                Log.i("response detail", apr_pw);
                                if(apr_id.equals("NO")) {
                                    Toast.makeText(getApplicationContext(), "아이디를 확인해주십시오", Toast.LENGTH_LONG).show();
                                }else if(apr_pw.equals("NO")){
                                    Toast.makeText(getApplicationContext(), "잘못된 비밀번호입니다", Toast.LENGTH_LONG).show();
                                }else{
                                    //login success
                                    Log.i("login success", "id password OK");
                                    Intent intent = new Intent(getBaseContext(), JoinGroup.class);
                                    intent.putExtra("name", response.body().getName());
                                    intent.putExtra("UserID", emailEdit.getText().toString());
                                    startActivity(intent);
                                }
                            } else if (response.code() == 404) {
                                //login fail
                            }
                        }
                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            Log.i("connect failed", "t.getMessage");
                        }
                    });

                }
            });
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
            try {
                GoogleSignInAccount acct = completedTask.getResult(ApiException.class);

                if (acct != null) {
                    String personName = acct.getDisplayName();
                    String personGivenName = acct.getGivenName();
                    String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();

                    Log.d("login", "handleSignInResult:personName "+personName);
                    Log.d("login", "handleSignInResult:personGivenName "+personGivenName);
                    Log.d("login", "handleSignInResult:personEmail "+personEmail);
                    Log.d("login", "handleSignInResult:personId "+personId);
                    Log.d("login", "handleSignInResult:personFamilyName "+personFamilyName);
                    Log.d("login", "handleSignInResult:personPhoto "+personPhoto);

                    googleLoginRequest(personName, personEmail, personId, personPhoto);

                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("login_err", "signInResult:failed code=" + e.getStatusCode());

            }
        }

        public void googleLoginRequest(String personName, String personEmail, String personId, Uri personPhoto){

            HashMap<String, String> map = new HashMap<>();

            map.put("id", personEmail);
            map.put("name", personName);

            Call<LoginResult> call = retrofitInterface.executegoogleLogin(map);

            call.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                    if (response.code() == 200) {
                        //login success
                        String apr_id =  response.body().getApprove_id();
                        if(apr_id.equals("NO")) {
                            Toast.makeText(getApplicationContext(), "새로운 계정을 생성하였습니다", Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(getBaseContext(), JoinGroup.class);
                        intent.putExtra("name", personName);
                        intent.putExtra("UserID", personEmail);
                        startActivity(intent);

                    } else if (response.code() == 404) {
                        //login fail
                    }
                }
                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {
                    Log.i("connect failed", "t.getMessage");
                }
            });

        }
}

