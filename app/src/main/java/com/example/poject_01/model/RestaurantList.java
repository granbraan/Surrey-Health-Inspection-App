 package com.example.poject_01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RestaurantList implements Iterable<Restaurant> //, Comparable<Restaurant>
         {
    private List<Restaurant> restaurants = new ArrayList<>();
    private static RestaurantList instance;



    public void addRestaurant(Restaurant r) {
        restaurants.add(r) ;
    }



    private void sortRestaurants(Restaurant r) {

    }


    public static RestaurantList getInstance(){
        if (instance == null){
            instance = new RestaurantList();
        }
        return instance;
    }

    @Override
    public Iterator<Restaurant> iterator() {
        return restaurants.iterator();
    }
/*
    public int compareTo(Restaurant compareFruit) {

        String compareName = compareFruit.get;

        //ascending order
        return this.quantity - compareQuantity;

        //descending order
        //return compareQuantity - this.quantity;

    }*/

}
