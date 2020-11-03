 package com.example.poject_01.model;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Iterator;
import java.util.List;

 /**
  * This class holds each restaurant, sorted by (#-a-z).
  * The list is sorted every time a restaurant is added.
  */
 public class RestaurantList implements Iterable<Restaurant>{
    private List<Restaurant> restaurants = new ArrayList<>();
    private static RestaurantList instance;


    public void addRestaurant(Restaurant r) {
        restaurants.add(r) ;
        Collections.sort(restaurants);
    }
    public List<Restaurant> getList(){
        return(this.restaurants);
    }


    public Restaurant getRestaurantIndex(int i){
        return restaurants.get(i);
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

 }
