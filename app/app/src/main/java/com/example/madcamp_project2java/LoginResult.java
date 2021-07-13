package com.example.madcamp_project2java;

import java.util.ArrayList;

public class LoginResult {

    private String name;

    private String email;

    private String profile;

    private String approve_id;

    private String approve_pw;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getApprove_id(){
        return approve_id;
    }
    public String getApprove_pw(){
        return approve_pw;
    }

    public String getProfile() {return profile;}

    private ArrayList<String> work;
    private ArrayList<Integer> progress;

    public ArrayList<String> getWork() {
        return work;
    }
    public ArrayList<Integer> getProgress(){
        return progress;
    }
}
