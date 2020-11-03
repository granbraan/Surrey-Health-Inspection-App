package com.example.poject_01.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.poject_01.model.MyAdapter;
import com.example.poject_01.model.RestaurantList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.example.poject_01.R;

public class DetailSingleRestaurant extends AppCompatActivity {

    private int index =0;
    private final RestaurantList restaurant_list = RestaurantList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_single_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extractDataFromIntent();
        displayNameAndLocation();

        //use of recycler view to show list of inspections
        RecyclerView recycler_view = findViewById(R.id.recycler_view);
        MyAdapter myAdapter = new MyAdapter(this,restaurant_list.getRestaurantIndex(index).getInspections(), index);
        recycler_view.setAdapter(myAdapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

    }

    //extract index from main activity
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        index = intent.getIntExtra("index=",0);

    }

    @SuppressLint("SetTextI18n")
    private void displayNameAndLocation() {
        //displays the contents of restaurant whose inspection list is shown
        TextView set_name = findViewById(R.id.restaurant_name_dsp);
        set_name.setText(restaurant_list.getRestaurantIndex(index).getName());

        TextView set_address = findViewById(R.id.address_dsp);
        set_address.setText(restaurant_list.getRestaurantIndex(index).getAddress());

        TextView set_gps = findViewById(R.id.gps_cords_dsp);
        String Latitude = String.valueOf(restaurant_list.getRestaurantIndex(index).getLatitude());
        String Longitude = String.valueOf(restaurant_list.getRestaurantIndex(index).getLongitude());
        set_gps.setText(Latitude+" (Latitude)\n"+ Longitude +" (Longitude)");

    }

    public  static Intent makeIntent(Context context, int ind)
    {
        Intent intent =  new Intent(context, DetailSingleRestaurant.class);
        intent.putExtra("index=",ind);
        return intent;
    }
}

