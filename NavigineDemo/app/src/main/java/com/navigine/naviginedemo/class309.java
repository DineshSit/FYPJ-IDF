package com.navigine.naviginedemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class class309 extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    ProgressBar progressBar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threeonine);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);




        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String[]  strings = ImageManager.ListImages();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter=new Adapter(strings,getApplicationContext());
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("myWorkdkdkdk", "onCreate: "+e.getMessage());

                }
            }
        });
    }
}
