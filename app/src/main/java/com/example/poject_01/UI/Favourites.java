package com.example.poject_01.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class Favourites extends AppCompatActivity {

    private Toolbar toolbar;
    public ArrayAdapter<Restaurant> favouritesAdapter;
    private List<Restaurant> restaurants;
    private RestaurantList favouriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        setUpToolBar();
        populateListView();
    }

    private void populateListView() {
        favouritesAdapter = new FavouriteListAdapter(Favourites.this, (List<Restaurant>) favouriteList);
        ListView list = findViewById(R.id.fav_list);
        list.setAdapter(favouritesAdapter);
    }
    private class FavouriteListAdapter extends ArrayAdapter<Restaurant>{
        private Context context;

        public FavouriteListAdapter(@NonNull Context context, List<Restaurant> restaurants) {
            super(context, R.layout.favourite_list_view, restaurants);
            Favourites.this.restaurants = restaurants;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View restaurantView = convertView;
            if(restaurantView == null)
            {
                restaurantView = getLayoutInflater().inflate(R.layout.favourite_list_view,parent,false);
            }

            Restaurant currentRestaurant = restaurants.get(position);
            TextView textName = findViewById(R.id.fav_rest_name);
            textName.setText(currentRestaurant.getName());
            if(currentRestaurant.numInspections()>0)
            {
                Inspection latestInspection = currentRestaurant.getLatestInspection();

                //date
                String date = ""+latestInspection.getInspectionDate();
                TextView textDate =findViewById(R.id.fav_inspection_date);
                textDate.setText("" + refactorDate(date));

                //
                TextView hazardLevel = findViewById(R.id.fav_insp_hzrd);
                hazardLevel.setText(context.getString(R.string.hazard_lvl_restaurant)+ " "+latestInspection.getHazardRating());
                if(latestInspection.getHazardRating().equals("Low"))
                {
                    hazardLevel.setTextColor(Color.GREEN);
                }
                else if(latestInspection.getHazardRating().equals("Moderate"))
                {
                    hazardLevel.setTextColor(Color.parseColor("#FF8800"));
                }
                else
                {
                    hazardLevel.setTextColor(Color.RED);
                }

            }




            return restaurantView;
        }
    };


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


    private void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_favourites);
        toolbar.inflateMenu(R.menu.fav_toolbar);
        toolbar.setTitle("Favourites");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = MapsActivity.getIntent(Favourites.this);
                if(item.getItemId() == R.id.switch_list) {
                    startActivity(intent);
                    return  true;
                }
                else
                    return false;
            }
        });


    }

    public  static Intent makeIntent(Context context)
    {
        Intent intent =  new Intent(context, Favourites.class);
        return intent;
    }
}