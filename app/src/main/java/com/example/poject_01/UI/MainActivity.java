package com.example.poject_01.UI;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;

import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;


public class MainActivity extends AppCompatActivity {
    // all restaurants added to this list, sorted by name - alphabetical order.
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readRestaurantData();
        readInspectionData();
        populateListView();
        registerClick();
    }



    private void readRestaurantData() {
        // setting up reader
        InputStream restaurantStream = getResources().openRawResource(R.raw.data_restaurants);
        BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(restaurantStream, StandardCharsets.UTF_8));
        String line;
        try {
            // jump over headers
            restaurantReader.readLine();
            while ((line = restaurantReader.readLine()) != null) {
                String[] tokens = line.split("\\*");
                setRestaurantData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void setRestaurantData(String[] tokens) {
        Restaurant r = new Restaurant(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
        restaurantList.addRestaurant(r);

        Log.d("MainActivity", "Added : " + r + " to restaurantList"  +"\n");
    }


    private void readInspectionData() {
        InputStream inspectionStream = getResources().openRawResource(R.raw.data_inspections);
        BufferedReader inspectionReader = new BufferedReader(new InputStreamReader(inspectionStream, StandardCharsets.UTF_8));
        String line;
        try {
            inspectionReader.readLine();
            while (((line = inspectionReader.readLine()) != null)) {
                String[] tokens = line.split("\\*");
                setInspectionData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void setInspectionData(String[] tokens) {
        String violations;
        Log.d("MainActivity", "length of tokens should be 6 or 7: " + tokens.length +"\n");
        String trackNum = tokens[0];
        if (tokens.length < 7 ) {
            violations = "";
        }
        else{
            violations = tokens[6];
        }
        // store inspection into restaurant with matching tracking number
        Inspection i = new Inspection(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], violations);
        for (Restaurant r : restaurantList) {
            if (Objects.equals(r.getTrackingNum(), trackNum)) {
                r.addInspection(i);
                Log.d("MainActivity", "Added: " + i + " to " + r.getName() +"\n");
            }

        }
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
            intent = detail_single_restaurant.makeIntent(MainActivity.this,position);
            startActivity(intent);
        });

    }


    private class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantListAdapter() {
            super(MainActivity.this, R.layout.restaurant_list_view, restaurantList.getList());
        }


        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            View restaurantView = convertView;
            if (restaurantView == null){
                restaurantView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
            }
            Restaurant currentRestaurant = restaurantList.getRestaurantIndex(position);

            // restaurant icon
            ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
            imageView.setImageResource(R.drawable.restaurant_image);

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
            TextView textViolations = restaurantView.findViewById(R.id.textNumIssues);
            textViolations.setText("There were: " + latestInspection.getNumViolations() + " violations" );

            // setting hazard colour
            setHazardIcon(restaurantView, latestInspection);
            }

            return restaurantView;
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

    // outputs date according to specifications set in user stories
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

        Log.d("MainActivity", "current date - "+ currentDate);
        Log.d("MainActivity", "inspection date - "+ inspectionDate);
        Log.d("MainActivity", "difference = "+ difference);
        if (difference <= 30){
            return( difference + " days ago");
        }
        else if(difference <= 365){
            return("" + inspectionDate.getMonth() + " " + inspectionDate.getDayOfMonth() );
        }
        else {
            return("" + inspectionDate.getYear() + " "+ inspectionDate.getMonth() );
        }

    }

}
