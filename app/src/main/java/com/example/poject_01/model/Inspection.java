package com.example.poject_01.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all data relating to one inspection
 */
public class Inspection implements Comparable<Inspection> {
    private int inspectionDate;
    private String inspectionType;
    private int numCritical;
    private int  numNonCritical;
    private String  hazardRating;
    private List<String> vioLump = new ArrayList<>();

    public Inspection(int inspectionDate, String inspectionType, int numCritical, int numNonCritical, String hazardRating, String vioLump) {
        this.inspectionDate = inspectionDate;
        this.inspectionType = inspectionType;
        this.numCritical = numCritical;
        this.numNonCritical = numNonCritical;
        this.hazardRating = hazardRating;
        // parsing violation lump - storing each violation as element in array list "vioLump"
        String[] tokens = vioLump.split("\\|");
        Log.d("Inspection Class", "vioLump size:  " + tokens.length  +"\n");
        for (int i=0; i<tokens.length; i++){
            this.vioLump.add(tokens[i]);
        }

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
                ", inspectionType='" + inspectionType + '\'' +
                ", numCritical=" + numCritical +
                ", numNonCritical=" + numNonCritical +
                ", hazardRating='" + hazardRating + '\'' +
                ", vioLump=" + vioLump +
                '}';
    }

    public void printViolations(){
        for (int i=0; i<vioLump.size(); i++){
            Log.d("Violation", vioLump.get(i)  +"\n");
        }
    }

    public String getInspectionType() {
        return inspectionType;
    }

    public int getNumCritical() {
        return numCritical;
    }

    public int getNumNonCritical() {
        return numNonCritical;
    }

    public String getHazardRating() {
        return hazardRating;
    }

    public int compareTo(Inspection compareInspection) {
        int compareDate = (compareInspection.getInspectionDate());

        //descending order
        return compareDate - this.inspectionDate;

    }


}
