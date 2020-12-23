package com.navigine.naviginedemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    public static String[]  list;
    Context context;

    public Adapter(String[] strings, Context context) {
        this.list = strings;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_recylerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

//        InputStream input = null;
//        Bitmap bitmap1=null;
//        try {
//            input = new URL(list[position]).openStream();
//            bitmap1= BitmapFactory.decodeStream(input);
//            holder.imageView.setImageBitmap(bitmap1);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Picasso.get().load(list[position]).placeholder(R.drawable.heart).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1 = new Intent(context, VIewImageActivity.class);
                in1.putExtra("image",position);
                in1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in1);


//                Toast.makeText(context, "hhhh", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(context,MainActivity2.class);
//                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageview);
        }
    }
}
