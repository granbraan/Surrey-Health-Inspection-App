package com.example.poject_01.model;

public class Inspection {
    private int inspectionDate;
    private String inspType;
    private int numCritical;
    private int  numNonCritical;
    private String  hazardRating;
    private String violLump;

    public Inspection(int inspectionDate, String inspType, int numCritical, int numNonCritical, String hazardRating, String violLump) {
        this.inspectionDate = inspectionDate;
        this.inspType = inspType;
        this.numCritical = numCritical;
        this.numNonCritical = numNonCritical;
        this.hazardRating = hazardRating;
        this.violLump = violLump;
    }

    @Override
    public String toString() {
        return "Inspection{" +
                "inspectionDate=" + inspectionDate +
                ", inspType='" + inspType + '\'' +
                ", numCritical=" + numCritical +
                ", numNonCritical=" + numNonCritical +
                ", hazardRating=" + hazardRating +
                ", violLump='" + violLump + '\'' +
                '}';
    }
}
