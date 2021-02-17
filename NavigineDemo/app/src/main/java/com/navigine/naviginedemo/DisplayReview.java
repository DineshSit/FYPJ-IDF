package com.navigine.naviginedemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayReview extends AppCompatActivity {

    //RecyclerView recyclerView;
    //private Adapter adapter;
    //private List list;
    FirebaseDatabase database;
    //HelperAdapter helperAdapter;
    DatabaseReference ref;
    //List<ReviewClass> reviewClass;
    //List<FetchData> fetchData;
    ListView listView;
    List<FirebaseListAdapter> list1 = new ArrayList<FirebaseListAdapter>();
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    ReviewClass reviewClass;
    static final String TAG = "NAVIGINE.Demo";
    private Uri image3;

    FirebaseListAdapter adapter1;




    //for recycler view
    //RecyclerView recyclerView;
    //ProductAdapter adapter;
    //List<ReviewClass> reviewClassList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        /*reviewClassList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerFdbs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, reviewClassList);
        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference("Reviews");
        ref.addListenerForSingleValueEvent(valueEventListener);

        //Query query = FirebaseDatabase.getInstance().getReference("Reviews")
        //        .orderByChild()
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            reviewClassList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReviewClass reviewClass = snapshot.getValue(ReviewClass.class);
                    reviewClassList.add(reviewClass);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}*/


        reviewClass = new ReviewClass();
        listView = (ListView) findViewById(R.id.listViewFb);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Reviews");
        list = new ArrayList<>();


//        final InputStream imageStream = getContentResolver().openInputStream(this.image3);
//        final int imageLength = imageStream.available();
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    reviewClass = ds.getValue(ReviewClass.class);
//                    //list.add(reviewClass.getRating() + "Stars" + "  " +reviewClass.getFeedback().toString());
//                    list.add("Place:" + reviewClass.getLocotion() +  "\n" +
//                            "Stars:" + reviewClass.getRating() +  "\n"
//                            + reviewClass.getFeedback() );
//                }
//                listView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        Query query = ref;
        FirebaseListOptions<ReviewClass> options = new FirebaseListOptions.Builder<ReviewClass>()
                .setLayout(R.layout.item_layout)

                .setQuery(query,ReviewClass.class)
                 .build();


        adapter1 = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView  place = v.findViewById(R.id.place);
                TextView  star = v.findViewById(R.id.star);
                TextView   para = v.findViewById(R.id.para);
                ImageView  image = v.findViewById(R.id.pic3);

                ReviewClass review = (ReviewClass) model;
                place.setText("Venue: " + review.getLocotion());
                star.setText("Rating: " +Float.toString(review.getRating()));
                para.setText("Comment: " +review.getFeedback().toString());

                Picasso.get().load(review.getPic()).into(image);


            }
        };
        //list1.add(adapter1);
        //customAdapter = new CustomAdapter((List<ReviewClass>) adapter1,this);

        listView.setAdapter(adapter1);

        //RECYCLER VIEW
        /*recyclerView = findViewById(R.id.recyclerFdbs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewClass = new ArrayList<>();

        databaseReference = database.getInstance().getReference("Reviews");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    ReviewClass data = ds.getValue(ReviewClass.class);
                    reviewClass.add(data);
                }
                helperAdapter = new HelperAdapter(reviewClass);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        adapter1.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        adapter1.stopListening();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listsearch, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchBar(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchBar(s);



//                ArrayList<String> userslist = new ArrayList<>();
//                        for (String user:list){
//                            if(user.toLowerCase().contains(newText.toLowerCase())){
//                                userslist.add(user);
//                            }
//                        }
//                 ArrayAdapter<String> adapter = new ArrayAdapter<String>(DisplayReview.this, android.R.layout.simple_list_item_1, userslist);
//                        listView.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu) ;
    }

    private void SearchBar(String s)
    {
        Query query = database.getReference("Reviews").orderByChild("locotion").startAt(s).endAt(s+"\uf8ff");
        FirebaseListOptions<ReviewClass> options = new FirebaseListOptions.Builder<ReviewClass>()
                .setLayout(R.layout.item_layout)

                .setQuery(query,ReviewClass.class)
                .build();


        adapter1 = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {
                TextView  place = v.findViewById(R.id.place);
                TextView  star = v.findViewById(R.id.star);
                TextView   para = v.findViewById(R.id.para);
                ImageView  image = v.findViewById(R.id.pic3);

                ReviewClass review = (ReviewClass) model;
                place.setText("Venue: " + review.getLocotion());
                star.setText("Rating: " +Float.toString(review.getRating()));
                para.setText("Comment: " +review.getFeedback().toString());

                Picasso.get().load(review.getPic()).into(image);


            }
        };

        adapter1.startListening();
        listView.setAdapter(adapter1);


    }




    }