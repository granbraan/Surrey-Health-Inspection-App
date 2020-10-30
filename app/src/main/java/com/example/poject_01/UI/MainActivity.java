package com.example.poject_01.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.poject_01.R;
import com.example.poject_01.model.Inspection;
import com.example.poject_01.model.Restaurant;
import com.example.poject_01.model.RestaurantList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // all restaurants added to this list, sorted by name - alphabetical order.
    private RestaurantList restaurantList= RestaurantList.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readRestaurantData();
        readInspectionData();
        Restaurant r = restaurantList.getRestaurantIndex(7);
        r.printAllInspections();
        r.printViolations(1);
    }



    private void readRestaurantData() {
        // setting up reader
        InputStream restaurantStream = getResources().openRawResource(R.raw.data_restaurants);
        BufferedReader restaurantReader = new BufferedReader(new InputStreamReader(restaurantStream, Charset.forName("UTF-8")));
        String line = "";
        try {
            // jump over headers
            restaurantReader.readLine();
            while (((line = restaurantReader.readLine()) != null)) {
                String[] tokens = line.split("\\*");
                setRestaurantData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void readInspectionData() {
        InputStream inspectionStream = getResources().openRawResource(R.raw.data_inspections);
        BufferedReader inspectionReader = new BufferedReader(new InputStreamReader(inspectionStream, Charset.forName("UTF-8")));
        String line = "";
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



    private void setRestaurantData(String[] tokens) {
        Restaurant r = new Restaurant(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
        restaurantList.addRestaurant(r);
        Log.d("MainActivity", "Added : " + r + " to restaurantList"  +"\n");
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
        Inspection i = new Inspection(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], violations);
        for (Restaurant r : restaurantList) {
            if (Objects.equals(r.getTrackingNum(), trackNum)) {
                r.addInspection(i);
                Log.d("MainActivity", "Added: " + i + " to " + r.getName() +"\n");
            }

        }
    }
}
