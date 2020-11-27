package com.example.poject_01.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import java.time.LocalDate;
import java.time.Month;
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
        setupToolbar();
        populateListView();
        registerClick();
    }

    private void setupToolbar() {
        toolbar =  findViewById(R.id.toolbar2);
        toolbar.inflateMenu(R.menu.toggle_button_list);
        toolbar.setTitle(R.string.list_of_rest);
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
            intent = RestaurantDetailsActivity.makeIntent(MainActivity.this,position,false);
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
                Log.d("MainActivity", "current restaurant: "+ currentRestaurant.getName() + " - num inspections: " + currentRestaurant.numInspections());
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
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

        Log.d("MainActivity", "current date - "+ currentDate);
        Log.d("MainActivity", "inspection date - "+ inspectionDate);
        int monthDisplay;
        if(inspectionDate.getMonth().equals(Month.JANUARY))
            monthDisplay = R.string.january;
        else if(inspectionDate.getMonth().equals(Month.FEBRUARY))
            monthDisplay = R.string.february;
        else if(inspectionDate.getMonth().equals(Month.MARCH))
            monthDisplay = R.string.march;
        else if(inspectionDate.getMonth().equals(Month.APRIL))
            monthDisplay = R.string.april;
        else if(inspectionDate.getMonth().equals(Month.MAY))
            monthDisplay = R.string.may;
        else if(inspectionDate.getMonth().equals(Month.JUNE))
            monthDisplay = R.string.june;
        else if(inspectionDate.getMonth().equals(Month.JULY))
            monthDisplay = R.string.july;
        else if(inspectionDate.getMonth().equals(Month.AUGUST))
            monthDisplay = R.string.august;
        else if(inspectionDate.getMonth().equals(Month.SEPTEMBER))
            monthDisplay = R.string.september;
        else if(inspectionDate.getMonth().equals(Month.OCTOBER))
            monthDisplay = R.string.october;
        else if(inspectionDate.getMonth().equals(Month.NOVEMBER))
            monthDisplay = R.string.november;
        else
            monthDisplay = R.string.december;


        if (difference <= 30){
            return( difference + " " + getString(R.string.days_ago_main_date));
        }
        else if(difference <= 365){
            return("" + getString(monthDisplay) + " " + inspectionDate.getDayOfMonth() );
        }
        else {
            return("" + inspectionDate.getYear() + " "+ getString(monthDisplay) );
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



    public static Intent getIntent (Context c)
    {
        Intent intent = new Intent(c,MainActivity.class);
        return intent;

    }

}
