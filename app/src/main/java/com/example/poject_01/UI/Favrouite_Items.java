package com.example.poject_01.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.PersonDBHelper;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

public class Favrouite_Items extends AppCompatActivity {

    private final RestaurantList restaurantList = RestaurantList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favrouite__items);
        populateListView();
        registerClick();

    }
    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }
    private void populateListView() {
        ArrayAdapter<Restaurant> restaurantAdapter = new RestaurantListAdapter();
        ListView list = findViewById(R.id.restaurantListView1);
        list.setAdapter(restaurantAdapter);
    }
    private void registerClick() {
        ListView listView = findViewById(R.id.restaurantListView1);
        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Log.d("MainActivity", "User clicked restaurant at position: " + position);
            Toast.makeText(this, "No of Inception:"+restaurantList.getRestaurantIndex(position).numInspections(), Toast.LENGTH_SHORT).show();
        });

    }
    private class RestaurantListAdapter extends ArrayAdapter<Restaurant> {
        public RestaurantListAdapter() {
            super(Favrouite_Items.this, R.layout.restaurant_list_view, restaurantList.getList());
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            View restaurantView = convertView;

            if (restaurantView == null){
                restaurantView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
                //LayoutInflater inflater=getLayoutInflater();
                //View view=inflater.inflate(R.layout.restaurant_list_view,null);
                ImageView add_fav1=restaurantView.findViewById(R.id.add_fav1);
                PersonDBHelper dbHandler1 = new PersonDBHelper(getApplicationContext());
                Cursor res2 = dbHandler1.getSpecificData(restaurantList.getRestaurantIndex(position).getName());
                if (!(res2.getCount() == 0)) {
                    add_fav1.setVisibility(View.VISIBLE);
                }
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
            else if(currentRestaurant.getName().contains("Kelly's")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.kellys_pub_logo);
            }
            else if(currentRestaurant.getName().contains("New York")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.new_york_fries_logo);
            }
            else if(currentRestaurant.getName().contains("7-Eleven")) {
                ImageView imageView = restaurantView.findViewById(R.id.iconRestaurantName);
                imageView.setImageResource(R.drawable.seven_eleven_logo);
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
                Log.d("Favourite Activity", "current restaurant: "+ currentRestaurant.getName() + " - num inspections: " + currentRestaurant.numInspections());
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
            else{

                TextView textDate = restaurantView.findViewById(R.id.textInspectionDate);
                textDate.setText(getResources().getString(R.string.no_recent_inspections_main));

                // number of violations
                TextView textViolations = restaurantView.findViewById(R.id.textNumIssues);
                textViolations.setText(getResources().getString(R.string.no_recent_inspections_main));


                // setting hazard icon
                ImageView hazardIcon = restaurantView.findViewById(R.id.iconHazardLevel);
                hazardIcon.setImageResource(R.drawable.white_background);


                // setting hazard colour

                ImageView hazardColour = restaurantView.findViewById(R.id.hazardColour);
                hazardColour.setBackgroundColor(getColor(R.color.white));

            }

            return restaurantView;
        }

    }
    // outputs date according to specifications described in the user story
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

        Log.d("FavActivity", "current date - "+ currentDate);
        Log.d("FavActivity", "inspection date - "+ inspectionDate);

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

    public  static Intent makeIntent(Context context) {
        Intent intent =  new Intent(context, Favrouite_Items.class);
        return intent;
    }

}