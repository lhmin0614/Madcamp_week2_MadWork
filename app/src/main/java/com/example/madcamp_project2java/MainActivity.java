package com.example.madcamp_project2java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


@SuppressLint("HandlerLeak")
public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ChatFrag chf;
    private CalendarFrag caf;
    private TodoFrag tdf;
    String username;
    String UserID;
    String roomNumber;
    String roomName;
    String userprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        UserID = intent.getStringExtra("UserID");
        roomNumber = intent.getStringExtra("groupID");
        roomName = intent.getStringExtra("groupName");
        userprofile = intent.getStringExtra("profile");


        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                
                switch (menuItem.getItemId())
                {
                    case R.id.chat:
                        setFrag(1);
                        break;
                    case R.id.calendar:
                        setFrag(2);
                        break;
                    case R.id.todo:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        chf = new ChatFrag();
        caf = new CalendarFrag();
        tdf = new TodoFrag();

        setFrag(1); //initial fragment
    }

    //change fragment
    public void setFrag(int n)
    {
        Bundle bundle = new Bundle();
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 1:
                bundle.putString("username", username);
                bundle.putString("UserID", UserID);
                bundle.putString("roomNumber", roomNumber);
                bundle.putString("groupName", roomName);
                bundle.putString("profile", userprofile);
                chf.setArguments(bundle);
                ft.replace(R.id.Main_Frame,chf);
                ft.commit();
                break;

            case 2:
                bundle.putString("username", username);
                bundle.putString("UserID", UserID);
                bundle.putString("roomNumber", roomNumber);
                bundle.putString("groupName", roomName);
                caf.setArguments(bundle);
                ft.replace(R.id.Main_Frame,caf);
                ft.commit();
                break;

            case 3:
                bundle.putString("username", username);
                bundle.putString("UserID", UserID);
                bundle.putString("roomNumber", roomNumber);
                bundle.putString("groupName", roomName);
                tdf.setArguments(bundle);
                ft.replace(R.id.Main_Frame,tdf);
                ft.commit();
                break;

        }
    }
}