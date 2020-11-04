package com.example.poject_01.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.poject_01.model.InspectionListAdapter;
import com.example.poject_01.model.RestaurantList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.poject_01.R;

/**
 * Displays information of the restaurant clicked by the user
 */
public class DetailSingleRestaurant extends AppCompatActivity {

    private int index = 0;
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_single_restaurant);
        extractDataFromIntent();
        displayNameAndLocation();


        //use of recycler view to show list of inspections
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        InspectionListAdapter myAdapter = new InspectionListAdapter(this, restaurantList.getRestaurantIndex(index).getInspections(), index);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        //back button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    //extract index from main activity
    private void extractDataFromIntent() {
        Intent intent = getIntent();
        index = intent.getIntExtra("index=",0);

    }

    @SuppressLint("SetTextI18n")
    private void displayNameAndLocation() {
        //displays the contents of restaurant whose inspection list is shown
        TextView setName = findViewById(R.id.restaurant_name_dsp);
        setName.setText(restaurantList.getRestaurantIndex(index).getName());

        TextView setAddress = findViewById(R.id.address_dsp);
        setAddress.setText(restaurantList.getRestaurantIndex(index).getAddress());

        TextView setGps = findViewById(R.id.gps_cords_dsp);
        String Latitude = String.valueOf(restaurantList.getRestaurantIndex(index).getLatitude());
        String Longitude = String.valueOf(restaurantList.getRestaurantIndex(index).getLongitude());
        setGps.setText(Latitude+" (Latitude)\n"+ Longitude +" (Longitude)");

        TextView noInspection = findViewById((R.id.no_inspection));
        if(restaurantList.getRestaurantIndex(index).getInspections().getNum_inspection()==0)
        {
            noInspection.setText(R.string.no_recent_inspections_main);
        }
        else
        {
            noInspection.setText("");
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  static Intent makeIntent(Context context, int ind)
    {
        Intent intent =  new Intent(context, DetailSingleRestaurant.class);
        intent.putExtra("index=",ind);
        return intent;
    }
}

