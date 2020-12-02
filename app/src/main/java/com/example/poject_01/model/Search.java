package com.example.poject_01.model;

/**
 * Class to search/filter restaurants
 */
public class Search {
    private static Search instance;
    private String search;
    private String input;
    private Restaurant restaurant;
    private boolean check;

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
        String[] words = search.split(",");
        for(String s: words) {
            input = s;
            check = false;
            if(hazardCheck() || isFavourite() || numViolations()) {
                continue;
            }

            if(!restaurant.getName().toLowerCase().contains(s)) {
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

    public boolean numViolations() {
        if(!hasInspections()) {
            return false;
        }

        int violations;
        if(input.contains(">=") || input.contains("<=")) {
            check = true;
        }
        if(input.contains(">=")) {
            String num = input.substring(input.indexOf(">=") + 2, input.length()); // get number
            violations = Integer.parseInt(num);
            return restaurant.getLatestInspection().getNumCritical() >= violations;
        }
        else if(input.contains("<=")) {
            String num = input.substring(input.indexOf("<=") + 2, input.length()); // get number
            violations = Integer.parseInt(num);
            return restaurant.getLatestInspection().getNumCritical() <= violations;
        }
        return false;
    }
    private boolean hasInspections() {
        return restaurant.numInspections() != 0;
    }

    public boolean isFavourite() {
        // TODO: implement favourite
        if(input.equals("favourite")) {
            check = true;
            return restaurant.getFavourite();
        }
        return false;
    }

    public boolean hazardCheck() {
        //hazard check
        if(input.toLowerCase().equals("low") || input.toLowerCase().equals("moderate") || input.toLowerCase().equals("high")) {
            if(!hasInspections()) { // no inspections
                return false;
            }
            check = true;
            String hazard = restaurant.getLatestInspection().getHazardRating().toLowerCase();
            return hazard.equals(input);
        }
        return false;
    }


}
