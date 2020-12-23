package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class VIewImageActivity extends AppCompatActivity {


    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v_iew_image);
        imageView=findViewById(R.id.image);
        int image=getIntent().getIntExtra("image",0);
        Picasso.get().load(Adapter.list[image]).placeholder(R.drawable.heart).into(imageView);

    }

}