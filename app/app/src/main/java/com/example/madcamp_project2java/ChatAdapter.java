package com.example.madcamp_project2java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatItem> items = new ArrayList<ChatItem>();
    private Context context;

    public ChatAdapter(Context context) { this.context = context; }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == 1) { //center
            Log.i("center View", "center view");
            view = inflater.inflate(R.layout.my_message, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == 0) {
            view = inflater.inflate(R.layout.their_message, parent, false);
            return new LeftViewHolder(view);
        } else{ //viewtyp==2
            view = inflater.inflate(R.layout.my_message, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof CenterViewHolder) {
            //ChatItem item = items.get(position);
            //((CenterViewHolder) viewHolder).setItem(item);
        } else if (viewHolder instanceof LeftViewHolder) {
            ChatItem item = items.get(position);
            ((LeftViewHolder) viewHolder).setItem(item);
        } else if (viewHolder instanceof RightViewHolder) {
            ChatItem item = items.get(position);
            ((RightViewHolder) viewHolder).setItem(item);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public void addItem(ChatItem item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void setItems(ArrayList<ChatItem> items){ this.items = items; }

    public ChatItem getItem(int position){
        return items.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;

        public CenterViewHolder(View itemView) {
            super(itemView);

            contentText = itemView.findViewById(R.id.content_text);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView contentText;
        TextView sendTimeText;
        View avatar;

        public LeftViewHolder(View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.name);
            contentText = itemView.findViewById(R.id.message_body);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
            avatar = itemView.findViewById(R.id.avatar);
        }

        public void setItem(ChatItem item){
            nameText.setText(item.getName());
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());
            int resID = context.getResources().getIdentifier(item.getProfile(), "drawable", context.getPackageName());
            avatar.setBackgroundResource(resID);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView contentText;
        TextView sendTimeText;

        public RightViewHolder(View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.message_body);
            sendTimeText = itemView.findViewById(R.id.send_time_text);
        }

        public void setItem(ChatItem item){
            contentText.setText(item.getContent());
            sendTimeText.setText(item.getSendTime());

        }
    }
}