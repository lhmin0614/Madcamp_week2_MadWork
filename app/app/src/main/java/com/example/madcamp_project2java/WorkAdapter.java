package com.example.madcamp_project2java;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> workarray;
    private ArrayList<Integer> progressarray;

    private ViewHolder mViewHolder;

    public WorkAdapter(Context mContext, ArrayList<String> workarray, ArrayList<Integer> progressarray){
        this.mContext=mContext;
        this.workarray=workarray;
        this.progressarray=progressarray;
    }

    @Override
    public int getCount() {
        return workarray.size();
    }

    @Override
    public Object getItem(int position) {
        return workarray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
            mViewHolder=new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }
        else{
            mViewHolder=(ViewHolder) convertView.getTag();
        }
        mViewHolder.txt_name.setText(workarray.get(position));

        mViewHolder.progressbar.setProgress(progressarray.get(position));
        mViewHolder.progresstext.setText(progressarray.get(position).toString()+"%");
        return convertView;
    }
    public class ViewHolder {
        private TextView txt_name;
        private ProgressBar progressbar;
        private TextView progresstext;
        public ViewHolder(View convertView) {
            txt_name = (TextView) convertView.findViewById(R.id.worktext);
            progressbar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            progresstext=(TextView) convertView.findViewById(R.id.progresstext);
        }

    }
}