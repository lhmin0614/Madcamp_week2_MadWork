package com.example.madcamp_project2java;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);
    @POST("/gettodo")
    Call<LoginResult> executeGettodo(@Body HashMap<String, String> map);
    @POST("/signup")
    Call<SignupResult> executeSignup (@Body HashMap<String, String> map);

    @POST("/googleLogin")
    Call<LoginResult> executegoogleLogin(@Body HashMap<String, String> map);

    @POST("/joingroup")
    Call<JoinGroupResult> executeJoinGroup(@Body HashMap<String, String> map);

    @POST("/work")
    Call<LoginResult> executeSendText(@Body HashMap<String, String> map);

    @POST("/progress")
    Call<LoginResult> executeProgress(@Body HashMap<String, String> map);
    @POST("/deletetodo")
    Call<LoginResult> executeDeletetodo(@Body HashMap<String,String> map);

    @POST("/getcalendar")
    Call<CalendarResult> executeCalendar(@Body HashMap<String, String> map);

    @POST("/savecalendar")
    Call<Void> executeSavecalendar(@Body HashMap<String, String> map);

    @POST("/removecalendar")
    Call<Void> executeRemovecalendar(@Body HashMap<String, String> map);

    //@POST("/grouplist")
    //Call<> executeJoinGroup(@Body HashMap<String, String> map);
}