package com.example.poject_01.model;


import android.util.Log;

/**
 * This class stores all related restaurant data
 * Holds all related inspection data in inspection list - implemented with arraylist
 */

public class Restaurant implements Comparable<Restaurant> {
    private String trackingNum;
    private String name;
    private String address;
    private String city;
    private String type;
    private double latitude;
    private double longitude;
    private InspectionList inspections;


    public Restaurant(String trackingNum, String name, String address, String city,String type, double latitude, double longitude) {
        this.trackingNum = trackingNum;
        this.name = name;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public void addInspection(Inspection inspection) {

        inspections.addInspection(inspection);
    }

    public void printInspections(){
        for(Inspection i : inspections) {

            Log.d("Restaurant Inspections: ", " " + i.toString());
        }
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Restaurant r) {
        return this.name.compareTo(r.name);
    }



    @Override
    public String toString() {
        return "Restaurant{" +
                "trackingNum='" + trackingNum + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


}
