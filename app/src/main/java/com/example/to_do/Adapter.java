package com.example.to_do;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    ArrayList<Data> DataList;
    Context context;


    public Adapter(Context context, ArrayList<Data> DataList) {
        this.DataList = DataList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = DataList.get(position);
        holder.title.setText(data.getTitle());
        holder.desc.setText(data.getDesc());
        holder.date.setText(data.getSelectdate());
        holder.time.setText(data.getSelecttime());
        holder.btn_share.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, data.getTitle() + "\n" + data.getDesc() + "\n" + data.getSelectdate() + "\n" + data.getSelecttime());
                share.setType("text/plain");
                context.startActivity(Intent.createChooser(share, "Send To"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, desc, date, time;
        ImageView btn_share;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tasktitle);
            desc = itemView.findViewById(R.id.taskdesc);
            date = itemView.findViewById(R.id.taskdate);
            time = itemView.findViewById(R.id.tasktime);
            btn_share = itemView.findViewById(R.id.btn_share);
        }


    }

}
