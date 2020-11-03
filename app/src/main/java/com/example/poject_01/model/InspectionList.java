package com.example.poject_01.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * This class holds all the inspections related to one restaurant,
 * sorted in descending order.
 */
public class InspectionList implements Iterable<Inspection> {
    private List<Inspection> inspections = new ArrayList<>();
    private static InspectionList instance;
    private int num_inspection = 0 ;


    public void addInspection(Inspection i) {
        inspections.add(i) ;num_inspection++;
    }
    public Inspection getInspectionIndex(int i){
        return inspections.get(i);
    }

    public int getNum_inspection() {
        return num_inspection;
    }

    public List<Inspection> getInspections() {
        return inspections;
    }

    @Override
    public Iterator<Inspection> iterator() {
        return inspections.iterator();
    }

    public void sort() {
        Collections.sort(inspections);
    }

    public static InspectionList getInstance(){
        if (instance == null){
            instance = new InspectionList();
        }
        return instance;
    }
}

