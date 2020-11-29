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
        String[] word = search.split(" ");
        for(String s: word) {
            if(restaurant.getName().toLowerCase().contains(s)) {
                return true;
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
}
