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

            if(hazardCheck(s)) {
                if(restaurant.getName().toLowerCase().contains(s)) {
                    return true;
                }
            }

        }

        return false;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public boolean numViolations() {
        if(restaurant.getInspections().getNumInspections() == 0) {
            return false;
        }
        if(search.contains(">=")) {
            String string = search.replaceAll(">="," ");
            string.trim();
            int violations = Integer.parseInt(string);
            return restaurant.getLatestInspection().getNumCritical() >= violations;
        }
        else {
            String string = search.replaceAll("<="," ");
            string.trim();
            int violations = Integer.parseInt(string);
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
