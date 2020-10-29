package com.example.poject_01.model;



public class Restaurant {
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

    public void setInspections(InspectionList inspections) {
        this.inspections = inspections;
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
