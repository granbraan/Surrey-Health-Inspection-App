package com.example.poject_01.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * This class Reads data specified by "reader" and populates an instantiated restaurant list "restaurantList"
 * setters are private, only the read() functions may be called from outside this class.
 */
public class Data {
    private BufferedReader reader;
    private RestaurantList restaurantList = RestaurantList.getInstance();


    public Data( BufferedReader reader) {
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
            Log.wtf("MainActivity - Initial  Restaurant Data", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void setRestaurantData(String[] tokens) {
        String str = tokens[1].replace("\"", "");


        Restaurant r = new Restaurant(tokens[0],str,tokens[2],tokens[3],tokens[4],Double.parseDouble(tokens[5]),Double.parseDouble(tokens[6]), false);
        restaurantList.addRestaurant(r);

        Log.d("MainActivity - Initial  Restaurant Data - Added", r + " to restaurantList"  +"\n");
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
            Log.wtf("MainActivity - Initial  Inspection  Data", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    private void setInspectionData(String[] tokens) {
        String violations;

        String trackNum = tokens[0];
        if (tokens.length < 7 ) {
            violations = "";
        }
        else{
            violations = tokens[6].replace("\"", "");
        }
        // store inspection into restaurant with matching tracking number
        Inspection i = new Inspection(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), tokens[5], violations);
        for (Restaurant r : restaurantList) {
            if (Objects.equals(r.getTrackingNum(), trackNum)) {
                r.addInspection(i);
                String d = "" + tokens[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate currentDate = LocalDate.now();
                LocalDate inspectionDate = LocalDate.parse(d, formatter);
                long difference = DAYS.between(inspectionDate, currentDate);

                if(difference <= 365){
                    r.addCriticalHazardYear(Integer.parseInt(tokens[3]));
                }
                Log.d("MainActivity - Initial  Inspection  Data - Added" , i + " to " + r.getName() +"\n");
            }

        }
    }


    public void readUpdatedRestaurantData(){
        restaurantList.clear();
        // setting up reader

        String line;
        try {
            // jump over headers
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                setRestaurantData(tokens);
            }
            reader.close();
        }
        catch (IOException e) {
            Log.wtf("MainActivity - Updated Inspection  Data", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }


    public void readUpdatedInspectionData() {
        String line;
        try {
            reader.readLine();
            while (((line = reader.readLine()) != null)) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (tokens.length > 0 ){

                setUpdatedInspectionData(tokens);
                }

            }
            reader.close();

        }
        catch (IOException e) {
            Log.wtf("MainActivity - ReadWriteData", "error reading file on line: " + e);
            e.printStackTrace();
        }
    }

    private void setUpdatedInspectionData(String[] tokens) {
        String violations;
        String hazard;
        String trackNum = tokens[0];

        if (tokens.length <= 5){
            hazard = "n/a";
            violations = "";
        }
        else if (tokens.length <= 6){
            hazard = "n/a";
            violations = tokens[5].replace("\"", "");
        }
        else{
            hazard = tokens[6];
            if(tokens[5].isEmpty()){
                violations = "";
            }
            else{
                violations = tokens[5].replace("\"", "");
            }

        }


        // store inspection into restaurant with matching tracking number
        Inspection i = new Inspection(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), hazard, violations);
        for (Restaurant r : restaurantList) {
            if (Objects.equals(r.getName(), "Hillside Lodge Food Service")){
                Log.d("MainActivity - Updated Inspection  Data - Added",  i + " to " + r.getName() +"\n");
                //Log.d("MainActivity - Updated Inspection  Data",  +"\n");
            }
            if (Objects.equals(r.getTrackingNum(), trackNum)) {
                r.addInspection(i);
                String d = "" + tokens[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate currentDate = LocalDate.now();
                LocalDate inspectionDate = LocalDate.parse(d, formatter);
                long difference = DAYS.between(inspectionDate, currentDate);

                if(difference <= 365){
                    r.addCriticalHazardYear(Integer.parseInt(tokens[3]));
                }

            }

        }
    }




}
