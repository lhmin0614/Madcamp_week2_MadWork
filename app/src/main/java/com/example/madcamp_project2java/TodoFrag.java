package com.example.madcamp_project2java;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TodoFrag extends Fragment {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.124:443";
    String username;
    String roomNumber;
    String userID;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view=inflater.inflate(R.layout.frag_todo,container,false);

        Bundle bundle = getArguments();
        username = bundle.getString("username");
        roomNumber = bundle.getString("groupName");
        userID = bundle.getString("UserID");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        handleLoginDialog();

        view.findViewById(R.id.sendwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleworksend();
            }
        });
        ListView listView=(ListView) view.findViewById(R.id.worklist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String worktext=parent.getAdapter().getItem(position).toString();
                //Toast.makeText(getApplicationContext(),worktext,Toast.LENGTH_LONG).show();
                handlelistclick(worktext);
            }
        });
        return view;
    }
    private void handleLoginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        HashMap<String, String> map = new HashMap<>();

        map.put("userID", userID);  //나중에 수정
        Call<LoginResult> call = retrofitInterface.executeLogin(map);

        call.enqueue(new Callback<LoginResult>() {
            private Context mContext;
            private WorkAdapter mWorkAdapter;
            private ListView mListView;
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.code() == 200) {
                    LoginResult result = response.body();
                    mListView=(ListView) requireView().findViewById(R.id.worklist);
                    mWorkAdapter=new WorkAdapter(mContext,result.getWork(),result.getProgress());
                    mListView.setAdapter(mWorkAdapter);

                } else if (response.code() == 404) {
                    Toast.makeText(getContext(), "Wrong Credentials",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
    private void handleworksend() {
        //View view = getLayoutInflater().inflate(R.layout.worksend_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //builder.setView(view).show();

        //Button sendBtn = view.findViewById(R.id.sendworkbutton);
        final EditText sendworktext = getView().findViewById(R.id.sendworktext);

        getView().findViewById(R.id.sendwork).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                HashMap<String, String> map = new HashMap<>();
                //Toast.makeText(getApplicationContext(),"dsfasdf",Toast.LENGTH_LONG).show();
                //보내는 부분
                map.put("work", sendworktext.getText().toString());
                map.put("userID", userID);
                Call<LoginResult> call=retrofitInterface.executeSendText(map);
                call.enqueue(new Callback<LoginResult>() {
                    private Context mContext;
                    private WorkAdapter mWorkAdapter;
                    private ListView mListView;
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200) {
                            LoginResult result = response.body();
                            mListView=(ListView) requireView().findViewById(R.id.worklist);
                            mWorkAdapter=new WorkAdapter(mContext,result.getWork(),result.getProgress());
                            mListView.setAdapter(mWorkAdapter);
                        }
                        else if (response.code() == 400) {
                            Toast.makeText(getContext(),
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    private void handlelistclick(final String worktext){
        View view = getLayoutInflater().inflate(R.layout.progress_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view).show();

        Button progressBtn = view.findViewById(R.id.getprogress);
        final EditText progressEdit = view.findViewById(R.id.progressedit);

        progressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                HashMap<String, String> map = new HashMap<>();

                map.put("progress", progressEdit.getText().toString());
                map.put("worktext", worktext);
                map.put("userID", userID);
                Call<LoginResult> call = retrofitInterface.executeProgress(map);

                call.enqueue(new Callback<LoginResult>() {
                    private Context mContext;
                    private WorkAdapter mWorkAdapter;
                    private ListView mListView;
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.code() == 200) {
                            LoginResult result = response.body();
                            mListView=(ListView) requireView().findViewById(R.id.worklist);
                            mWorkAdapter=new WorkAdapter(mContext,result.getWork(),result.getProgress());
                            mListView.setAdapter(mWorkAdapter);
                        } else if (response.code() == 404) {
                            Toast.makeText(getContext(), "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}