package com.example.madcamp_project2java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinGroup extends AppCompatActivity {

    Context mContext;
    String username;
    String UserID;
    String groupID;
    String groupName;
    String profile;
    Button createGroup;
    ListView listview;
    EditText editGroupName;
    ArrayList<String> mDatas;

    //retrofit elements
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.124:443";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        mContext = this;

        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        UserID = intent.getStringExtra("UserID");
        profile = intent.getStringExtra("profile");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        //groupList
        mDatas= new ArrayList<String>();

        handleCreateGroup();
        handleGroupList();

    }

    //Group 만들기
    private void handleCreateGroup() {
        createGroup  = (Button) findViewById(R.id.create_button);
        editGroupName = (EditText) findViewById(R.id.editTextGroupName);
        createGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("groupName", editGroupName.getText().toString());
                Call<LoginResult> call = retrofitInterface.executeJoinGroup(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        String apr_id =  response.body().getApprove_id();
                        if(apr_id.equals("NO")){
                            Toast.makeText(getApplicationContext(), "이미 존재하는 그룹입니다", Toast.LENGTH_LONG).show();
                        }
                        else{
                            mDatas.add(editGroupName.getText().toString());
                            ArrayAdapter adapter= new ArrayAdapter( mContext, android.R.layout.simple_list_item_1, mDatas);
                            listview= (ListView)findViewById(R.id.listview);
                            listview.setAdapter(adapter);
                            listview.setOnItemClickListener(listener);
                        }
                        editGroupName.setText("");
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.i("connect failed", "t.getMessage");
                    }
                });
            }
        });
    }

    //DB에서 GroupList 가져와서 보여주기
    private void handleGroupList(){
        HashMap<String, String> map_for_grouplist = new HashMap<>();
        map_for_grouplist.put("groupName", editGroupName.getText().toString());
        Call<JoinGroupResult> call = retrofitInterface.executeGroupList(map_for_grouplist);

        call.enqueue(new Callback<JoinGroupResult>() {
            @Override
            public void onResponse(Call<JoinGroupResult> call, Response<JoinGroupResult> response) {
                ArrayList<String> groupList = response.body().getGroupIds();
                for(int i=0; i < groupList.size(); i++){
                    mDatas.add(groupList.get(i));
                }
                ArrayAdapter adapter= new ArrayAdapter( mContext, android.R.layout.simple_list_item_1, mDatas);
                listview= (ListView)findViewById(R.id.listview);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(listener);
            }
            @Override
            public void onFailure(Call<JoinGroupResult> call, Throwable t) {
                Log.i("connect failed", "t.getMessage");
            }
        });

        ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDatas);
        listview= (ListView)findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(listener);
    }

    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //mDatas.get(position)으로 intent 전달
            Intent group_to_main = new Intent( getBaseContext() , MainActivity.class);
            group_to_main.putExtra("name", username);
            group_to_main.putExtra("UserID", UserID);
            group_to_main.putExtra("groupID", "1");
            group_to_main.putExtra("profile", profile);
            group_to_main.putExtra("groupName", mDatas.get(position));
            startActivity(group_to_main);
        }
    };
}