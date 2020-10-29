package com.example.poject_01.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.poject_01.R;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private RestaurantList restaurantList= RestaurantList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readRestaurantData();
        readInspectionData();
        sortRestaurantData(restaurantList);
    }



    private void readRestaurantData() {
        InputStream restaurantStream = getResources().openRawResource(R.raw.data_restaurants);
        BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(restaurantStream, Charset.forName("UTF-8")));
        String line = "";
        try {
            restaurantReader.readLine();
            while (((line = restaurantReader.readLine()) != null)) {
                String[] tokens = line.split(",");
                setRestaurantData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void readInspectionData() {
        InputStream restaurantStream = getResources().openRawResource(R.raw.data_inspections);
        BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(restaurantStream, Charset.forName("UTF-8")));
        String line = "";
        try {
            restaurantReader.readLine();
            while (((line = restaurantReader.readLine()) != null)) {
                String[] tokens = line.split(",");
                setInspectionData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }

    private void sortRestaurantData(RestaurantList restaurantList) {
        //Collections.sort(restaurantList);

    }

    private void setRestaurantData(String[] tokens) {
        Restaurant r = new Restaurant(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
        restaurantList.addRestaurant(r);
        Log.d("MainActivity", "Restaurant: " + r);
    }

    private void setInspectionData(String[] tokens) {


    }

}
