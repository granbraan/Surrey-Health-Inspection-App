package com.example.poject_01.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Displays a scrollable list of restaurants, sorted alphabetically by name.
 * The list of restaurants and the data relating to each restaurant is read from "data_restaurants.csv" and "data_inspection.csv"
 */
public class MainActivity extends AppCompatActivity {
    // all restaurants added to this list, sorted by name - alphabetical order.
    private final RestaurantList restaurantList = RestaurantList.getInstance();
    private List<Restaurant> restaurants;
    private Intent intent;
    private Toolbar toolbar;
    public ArrayAdapter<Restaurant> restaurantAdapter;
    private FragmentManager filterFrag ;
    private static MainActivity instance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = MainActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupSearchBar();
        setupToolbar();
        populateListView();
        registerClick();

    }

    private void setupSearchBar() {
        EditText searchBar = findViewById(R.id.searchMainList);
        searchBar.addTextChangedListener(getTextWatcher());
        setupSearchClear(searchBar);
    }

    private void setupSearchClear(EditText searchBar) {
        Button clearSearch = findViewById(R.id.clearSearchMain);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });
    }


    private void setupToolbar() {
        filterFrag = getSupportFragmentManager();
        toolbar =  findViewById(R.id.toolbar2);
        toolbar.inflateMenu(R.menu.toggle_button_list);
        toolbar.setTitle("List of Restaurants");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case(R.id.switch_map):
                        finish();
                        return  true;

                    case(R.id.filter):
                        ListFilterFragment filter = new ListFilterFragment();
                        filter.setCancelable(false);
                        filter.show(filterFrag, "MessageDialog");
                        return true;


                    default:
                        return false;
                }
            }
        });
    }


    private void populateListView() {
        restaurantAdapter = new RestaurantListAdapter(MainActivity.this, restaurantList.getList());
        ListView list = findViewById(R.id.restaurantListView);
        list.setAdapter(restaurantAdapter);
    }


    private void registerClick() {
        ListView listView = findViewById(R.id.restaurantListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("MainActivity", "User clicked restaurant at position: " + position);
                intent = RestaurantDetailsActivity.makeIntent(MainActivity.this, position, false);
                MainActivity.this.startActivity(intent);
            }
        });

    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            // not used
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

           // filters list view with user inputted string 's'
            @Override
            public void afterTextChanged(Editable s) {
                restaurantAdapter.getFilter().filter(s.toString());
            }
        };
    }

    private class RestaurantListAdapter extends ArrayAdapter<Restaurant> implements Filterable {

        private Context context;

        public RestaurantListAdapter(Context context, List<Restaurant> restaurants) {
            super(context, R.layout.restaurant_list_view, restaurants);
            MainActivity.this.restaurants = restaurants;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (!restaurants.isEmpty()){
                return restaurants.size();
            }
            return 0;

        }

        @Override
        public View getView(int position,  View convertView, ViewGroup parent) {
            View restaurantView = convertView;
            if (restaurantView == null){
                restaurantView = getLayoutInflater().inflate(R.layout.restaurant_list_view, parent, false);
            }
            Restaurant currentRestaurant = restaurants.get(position);

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
                //Log.d("MainActivity", "current restaurant: "+ currentRestaurant.getName() + " - num inspections: " + currentRestaurant.numInspections());
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

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<Restaurant> originalRestaurants = restaurantList.getList();
                    List<Restaurant> FilteredArrList = new ArrayList<>();
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    if (constraint == null || constraint.length() == 0) {
                        // return original list
                        results.count = originalRestaurants.size();
                        results.values = originalRestaurants;
                    }
                    // multiple filters are concatenated into one string and separated by the '|' character
                    else if (constraint.toString().toUpperCase().contains("|")){
                        List<Restaurant> FilteredArrList2 = new ArrayList<>();
                        String input = constraint.toString().toUpperCase();
                        String[] tokens = input.split("\\|");
                        for (Restaurant r : originalRestaurants) {
                            String rName = r.getName();
                            if (rName.toUpperCase().contains(tokens[0])) {
                                FilteredArrList.add(r);

                            }
                        }
                        for (Restaurant r : FilteredArrList) {
                            Log.d("main", r.toString());
                            if (r.numInspections() > 0) {
                                String hazard = r.getLatestInspection().getHazardRating().toUpperCase();
                                if (Objects.equals(tokens[2], hazard)) {
                                    FilteredArrList2.add(r);

                                }

                            }
                        }
                        results.count = FilteredArrList2.size();
                        results.values = FilteredArrList2;

                    }
                    else {
                        constraint = constraint.toString().toUpperCase();
                        if (Objects.equals(constraint, "LOW") || Objects.equals(constraint, "MODERATE") || Objects.equals(constraint, "HIGH")) {
                            for (Restaurant r : originalRestaurants) {
                                if (r.numInspections() > 0) {
                                    String data = r.getLatestInspection().getHazardRating().toUpperCase();
                                    if (Objects.equals(constraint, data)) {
                                        FilteredArrList.add(r);

                                    }

                                }
                            }
                        }
                        else {
                            for (Restaurant r : originalRestaurants) {
                                String data = r.getName();
                                if (data.toUpperCase().contains(constraint)) {
                                    FilteredArrList.add(r);

                                }
                            }

                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;

                    }
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    Log.d("publish results", constraint +"");
                    restaurants = (List<Restaurant>) results.values;
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }
            };
            return filter;
        }

    }



    // outputs date according to specifications described in the user story
    private String refactorDate(String d) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate currentDate = LocalDate.now();
        LocalDate inspectionDate = LocalDate.parse(d, formatter);
        long difference = DAYS.between(inspectionDate, currentDate);

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

    public  static Intent getLaunchIntent(Context context) {
        Intent intent =  new Intent(context, MainActivity.class);
        return intent;
    }

    public List<Restaurant> getFilteredList(){
        return (restaurants);
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

    public static MainActivity getInstance(){
        return instance;
    }





}
