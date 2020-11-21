package com.example.poject_01.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poject_01.R;
import com.example.poject_01.model.InspectionListAdapter;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.util.Map;

/**
 * Displays information of the restaurant clicked by the user
 */
public class RestaurantDetailsActivity extends AppCompatActivity {

    private int index;
    private Restaurant restaurant;
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    private  boolean flag;
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
        restaurant = restaurantList.getRestaurantIndex(index);
        flag = intent.getBooleanExtra("flag",false);

    }

    @SuppressLint("SetTextI18n")
    private void displayNameAndLocation() {
        //displays the contents of restaurant whose inspection list is shown
        TextView setName = findViewById(R.id.restaurant_name_dsp);
        setName.setText(restaurant.getName());

        TextView setAddress = findViewById(R.id.address_dsp);
        setAddress.setText(restaurant.getAddress() + ", " + restaurant.getCity());

        TextView setGps = findViewById(R.id.gps_cords_dsp);
        String Latitude = String.valueOf(restaurant.getLatitude());
        String Longitude = String.valueOf(restaurant.getLongitude());
        setGps.setText(Latitude+" (Latitude)\n"+ Longitude +" (Longitude)");
        setGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag) {
                    Intent intent = MapsActivity.makeLaunchIntent(RestaurantDetailsActivity.this,
                            restaurant.getLatitude(),
                            restaurant.getLongitude(), true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finish();
            }
        });

        TextView noInspection = findViewById((R.id.no_inspection));
        if(restaurant.getInspections().getNum_inspection() == 0)
        {
            noInspection.setText(R.string.no_recent_inspections_main);
        }
        else
        {
            noInspection.setText("");
        }

        ImageView imageView = findViewById(R.id.imageView);

        if(restaurant.getName().contains("Church's")) {
            imageView.setImageResource(R.drawable.churchs_chicken_logo);
        }
        else if(restaurant.getName().contains("A & W") || restaurant.getName().contains("A&W")) {
            imageView.setImageResource(R.drawable.a_and_w_logo);
        }
        else if(restaurant.getName().contains("Booster")) {
            imageView.setImageResource(R.drawable.booster_juice_logo);
        }
        else if(restaurant.getName().contains("Burger King")) {
            imageView.setImageResource(R.drawable.burger_king_logo);
        }
        else if(restaurant.getName().contains("Dairy")) {
            imageView.setImageResource(R.drawable.dairy_queen_logo);
        }
        else if(restaurant.getName().contains("Five Guys")) {
            imageView.setImageResource(R.drawable.five_guys_burger_and_fries_logo);
        }
        else if(restaurant.getName().contains("Apna")) {
            imageView.setImageResource(R.drawable.apna_chaat_house_logo);
        }
        else if(restaurant.getName().contains("Kelly's")) {
            imageView.setImageResource(R.drawable.kellys_pub_logo);
        }
        else if(restaurant.getName().contains("New York")) {
            imageView.setImageResource(R.drawable.new_york_fries_logo);
        }
        else if(restaurant.getName().contains("7-Eleven")) {
            imageView.setImageResource(R.drawable.seven_eleven_logo);
        }
        else {
            // restaurant icon
            imageView.setImageResource(R.drawable.restaurant_image);
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

    public  static Intent makeIntent(Context context, int ind,boolean flag)
    {
        Intent intent =  new Intent(context, RestaurantDetailsActivity.class);
        intent.putExtra("index=",ind);
        intent.putExtra("flag",flag);
        return intent;
    }
}

