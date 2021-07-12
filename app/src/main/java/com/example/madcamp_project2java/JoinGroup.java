package com.example.madcamp_project2java;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinGroup extends AppCompatActivity {

    String username;
    String UserID;
    Integer groupID;
    String groupName;
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

        Intent intent = getIntent();
        username = intent.getStringExtra("name");
        UserID = intent.getStringExtra("UserID");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        mDatas= new ArrayList<String>();
        mDatas.add("1");
        mDatas.add("KOREA");
        mDatas.add("CANADA");
        mDatas.add("FRANCE");
        mDatas.add("MEXICO");
        mDatas.add("POLAND");
        mDatas.add("SAUDI ARABIA");

        handleCreateGroup();
        handleGroupList();

    }

    private void handleCreateGroup() {
        createGroup  = (Button) findViewById(R.id.create_button);
        editGroupName = (EditText) findViewById(R.id.editTextGroupName);
        createGroup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("groupName", editGroupName.getText().toString());
                Call<JoinGroupResult> call = retrofitInterface.executeJoinGroup(map);

//                call.enqueue(new Callback<JoinGroupResult>() {
//                    @Override
//                    public void onResponse(Call<JoinGroupResult> call, Response<JoinGroupResult> response) {
//                        groupName = response.body().getGroupname();
//                        groupID = response.body().getGroupId();
//                        mDatas.add(new Group(groupName, groupID));
//                        ArrayAdapter adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, mDatas);
//                        listview= (ListView)findViewById(R.id.listview);
//                        listview.setAdapter(adapter);
//                        listview.setOnItemClickListener(listener);
//                    }
//                    @Override
//                    public void onFailure(Call<JoinGroupResult> call, Throwable t) {
//                        Log.i("connect failed", "t.getMessage");
//                    }
//                });
            }
        });
    }

    private void handleGroupList(){
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
            group_to_main.putExtra("groupName", mDatas.get(position));
            startActivity(group_to_main);
        }
    };
}