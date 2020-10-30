package com.example.poject_01.model;

import android.util.Log;

/**
 * This class holds all data relating to one inspection
 */
public class Inspection implements Comparable<Inspection> {
    private int inspectionDate;
    private String inspectionType;
    private int numCritical;
    private int  numNonCritical;
    private String  hazardRating;
    private String violLump;

    public Inspection(int inspectionDate, String inspectionType, int numCritical, int numNonCritical, String hazardRating, String violLump) {
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numCritical = numCritical;
        this.numNonCritical = numNonCritical;
        this.hazardRating = hazardRating;
        this.violLump = violLump;
    }

    public int getInspectionDate() {
        return inspectionDate;
    }

    public void printDate(){
        Log.d("date", "" + this.inspectionDate);
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "inspectionDate=" + inspectionDate +
                ", inspType='" + inspectionType + '\'' +
                ", numCritical=" + numCritical +
                ", numNonCritical=" + numNonCritical +
                ", hazardRating=" + hazardRating +
                ", violLump='" + violLump + '\'' +
                '}';
    }
    public int compareTo(Inspection compareInspection) {
        int compareDate = (compareInspection.getInspectionDate());

        //descending order
        return compareDate - this.inspectionDate;

    }


}
