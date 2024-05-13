package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdaptor extends RecyclerView.Adapter<MyAdaptor.MyViewHolder> {
    Context context;
    ArrayList<issue_dataholder> list;

    public MyAdaptor(Context context, ArrayList<issue_dataholder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.userentry,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        issue_dataholder user = list.get(position);
        holder.roomnum.setText(user.getIss_roomnum());
        holder.email.setText(user.getIss_email());
        holder.issue.setText(user.getIss_issues());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView roomnum, email, issue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            roomnum = itemView.findViewById(R.id.text_roomnum);
            email = itemView.findViewById(R.id.text_email);
            issue = itemView.findViewById(R.id.text_issue);
        }
    }
}
