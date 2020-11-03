package com.example.poject_01.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds all data relating to one inspection
 *
 */
public class Inspection implements Comparable<Inspection> {
    private int inspectionDate;
    private String inspectionType;
    private int numCritical;
    private int  numNonCritical;
    private String  hazardRating;
    private List<String> vioList = new ArrayList<>();

    // constructor
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
            this.vioList.add(tokens[i]);
        }

    }

    public int getInspectionDate() {
        return inspectionDate;
    }

    public int getNumViolations(){
        return (numCritical + numCritical);
    }

    public String getHazardRating() {
        return hazardRating;
    }



    public List <String> getViolationList(){
        return vioList;
    }

    public String getSpecificViolation(int i) {
        return vioList.get(i);
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "inspectionDate=" + inspectionDate +
                ", inspectionType='" + inspectionType + '\'' +
                ", numCritical=" + numCritical +
                ", numNonCritical=" + numNonCritical +
                ", hazardRating='" + hazardRating + '\'' +
                ", vioLump=" + vioList +
                '}';
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


    public int compareTo(Inspection compareInspection) {
        int compareDate = (compareInspection.getInspectionDate());

        //descending order
        return compareDate - this.inspectionDate;

    }


}
