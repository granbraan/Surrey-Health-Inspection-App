package com.example.poject_01.UI;

import android.os.Bundle;

import com.example.poject_01.model.MyAdapter;
import com.example.poject_01.model.RestaurantList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.poject_01.R;

public class detail_single_restaurant extends AppCompatActivity {

    private int index =0;
    private RecyclerView recycler_view;
    private RestaurantList restaurant_list = RestaurantList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_single_restaurant);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Display_name_and_location();
        recycler_view=findViewById(R.id.recycler_view);
        MyAdapter myAdapter = new MyAdapter(this,restaurant_list.getRestaurantIndex(index).getInspections());
        recycler_view.setAdapter(myAdapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

    }

    private void Display_name_and_location() {
        TextView set_name = findViewById(R.id.restaurant_name_dsp);
        set_name.setText(restaurant_list.getRestaurantIndex(index).getName());

        TextView set_address = findViewById(R.id.address_dsp);
        String Latitude = String.valueOf(restaurant_list.getRestaurantIndex(index).getLatitude());
        String Longitude = String.valueOf(restaurant_list.getRestaurantIndex(index).getLongitude());
        set_address.setText(Latitude+" (Latitude)\n"+Longitude+" (Longitude)");

    }
}

