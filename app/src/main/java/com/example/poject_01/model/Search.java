package com.example.poject_01.model;

/**
 * Class to search/filter restaurants
 */
public class Search {
    private static Search instance;
    private String search;
    private Restaurant restaurant;

    private Search() {

    }

    public static Search getInstance() {
        if(instance == null) {
            instance = new Search();
        }
        return instance;
    }

    public boolean filter(Restaurant restaurant) {
        this.restaurant = restaurant;
        if(search == null || search.equals("")) {
            return true;
        }
        //restaurant check
        String[] word = search.split(" ");
        for(String s: word) {
            if(restaurant.getName().toLowerCase().contains(s)) {
                return false;
            }

        }

        return true;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean numViolations(int violations, String compare) {
        if(restaurant.getInspections().getNumInspections() == 0) {
            return false;
        }
        if(compare.equals(">=")) {
            return restaurant.getLatestInspection().getNumCritical() >= violations;
        }
        else {
            return restaurant.getLatestInspection().getNumCritical() <= violations;
        }
    }



    public boolean isFavourite() {
        // TODO: implement favourite
        return false;
    }

    public boolean hazardCheck(String hazard) {
        //hazard check
        if(hazard.toLowerCase().equals("low")
                || hazard.toLowerCase().equals("medium")
                || hazard.toLowerCase().equals("high")) {
            return restaurant.getInspections().getNumInspections() != 0;
        }
        return false;
    }


}
