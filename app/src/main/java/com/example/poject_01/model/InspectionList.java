package com.example.poject_01.model;

import java.util.ArrayList;
import java.util.List;

public class InspectionList {
    private List<Inspection> inspections = new ArrayList<>();
    private static InspectionList instance;



    public void addInspection(Inspection i) {
        inspections.add(i) ;
    }




    public static InspectionList getInstance(){
        if (instance == null){
            instance = new InspectionList();
        }
        return instance;
    }
}

