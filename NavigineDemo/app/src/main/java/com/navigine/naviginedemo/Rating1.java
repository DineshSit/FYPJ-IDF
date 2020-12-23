package com.navigine.naviginedemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.navigine.naviginedemo.VenueClass;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.firebase.database.DatabaseReference;
import com.stfalcon.multiimageview.MultiImageView;


public class Rating1 extends AppCompatActivity {
    private static final String APP_TAG = "";
    SharedPreferences spref;
    EditText FeedMessage;
    Button SendFeed;
    String rating1 = ""; //String rating1="",rating2="", rating3="";
    RatingBar ratingbar1;  //RatingBar ratingbar1,ratingbar2,ratingbar3;
    DatabaseReference ref;
    Feedback feedback; // get,set class
    ListView listView;
    Spinner spinner;
    FirebaseDatabase database;
    String setLocation1;
    static final String TAG = "NAVIGINE.Demo";
    long maxid = 0;
    private static final int SELECT_MULTI_IMAGE_REQUEST_CODE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    ProgressDialog progressDialog;



    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;


    MultiImageView multiImageView;
    private Uri imageUri;
    List<Uri> imageUriList;
    boolean isPermissionGranter;


    //Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating1); // link java to xml
        imageUriList = new ArrayList<>();
        multiImageView = findViewById(R.id.multiImage);
        //  multiImageView.setShape(MultiImageView.Shape.RECTANGLE);
        progressDialog=new ProgressDialog(this);

        Button pic = (Button) findViewById(R.id.button2);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });
        //user variable
        listView = (ListView) findViewById(R.id.ListViewLoc);
        FeedMessage = (EditText) findViewById(R.id.FeedMessage);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        SendFeed = (Button) findViewById(R.id.sendFeed);
        //spinner = (Spinner) findViewById(R.id.spinnerLoc);

        //display list of venues from db on listview
        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        final String phoneNumber = sp.getString("phoneNumber", "");
        final String location = sp.getString("location", "");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Venues");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ref = database.getReference("Reviews");
                ref.child(location).child("location").setValue(setLocation1);
                setLocation1 = adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "Location " + adapter.getItem(position) + " has been selected!", Toast.LENGTH_SHORT).show();
            }
        });

        //
        DatabaseReference reff1;
        reff1 = FirebaseDatabase.getInstance().getReference().child("Reviews");
        // auto increment feedback count
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    maxid = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //insert data to fb
        //feedback = new Feedback();
        //ref = FirebaseDatabase.getInstance().getReference().child("Feedback");
        SendFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rating1 = String.valueOf(ratingbar1.getRating());
                //feedback.setRreviews(FeedMessage.getText().toString().trim());
                //feedback.setRrating(rating1);
                //feedback.setRlocation(loc);
                //ref.push().setValue(feedback);
                float rate = ratingbar1.getRating();
                String fb = FeedMessage.getText().toString().trim();

                //check user input
                if (adapter.isEmpty()) {
                    Toast.makeText(Rating1.this, "Please select a location", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(fb)) {
                    FeedMessage.setError("Please enter feedback");
                    return;
                }
               else if (ratingbar1.getRating() == 0.0) {
                    Toast.makeText(Rating1.this, "Please enter rating", Toast.LENGTH_SHORT).show();
                    return;
                }else if(imageUriList.size()==0)
                {
                    Toast.makeText(Rating1.this, "Please Select any Image", Toast.LENGTH_SHORT).show();
                }else
                {
                    progressDialog.setTitle("Please wait!");
                    progressDialog.setMessage("Image is Uploading Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    for (int i=0;i<imageUriList.size();i++)
                    {
                        UploadImage(imageUriList.get(i));
                        if (i+1==imageUriList.size())
                        {

                            Toast.makeText(Rating1.this, "Review submitted!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), DisplayReview.class));
                            progressDialog.dismiss();
                        }
                    }


                    database = FirebaseDatabase.getInstance();
                    ref = database.getReference("Reviews");
                    ReviewClass reviewclass = new ReviewClass(setLocation1, rate, fb);
                    ref.child(String.valueOf(maxid + 1)).setValue(reviewclass);

                    spref = getSharedPreferences("MyUserProfile", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("location", setLocation1);
                    editor.putFloat("rating", rate);
                    editor.putString("feedback", fb);
                    editor.commit();

                }
            }
        });
    }

    private void UploadImage(Uri uri) {
        try {
            final InputStream imageStream = getContentResolver().openInputStream(uri);
            final int imageLength = imageStream.available();

            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {
                    try {

                        final String imageName = ImageManager.UploadImage(imageStream, imageLength);

                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(Rating1.this, "Image Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(Rating1.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
            th.start();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void SelectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel "};
        AlertDialog.Builder builder = new AlertDialog.Builder(Rating1.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (items[i].equals("Camera")) {
                    chceckPermission();
                    if (isPermissionGranter) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (intent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                    }

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MediaStore.Images.Media.TITLE, "New Pic");
                        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Front Camera Pic");
                        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }


                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_MULTI_IMAGE_REQUEST_CODE);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
        ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {


                imageUriList.add(imageUri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Rating1.this.getContentResolver(), imageUri);
                    multiImageView.addImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (requestCode == SELECT_MULTI_IMAGE_REQUEST_CODE) {

                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        imageUriList.add(clipData.getItemAt(i).getUri());
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(Rating1.this.getContentResolver(), clipData.getItemAt(i).getUri());
                            multiImageView.addImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    imageUriList.add(data.getData());
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(Rating1.this.getContentResolver(), data.getData());
                        multiImageView.addImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void chceckPermission() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    isPermissionGranter = true;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);

            }
        }).check();
    }

    //list = new ArrayList<>();
    //arrayAdapter = new ArrayAdapter<>(this,R.layout.)
    // display list of location from DB in spinner
    //reff1 = FirebaseDatabase.getInstance().getReference("Venues");
    //arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayList);
    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //spinner.setAdapter(arrayAdapter);
    //spinner = findViewById(R.id.spinnerLoc);
    //listView = (ListView) findViewById(R.id.ListViewLoc) ;
    private Bitmap mergeThemAll(List<Bitmap> orderImagesList) {
        Bitmap result = null;
        if (orderImagesList != null && orderImagesList.size() > 0) {
            // if two images > increase the width only
            if (orderImagesList.size() == 2)
                result = Bitmap.createBitmap(orderImagesList.get(0).getWidth() * 2, orderImagesList.get(0).getHeight(), Bitmap.Config.ARGB_8888);
                // increase the width and height
            else if (orderImagesList.size() > 2)
                result = Bitmap.createBitmap(orderImagesList.get(0).getWidth() * 2, orderImagesList.get(0).getHeight() * 2, Bitmap.Config.ARGB_8888);
            else // don't increase anything
                result = Bitmap.createBitmap(orderImagesList.get(0).getWidth(), orderImagesList.get(0).getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            for (int i = 0; i < orderImagesList.size(); i++) {
                canvas.drawBitmap(orderImagesList.get(i), orderImagesList.get(i).getWidth() * (i % 2), orderImagesList.get(i).getHeight() * (i / 2), paint);
            }
        } else {
            Log.e("MergeError", "Couldn't merge bitmaps");
        }
        return result;
    }

}