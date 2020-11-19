package com.example.poject_01.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.poject_01.R;
import com.example.poject_01.model.DownloadDataAsyncTask;
import com.example.poject_01.model.DownloadRequest;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.Data;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Displays a scrollable list of restaurants, sorted alphabetically by name.
 * The list of restaurants and the data relating to each restaurant is read from "data_restaurants.csv" and "data_inspection.csv"
 */
public class MainActivity extends AppCompatActivity {
    // all restaurants added to this list, sorted by name - alphabetical order.
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    private Intent intent;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateListView();
        registerClick();

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.inflateMenu(R.menu.toggle_button_list);
        toolbar.setTitle("List of Restaurants");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.switch_map) {
                    finish();
                    return  true;
                }
                else
                    return false;
            }
        });
    }



    private void populateListView() {
        ArrayAdapter<Restaurant> restaurantAdapter = new RestaurantListAdapter();
        ListView list = findViewById(R.id.restaurantListView);
        list.setAdapter(restaurantAdapter);
    }


    private void registerClick() {
        ListView listView = findViewById(R.id.restaurantListView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("MainActivity", "User clicked restaurant at position: " + position);
            intent = RestaurantDetailsActivity.makeIntent(MainActivity.this,position);
            startActivity(intent);
        });

    }


    private class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantListAdapter() {
            super(MainActivity.this, R.layout.restaurant_list_view, restaurantList.getList());
        }


        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            View restaurantView = convertView;
            if (restaurantView == null){
                restaurantView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
            }
            Restaurant currentRestaurant = restaurantList.getRestaurantIndex(position);

            if(currentRestaurant.getName().contains("Church's")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.churchs_chicken_logo);
            }
            else if(currentRestaurant.getName().contains("A & W") || currentRestaurant.getName().contains("A&W")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.a_and_w_logo);
            }
            else if(currentRestaurant.getName().contains("Booster")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.booster_juice_logo);
            }
            else if(currentRestaurant.getName().contains("Burger King")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.burger_king_logo);
            }
            else if(currentRestaurant.getName().contains("Dairy")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.dairy_queen_logo);
            }
            else if(currentRestaurant.getName().contains("Five Guys")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.five_guys_burger_and_fries_logo);
            }
            else if(currentRestaurant.getName().contains("Apna")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.apna_chaat_house_logo);
            }
            else {
                // restaurant icon
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.restaurant_image);
            }
            // name
            TextView textName = restaurantView.findViewById(R.id.textRestaurantName);
            textName.setText(currentRestaurant.getName());

            // checking for recent inspections
            if (currentRestaurant.numInspections() > 0){
                Inspection latestInspection = currentRestaurant.getLatestInspection();

                // inspection date
                String date = "" +latestInspection.getInspectionDate();
                TextView textDate = restaurantView.findViewById(R.id.textInspectionDate);
                textDate.setText("" + refactorDate(date));

                // number of violations
                setNumViolations(restaurantView, latestInspection);


                // setting hazard icon
                setHazardIcon(restaurantView, latestInspection);

                // setting hazard colour
                setHazardColour(restaurantView, latestInspection);
            }

            return restaurantView;
        }

    }



    // outputs date according to specifications described in the user story
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

        Log.d("MainActivity", "current date - "+ currentDate);
        Log.d("MainActivity", "inspection date - "+ inspectionDate);

        if (difference <= 30){
            return( difference + " " + getString(R.string.days_ago_main_date));
        }
        else if(difference <= 365){
            return("" + inspectionDate.getMonth() + " " + inspectionDate.getDayOfMonth() );
        }
        else {
            return("" + inspectionDate.getYear() + " "+ inspectionDate.getMonth() );
        }

    }


    @SuppressLint("SetTextI18n")
    private void setNumViolations(View restaurantView, Inspection latestInspection) {
        TextView textViolations = restaurantView.findViewById(R.id.textNumIssues);
        int violations = latestInspection.getNumViolations();
        if (violations == 1){
            textViolations.setText(getString(R.string.violations_text_main_single_1)+ " " +  latestInspection.getNumViolations() + " " + getString(R.string.violations_text_single));
        }
        else{
            textViolations.setText(getString(R.string.violations_text_main_1) + " " +  latestInspection.getNumViolations() + " " + getString(R.string.violations_text_main_2) );
        }
    }


    private void setHazardIcon(View v, Inspection i) {
        ImageView hazardIcon = v.findViewById(R.id.iconHazardLevel);
        String hazardRating = i.getHazardRating();
        if (Objects.equals(hazardRating.toUpperCase(), "LOW") ){
            hazardIcon.setImageResource(R.drawable.low_risk);
        }
        else if (Objects.equals(hazardRating.toUpperCase(), "MODERATE")){
            hazardIcon.setImageResource(R.drawable.medium_risk);
        }
        else if (Objects.equals(hazardRating.toUpperCase(), "HIGH")){
            hazardIcon.setImageResource(R.drawable.high_risk);
        }
    }


    private void setHazardColour(View v, Inspection i) {
        ImageView hazardColour = v.findViewById(R.id.hazardColour);
        String hazardRating = i.getHazardRating();
        if (Objects.equals(hazardRating.toUpperCase(), "LOW") ){
            hazardColour.setBackgroundColor(Color.GREEN);
        }
        else if (Objects.equals(hazardRating.toUpperCase(), "MODERATE")){
            hazardColour.setBackgroundColor(Color.parseColor("#FF8800"));
        }
        else if (Objects.equals(hazardRating.toUpperCase(), "HIGH")){
            hazardColour.setBackgroundColor(Color.RED);
        }
    }

    public  static Intent makeIntent(Context context)
    {
        Intent intent =  new Intent(context, MainActivity.class);
        return intent;
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT",true);
        startActivity(intent);
    }

}
