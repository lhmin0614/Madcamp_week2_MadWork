package com.example.madcamp_project2java;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import com.example.madcamp_project2java.databinding.FragChatBinding;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatFrag extends Fragment {
    private View view;
    EditText editText;
    private Socket mSocket;
    FragChatBinding binding;
    private String username;
    private String roomNumber;
    private String userID;
    private ChatAdapter adapter;
    private Gson gson = new Gson();
    SimpleDateFormat format;
    View sendButton;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.frag_chat,container,false);
        editText = view.findViewById(R.id.editText);
        recyclerView = view.findViewById(R.id.recyclerView);
        binding = FragChatBinding.inflate(getLayoutInflater());

        format = new SimpleDateFormat("HH:mm");

        init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onPause() {
        mSocket.disconnect();
        super.onPause();
    }

    private void init() {
        // MainActivity로부터 username과 roomNumber를 넘겨받음
        Bundle bundle = getArguments();
        username = bundle.getString("username");
        roomNumber = bundle.getString("groupName");
        userID = bundle.getString("UserID");



        adapter = new ChatAdapter(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 메시지 전송 버튼
        sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        try {
            mSocket = IO.socket("http://192.249.18.165:80");
            Log.d("SOCKET", "Connection success : " + mSocket.id());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();

        //initial db update
        mSocket.on("dbUpdate", (args)->{
            Log.i("dbmessage", "dbmessage 받음");
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            addChat(data);
        });

        mSocket.on(Socket.EVENT_CONNECT, (args) -> {
            Log.i("userid", userID);
            mSocket.emit("enter", gson.toJson(new RoomData(username, roomNumber, userID)));
        });

        //update chatting
        mSocket.on("update", (args) -> {
            MessageData data = gson.fromJson(args[0].toString(), MessageData.class);
            Log.i("MSG from group", data.getContent());
            Log.i("MSG type from group", data.getType());
            addChat(data);
        });
    }

    // 리사이클러뷰에 채팅 추가
    private void addChat(MessageData data) {
        getActivity().runOnUiThread(() -> {
            Log.i("addChat", data.getContent());
            if (data.getType().equals("ENTER")) {
                Log.i("Enter msg", "msg at center");
                //adapter.addItem(new ChatItem(data.getFrom(), data.getContent(),data.getSendTime(), 1)); //center msg
                //recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else if (data.getType().equals("LEFT")){
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), data.getSendTime(), 2)); //center msg
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
            else if (data.getType().equals("IMAGE")) {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), data.getSendTime(), 3)); //left image
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            } else {
                adapter.addItem(new ChatItem(data.getFrom(), data.getContent(), data.getSendTime(), 0)); //left msg
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    public void sendMessage() {
        mSocket.emit("newMessage", gson.toJson(new MessageData("MESSAGE",
                username,
                userID,
                roomNumber,
                editText.getText().toString(),
                format.format(System.currentTimeMillis()))));
        Log.d("MESSAGE", new MessageData("MESSAGE",
                username,
                userID,
                roomNumber,
                editText.getText().toString(),
                format.format(System.currentTimeMillis())).toString());
        Log.d("MESSAGE text", editText.getText().toString());
        adapter.addItem(new ChatItem(username, editText.getText().toString(), format.format(System.currentTimeMillis()), 2)); //right text
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        editText.setText("");
    }


}