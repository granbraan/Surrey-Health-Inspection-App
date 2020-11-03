package com.example.poject_01.model;

import android.util.Log;

import com.example.poject_01.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

/**
 * This class Reads data specified by "reader"
 * setters are private, only the read() functions may be called from outside this class
 */
public class Data {
    private RestaurantList restaurantList;
    private BufferedReader reader;

    public Data(RestaurantList restaurantList, BufferedReader reader) {
        this.restaurantList = restaurantList;
        this.reader = reader;
    }

    public void readRestaurantData(){
        // setting up reader
        String line;
        try {
            // jump over headers
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("\\*");
                setRestaurantData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity - ReadWriteData", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }

    private void setRestaurantData(String[] tokens) {
        Restaurant r = new Restaurant(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]));
        restaurantList.addRestaurant(r);

        Log.d("MainActivity - ReadWriteData", "Added : " + r + " to restaurantList"  +"\n");
    }

    public void readInspectionData() {

        String line;
        try {
            reader.readLine();
            while (((line = reader.readLine()) != null)) {
                String[] tokens = line.split("\\*");
                setInspectionData(tokens);
            }
        }
        catch (IOException e) {
            Log.wtf("MainActivity - ReadWriteData", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }

    private void setInspectionData(String[] tokens) {
        String violations;
        Log.d("MainActivity - ReadWriteData", "length of tokens should be 6 or 7: " + tokens.length +"\n");
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
                Log.d("MainActivity - ReadWriteData", "Added: " + i + " to " + r.getName() +"\n");
            }

        }
    }

}
