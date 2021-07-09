package com.example.madcamp_project2java;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<SignupResult> executeSignup (@Body HashMap<String, String> map);

    @POST("/googleLogin")
    Call<LoginResult> executegoogleLogin(@Body HashMap<String, String> map);
}